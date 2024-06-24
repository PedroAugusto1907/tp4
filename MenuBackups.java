// -------------------------------------------------------
// A classe MenuLivro é responsável pela interação com
// o usuário. É a partir dela, que o usuário inclui, 
// altera, exclui e consulta as entidades cadastradas
// no banco de dados.
// 
// Adicionalmente, a classe MenuLivro oferece um método
// LeLivro() e outro MostraLivro() que cuidam da entrada
// e da saída de dados de livros e das classes relacionadas.
// -------------------------------------------------------

import java.io.File;
import java.util.Scanner;

import arquivos.ArquivoBackup;
import arquivos.ArquivoCategorias;
import arquivos.ArquivoLivros;

public class MenuBackups {

  private static Scanner console = new Scanner(System.in);
  private ArquivoLivros arqLivros;
  private ArquivoCategorias arqCategorias;

  public MenuBackups() {
    try {
      arqLivros = new ArquivoLivros();
      arqCategorias = new ArquivoCategorias();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void menu() throws Exception {

    // Mostra o menu
    int opcao;
    do {
      System.out.println("\n\n\nBOOKAEDS 1.0");
      System.out.println("------------");
      System.out.println("\n> Início > Backups");
      System.out.println("\n1) Criar backup");
      System.out.println("2) Restaurar backup");
      System.out.println("3) Remover backup");
      System.out.println("\n0) Retornar ao menu anterior");

      System.out.print("\nOpção: ");
      try {
        opcao = Integer.valueOf(console.nextLine());
      } catch (NumberFormatException e) {
        opcao = -1;
      }

      // Seleciona a operação
      switch (opcao) {
        case 1:
          criarBackup();
          break;
        case 2:
          restaurarBackup();
          break;
        case 3:
          removerBackup();
          break;
        case 0:
          break;
        default:
          System.out.println("Opção inválida");
      }
    } while (opcao != 0);

    // Fecha os arquivos
    try {
      arqLivros.close();
      arqCategorias.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public void criarBackup() throws Exception {
    ArquivoBackup.compacta();
    System.out.println("Backup criado.");
  }

  public void restaurarBackup() throws Exception {
    File pastaBackups = new File("backup");
    File[] arquivos = pastaBackups.listFiles();

    System.out.println("Arquivos de backup:");

    for (int i = 0; i < arquivos.length; i++) {
      System.out.println("\n" + (i + 1) + ") " + arquivos[i].getName());
    }

    int opcao;

    System.out.print("\nOpção: ");
    try {
      opcao = Integer.valueOf(console.nextLine());
    } catch (NumberFormatException e) {
      opcao = -1;
    }

    if (opcao > 0 && opcao <= arquivos.length) {
      ArquivoBackup.descompacta(arquivos[opcao - 1].getName());

      System.out.print("\nBackup restaurado.");
    } else {
      System.out.println("\nOpção inválida");
    }
  }

  public void removerBackup() {
    File pastaBackups = new File("backup");
    File[] arquivos = pastaBackups.listFiles();

    System.out.println("Arquivos de backup:");

    for (int i = 0; i < arquivos.length; i++) {
      System.out.println("\n" + (i + 1) + ") " + arquivos[i].getName());
    }

    int opcao;

    System.out.print("\nOpção: ");
    try {
      opcao = Integer.valueOf(console.nextLine());
    } catch (NumberFormatException e) {
      opcao = -1;
    }

    if (opcao > 0 && opcao <= arquivos.length) {
      arquivos[opcao - 1].delete();
      System.out.print("\nBackup removido.");
    } else {
      System.out.println("\nOpção inválida");
    }
  }
}
