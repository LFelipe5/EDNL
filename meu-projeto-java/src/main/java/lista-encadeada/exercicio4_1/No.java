// ------------------------------------------------------------------------
// Classe Nó
// Representa cada elemento da lista com dado e ponteiro para o próximo
// ------------------------------------------------------------------------
public class No
{
   string nome; // nome armazenado
   string horario; // horario armazenado
   No proximo; // referência para o próximo nó

   public No(string nome, string horario)
   {
      this.nome = nome;
      this.horario = horario;
      this.proximo = null;
   }
}