import java.util.*;

public class BuscadorRede implements BuscadorInterface {
    // Constantes de cores ANSI
    public static final String ANSI_BRIGHT_GREEN = "\u001B[92m";
    public static final String ANSI_BRIGHT_BLUE = "\u001B[94m";
    public static final String ANSI_RESET = "\u001B[0m";

    // Classe interna para armazenar o resultado da busca
    public static class ResultadoBusca {
        private List<String> caminho;
        private double latenciaTotal;

        public ResultadoBusca(List<String> caminho, double latenciaTotal) {
            this.caminho = caminho;
            this.latenciaTotal = latenciaTotal;
        }

        public List<String> getCaminho() {
            return caminho;
        }

        public double getLatenciaTotal() {
            return latenciaTotal;
        }
    }

    // Implementação dos métodos definidos na interface BuscadorInterface

    @Override
    public ResultadoBusca encontrarCaminhoDijkstra(GrafoInterface grafo, String inicio, String fim) {
        // Validação dos nós
        if (!grafo.existeIp(inicio) || !grafo.existeIp(fim)) {
            return null;
        }

        // Mapa de distâncias mínimas (latências)
        Map<String, Double> distancias = new HashMap<>();
        // Mapa de predecessores para reconstruir o caminho
        Map<String, String> predecessores = new HashMap<>();
        // Conjunto de nós visitados
        Set<String> visitados = new HashSet<>();
        // Fila de prioridade para sempre processar o nó com menor distância
        PriorityQueue<String> fila = new PriorityQueue<>(
            Comparator.comparingDouble(distancias::get)
        );

        // Inicialização
        Set<String> todosOsIps = grafo.obterTodosOsIps();
        for (String ip : todosOsIps) {
            distancias.put(ip, Double.MAX_VALUE);
            predecessores.put(ip, null);
        }
        distancias.put(inicio, 0.0);
        fila.offer(inicio);

        // Algoritmo de Dijkstra
        while (!fila.isEmpty()) {
            String noAtual = fila.poll();

            if (visitados.contains(noAtual)) {
                continue;
            }

            visitados.add(noAtual);

            // Para cada vizinho do nó atual
            List<Aresta> adjacentes = grafo.obterAdjacentes(noAtual);
            for (Aresta aresta : adjacentes) {
                String vizinho = aresta.getDestino();
                double latenciaAresta = aresta.getMediaLatencia();
                double novaDistancia = distancias.get(noAtual) + latenciaAresta;

                // Se encontramos um caminho mais curto, atualizamos
                if (novaDistancia < distancias.get(vizinho)) {
                    distancias.put(vizinho, novaDistancia);
                    predecessores.put(vizinho, noAtual);
                    fila.offer(vizinho);
                }
            }
        }

        // Verifica se existe caminho
        if (distancias.get(fim) == Double.MAX_VALUE) {
            return null; // Sem caminho disponível
        }

        // Reconstrói o caminho
        List<String> caminho = new ArrayList<>();
        String noAtual = fim;
        while (noAtual != null) {
            caminho.add(0, noAtual);
            noAtual = predecessores.get(noAtual);
        }

        return new ResultadoBusca(caminho, distancias.get(fim));
    }

