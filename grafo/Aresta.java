/**
 * CLASSE: Aresta
 * Representa a conexão consolidada entre dois dispositivos na rede.
 */
public class Aresta {
   // ATRIBUTOS

   private String destino;           // Endereço IP do nó de destino (Vértice adjacente)
   private int totalPacotes;         // Número total de ocorrências desta conexão no log
   private double mediaTamanhoBytes;  // Média do volume de dados trafegados nesta aresta
   private double mediaLatencia;     // O "PESO" da aresta: Tempo médio de resposta (custo)

   /**
    * CONSTRUTOR: Inicializa os dados processados da conexão.
    */
   public Aresta(String destino, int totalPacotes, double mediaTamanhoBytes, double mediaLatencia) {
      this.destino = destino;
      this.totalPacotes = totalPacotes;
      this.mediaTamanhoBytes = mediaTamanhoBytes;
      this.mediaLatencia = mediaLatencia;
   }

   // MÉTODOS GETTERS: Permitem que os algoritmos de busca leiam os pesos e destinos
   public String getDestino() { return destino; }
   public double getMediaLatencia() { return mediaLatencia; }
   public int getTotalPacotes() { return totalPacotes; }
   public double getMediaTamanhoBytes() { return mediaTamanhoBytes; }
}