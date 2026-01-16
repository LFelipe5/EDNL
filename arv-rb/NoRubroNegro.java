public class NoRubroNegro<T> {

   private T dado;
   private NoRubroNegro<T> pai;
   private NoRubroNegro<T> esquerda;
   private NoRubroNegro<T> direita;
   private Cor cor;

   // Construtor
   public NoRubroNegro(T dado) {
      this.setDado(dado);
      this.cor = Cor.VERMELHO;
   }

   // Retorna o dado armazenado neste nó.
   public T getDado() { return this.dado; }

   // Define o dado armazenado neste nó.
   public void setDado(T dado) {
      if (dado == null) throw new IllegalArgumentException("Dados não podem ser nulos.");
      this.dado = dado;
   }

   // Retorna o nó pai.
   public NoRubroNegro<T> getPai() { return this.pai; }

   // Define o nó pai. (Usado internamente pelos setters)
   public void setPai(NoRubroNegro<T> pai) { this.pai = pai; }

   // Retorna o filho da esquerda.
   public NoRubroNegro<T> getEsquerda() { return this.esquerda; }

   // Define o filho da esquerda.
   public void setEsquerda(NoRubroNegro<T> filho) {
      this.esquerda = filho;
      if (filho != null) filho.setPai(this);
   }

   // Retorna o filho da direita.
   public NoRubroNegro<T> getDireita() { return this.direita; }

   // Define o filho da direita.
   public void setDireita(NoRubroNegro<T> filho) {
      this.direita = filho;
      if (filho != null) filho.setPai(this);
   }

   // Retorna a cor do nó.
   public Cor getCor() { return cor; }

   // Define a cor do nó.
   public void setCor(Cor cor) { this.cor = cor; }
}