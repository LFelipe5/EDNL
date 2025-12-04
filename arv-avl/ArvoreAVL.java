// Imports necessários para a GUI
import javax.swing.*;
import java.awt.*;

// Note que a classe exige <T extends Comparable<T>>
public class ArvoreAVL<T extends Comparable<T>> implements ArvoreAVLInterface<T> {

   private NoBinario<T> raiz;

   // Construtor: A AVL começa vazia
   public ArvoreAVL() {
      this.raiz = null;
   }

   // ------------------------------------------------------------
   // Implementação do método de inserção de um nó
   @Override 
   public void inserirNo(T dado) {
      // Inserção AVL: usamos uma rotina recursiva que retorna a nova raiz da subárvore
      this.raiz = inserirRecursivo(this.raiz, dado);
   } 

   // Inserção recursiva que mantém o balanceamento AVL
   private NoBinario<T> inserirRecursivo(NoBinario<T> no, T dado) {
      if (no == null) {
         return new NoBinario<>(dado);
      }

      int cmp = dado.compareTo(no.getDado());
      if (cmp < 0) {
         no.setEsquerda( inserirRecursivo(no.getEsquerda(), dado) );
      } else if (cmp > 0) {
         no.setDireita( inserirRecursivo(no.getDireita(), dado) );
      } else {
         // duplicado: não insere
         System.out.println("Nó " + dado + " já existe (duplicado). Inserção cancelada.");
         return no;
      }
      
      // Após inserção, atualiza altura e fator de balanceamento
      no = balancear(no);
      return no;
   }

   @Override
   public NoBinario<T> balancear(NoBinario<T> no) {
      // Atualiza altura e fator de balanceamento
      atualizarAltura(no);
      int fb = fatorBalanceamento(no);
      no.setFb(fb);

      // Caso desbalanceado à esquerda
      if (fb > 1) {
         // Se o filho esquerdo está balanceado ou inclinado à esquerda -> rotação simples direita
         if (fatorBalanceamento(no.getEsquerda()) >= 0) {
            return rotacaoDireita(no);
         } else {
            // caso LR -> rotação dupla
            no.setEsquerda( rotacaoEsquerda(no.getEsquerda()) );
            return rotacaoDireita(no);
         }
      }

      // Caso desbalanceado à direita
      if (fb < -1) {
         // Se o filho direito está balanceado ou inclinado à direita -> rotação simples esquerda
         if (fatorBalanceamento(no.getDireita()) <= 0) {
            return rotacaoEsquerda(no);
         } else {
            // caso RL -> rotação dupla
            no.setDireita( rotacaoDireita(no.getDireita()) );
            return rotacaoEsquerda(no);
         }
      }

      return no; // Nó já está balanceado
   }

   // Retorna altura do nó (0 se null)
   private int altura(NoBinario<T> no) {
      return (no == null) ? 0 : no.getAltura();
   }

   // Atualiza a altura do nó baseado nos filhos
   private void atualizarAltura(NoBinario<T> no) {
      int h = 1 + Math.max( altura(no.getEsquerda()), altura(no.getDireita()) );
      no.setAltura(h);
   }

   // Fator de balanceamento = altura(esq) - altura(dir)
   private int fatorBalanceamento(NoBinario<T> no) {
      if (no == null) return 0;
      return altura(no.getEsquerda()) - altura(no.getDireita());
   }

   // Rotação à direita (y é raiz da subárvore desbalanceada)
   private NoBinario<T> rotacaoDireita(NoBinario<T> y) {
      NoBinario<T> x = y.getEsquerda();
      NoBinario<T> T2 = x.getDireita();

      // Rotaciona
      x.setDireita(y);
      y.setEsquerda(T2);

      // Atualiza alturas
      atualizarAltura(y);
      atualizarAltura(x);

      // Atualiza fatores
      y.setFb(fatorBalanceamento(y));
      x.setFb(fatorBalanceamento(x));

      return x; // nova raiz
   }

