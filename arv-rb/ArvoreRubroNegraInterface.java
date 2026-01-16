public interface ArvoreRubroNegraInterface<T extends Comparable<T>> {
   
   // Método para inserir um nó na árvore Rubro-Negra
   void inserirNo(T dado);
   
   // Método para buscar um nó na árvore Rubro-Negra
   NoRubroNegro<T> buscarNo(T dado);
   
   // Método para excluir um nó da árvore Rubro-Negra
   void excluirNo(T dado);

   // Método para imprimir a árvore Rubro-Negra em pré-ordem
   void imprimirPreOrdem();
   
   // Método para imprimir a árvore Rubro-Negra em pós-ordem
   void imprimirPosOrdem();

   // Método para imprimir a árvore Rubro-Negra em ordem
   void imprimirNaOrdem();

   // Método para obter a raiz da árvore Rubro-Negra
   NoRubroNegro<T> getRaiz();

   // Método para verificar se a árvore Rubro-Negra está vazia
   boolean isVazia();
}