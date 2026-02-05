import java.util.List;
import java.util.Set;

/**
 * INTERFACE: GrafoInterface
 * Define o contrato para a estrutura de dados que armazenará a topologia da rede.
 */
public interface GrafoInterface {
   // Adiciona uma conexão bruta e consolida pesos (médias)
   void adicionarDadosBrutos(String origem, String destino, int bytes, double latencia);
   
   // Retorna os vizinhos diretos de um determinado nó
   List<Aresta> obterAdjacentes(String ip);
   
   // Retorna o conjunto de todos os IPs únicos presentes no grafo
   Set<String> obterTodosOsIps();
   
   // Retorna a contagem total de vértices (nós) do grafo
   int getQuantidadeNos();
   
   // Valida se um determinado vértice existe na estrutura
   boolean existeIp(String ip);
}