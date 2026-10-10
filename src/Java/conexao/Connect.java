package conexao;
import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {

    private static final String URL = "jdbc:mysql://localhost:3306/aps_26_2";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection conectar() throws Exception {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro na conexão.\n\nO XAMPP está ligado?");
            throw new RuntimeException("Erro na conexão. O XAMPP está ligado? \n\n" + e.getMessage(), e);
        }
    }
}