    @Override
    public void executarBFS(GrafoInterface grafo, String inicio, String destino) {
        // Lógica para realizar a travessia em largura para explorar a vizinhança
        // Validação dos nós
        if (!grafo.existeIp(inicio) || !grafo.existeIp(destino)) {
            System.out.println("[BFS] IPs inválidos para busca.");
            return;
        }

        Queue<String> fila = new LinkedList<>();
        Set<String> visitados = new HashSet<>();
        Map<String, Integer> nivel = new HashMap<>();
        Map<String, String> predecessores = new HashMap<>();
        List<String> ordemVisita = new ArrayList<>();

        // Inicializa
        fila.offer(inicio);
        visitados.add(inicio);
        nivel.put(inicio, 0);
        predecessores.put(inicio, null);

        boolean encontrado = false;

        // BFS clássico (por saltos)
        while (!fila.isEmpty()) {
            String atual = fila.poll();
            ordemVisita.add(atual);

            if (atual.equals(destino)) {
                encontrado = true;
                break; // Destino já encontrado: menor número de saltos garantido
            }

            List<Aresta> adjacentes = grafo.obterAdjacentes(atual);
            for (Aresta aresta : adjacentes) {
                String vizinho = aresta.getDestino();
                if (!visitados.contains(vizinho)) {
                    visitados.add(vizinho);
                    predecessores.put(vizinho, atual);
                    nivel.put(vizinho, nivel.get(atual) + 1);
                    fila.offer(vizinho);
                }
            }
        }

        // Exibição de ordem de visita
        System.out.print("\n[BFS] Ordem de visita: ");
        for (int i = 0; i < ordemVisita.size(); i++) {
            String ip = ordemVisita.get(i);
            if (ip.equals(inicio)) System.out.print(ANSI_BRIGHT_GREEN + ip + ANSI_RESET);
            else if (ip.equals(destino)) System.out.print(ANSI_BRIGHT_BLUE + ip + ANSI_RESET);
            else System.out.print(ip);

            if (i < ordemVisita.size() - 1) System.out.print(" -> ");
        }
        System.out.println();

        if (!encontrado) {
            System.out.println("[BFS] Destino não alcançável a partir da origem.");
            return;
        }

        // Reconstrução do caminho em número mínimo de saltos
        List<String> caminho = new ArrayList<>();
        String noAtual = destino;
        while (noAtual != null) {
            caminho.add(0, noAtual);
            noAtual = predecessores.get(noAtual);
        }

        System.out.print("\n[BFS] Menor caminho (saltos): ");
        for (int i = 0; i < caminho.size(); i++) {
            String ip = caminho.get(i);
            if (ip.equals(inicio)) System.out.print(ANSI_BRIGHT_GREEN + ip + ANSI_RESET);
            else if (ip.equals(destino)) System.out.print(ANSI_BRIGHT_BLUE + ip + ANSI_RESET);
            else System.out.print(ip);

            if (i < caminho.size() - 1) System.out.print(" -> ");
        }
        System.out.println();
        System.out.println("[BFS] Saltos: " + (caminho.size() - 1));
    }

