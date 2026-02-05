import java.io.PrintWriter;

/**
 * CLASSE: ExportadorGephi
 * Converte a topologia consolidada em um formato CSV para análise visual.
 */
public class ExportadorGephi {

   /**
    * MÉTODO: exportarParaCSV
    * Objetivo: Cria a lista de arestas com pesos ajustados para visualização gráfica.
    */
   public void exportarParaCSV(GrafoInterface grafo, String caminho) {
      System.out.println("\nExportando para Gephi...");
      try (PrintWriter writer = new PrintWriter(caminho)) {
         // Cabeçalho compatível com o Data Laboratory do Gephi
         writer.println("Source;Target;Weight;Type");
         for (String o : grafo.obterTodosOsIps()) {
            for (Aresta a : grafo.obterAdjacentes(o)) {
               // Inversão da latência: Menor latência = maior espessura de linha no gráfico
               double peso = 1.0 / (a.getMediaLatencia() + 0.001);
               writer.printf("%s;%s;%.6f;Directed\n", o, a.getDestino(), peso);
            }
         }
         System.out.println("Arquivo '" + caminho + "' gerado com sucesso!");
      } catch (Exception e) { e.printStackTrace(); }
   }
}