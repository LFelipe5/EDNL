import java.io.BufferedReader;
import java.io.FileReader;

/**
 * CLASSE: LeitorJSON
 * Implementa a leitura de arquivos texto linha a linha para otimização de memória RAM.
 */
public class LeitorJSON implements LeitorDadosInterface {

   /**
    * MÉTODO: carregarParaGrafo
    * Objetivo: Transforma o arquivo JSON em dados estruturados dentro do grafo.
    */
   @Override
   public void carregarParaGrafo(String caminho, GrafoInterface grafo) {
      try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
         String linha;
         while ((linha = br.readLine()) != null) {
            // Parsing simplificado para evitar dependências de bibliotecas externas (Jackson/Gson)
            if (linha.contains("{")) {
               String o = extrairValor(linha, "\"origem\": \"", "\"");
               String d = extrairValor(linha, "\"destino\": \"", "\"");
               int b = Integer.parseInt(extrairValor(linha, "\"bytes\": ", ","));
               // Limpeza de caracteres especiais na latência
               double l = Double.parseDouble(linha.split("\"latencia\":")[1].replaceAll("[}\\],\\s]", ""));
               grafo.adicionarDadosBrutos(o, d, b, l);
            }
         }
      } catch (Exception e) { e.printStackTrace(); }
   }

   /**
    * MÉTODO PRIVADO: extrairValor
    * Objetivo: Encontra e recorta o conteúdo de uma chave JSON.
    */
   private String extrairValor(String l, String c, String d) {
      int inicio = l.indexOf(c) + c.length();
      return l.substring(inicio, l.indexOf(d, inicio));
   }
}