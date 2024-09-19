package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ScriptModel {
    private File pastaSelecionada;
    private List<File> scripts = new ArrayList<>();

    // Define a pasta selecionada e carrega os scripts
    public void setPastaSelecionada(File pasta) {
        this.pastaSelecionada = pasta;
        carregarScripts();
    }

    // Carrega os scripts da pasta selecionada
    private void carregarScripts() {
        scripts.clear();
        if (pastaSelecionada != null) {
            File[] arquivos = pastaSelecionada.listFiles((dir, nome) -> nome.endsWith(".sh"));
            if (arquivos != null) {
                for (File arquivo : arquivos) {
                    scripts.add(arquivo);
                }
            }
        }
    }

    public List<File> getScripts() {
        return scripts;
    }

    public String executarScript(File arquivoScript) {
        StringBuilder saida = new StringBuilder();
        try {
            // Construa o comando para executar o script no Windows
            String comando = "cmd /c " + arquivoScript.getAbsolutePath();
            Process processo = Runtime.getRuntime().exec(comando);

            // Ler a saída do script
            BufferedReader entradaPadrao = new BufferedReader(new InputStreamReader(processo.getInputStream()));
            BufferedReader entradaErro = new BufferedReader(new InputStreamReader(processo.getErrorStream()));

            String linha;
            while ((linha = entradaPadrao.readLine()) != null) {
                saida.append(linha).append("\n");
            }
            while ((linha = entradaErro.readLine()) != null) {
                saida.append("ERROR: ").append(linha).append("\n");
            }

            // Espera o processo terminar
            int codigoSaida = processo.waitFor();
            if (codigoSaida == 0) {
                saida.append("Script executado com sucesso.\n");
            } else {
                saida.append("Script falhou com código de saída: ").append(codigoSaida).append("\n");
            }
        } catch (Exception e) {
            saida.append("Erro ao executar o script: ").append(e.getMessage()).append("\n");
        }
        return saida.toString();
    }
}
