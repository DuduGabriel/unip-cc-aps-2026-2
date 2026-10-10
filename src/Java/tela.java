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

    public void organizaTabela(){
        //CRIA A TABELA COM OS DADOS PADRÕES, DEFINE SEU TAMANHO E SEU TITULO
        tabela.setModel(sql.selectInicial());
        tabela.getColumnModel().getColumn(0).setPreferredWidth(20);
        tabela.getColumnModel().getColumn(0).setHeaderValue("ID");

        tabela.getColumnModel().getColumn(1).setPreferredWidth(30);
        tabela.getColumnModel().getColumn(1).setHeaderValue("Nome");

        tabela.getColumnModel().getColumn(2).setPreferredWidth(30);
        tabela.getColumnModel().getColumn(2).setHeaderValue("Latitude");

        tabela.getColumnModel().getColumn(3).setPreferredWidth(30);
        tabela.getColumnModel().getColumn(3).setHeaderValue("Longitude");

        tabela.getColumnModel().getColumn(4).setPreferredWidth(40);
        tabela.getColumnModel().getColumn(4).setHeaderValue("Data da Imagem");

        tabela.getColumnModel().getColumn(5).setPreferredWidth(20);
        tabela.getColumnModel().getColumn(5).setHeaderValue("Nuvens");

        tabela.getColumnModel().getColumn(6).setPreferredWidth(50);
        tabela.getColumnModel().getColumn(6).setHeaderValue("Imagem");
    }
    public JComboBox<String> carregaCmbBox(JComboBox<String> var){
        //COLOCA OS ITENS DENTRO DO JCOMBOBOX
        var.addItem("Nome");
        var.addItem("Nuvens");
        var.addItem("Latitude");
        var.addItem("Longitude");
        var.addItem("Data");
        var.addItem("Hora");
        var.addItem("Tamanho Imagem");
        var.addItem("Tamanho Arquivo");

        return var;
    }

    public void carregarTela(){
        //CRIA A FRAME
        JFrame frame = new JFrame("Programa");
        frame.setSize(700, 600);
        frame.setMinimumSize(new Dimension(600, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(scrollTabela);

        //CRIA OS PAINEIS
        JPanel panelCima = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panelCentro = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel panelBaixo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        frame.add(panelCima, BorderLayout.NORTH);
        frame.add(panelCentro,BorderLayout.CENTER);
        frame.add(panelBaixo, BorderLayout.SOUTH);

        //ADICIONA OS COMPONENTES AOS PAINEIS, E INICIA OS COMPONENTES
        panelCima.add(txtBusca);
        panelCima.add(cmbTipoPesquisa);
        panelCima.add(btnPesquisa);
        panelCentro.add(tabela);
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