   // Rotação à esquerda (x é raiz da subárvore desbalanceada)
   private NoBinario<T> rotacaoEsquerda(NoBinario<T> x) {
      NoBinario<T> y = x.getDireita();
      NoBinario<T> T2 = y.getEsquerda();

      // Rotaciona
      y.setEsquerda(x);
      x.setDireita(T2);

      // Atualiza alturas
      atualizarAltura(x);
      atualizarAltura(y);

      // Atualiza fatores
      x.setFb(fatorBalanceamento(x));
      y.setFb(fatorBalanceamento(y));

      return y; // nova raiz
   }


   // ------------------------------------------------------------
   // Implementação do método que busca um nó
   @Override
   public NoBinario<T> buscarNo(T dado) {
      return buscarNoRecursivo(this.raiz, dado);
   }

   // Busca O(log n) - muito mais rápida
   private NoBinario<T> buscarNoRecursivo(NoBinario<T> noAtual, T dado) {
      if (noAtual == null) {
         return null; // Não encontrado
      }

      int comparacao = dado.compareTo(noAtual.getDado());

      if (comparacao == 0) {        // Encontrado
         return noAtual; 
      } else if (comparacao < 0) {  // Busca na esquerda
         return buscarNoRecursivo(noAtual.getEsquerda(), dado); 
      } else {                      // Busca na direita
         return buscarNoRecursivo(noAtual.getDireita(), dado); 
      }
   }


   // ------------------------------------------------------------
   // Implementação do método que exclui um nó
   @Override
   public void excluirNo(T dado) {
      // A raiz é atualizada, caso ela própria seja excluída
      this.raiz = excluirNoRecursivo(this.raiz, dado);
   }

   private NoBinario<T> excluirNoRecursivo(NoBinario<T> noAtual, T dado) {
      // 1. Nó não encontrado
      if (noAtual == null) {
         return null; 
      }

      // 2. Procurando o nó...
      int comparacao = dado.compareTo(noAtual.getDado());

      if (comparacao < 0) {         // Nó está na esquerda
         noAtual.setEsquerda( excluirNoRecursivo(noAtual.getEsquerda(), dado) );
      } else if (comparacao > 0) {  // Nó está na direita
         noAtual.setDireita( excluirNoRecursivo(noAtual.getDireita(), dado) );
      } else {
         // 3. Achou o nó (noAtual) para excluir
            
         // CENÁRIO 1: Nó folha
         if (noAtual.getEsquerda() == null && noAtual.getDireita() == null) {
            return null; // O pai receberá 'null'
         }

         // CENÁRIO 2: Nó com UM filho (direito)
         if (noAtual.getEsquerda() == null) {
            return noAtual.getDireita(); // Retorna o filho da direita para o pai
         }

         // CENÁRIO 2: Nó com UM filho (esquerdo)
         if (noAtual.getDireita() == null) {
            return noAtual.getEsquerda(); // Retorna o filho da esquerda para o pai
         }

         // CENÁRIO 3: Nó com DOIS filhos
         // Encontrar o sucessor (o menor nó da sub-árvore direita)
         NoBinario<T> sucessor = encontrarMenorValor(noAtual.getDireita());
            
         // Copiar o valor do sucessor para este nó
         noAtual.setDado(sucessor.getDado());
            
         // Excluir o nó sucessor (que agora está duplicado) da sub-árvore direita
         noAtual.setDireita( excluirNoRecursivo(noAtual.getDireita(), sucessor.getDado()) );
      }

      // Após exclusão, atualiza altura e fator de balanceamento
      noAtual = balancear(noAtual);
      return noAtual;
   }

   // Método auxiliar para encontrar o Sucessor (Cenário 3)
   private NoBinario<T> encontrarMenorValor(NoBinario<T> no) {
      NoBinario<T> atual = no;
      while (atual.getEsquerda() != null) {
         atual = atual.getEsquerda();
      }
      return atual;
   }


   // ------------------------------------------------------------
   // Implementação do método de impressão em pré-ordem
   @Override
   public void imprimirPreOrdem() {
      imprimirPreOrdemRecursivo(this.raiz, 0);
   }

