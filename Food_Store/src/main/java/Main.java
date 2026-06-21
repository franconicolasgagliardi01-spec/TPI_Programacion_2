import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner sc = new Scanner(System.in);

    static void main(String[] args) {
        while (true){ //Bucle principal del programa
            int opcion = -1; //Inicializo opcion en -1 en caso de que entre a catch para que no se rompa
            boolean salir = false; //variable que finalizara el programa

            do { //valido que la opcion sea un numero valido
                try{ //Atrapo errores de formato
                    System.out.println("=== SISTEMA DE PEDIDOS (FOOD STORE) ===");
                    System.out.println("1. Categorías\n2. Productos\n3. Usuarios\n4. Pedidos\n0. Salir");
                    opcion = Integer.parseInt(sc.nextLine());
                }catch (NumberFormatException nfe){
                    System.out.println("Debe ingresar un numero entero...");
                }
            }while (!List.of(1, 2, 3, 4, 0).contains(opcion));

            switch (opcion){ //switch con opciones que llama a los submenu
                case 1:
                    menuCategorias();
                    break;
                case 2:
                    menuProductos();
                    break;
                case 3:
                    menuUsuarios();
                    break;
                case 4:
                    menuPedidos();
                    break;
                default:
                    salir = true;
            }

            if (salir){ //En caso de que salir sea verdadero rompe el bucle
                break;
            }

        }
        System.out.println("Cerrando programa...");
    }

    public static void menuCategorias(){
        System.out.println("=== Categorias ===");
        int opcion = menu();

        switch (opcion){
            case 1:
                //Logica que lista categorias
                break;
            case 2:
                //Logica que crea categorias
                break;
            case 3:
                //Logica que edita categorias
                break;
            case 4:
                //Logica que elimina categorias
                break;
        }
    }

    public static void menuProductos(){
        System.out.println("=== Productos ===");
        int opcion = menu();

        switch (opcion){
            case 1:
                //Logica que lista productos
                break;
            case 2:
                //Logica que crea productos
                break;
            case 3:
                //Logica que edita productos
                break;
            case 4:
                //Logica que elimina productos
                break;
        }
    }

    public static void menuUsuarios(){
        System.out.println("=== Usuarios ===");
        int opcion = menu();

        switch (opcion){
            case 1:
                //Logica que lista usuarios
                break;
            case 2:
                //Logica que crea usuarios
                break;
            case 3:
                //Logica que edita usuarios
                break;
            case 4:
                //Logica que elimina usuarios
                break;
        }
    }

    public static void menuPedidos(){
        System.out.println("=== Pedidos ===");
        int opcion = menu();

        switch (opcion){
            case 1:
                //Logica que lista pedidos
                break;
            case 2:
                //Logica que crea pedidos
                break;
            case 3:
                //Logica que edita pedidos
                break;
            case 4:
                //Logica que elimina pedidos
                break;
        }
    }

    public static int menu(){
        int opcion = -1;

        do {
            try {
                System.out.println("1. Listar\n2. Crear\n3. Editar\n4. Eliminar");
                opcion = Integer.parseInt(sc.nextLine());
            }catch (NumberFormatException nfe){
                System.out.println("Debe ingresar un numero entero...");
            }
        }while (!List.of(1, 2, 3, 4).contains(opcion));

        return opcion;
    }
}