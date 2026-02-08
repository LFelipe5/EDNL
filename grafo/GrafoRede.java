import java.util.*;


public class GrafoRede implements GrafoInterface {
    private Map<String, List<Aresta> > listaAdjacencia;

    // Constructor para inicializar a estrutura de dados do grafo
    public GrafoRede() {
      this.listaAdjacencia = new HashMap<>();
    }

    // Implementação dos métodos definidos na interface GrafoInterface
    @Override
    public void adicionarDadosBrutos(String origem, String destino, int bytes, double latencia) {
        // Garante que o nó de origem existe na lista de adjacência
        listaAdjacencia.putIfAbsent(origem, new ArrayList<>());
        
        // Procura a aresta existente entre origem e destino
        List<Aresta> vizinhos = listaAdjacencia.get(origem);
        Aresta arestaExistente = null;
        
        for (Aresta aresta : vizinhos) {
            if (aresta.getDestino().equals(destino)) {
                arestaExistente = aresta;
                break;
            }
        }
        
        if (arestaExistente == null) {
            // Primeira ocorrência desta conexão
            vizinhos.add(new Aresta(destino, 1, bytes, latencia));
        } else {
            // Atualizar a aresta com as novas médias
            int novoTotal = arestaExistente.getTotalPacotes() + 1;
            double novaMediaBytes = (arestaExistente.getMediaTamanhoBytes() * arestaExistente.getTotalPacotes() + bytes) / novoTotal;
            double novaMediaLatencia = (arestaExistente.getMediaLatencia() * arestaExistente.getTotalPacotes() + latencia) / novoTotal;
            
            // Remove a aresta antiga e adiciona a atualizada
            vizinhos.remove(arestaExistente);
            vizinhos.add(new Aresta(destino, novoTotal, novaMediaBytes, novaMediaLatencia));
        }
    }

    @Override
    public List<Aresta> obterAdjacentes(String ip) {
        // Lógica para retornar os vizinhos diretos de um determinado nó
        return listaAdjacencia.getOrDefault(ip, new ArrayList<>());
    }

    @Override
    public Set<String> obterTodosOsIps() {
        // Lógica para retornar o conjunto de todos os IPs únicos presentes no grafo
        Set<String> todosOsIps = new HashSet<>(listaAdjacencia.keySet());
        
        // Adiciona também os IPs de destino das arestas
        for (List<Aresta> arestas : listaAdjacencia.values()) {
            for (Aresta aresta : arestas) {
                todosOsIps.add(aresta.getDestino());
            }
        }
        
        return todosOsIps;
    }

    @Override
    public int getQuantidadeNos() {
        // Lógica para retornar a contagem total de vértices (nós) do grafo
        return obterTodosOsIps().size();
    }

    @Override
    public boolean existeIp(String ip) {
        // Lógica para validar se um determinado vértice existe na estrutura
        return obterTodosOsIps().contains(ip);
    }
}