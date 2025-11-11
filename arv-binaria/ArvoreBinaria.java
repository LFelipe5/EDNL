// Versões recursivas usadas; remove dependência de Queue/LinkedList/Stack

public class ArvoreBinaria<T> implements ArvoreBinariaInterface<T> {

   private NoBinario<T> raiz;
   // Campos auxiliares usados pela busca recursiva do nó mais profundo
   private int maxProf;
   private NoBinario<T> maisProfundo;
   private NoBinario<T> paiMaisProfundo;

   // Construtor que "adota" um nó raiz já criado
   public ArvoreBinaria(NoBinario<T> noRaiz) {
      if (noRaiz == null) {
         throw new IllegalArgumentException("A raiz não pode ser nula.");
      }
      this.raiz = noRaiz;
   }

   // Implementação do método que busca um nó na árvore.
   @Override
   public NoBinario<T> buscarNo(T dado) {
      if (dado == null) {
         throw new IllegalArgumentException("Dado para busca não pode ser nulo.");
      }
      return buscarRec(this.raiz, dado);
   }
   
   // Helper recursivo para busca em pré-ordem (visita atual, depois esquerda, depois direita)
   private NoBinario<T> buscarRec(NoBinario<T> atual, T dado) {
      if (atual == null) {
         return null;
      }
      // Usa equals para comparação (assume que dado não é null porque setDado proíbe nulos)
      if (atual.getDado().equals(dado)) {
         return atual;
      }
      NoBinario<T> encontrado = buscarRec(atual.getEsquerda(), dado);
      if (encontrado != null) {
         return encontrado;
      }
      return buscarRec(atual.getDireita(), dado);
   }

   // Achar o nó mais profundo (e seu pai) usando travessia recursiva
   private void acharMaisProfundo(NoBinario<T> atual, NoBinario<T> pai, int profundidade) {
      if (atual == null) return;
      if (profundidade > this.maxProf) {
         this.maxProf = profundidade;
         this.maisProfundo = atual;
         this.paiMaisProfundo = pai;
      }
      acharMaisProfundo(atual.getEsquerda(), atual, profundidade + 1);
      acharMaisProfundo(atual.getDireita(), atual, profundidade + 1);
   }

   // Implementação do método que insere um nó na árvore.
   @Override
   public void inserirNo(T dadosPai, T dadosFilho, char lado) throws Exception {
      if (dadosPai == null) {
         throw new IllegalArgumentException("Dados do pai não podem ser nulos.");
      }
      if (dadosFilho == null) {
         throw new IllegalArgumentException("Dados do filho não podem ser nulos.");
      }

      // Busca o nó pai na árvore
      NoBinario<T> noPai = buscarNo(dadosPai);
      if (noPai == null) {
         throw new Exception("Pai não encontrado na árvore.");
      }

      char ladoUp = Character.toUpperCase(lado);
      // Cria o nó filho (setDado de NoBinario fará validações adicionais)
      NoBinario<T> novo = new NoBinario<>(dadosFilho);

      if (ladoUp == 'E') {
         if (noPai.getEsquerda() != null) {
            throw new Exception("Já existe um filho à esquerda do nó pai.");
         }
         noPai.setEsquerda(novo);
      } else if (ladoUp == 'D') {
         if (noPai.getDireita() != null) {
            throw new Exception("Já existe um filho à direita do nó pai.");
         }
         noPai.setDireita(novo);
      } else {
         throw new Exception("Lado inválido. Use 'E' para esquerda ou 'D' para direita.");
      }
   }

   // Implementação do método que exclui um nó na árvore.
   @Override
   public boolean excluirNo(T dado) {
      if (dado == null) {
         throw new IllegalArgumentException("Dado para exclusão não pode ser nulo.");
      }

      if (this.raiz == null) {
         return false; // árvore vazia
      }

      // Encontra o nó a ser excluído
      NoBinario<T> alvo = buscarNo(dado);
      if (alvo == null) {
         return false; // não encontrado
      }

      // Se é o único nó (raiz sem filhos)
      if (alvo == this.raiz && this.raiz.getEsquerda() == null && this.raiz.getDireita() == null) {
         this.raiz = null;
         return true;
      }

      // Encontra o nó mais profundo (último em nível) recursivamente
      // Usa variáveis temporárias da instância durante a busca
      this.maxProf = -1;
      this.maisProfundo = null;
      this.paiMaisProfundo = null;
      acharMaisProfundo(this.raiz, null, 0);

      NoBinario<T> profundo = this.maisProfundo;
      NoBinario<T> paiProf = this.paiMaisProfundo;

      if (profundo == null) {
         return false;
      }

      // Se o nó mais profundo é o próprio alvo, apenas desconecta-o
      if (profundo == alvo) {
         if (paiProf == null) { // era raiz
            this.raiz = null;
         } else if (paiProf.getEsquerda() == profundo) {
            paiProf.setEsquerda(null);
         } else if (paiProf.getDireita() == profundo) {
            paiProf.setDireita(null);
         }
         return true;
      }

      // Substitui o dado do alvo pelo dado do nó profundo e remove o nó profundo
      alvo.setDado(profundo.getDado());
      if (paiProf == null) {
         this.raiz = null;
      } else if (paiProf.getEsquerda() == profundo) {
         paiProf.setEsquerda(null);
      } else if (paiProf.getDireita() == profundo) {
         paiProf.setDireita(null);
      }

      return true;
   }

   
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

   // Implementação do método que exibe a árvore em pós-ordem.
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

   // Implementação do método que exibe a árvore em ordem.
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
   
   // Implementação do método que retorna a raiz da árvore.
   @Override
   public NoBinario<T> getRaiz() { 
   return this.raiz;
   }

   // Implementação do método que verifica se a árvore está vazia.
   @Override
   public boolean isVazia() { 
      return this.raiz == null;
   }
}