package Controller;

import Model.ScriptModel;
import View.ScriptView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ScriptController {
    private ScriptModel model;
    private ScriptView view;
    private SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ScriptController(ScriptModel model, ScriptView view) {
        this.model = model;
        this.view = view;

        // Adiciona ouvintes aos botões na visualização
        this.view.adicionarBotaoSelecionarPasta(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selecionarPasta();
            }
        });

        this.view.adicionarOuvidorBotaoExecutarScript(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executarScriptsSelecionados();
            }
        });
    }

    private void selecionarPasta() {
        JFileChooser seletorDePasta = new JFileChooser();
        seletorDePasta.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int valorRetorno = seletorDePasta.showOpenDialog(view.getJanela());
        if (valorRetorno == JFileChooser.APPROVE_OPTION) {
            File pastaSelecionada = seletorDePasta.getSelectedFile();
            model.setPastaSelecionada(pastaSelecionada); // Atualizado para o nome correto do método
            view.atualizarListaScripts(model.getScripts());
        }
    }

    private void executarScriptsSelecionados() {
        List<File> scripts = model.getScripts();
        StringBuilder resultado = new StringBuilder();
        boolean sucesso = true;

        for (JCheckBox checkBox : view.getCaixasSelecaoScripts()) {
            if (checkBox.isSelected()) {
                File arquivoScript = new File(checkBox.getActionCommand());
                String nomeScript = arquivoScript.getName();
                String timestamp = formatoData.format(new Date());
                resultado.append("[").append(timestamp).append("] Executando script: ").append(nomeScript).append("\n");

                String resultadoExecucao = model.executarScript(arquivoScript); // Atualizado para o nome correto do método
                resultado.append(resultadoExecucao).append("\n");

                // Logar resultados
                view.adicionarLog("[" + timestamp + "] Executando script: " + nomeScript);
                view.adicionarLog(resultadoExecucao);

                if (resultadoExecucao.contains("ERROR:") || resultadoExecucao.contains("Script falhou")) { // Atualizado para "ERROR:"
                    sucesso = false;
                }
            }
        }

        view.setSaida(resultado.toString());

        // Mostrar caixa de diálogo após a execução
        if (sucesso) {
            JOptionPane.showMessageDialog(view.getJanela(), "Scripts executados com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view.getJanela(), "Alguns scripts falharam durante a execução.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
