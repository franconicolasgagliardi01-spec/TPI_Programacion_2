import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");

            Connection conn =
                    DriverManager.getConnection("jdbc:sqlite:test.db");

            System.out.println("Conexión exitosa");
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}