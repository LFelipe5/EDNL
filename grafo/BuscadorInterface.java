/**
 * INTERFACE: BuscadorInterface
 * Contrato para os algoritmos de busca e análise de caminhos.
 */
public interface BuscadorInterface {
   // Retorna o caminho de menor custo (latência) entre dois pontos
   BuscadorRede.ResultadoBusca encontrarCaminhoDijkstra(GrafoInterface grafo, String inicio, String fim);
   
   // Realiza a travessia em largura para explorar a vizinhança
   void executarBFS(GrafoInterface grafo, String inicio, String destino);
   
   // Calcula a latência mínima entre todos os pares de nós simultaneamente
   void executarFloydWarshall(GrafoInterface grafo, String de, String para);
   
   // Executa uma rota de visitação completa baseada em proximidade
   void executarTSP(GrafoInterface grafo, String inicio, String destino);
}