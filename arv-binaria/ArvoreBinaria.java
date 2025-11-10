import java.util.Queue;
import java.util.LinkedList;
import java.util.Stack;

public class ArvoreBinaria<T> implements ArvoreBinariaInterface<T> {

   private NoBinario<T> raiz;

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

      // Encontra o nó mais profundo (último em nível) usando BFS
      Queue<NoBinario<T>> fila = new LinkedList<>();
      fila.add(this.raiz);
      NoBinario<T> profundo = null;
      while (!fila.isEmpty()) {
         NoBinario<T> atual = fila.poll();
         profundo = atual;
         if (atual.getEsquerda() != null) fila.add(atual.getEsquerda());
         if (atual.getDireita() != null) fila.add(atual.getDireita());
      }

      if (profundo == null) {
         return false; // deve ser improvável
      }

      // Se o nó mais profundo é o próprio alvo, apenas desconecta-o
      if (profundo == alvo) {
         NoBinario<T> paiProf = profundo.getPai();
         if (paiProf == null) { // deveria ser raiz tratada acima
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
      NoBinario<T> paiProf = profundo.getPai();
      if (paiProf == null) {
         // profundo era raiz (caso já tratado), mas cobre por segurança
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

      Stack<NoBinario<T>> pilha = new Stack<>();
      pilha.push(this.raiz);

      while (!pilha.isEmpty()) {
         NoBinario<T> atual = pilha.pop();
         System.out.print(atual.getDado() + " ");
         // empilha primeiro a direita, depois a esquerda para que a esquerda seja processada primeiro
         if (atual.getDireita() != null) pilha.push(atual.getDireita());
         if (atual.getEsquerda() != null) pilha.push(atual.getEsquerda());
      }
      System.out.println(); // nova linha ao final
   }

   // Implementação do método que exibe a árvore em pós-ordem.
   @Override
   public void imprimirPosOrdem() {
      if (this.raiz == null) {
         System.out.println("(árvore vazia)");
         return;
      }

      // Método iterativo usando duas pilhas: s1 para processamento, s2 para ordem reversa
      Stack<NoBinario<T>> s1 = new Stack<>();
      Stack<NoBinario<T>> s2 = new Stack<>();

      s1.push(this.raiz);
      while (!s1.isEmpty()) {
         NoBinario<T> atual = s1.pop();
         s2.push(atual);
         // empilhar esquerda e direita (não importa a ordem aqui)
         if (atual.getEsquerda() != null) s1.push(atual.getEsquerda());
         if (atual.getDireita() != null) s1.push(atual.getDireita());
      }

      // s2 contém os nós na ordem raiz-direita-esquerda; desempilhando obtemos pós-ordem
      while (!s2.isEmpty()) {
         System.out.print(s2.pop().getDado() + " ");
      }
      System.out.println();
   }

   // Implementação do método que exibe a árvore em ordem.
   @Override
   public void imprimirNaOrdem() {
      if (this.raiz == null) {
         System.out.println("(árvore vazia)");
         return;
      }

      Stack<NoBinario<T>> pilha = new Stack<>();
      NoBinario<T> atual = this.raiz;

      // iteração in-order: descer à esquerda empilhando, processar, ir à direita
      while (atual != null || !pilha.isEmpty()) {
         while (atual != null) {
            pilha.push(atual);
            atual = atual.getEsquerda();
         }
         atual = pilha.pop();
         System.out.print(atual.getDado() + " ");
         atual = atual.getDireita();
      }
      System.out.println();
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