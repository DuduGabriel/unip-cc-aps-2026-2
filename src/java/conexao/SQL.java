package conexao;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.sql.*;


public class SQL {


    public TableModel selectInicial()  {
        String sqlSelectInicial = """
            SELECT id, imagem, latitude, longitude,
                   tamanho_arquivo, nome_arquivo, data_captura
            FROM imagens
            ORDER BY id ASC
            LIMIT 10
            """;

        String[] colunas = {
                "Imagem",
                "ID",
                "Latitude",
                "Longitude",
                "Tamanho (bytes)",
                "Nome do arquivo",
                "Data de captura"
        };

        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public Class<?> getColumnClass(int coluna) {
                if (coluna == 0)
                    return ImageIcon.class;
                return Object.class;
            }

            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };

        try (Connection conn = Connect.conectar(); PreparedStatement stmt = conn.prepareStatement(sqlSelectInicial); ResultSet rs = stmt.executeQuery()){

            while (rs.next()){
                ImageIcon miniatura = null;
                Blob blob = rs.getBlob("imagem");

                if(blob != null){
                    try{
                        byte[] bytes = blob.getBytes(1, (int) blob.length());
                        System.out.println("ID: " + rs.getInt("id") + " | Tamanho BLOB: " + bytes.length + " bytes");
                        BufferedImage imagem = ImageIO.read(new ByteArrayInputStream(bytes));

                        if (imagem != null){
                            Image imagemMiniatura = imagem.getScaledInstance(100, 75, Image.SCALE_SMOOTH);
                            miniatura = new ImageIcon(imagemMiniatura);

                            System.out.println("Imagem carregada");
                        }
                        else{
                            System.out.println("Formato de imagem não reconhecido para ID: " + rs.getInt("id"));
                        }
                    }
                    catch (Exception e){
                        System.out.println("Erro ao carregar Blob da imagem de ID " + rs.getInt("id"));
                    }
                    finally {
                        blob.free();
                    }
                }
                model.addRow(new Object[]{
                        miniatura,
                        rs.getInt("id"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude"),
                        rs.getLong("tamanho_arquivo"),
                        rs.getString("nome_arquivo"),
                        rs.getDate("data_captura")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }
}