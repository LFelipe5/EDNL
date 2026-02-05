import java.util.*;

/**
 * CLASSE: AppRede
 * Ponto de entrada (Main). Coordena o fluxo de carga, interação e exibição.
 */
public class AppRede {
   public static void main(String[] args) {
      // INICIALIZAÇÃO DE ATRIBUTOS (Implementações via Polimorfismo)
      Scanner sc = new Scanner(System.in);
      GrafoInterface grafo = new GrafoRede();
      BuscadorInterface buscador = new BuscadorRede();
      LeitorJSON leitor = new LeitorJSON();

      System.out.println(">>> ANALISADOR DE REDE EM GRAFOS <<<");
      leitor.carregarParaGrafo("trafego_rede.json", grafo);

      // LOOP DE VALIDAÇÃO: Garante que os IPs informados existam no grafo carregado
      String o, d;
      while (true) {
         System.out.print("\nIP Origem (" + BuscadorRede.ANSI_BRIGHT_GREEN + "Verde" + BuscadorRede.ANSI_RESET + "): "); 
         o = sc.nextLine().trim();
         System.out.print("IP Destino (" + BuscadorRede.ANSI_BRIGHT_BLUE + "Azul" + BuscadorRede.ANSI_RESET + "): "); 
         d = sc.nextLine().trim();
         
         if (grafo.existeIp(o) && grafo.existeIp(d)) break;
         System.out.println("Erro: IPs não encontrados na topologia.");
      }

      // EXIBIÇÃO DIJKSTRA: Requer laço de repetição manual para colorir as setas '->'
      BuscadorRede.ResultadoBusca res = buscador.encontrarCaminhoDijkstra(grafo, o, d);
      if (res != null) {
         System.out.print("\n[DIJKSTRA] Rota: ");
         List<String> path = res.getCaminho();
         for(int i=0; i<path.size(); i++) {
            String ip = path.get(i);
            // Aplica cores conforme a posição do IP no trajeto
            if(ip.equals(o)) System.out.print(BuscadorRede.ANSI_BRIGHT_GREEN + ip + BuscadorRede.ANSI_RESET);
            else if(ip.equals(d)) System.out.print(BuscadorRede.ANSI_BRIGHT_BLUE + ip + BuscadorRede.ANSI_RESET);
            else System.out.print(ip);
            
            if(i < path.size()-1) System.out.print(" -> ");
         }
         System.out.printf(Locale.US, "\nLatência total: %.6f s\n", res.getLatenciaTotal());
      }

      // EXECUÇÃO DOS DEMAIS MÉTODOS ANALÍTICOS
      buscador.executarBFS(grafo, o, d);
      buscador.executarFloydWarshall(grafo, o, d);
      buscador.executarTSP(grafo, o, d);

      // FINALIZAÇÃO: Exportação para análise visual
      new ExportadorGephi().exportarParaCSV(grafo, "analise_gephi.csv");
      sc.close();
   }
}