import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class TestarArvoreRN {

   public static void main(String[] args) {
      Scanner scanner = new Scanner(System.in);

      System.out.println("=== Teste de Carga para Árvore Rubro-Negra ===");

      try {
         // ----------------------------------------------------------------------
         // Configuração dos dados de entrada
         System.out.print("Quantidade de nós a inserir: ");
         int intQuantidade = scanner.nextInt();

         System.out.print("Valor Mínimo: ");
         int intValorMinimo = scanner.nextInt();

         System.out.print("Valor Máximo: ");
         int intValorMaximo = scanner.nextInt();

         // ----------------------------------------------------------------------
         // 1. Gera os números usando a classe auxiliar
         GeradorDeNumeros gerador = new GeradorDeNumeros(intQuantidade, intValorMinimo, intValorMaximo);
         List<Integer> chavesParaInserir = gerador.getResultado();

         // ----------------------------------------------------------------------
         // 2. Exibe os números gerados (que serão inseridos na árvore)
         System.out.println("\nNúmeros gerados: " + chavesParaInserir);

         // ----------------------------------------------------------------------
         // 3. Montando a árvore Rubro-Negra
         ArvoreRB<Integer> arvoreTeste = new ArvoreRB<>();
         
         long inicio = System.nanoTime();
         for (Integer valor : chavesParaInserir) {
            arvoreTeste.inserirNo(valor);
         }
         
         long fim = System.nanoTime();
         System.out.println("\nÁrvore montada em " + (fim - inicio) + " nanosegundos.");
         
         System.out.println("\nImpressão Pré-Ordem (R=Red, B=Black):");
         arvoreTeste.imprimirPreOrdem();

         System.out.println("\nRaiz da árvore: " + arvoreTeste.getRaiz().getDado() + 
                            " (" + arvoreTeste.getRaiz().getCor() + ")");

         // arvoreTeste.excluirNo(arvoreTeste.getRaiz().getDado()); // Exclui a raiz para testar a remoção

         // Remoção aleatórioa de dois valores valor
         Collections.shuffle(chavesParaInserir);
         for (int i = 0; i < 2 && i < chavesParaInserir.size(); i++) {
            Integer valorParaRemover = chavesParaInserir.get(i);
            System.out.println("\nRemovendo valor: " + valorParaRemover);
            arvoreTeste.excluirNo(valorParaRemover);
            System.out.println("Árvore após remoção de " + valorParaRemover + ":");
            arvoreTeste.imprimirPreOrdem();
         }

         // busca de um valor aleatório que exista na árvore
         if (!chavesParaInserir.isEmpty()) {
            Integer valorParaBuscar = chavesParaInserir.get(3);
            System.out.println("\nBuscando valor: " + valorParaBuscar);
            NoRubroNegro<Integer> resultadoBusca = arvoreTeste.buscarNo(valorParaBuscar);
            if (resultadoBusca != null) {
               System.out.println("Valor encontrado: " + resultadoBusca.getDado() + 
                                  " (" + resultadoBusca.getCor() + ")");
            } else {
               System.out.println("Valor " + valorParaBuscar + " não encontrado na árvore.");
            }
         }

      } catch (Exception e) {
         System.err.println("Erro: " + e.getMessage());
         e.printStackTrace();
      } finally {
         scanner.close();
      }
   }
}