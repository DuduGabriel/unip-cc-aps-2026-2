package conexao;
import net.proteanit.sql.DbUtils;
import javax.swing.table.TableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class SQL {
    String sqlSelect = "SELECT * FROM imagens";

    public TableModel selectInicial() {
        try (Connection conn = Connect.conectar();
             PreparedStatement stmt = conn.prepareStatement(sqlSelect)){

            ResultSet rs = stmt.executeQuery();
            return DbUtils.resultSetToTableModel(rs);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}