   private void imprimirPreOrdemRecursivo(NoBinario<T> noAtual, int nivel) {
      if (noAtual == null) return;
      // 1. Raiz
      System.out.println(indentacao(nivel) + " " + noAtual.getDado()); 
      // 2. Esquerda
      imprimirPreOrdemRecursivo(noAtual.getEsquerda(), nivel + 1); 
      // 3. Direita
      imprimirPreOrdemRecursivo(noAtual.getDireita(), nivel + 1); 
   }


   // ------------------------------------------------------------
   // Implementação do método de impressão em pós-ordem  
   @Override
   public void imprimirPosOrdem() {
      imprimirPosOrdemRecursivo(this.raiz, 0);
   }

   private void imprimirPosOrdemRecursivo(NoBinario<T> noAtual, int nivel) {
      if (noAtual == null) return;
      // 1. Esquerda
      imprimirPosOrdemRecursivo(noAtual.getEsquerda(), nivel + 1);
      // 2. Direita 
      imprimirPosOrdemRecursivo(noAtual.getDireita(), nivel + 1); 
      // 3. Raiz
      System.out.println(indentacao(nivel) + " " + noAtual.getDado()); 
   }


   // ------------------------------------------------------------
   // Implementação do método de impressão em ordem
   @Override
   public void imprimirNaOrdem() {
      imprimirNaOrdemRecursivo(this.raiz, 0);
   }

   private void imprimirNaOrdemRecursivo(NoBinario<T> noAtual, int nivel) {
      if (noAtual == null) return;
      // 1. Esquerda
      imprimirNaOrdemRecursivo(noAtual.getEsquerda(), nivel + 1); 
      // 2. Raiz
      System.out.println(indentacao(nivel) + " " + noAtual.getDado()); 
      // 3. Direita
      imprimirNaOrdemRecursivo(noAtual.getDireita(), nivel + 1); 
   }

   private String indentacao(int nivel) {
      if (nivel == 0) return "";
      return " ".repeat(nivel * 3) + "|- ";
   }


   // ------------------------------------------------------------
   // Implementação do método que exibe a árvore no terminal
   @Override
   public void exibirArvoreTerminal() {
         if (this.raiz == null) {
            System.out.println("(árvore vazia)");
            return;
         }

         // Imprime a árvore em ASCII usando prefixos
         exibirArvoreTerminalRec(this.raiz, "", true);
    }

    // Helper recursivo que imprime cada nó com prefixos para montar a estrutura
    private void exibirArvoreTerminalRec(NoBinario<T> no, String prefix, boolean isTail) {
       if (no == null) return;

       String connector = isTail ? "└── " : "├── ";
       String info = String.valueOf(no.getDado());
       // Mostra também altura e fator de balanceamento, se disponíveis
       info += " (h=" + no.getAltura() + ", fb=" + no.getFb() + ")";

       System.out.println(prefix + connector + info);

       NoBinario<T> left = no.getEsquerda();
       NoBinario<T> right = no.getDireita();

       // Se não há filhos, retorna
       if (left == null && right == null) return;

       String childPrefix = prefix + (isTail ? "    " : "│   ");

       // Imprime filho esquerdo (se existir) e filho direito
       if (left != null && right != null) {
          // esquerda não é tail, direita é tail
          exibirArvoreTerminalRec(left, childPrefix, false);
          exibirArvoreTerminalRec(right, childPrefix, true);
       } else if (left != null) {
          exibirArvoreTerminalRec(left, childPrefix, true);
       } else { // right != null
          exibirArvoreTerminalRec(right, childPrefix, true);
       }
    }
   
   // ------------------------------------------------------------
   // Implementação do método que exibe a árvore em modo gráfico
   @Override
   public void exibirArvoreGrafica() {
      
   }


   // ------------------------------------------------------------
   // Implementação do método que retorna a raiz da árvore
   @Override
   public NoBinario<T> getRaiz() { return this.raiz; }


   // ------------------------------------------------------------
   // Implementação do método que verifica se a árvore está vazia
   @Override
   public boolean isVazia() { return this.raiz == null; }
}