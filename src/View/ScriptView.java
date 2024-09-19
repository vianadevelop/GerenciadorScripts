package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;

public class ScriptView {
    private JFrame janela;
    private JButton botaoSelecionarPasta;
    private JButton botaoExecutarScript;
    private JButton botaoModoClaro;
    private JButton botaoModoEscuro;
    private JTextArea areaSaida;
    private JTextArea areaLog;
    private JPanel painelScripts;
    private PainelFundo painelFundo; // Painel com imagem de fundo
    private JPanel painelBotoes; // Painel para os botões
    private JPanel painelConfiguracoes; // Painel de configurações

    private Color corFundoModoClaro = Color.WHITE;
    private Color corTextoModoClaro = Color.BLACK;
    private Color corFundoModoEscuro = Color.DARK_GRAY;
    private Color corTextoModoEscuro = Color.WHITE;

    public ScriptView() {
        janela = new JFrame("Gerenciador de Scripts");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setSize(410, 310); // Tamanho ajustado

        // Inicializar os botões
        botaoSelecionarPasta = new JButton("Selecionar a pasta");
        botaoExecutarScript = new JButton("Executar Script");
        botaoModoClaro = new JButton("Modo Claro");
        botaoModoEscuro = new JButton("Modo Escuro");

        // Ajustar o tamanho dos botões
        Dimension tamanhoBotao = new Dimension(120, 30); // Tamanho menor para os botões
        botaoSelecionarPasta.setPreferredSize(tamanhoBotao);
        botaoExecutarScript.setPreferredSize(tamanhoBotao);
        botaoModoClaro.setPreferredSize(tamanhoBotao);
        botaoModoEscuro.setPreferredSize(tamanhoBotao);

        // Diminuir o tamanho da fonte dos botões
        Font fonteBotao = new Font("Arial", Font.PLAIN, 12); // Tamanho da fonte ajustado
        botaoSelecionarPasta.setFont(fonteBotao);
        botaoExecutarScript.setFont(fonteBotao);
        botaoModoClaro.setFont(fonteBotao);
        botaoModoEscuro.setFont(fonteBotao);

        areaSaida = new JTextArea();
        areaSaida.setEditable(false);
        areaLog = new JTextArea();
        areaLog.setEditable(false);

        painelScripts = new JPanel();
        painelScripts.setLayout(new BoxLayout(painelScripts, BoxLayout.Y_AXIS));

        // Painel para os botões
        painelBotoes = new JPanel();
        painelBotoes.setLayout(new FlowLayout()); // Layout para os botões lado a lado
        painelBotoes.add(botaoSelecionarPasta);
        painelBotoes.add(botaoExecutarScript);

        // Criar o painel de configurações
        painelConfiguracoes = new JPanel();
        painelConfiguracoes.setLayout(new GridLayout(2, 1)); // Layout para botões empilhados

        // Adicionar botões de tema ao painel de configurações
        painelConfiguracoes.add(botaoModoClaro);
        painelConfiguracoes.add(botaoModoEscuro);

        // Carregar a imagem de fundo
        Image imagemFundo = carregarImagem("util/images.jpeg"); // Caminho ajustado
        painelFundo = new PainelFundo(imagemFundo);

        // Configuração dos painéis
        painelFundo.setLayout(new BorderLayout());
        painelFundo.add(new JScrollPane(painelScripts), BorderLayout.CENTER);

        // Criar o JTabbedPane e adicionar abas
        JTabbedPane painelGuias = new JTabbedPane();
        painelGuias.addTab("Scripts", painelFundo);
        painelGuias.addTab("Logs", new JScrollPane(areaLog));
        painelGuias.addTab("Configurações", painelConfiguracoes); // Adiciona a aba de configurações

        // Adicionar o painelGuias à janela
        janela.setLayout(new BorderLayout());
        janela.add(painelGuias, BorderLayout.CENTER); // Adicionar o JTabbedPane no centro
        janela.add(painelBotoes, BorderLayout.SOUTH); // Adicionar os botões na parte inferior
        janela.add(new JScrollPane(areaSaida), BorderLayout.EAST); // Adicionar a área de saída no lado direito

        janela.setVisible(true);

        // Adicionar ouvintes de ação para os botões de tema
        botaoModoClaro.addActionListener(e -> setModoClaro());
        botaoModoEscuro.addActionListener(e -> setModoEscuro());

        // Inicializa no modo claro
        setModoClaro();
    }

    private Image carregarImagem(String caminho) {
        try {
            BufferedImage img = ImageIO.read(new File(caminho)); // Ajustado para ler o arquivo diretamente
            return img;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JFrame getJanela() {
        return janela;
    }

    public void adicionarBotaoSelecionarPasta(ActionListener ouvidor) {
        botaoSelecionarPasta.addActionListener(ouvidor);
    }

    public void adicionarOuvidorBotaoExecutarScript(ActionListener ouvidor) {
        botaoExecutarScript.addActionListener(ouvidor);
    }

    public void atualizarListaScripts(List<File> scripts) {
        painelScripts.removeAll();
        for (File script : scripts) {
            JCheckBox caixaSelecao = new JCheckBox(script.getName());
            caixaSelecao.setActionCommand(script.getAbsolutePath());
            painelScripts.add(caixaSelecao);
        }
        painelScripts.revalidate();
        painelScripts.repaint();
    }

    public List<JCheckBox> getCaixasSelecaoScripts() {
        Component[] componentes = painelScripts.getComponents();
        List<JCheckBox> caixasSelecao = new ArrayList<>();
        for (Component componente : componentes) {
            if (componente instanceof JCheckBox) {
                caixasSelecao.add((JCheckBox) componente);
            }
        }
        return caixasSelecao;
    }

    public void setSaida(String saida) {
        areaSaida.setText(saida);
    }

    public void adicionarLog(String mensagemLog) {
        areaLog.append(mensagemLog + "\n");
    }

    // Métodos para alternar o tema
    public void setModoClaro() {
        painelFundo.setBackground(corFundoModoClaro);
        painelScripts.setBackground(corFundoModoClaro);
        areaSaida.setBackground(corFundoModoClaro);
        areaLog.setBackground(corFundoModoClaro);
        areaSaida.setForeground(corTextoModoClaro);
        areaLog.setForeground(corTextoModoClaro);
        UIManager.put("Panel.background", corFundoModoClaro);
        UIManager.put("TextArea.background", corFundoModoClaro);
        UIManager.put("TextArea.foreground", corTextoModoClaro);
        SwingUtilities.updateComponentTreeUI(janela);
    }

    public void setModoEscuro() {
        painelFundo.setBackground(corFundoModoEscuro);
        painelScripts.setBackground(corFundoModoEscuro);
        areaSaida.setBackground(corFundoModoEscuro);
        areaLog.setBackground(corFundoModoEscuro);
        areaSaida.setForeground(corTextoModoEscuro);
        areaLog.setForeground(corTextoModoEscuro);
        UIManager.put("Panel.background", corFundoModoEscuro);
        UIManager.put("TextArea.background", corFundoModoEscuro);
        UIManager.put("TextArea.foreground", corTextoModoEscuro);
        SwingUtilities.updateComponentTreeUI(janela);
    }

    // Classe interna para o painel com imagem de fundo
    public class PainelFundo extends JPanel {
        private Image imagemFundo;

        public PainelFundo(Image imagem) {
            this.imagemFundo = imagem;
            setOpaque(false); // Tornar o painel transparente para ver a imagem de fundo
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(imagemFundo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
