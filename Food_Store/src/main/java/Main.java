import config.DataBaseConection;
import config.DataLoader;
import config.DataSaver;
import entities.Categoria;
import entities.Pedido;
import entities.Producto;
import entities.Usuario;
import enums.Estado;
import enums.FormaPago;
import enums.Rol;
import service.CategoriaService;
import service.PedidoService;
import service.ProductoService;
import service.UsuarioService;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static final String URL = "jdbc:sqlite:foodstore.db";

    private static CategoriaService categoriaService = new CategoriaService();
    private static ProductoService productoService = new ProductoService(categoriaService);
    private static UsuarioService usuarioService = new UsuarioService();
    private static PedidoService pedidoService = new PedidoService(usuarioService, productoService);

    public static void main(String[] args) {
        DataBaseConection.crearBD(URL);
        DataLoader.cargarTodo(URL, categoriaService, productoService, usuarioService, pedidoService);

        try {
            while (true) {
                int opcion = -1;
                boolean salir = false;

                do {
                    try {
                        System.out.println("=== SISTEMA DE PEDIDOS (FOOD STORE) ===");
                        System.out.println("1. Categorías\n2. Productos\n3. Usuarios\n4. Pedidos\n0. Salir");
                        opcion = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException nfe) {
                        System.out.println("Debe ingresar un numero entero...");
                    }
                } while (!List.of(1, 2, 3, 4, 0).contains(opcion));

                switch (opcion) {
                    case 1: menuCategorias(); break;
                    case 2: menuProductos(); break;
                    case 3: menuUsuarios(); break;
                    case 4: menuPedidos(); break;
                    default: salir = true;
                }

                if (salir) break;
            }
        } finally {
            DataSaver.guardarTodo(URL, categoriaService, productoService, usuarioService, pedidoService);
        }

        System.out.println("Cerrando programa...");
    }

    // ===================== CATEGORIAS =====================

    public static void menuCategorias() {
        System.out.println("=== Categorias ===");
        int opcion = menu();
        switch (opcion) {
            case 1: listarCategorias(); break;
            case 2: crearCategoria(); break;
            case 3: editarCategoria(); break;
            case 4: eliminarCategoria(); break;
        }
    }

    private static void listarCategorias() {
        List<Categoria> categorias = categoriaService.listar();
        if (categorias.isEmpty()) {
            System.out.println("No hay categorías cargadas");
            return;
        }
        for (Categoria c : categorias) {
            System.out.println("ID: " + c.getId() + " | Nombre: " + c.getNombre()
                    + " | Descripción: " + c.getDescripcion());
        }
    }

    private static void crearCategoria() {
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Descripción: ");
        String descripcion = sc.nextLine();

        try {
            Categoria categoria = categoriaService.crear(nombre, descripcion);
            System.out.println("Categoría creada con id " + categoria.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void editarCategoria() {
        // FIX 1: cortar si no hay datos antes de pedir el id
        if (categoriaService.listar().isEmpty()) {
            System.out.println("No hay categorías cargadas");
            return;
        }
        listarCategorias();
        System.out.print("Ingrese el id de la categoría a editar: ");
        Long id;
        try {
            id = Long.parseLong(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Debe ingresar un número entero");
            return;
        }

        System.out.print("Nuevo nombre (Enter para no modificar): ");
        String nombre = sc.nextLine();
        System.out.print("Nueva descripción (Enter para no modificar): ");
        String descripcion = sc.nextLine();

        try {
            categoriaService.editar(id, nombre.isBlank() ? null : nombre,
                    descripcion.isBlank() ? null : descripcion);
            System.out.println("Categoría actualizada correctamente");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void eliminarCategoria() {
        if (categoriaService.listar().isEmpty()) {
            System.out.println("No hay categorías cargadas");
            return;
        }
        listarCategorias();
        System.out.print("Ingrese el id de la categoría a eliminar: ");
        Long id;
        try {
            id = Long.parseLong(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Debe ingresar un número entero");
            return;
        }

        System.out.print("¿Confirma la eliminación? (S/N): ");
        if (!sc.nextLine().equalsIgnoreCase("S")) {
            System.out.println("Operación cancelada");
            return;
        }

        try {
            categoriaService.eliminar(id);
            System.out.println("Categoría eliminada correctamente");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ===================== PRODUCTOS =====================

    public static void menuProductos() {
        System.out.println("=== Productos ===");
        int opcion = menu();
        switch (opcion) {
            case 1: listarProductos(); break;
            case 2: crearProducto(); break;
            case 3: editarProducto(); break;
            case 4: eliminarProducto(); break;
        }
    }

    private static void listarProductos() {
        List<Producto> productos = productoService.listar();
        if (productos.isEmpty()) {
            System.out.println("No hay productos cargados");
            return;
        }
        for (Producto p : productos) {
            System.out.println("ID: " + p.getId() + " | Nombre: " + p.getNombre()
                    + " | Precio: $" + p.getPrecio() + " | Stock: " + p.getStock()
                    + " | Disponible: " + (p.isDisponible() ? "Sí" : "No")
                    + " | Categoría: " + p.getCategoria().getNombre());
        }
    }

    private static void crearProducto() {
        // FIX: cortar si no hay categorías antes de pedir datos
        if (categoriaService.listar().isEmpty()) {
            System.out.println("No hay categorías cargadas. Debe crear una categoría primero.");
            return;
        }
        listarCategorias();
        System.out.print("Id de la categoría: ");
        Long idCategoria;
        try {
            idCategoria = Long.parseLong(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Debe ingresar un número entero");
            return;
        }

        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Descripción: ");
        String descripcion = sc.nextLine();

        double precio;
        try {
            System.out.print("Precio: ");
            precio = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Debe ingresar un número válido para el precio");
            return;
        }

        int stock;
        try {
            System.out.print("Stock: ");
            stock = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Debe ingresar un número entero para el stock");
            return;
        }

        System.out.print("Imagen (URL o ruta, opcional - Enter para omitir): ");
        String imagen = sc.nextLine();

        System.out.print("¿Disponible? (S/N): ");
        boolean disponible = sc.nextLine().equalsIgnoreCase("S");

        try {
            Producto producto = productoService.crear(nombre, precio, descripcion, stock,
                    imagen.isBlank() ? null : imagen, disponible, idCategoria);
            System.out.println("Producto creado con id " + producto.getId());
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void editarProducto() {
        if (productoService.listar().isEmpty()) {
            System.out.println("No hay productos cargados");
            return;
        }
        listarProductos();
        System.out.print("Ingrese el id del producto a editar: ");
        Long id;
        try {
            id = Long.parseLong(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Debe ingresar un número entero");
            return;
        }

        System.out.print("Nuevo nombre (Enter para no modificar): ");
        String nombre = sc.nextLine();

        System.out.print("Nuevo precio (Enter para no modificar): ");
        String precioStr = sc.nextLine();
        Double precio = null;
        if (!precioStr.isBlank()) {
            try {
                precio = Double.parseDouble(precioStr);
            } catch (NumberFormatException e) {
                System.out.println("Precio inválido, se mantiene el actual");
            }
        }

        System.out.print("Nueva descripción (Enter para no modificar): ");
        String descripcion = sc.nextLine();

        System.out.print("Nuevo stock (Enter para no modificar): ");
        String stockStr = sc.nextLine();
        Integer stock = null;
        if (!stockStr.isBlank()) {
            try {
                stock = Integer.parseInt(stockStr);
            } catch (NumberFormatException e) {
                System.out.println("Stock inválido, se mantiene el actual");
            }
        }

        System.out.print("Nueva imagen (Enter para no modificar): ");
        String imagen = sc.nextLine();

        System.out.print("Nueva categoría - id (Enter para no modificar): ");
        String idCategoriaStr = sc.nextLine();
        Long idCategoria = null;
        if (!idCategoriaStr.isBlank()) {
            try {
                idCategoria = Long.parseLong(idCategoriaStr);
            } catch (NumberFormatException e) {
                System.out.println("Id de categoría inválido, se mantiene la actual");
            }
        }

        try {
            productoService.editar(id, nombre.isBlank() ? null : nombre, precio,
                    descripcion.isBlank() ? null : descripcion, stock,
                    imagen.isBlank() ? null : imagen, null, idCategoria);
            System.out.println("Producto actualizado correctamente");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void eliminarProducto() {
        if (productoService.listar().isEmpty()) {
            System.out.println("No hay productos cargados");
            return;
        }
        listarProductos();
        System.out.print("Ingrese el id del producto a eliminar: ");
        Long id;
        try {
            id = Long.parseLong(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Debe ingresar un número entero");
            return;
        }

        System.out.print("¿Confirma la eliminación? (S/N): ");
        if (!sc.nextLine().equalsIgnoreCase("S")) {
            System.out.println("Operación cancelada");
            return;
        }

        try {
            productoService.eliminar(id);
            System.out.println("Producto eliminado correctamente");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ===================== USUARIOS =====================

    public static void menuUsuarios() {
        System.out.println("=== Usuarios ===");
        int opcion = menu();
        switch (opcion) {
            case 1: listarUsuarios(); break;
            case 2: crearUsuario(); break;
            case 3: editarUsuario(); break;
            case 4: eliminarUsuario(); break;
        }
    }

    private static void listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listar();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios cargados");
            return;
        }
        for (Usuario u : usuarios) {
            System.out.println("ID: " + u.getId() + " | Nombre: " + u.getNombre() + " " + u.getApellido()
                    + " | Email: " + u.getEmail() + " | Celular: " + u.getCelular()
                    + " | Rol: " + u.getRol());
        }
    }

    private static void crearUsuario() {
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Apellido: ");
        String apellido = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Celular: ");
        String celular = sc.nextLine();

        Rol rol = pedirRol();
        if (rol == null) {
            System.out.println("Operación cancelada");
            return;
        }

        try {
            Usuario usuario = usuarioService.crear(nombre, apellido, email, celular, rol);
            System.out.println("Usuario creado con id " + usuario.getId());
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void editarUsuario() {
        if (usuarioService.listar().isEmpty()) {
            System.out.println("No hay usuarios cargados");
            return;
        }
        listarUsuarios();
        System.out.print("Ingrese el id del usuario a editar: ");
        Long id;
        try {
            id = Long.parseLong(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Debe ingresar un número entero");
            return;
        }

        System.out.print("Nuevo nombre (Enter para no modificar): ");
        String nombre = sc.nextLine();
        System.out.print("Nuevo apellido (Enter para no modificar): ");
        String apellido = sc.nextLine();
        System.out.print("Nuevo email (Enter para no modificar): ");
        String email = sc.nextLine();
        System.out.print("Nuevo celular (Enter para no modificar): ");
        String celular = sc.nextLine();

        System.out.print("¿Modificar rol? (S/N): ");
        Rol rol = null;
        if (sc.nextLine().equalsIgnoreCase("S")) {
            rol = pedirRol();
        }

        try {
            usuarioService.editar(id,
                    nombre.isBlank() ? null : nombre,
                    apellido.isBlank() ? null : apellido,
                    email.isBlank() ? null : email,
                    celular.isBlank() ? null : celular,
                    rol);
            System.out.println("Usuario actualizado correctamente");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void eliminarUsuario() {
        if (usuarioService.listar().isEmpty()) {
            System.out.println("No hay usuarios cargados");
            return;
        }
        listarUsuarios();
        System.out.print("Ingrese el id del usuario a eliminar: ");
        Long id;
        try {
            id = Long.parseLong(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Debe ingresar un número entero");
            return;
        }

        System.out.print("¿Confirma la eliminación? (S/N): ");
        if (!sc.nextLine().equalsIgnoreCase("S")) {
            System.out.println("Operación cancelada");
            return;
        }

        try {
            usuarioService.eliminar(id);
            System.out.println("Usuario eliminado correctamente");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static Rol pedirRol() {
        while (true) {
            System.out.println("Rol: 1. ADMIN  2. USUARIO  (Enter para cancelar)");
            String entrada = sc.nextLine();
            if (entrada.isBlank()) return null;
            switch (entrada) {
                case "1": return Rol.ADMIN;
                case "2": return Rol.USUARIO;
                default: System.out.println("Opción inválida");
            }
        }
    }

    // ===================== PEDIDOS =====================

    public static void menuPedidos() {
        System.out.println("=== Pedidos ===");
        int opcion = menu();
        switch (opcion) {
            case 1: listarPedidos(); break;
            case 2: crearPedido(); break;
            case 3: actualizarPedido(); break;
            case 4: eliminarPedido(); break;
        }
    }

    private static void listarPedidos() {
        List<Pedido> pedidos = pedidoService.listar();
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos cargados");
            return;
        }
        for (Pedido p : pedidos) {
            System.out.println("ID: " + p.getId()
                    + " | Usuario: " + p.getUsuario().getNombre() + " " + p.getUsuario().getApellido()
                    + " | Estado: " + p.getEstado()
                    + " | Forma de pago: " + (p.getFormaPago() != null ? p.getFormaPago() : "Sin definir")
                    + " | Total: $" + p.getTotal()
                    + " | Fecha: " + p.getFecha());
        }
    }

    private static void crearPedido() {
        // FIX: cortar si no hay usuarios
        if (usuarioService.listar().isEmpty()) {
            System.out.println("No hay usuarios cargados. Debe crear un usuario primero.");
            return;
        }
        // FIX: cortar si no hay productos
        if (productoService.listar().isEmpty()) {
            System.out.println("No hay productos cargados. Debe crear un producto primero.");
            return;
        }

        listarUsuarios();
        System.out.print("Id del usuario: ");
        Long idUsuario;
        try {
            idUsuario = Long.parseLong(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Debe ingresar un número entero");
            return;
        }

        // FIX: cancelar si el usuario no elige forma de pago
        FormaPago formaPago = pedirFormaPago();
        if (formaPago == null) {
            System.out.println("Operación cancelada");
            return;
        }

        Pedido pedido;
        try {
            pedido = pedidoService.crear(idUsuario, formaPago);
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }
        System.out.println("Pedido creado con id " + pedido.getId() + ". Ahora cargá los productos.");

        boolean agregoAlMenosUno = false;
        while (true) {
            listarProductos();
            System.out.print("Id del producto a agregar (Enter para terminar): ");
            String idProductoStr = sc.nextLine();
            if (idProductoStr.isBlank()) break;

            Long idProducto;
            try {
                idProducto = Long.parseLong(idProductoStr);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero");
                continue;
            }

            System.out.print("Cantidad: ");
            int cantidad;
            try {
                cantidad = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero");
                continue;
            }

            try {
                pedidoService.agregarDetalle(pedido.getId(), idProducto, cantidad);
                agregoAlMenosUno = true;
                System.out.println("Producto agregado. Total actual: $" + pedido.getTotal());
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Se cancela la creación del pedido.");
                pedidoService.eliminar(pedido.getId());
                return;
            }
        }

        if (!agregoAlMenosUno) {
            System.out.println("No se cargó ningún producto, se cancela el pedido.");
            pedidoService.eliminar(pedido.getId());
            return;
        }

        System.out.println("Pedido finalizado. Total: $" + pedido.getTotal());
    }

    private static void actualizarPedido() {
        if (pedidoService.listar().isEmpty()) {
            System.out.println("No hay pedidos cargados");
            return;
        }
        listarPedidos();
        System.out.print("Ingrese el id del pedido a actualizar: ");
        Long id;
        try {
            id = Long.parseLong(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Debe ingresar un número entero");
            return;
        }

        System.out.print("¿Modificar estado? (S/N): ");
        if (sc.nextLine().equalsIgnoreCase("S")) {
            Estado estado = pedirEstado();
            if (estado != null) {
                try {
                    pedidoService.actualizarEstado(id, estado);
                    System.out.println("Estado actualizado");
                } catch (RuntimeException e) {
                    System.out.println("Error: " + e.getMessage());
                    return;
                }
            }
        }

        System.out.print("¿Modificar forma de pago? (S/N): ");
        if (sc.nextLine().equalsIgnoreCase("S")) {
            FormaPago formaPago = pedirFormaPago();
            if (formaPago != null) {
                try {
                    pedidoService.actualizarFormaPago(id, formaPago);
                    System.out.println("Forma de pago actualizada");
                } catch (RuntimeException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }

    private static void eliminarPedido() {
        if (pedidoService.listar().isEmpty()) {
            System.out.println("No hay pedidos cargados");
            return;
        }
        listarPedidos();
        System.out.print("Ingrese el id del pedido a eliminar: ");
        Long id;
        try {
            id = Long.parseLong(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Debe ingresar un número entero");
            return;
        }

        System.out.print("¿Confirma la eliminación? (S/N): ");
        if (!sc.nextLine().equalsIgnoreCase("S")) {
            System.out.println("Operación cancelada");
            return;
        }

        try {
            pedidoService.eliminar(id);
            System.out.println("Pedido eliminado correctamente");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static FormaPago pedirFormaPago() {
        while (true) {
            System.out.println("Forma de pago: 1. TARJETA  2. TRANSFERENCIA  3. EFECTIVO  (Enter para cancelar)");
            String entrada = sc.nextLine();
            if (entrada.isBlank()) return null;
            switch (entrada) {
                case "1": return FormaPago.TARJETA;
                case "2": return FormaPago.TRANSFERENCIA;
                case "3": return FormaPago.EFECTIVO;
                default: System.out.println("Opción inválida");
            }
        }
    }

    private static Estado pedirEstado() {
        while (true) {
            System.out.println("Estado: 1. PENDIENTE  2. CONFIRMADO  3. TERMINADO  4. CANCELADO  (Enter para cancelar)");
            String entrada = sc.nextLine();
            if (entrada.isBlank()) return null;
            switch (entrada) {
                case "1": return Estado.PENDIENTE;
                case "2": return Estado.CONFIRMADO;
                case "3": return Estado.TERMINADO;
                case "4": return Estado.CANCELADO;
                default: System.out.println("Opción inválida");
            }
        }
    }

    public static int menu() {
        int opcion = -1;
        do {
            try {
                System.out.println("1. Listar\n2. Crear\n3. Editar\n4. Eliminar");
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException nfe) {
                System.out.println("Debe ingresar un numero entero...");
            }
        } while (!List.of(1, 2, 3, 4).contains(opcion));
        return opcion;
    }
}
