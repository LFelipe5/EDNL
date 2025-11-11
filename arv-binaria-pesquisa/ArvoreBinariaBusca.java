// Note que a classe exige <T extends Comparable<T>>
public class ArvoreBinariaBusca<T extends Comparable<T>> implements ArvoreBinariaBuscaInterface<T> {

   private NoBinario<T> raiz;

   // Construtor
   public ArvoreBinariaBusca() {
      this.raiz = null;
   }

   // Implementação do método que insere um nó
   @Override
   public void inserirNo(T dado) {
      if (dado == null) {
         throw new IllegalArgumentException("Dado para inserção não pode ser nulo.");
      }

      NoBinario<T> novo = new NoBinario<>(dado);

      if (this.raiz == null) {
         this.raiz = novo;
         return;
      }

      NoBinario<T> atual = this.raiz;
      while (true) {
         int cmp = dado.compareTo(atual.getDado());
         if (cmp == 0) {
            // Decide não permitir duplicatas
            throw new IllegalArgumentException("Elemento já existe na árvore.");
         } else if (cmp < 0) {
            if (atual.getEsquerda() == null) {
               atual.setEsquerda(novo);
               return;
            }
            atual = atual.getEsquerda();
         } else {
            if (atual.getDireita() == null) {
               atual.setDireita(novo);
               return;
            }
            atual = atual.getDireita();
         }
      }
   } 

   // Implementação do método que busca um nó
   @Override
   public NoBinario<T> buscarNo(T dado) {
      if (dado == null) {
         throw new IllegalArgumentException("Dado para busca não pode ser nulo.");
      }

      NoBinario<T> atual = this.raiz;
      while (atual != null) {
         int cmp = dado.compareTo(atual.getDado());
         if (cmp == 0) {
            return atual;
         } else if (cmp < 0) {
            atual = atual.getEsquerda();
         } else {
            atual = atual.getDireita();
         }
      }
      return null;
   }

   // Implementação do método que exclui um nó
   @Override
   public void excluirNo(T dado) {
      if (dado == null) {
         throw new IllegalArgumentException("Dado para exclusão não pode ser nulo.");
      }

      NoBinario<T> alvo = buscarNo(dado);
      if (alvo == null) {
         return; // nada a fazer
      }

      // Caso 1: dois filhos -> encontrar sucessor (menor na subárvore direita)
      if (alvo.getEsquerda() != null && alvo.getDireita() != null) {
         NoBinario<T> sucessor = alvo.getDireita();
         while (sucessor.getEsquerda() != null) {
            sucessor = sucessor.getEsquerda();
         }
         // copia o dado do sucessor para o alvo e remove o sucessor
         alvo.setDado(sucessor.getDado());
         NoBinario<T> paiSuc = sucessor.getPai();
         NoBinario<T> filhoSuc = sucessor.getDireita(); // sucessor não tem esquerda
         if (paiSuc.getEsquerda() == sucessor) {
            paiSuc.setEsquerda(filhoSuc);
         } else {
            paiSuc.setDireita(filhoSuc);
         }
         return;
      }

      // Caso 2/3: zero ou um filho
      NoBinario<T> filho = (alvo.getEsquerda() != null) ? alvo.getEsquerda() : alvo.getDireita();
      if (filho == null) {
         // folha
         NoBinario<T> pai = alvo.getPai();
         if (pai == null) {
            this.raiz = null; // era a raiz e única
         } else if (pai.getEsquerda() == alvo) {
            pai.setEsquerda(null);
         } else {
            pai.setDireita(null);
         }
      } else {
         // um filho: copiar os dados do filho para o alvo e reencaminhar os netos
         alvo.setDado(filho.getDado());
         alvo.setEsquerda(filho.getEsquerda());
         alvo.setDireita(filho.getDireita());
      }
   }

   // Implementação do método que imprime a árvore em pré-ordem
   @Override
   public void imprimirPreOrdem() {
      if (this.raiz == null) {
         System.out.println("(árvore vazia)");
         return;
      }
      imprimirPreOrdemRec(this.raiz);
      System.out.println();
   }

   private void imprimirPreOrdemRec(NoBinario<T> no) {
      if (no == null) return;
      System.out.print(no.getDado() + " ");
      imprimirPreOrdemRec(no.getEsquerda());
      imprimirPreOrdemRec(no.getDireita());
   }

   // Implementação do método que imprime a árvore em pós-ordem
   @Override
   public void imprimirPosOrdem() {
      if (this.raiz == null) {
         System.out.println("(árvore vazia)");
         return;
      }
      imprimirPosOrdemRec(this.raiz);
      System.out.println();
   }

   private void imprimirPosOrdemRec(NoBinario<T> no) {
      if (no == null) return;
      imprimirPosOrdemRec(no.getEsquerda());
      imprimirPosOrdemRec(no.getDireita());
      System.out.print(no.getDado() + " ");
   }

   // Implementação do método que imprime a árvore em ordem
   @Override
   public void imprimirNaOrdem() {
      if (this.raiz == null) {
         System.out.println("(árvore vazia)");
         return;
      }
      imprimirNaOrdemRec(this.raiz);
      System.out.println();
   }

   private void imprimirNaOrdemRec(NoBinario<T> no) {
      if (no == null) return;
      imprimirNaOrdemRec(no.getEsquerda());
      System.out.print(no.getDado() + " ");
      imprimirNaOrdemRec(no.getDireita());
   }

   // Implementação do método que retorna a raiz da árvore
   @Override
   public NoBinario<T> getRaiz() { 
      return this.raiz;
   }

   // Implementação do método que verifica se a árvore está vazia
   @Override
   public boolean isVazia() { 
        return this.raiz == null;
   }
}