    @Override
    public void executarFloydWarshall(GrafoInterface grafo, String de, String para) {
        // Lógica para calcular a latência mínima entre todos os pares de nós simultaneamente usando Floyd-Warshall
        // Validação dos nós
        if (!grafo.existeIp(de) || !grafo.existeIp(para)) {
            System.out.println("[Floyd-Warshall] IPs inválidos para cálculo.");
            return;
        }

        // Lista ordenada de nós para indexação
        List<String> nosList = new ArrayList<>(grafo.obterTodosOsIps());
        int n = nosList.size();
        Map<String, Integer> idx = new HashMap<>();
        for (int i = 0; i < n; i++) idx.put(nosList.get(i), i);

        final double INF = Double.POSITIVE_INFINITY;
        double[][] dist = new double[n][n];
        Integer[][] next = new Integer[n][n]; // para reconstruir caminho

        // Inicialização
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dist[i][j] = (i == j) ? 0.0 : INF;
                next[i][j] = null;
            }
        }

        // Preenche as arestas a partir da lista de adjacência
        for (String u : nosList) {
            int ui = idx.get(u);
            List<Aresta> adj = grafo.obterAdjacentes(u);
            for (Aresta a : adj) {
                String v = a.getDestino();
                if (!idx.containsKey(v)) continue;
                int vi = idx.get(v);
                double w = a.getMediaLatencia();
                // Se houver múltiplas arestas, mantém a menor latência
                if (w < dist[ui][vi]) {
                    dist[ui][vi] = w;
                    next[ui][vi] = vi;
                }
            }
        }

        // Floyd–Warshall principal
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                if (dist[i][k] == INF) continue;
                for (int j = 0; j < n; j++) {
                    if (dist[k][j] == INF) continue;
                    double nd = dist[i][k] + dist[k][j];
                    if (nd < dist[i][j]) {
                        dist[i][j] = nd;
                        next[i][j] = next[i][k];
                    }
                }
            }
        }

        int si = idx.get(de);
        int ti = idx.get(para);

        if (dist[si][ti] == INF) {
            System.out.println("[Floyd-Warshall] Não existe caminho entre os nós informados.");
            return;
        }

        // Reconstrói o caminho usando a matriz `next`
        List<String> caminho = new ArrayList<>();
        int u = si;
        caminho.add(nosList.get(u));
        while (u != ti) {
            Integer nx = next[u][ti];
            if (nx == null) { // alguma inconsistência
                System.out.println("[Floyd-Warshall] Falha na reconstrução do caminho.");
                return;
            }
            u = nx;
            caminho.add(nosList.get(u));
        }

        // Exibe resultado
        System.out.print("\n[Floyd-Warshall] Menor latência entre ");
        System.out.print(ANSI_BRIGHT_GREEN + de + ANSI_RESET + " e " + ANSI_BRIGHT_BLUE + para + ANSI_RESET + ": ");
        System.out.printf(Locale.US, "%.6f s\n", dist[si][ti]);

        System.out.print("[Floyd-Warshall] Caminho: ");
        for (int i = 0; i < caminho.size(); i++) {
            String ip = caminho.get(i);
            if (ip.equals(de)) System.out.print(ANSI_BRIGHT_GREEN + ip + ANSI_RESET);
            else if (ip.equals(para)) System.out.print(ANSI_BRIGHT_BLUE + ip + ANSI_RESET);
            else System.out.print(ip);

            if (i < caminho.size() - 1) System.out.print(" -> ");
        }
        System.out.println();
    }

    @Override
    public void executarTSP(GrafoInterface grafo, String inicio, String destino) {
        // Lógica para executar uma rota de visitação completa baseada em proximidade usando TSP
        // Validação básica
        if (!grafo.existeIp(inicio) || !grafo.existeIp(destino)) {
            System.out.println("[TSP] IPs inválidos para execução.");
            return;
        }

        // Conjunto de nós a visitar (toda a topologia)
        Set<String> todos = new HashSet<>(grafo.obterTodosOsIps());
        if (todos.isEmpty()) {
            System.out.println("[TSP] Grafo vazio.");
            return;
        }

        // Remover a origem da lista de não visitados (já iniciamos por ela)
        Set<String> naoVisitados = new HashSet<>(todos);
        naoVisitados.remove(inicio);

        List<String> rota = new ArrayList<>();
        rota.add(inicio);
        double latenciaTotal = 0.0;
        String atual = inicio;

        // Heurística: vizinho mais próximo (usar Dijkstra se não houver aresta direta)
        while (!naoVisitados.isEmpty()) {
            String melhor = null;
            double melhorDist = Double.POSITIVE_INFINITY;
            List<String> melhorCaminho = null;

            // Para cada candidato não visitado, calcula distância mínima do nó atual
            for (String candidato : naoVisitados) {
                // Primeiro tenta buscar aresta direta mais barata
                double direta = Double.POSITIVE_INFINITY;
                for (Aresta a : grafo.obterAdjacentes(atual)) {
                    if (a.getDestino().equals(candidato)) {
                        direta = Math.min(direta, a.getMediaLatencia());
                    }
                }

                if (direta < melhorDist) {
                    melhor = candidato;
                    melhorDist = direta;
                    melhorCaminho = Arrays.asList(atual, candidato);
                }

                // Se não houver ligação direta barata, usa Dijkstra para obter caminho e distância
                BuscadorRede.ResultadoBusca res = encontrarCaminhoDijkstra(grafo, atual, candidato);
                if (res != null && res.getLatenciaTotal() < melhorDist) {
                    melhor = candidato;
                    melhorDist = res.getLatenciaTotal();
                    melhorCaminho = res.getCaminho();
                }
            }

            if (melhor == null || melhorCaminho == null) {
                System.out.println("[TSP] Não foi possível alcançar os nós restantes a partir de " + atual);
                return;
            }

            // Anexa o caminho encontrado à rota (omitindo o primeiro nó porque é o atual)
            for (int i = 1; i < melhorCaminho.size(); i++) {
                rota.add(melhorCaminho.get(i));
            }
            latenciaTotal += melhorDist;

            // Atualiza estado
            atual = melhor;
            naoVisitados.remove(melhor);
        }

        // Garante que terminamos no nó destino: se não, conecta usando Dijkstra
        if (!atual.equals(destino)) {
            BuscadorRede.ResultadoBusca res = encontrarCaminhoDijkstra(grafo, atual, destino);
            if (res == null) {
                System.out.println("[TSP] Não foi possível alcançar o destino final a partir de " + atual);
                return;
            }
            List<String> caminhoParaDestino = res.getCaminho();
            // Anexa (omitindo primeiro elemento porque é o atual)
            for (int i = 1; i < caminhoParaDestino.size(); i++) rota.add(caminhoParaDestino.get(i));
            latenciaTotal += res.getLatenciaTotal();
        }

        // Exibe rota e latência
        System.out.print("\n[TSP] Rota (heurística vizinho mais próximo): ");
        for (int i = 0; i < rota.size(); i++) {
            String ip = rota.get(i);
            if (ip.equals(inicio)) System.out.print(ANSI_BRIGHT_GREEN + ip + ANSI_RESET);
            else if (ip.equals(destino)) System.out.print(ANSI_BRIGHT_BLUE + ip + ANSI_RESET);
            else System.out.print(ip);

            if (i < rota.size() - 1) System.out.print(" -> ");
        }
        System.out.println();
        System.out.printf(Locale.US, "[TSP] Latência total aproximada: %.6f s\n", latenciaTotal);
    }
}