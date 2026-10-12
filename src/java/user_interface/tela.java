package user_interface;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import conexao.SQL;

public class tela {
    private SQL sql = new SQL();
    private JTable tabela = new JTable();
    private JScrollPane scrollTabela = new JScrollPane(tabela);
    private JButton btnSair = new JButton("Sair");
    private JTextField txtBusca = new JTextField(40);
    private JComboBox<String> cmbTipoPesquisa = new JComboBox<>();
    private JButton btnPesquisa = new JButton("Pesquisar");

    public void organizaTabela() {
        //CRIA A TABELA COM OS DADOS PADRÕES, DEFINE SEU TAMANHO E SEU TITULO
        tabela.setModel(sql.selectInicial());

        tabela.setRowHeight(85);
        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabela.getTableHeader().setPreferredSize(new Dimension(0, 30));
        tabela.getTableHeader().setReorderingAllowed(false);

        int[] larguras = {120, 50, 100, 100, 130, 250, 130};

        for (int i = 0; i < larguras.length; i++)
            tabela.getColumnModel().getColumn(i).setPreferredWidth(larguras[i]);
    }


    public JComboBox<String> carregaCmbBox(JComboBox<String> var){
        //COLOCA OS ITENS DENTRO DO JCOMBOBOX
        var.addItem("ID");
        var.addItem("Latitude");
        var.addItem("Longitude");
        var.addItem("Tamanho Arquivo");
        var.addItem("Nome arquivo");
        var.addItem("Data");

        return var;
    }

    public void carregarTela(){
        //CRIA A FRAME
        JFrame frame = new JFrame("Programa");
        frame.setSize(700, 600);
        frame.setMinimumSize(new Dimension(600, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        //CRIA OS PAINEIS
        JPanel panelCima = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panelCentro = new JPanel(new BorderLayout());
        JPanel panelBaixo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        frame.add(panelCima, BorderLayout.NORTH);
        frame.add(panelCentro,BorderLayout.CENTER);
        frame.add(panelBaixo, BorderLayout.SOUTH);

        //ADICIONA OS COMPONENTES AOS PAINEIS, E INICIA OS COMPONENTES
        panelCima.add(txtBusca);
        panelCima.add(cmbTipoPesquisa);
        panelCima.add(btnPesquisa);
        panelCentro.add(scrollTabela, BorderLayout.CENTER);
        panelBaixo.add(btnSair);

        cmbTipoPesquisa = carregaCmbBox(cmbTipoPesquisa);
        txtBusca.setSize(300, 30);
        cmbTipoPesquisa.setSize(250, 30);
        btnPesquisa.setSize(250, 30);
        tabela.setPreferredScrollableViewportSize(new Dimension(500, 200));
        btnSair.setSize(500, 30);

        organizaTabela();

        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        btnPesquisa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, cmbTipoPesquisa.getSelectedItem()
                        + "\n" + txtBusca.getText());
            }
        });

        frame.setVisible(true);
    }

    static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new tela().carregarTela();
        });
    }
}
