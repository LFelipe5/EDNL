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
         ArvoreRubroNegra<Integer> arvoreTeste = new ArvoreRubroNegra<>();
         
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

      } catch (Exception e) {
         System.err.println("Erro: " + e.getMessage());
         e.printStackTrace();
      } finally {
         scanner.close();
      }
   }
}