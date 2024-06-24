package arquivos;

import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;
import aeds3.Arquivo;
import aeds3.ArvoreBMais;
import aeds3.HashExtensivel;
import aeds3.ListaInvertida;
import aeds3.ParIntInt;
import entidades.Livro;

public class ArquivoLivros extends Arquivo<Livro> {

  HashExtensivel<ParIsbnId> indiceIndiretoISBN;
  ArvoreBMais<ParIntInt> relLivrosDaCategoria;
  ListaInvertida listaInvertidaTitulos;
  List<String> stopWords;

  public ArquivoLivros() throws Exception {
    super("livros", Livro.class.getConstructor());
    indiceIndiretoISBN = new HashExtensivel<>(
        ParIsbnId.class.getConstructor(),
        4,
        "dados/livros_isbn.hash_d.db",
        "dados/livros_isbn.hash_c.db");
    relLivrosDaCategoria = new ArvoreBMais<>(ParIntInt.class.getConstructor(), 4, "dados/livros_categorias.btree.db");
    listaInvertidaTitulos = new ListaInvertida(4, "dados/livros_dicionario.listainv.db",
        "dados/livros_blocos.listainv.db");
    stopWords = Files.readAllLines(Paths.get("stopwords.txt"));
  }

  private void adicionarTitulosListaInvertida(String titulo, int livroId) throws Exception {
    String[] palavras = titulo.split(" ");
    for (String palavra : palavras) {
      if (!stopWords.contains(palavra)) {
        var palavraNormalizada = Normalizer.normalize(palavra, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")
            .toLowerCase();

        listaInvertidaTitulos.create(palavraNormalizada, livroId);
      }
    }
  }

  private boolean removerTitulosListaInvertida(String titulo, int livroId) throws Exception {
    String[] palavras = titulo.split(" ");
    for (String palavra : palavras) {
      var palavraNormalizada = Normalizer.normalize(palavra, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")
          .toLowerCase();

      listaInvertidaTitulos.delete(palavraNormalizada, livroId);
    }

    return true;
  }

  public List<Livro> buscarLivrosPorTermos(String termos) throws Exception {
    var termosDividivos = termos.split(" ");

    Set<Integer> resultados = new HashSet<>();
    for (String termo : termosDividivos) {
      var termoNormalizado = Normalizer.normalize(termo, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")
          .toLowerCase();

      int[] livroIds = listaInvertidaTitulos.read(termoNormalizado);
      for (int livroId : livroIds) {
        resultados.add(livroId);
      }
    }

    List<Livro> livrosEncontrados = new ArrayList<>();
    for (int livroId : resultados) {
      Livro livro = super.read(livroId);
      if (livro != null) {
        livrosEncontrados.add(livro);
      }
    }
    return livrosEncontrados;
  }

  @Override
  public int create(Livro obj) throws Exception {
    int id = super.create(obj);
    obj.setID(id);
    indiceIndiretoISBN.create(new ParIsbnId(obj.getIsbn(), obj.getID()));
    relLivrosDaCategoria.create(new ParIntInt(obj.getIdCategoria(), obj.getID()));
    adicionarTitulosListaInvertida(obj.getTitulo(), obj.getID());
    return id;
  }

  public Livro readISBN(String isbn) throws Exception {
    ParIsbnId pii = indiceIndiretoISBN.read(ParIsbnId.hashIsbn(isbn));
    if (pii == null)
      return null;
    int id = pii.getId();
    return super.read(id);
  }

  @Override
  public boolean delete(int id) throws Exception {
    Livro obj = super.read(id);
    if (obj != null)
      if (indiceIndiretoISBN.delete(ParIsbnId.hashIsbn(obj.getIsbn()))
          &&
          relLivrosDaCategoria.delete(new ParIntInt(obj.getIdCategoria(), obj.getID()))
          &&
          removerTitulosListaInvertida(obj.getTitulo(), obj.getID()))
        return super.delete(id);
    return false;
  }

  @Override
  public boolean update(Livro novoLivro) throws Exception {
    Livro livroAntigo = super.read(novoLivro.getID());
    if (livroAntigo != null) {

      // Testa alteração do ISBN
      if (livroAntigo.getIsbn().compareTo(novoLivro.getIsbn()) != 0) {
        indiceIndiretoISBN.delete(ParIsbnId.hashIsbn(livroAntigo.getIsbn()));
        indiceIndiretoISBN.create(new ParIsbnId(novoLivro.getIsbn(), novoLivro.getID()));
      }

      // Testa alteração da categoria
      if (livroAntigo.getIdCategoria() != novoLivro.getIdCategoria()) {
        relLivrosDaCategoria.delete(new ParIntInt(livroAntigo.getIdCategoria(), livroAntigo.getID()));
        relLivrosDaCategoria.create(new ParIntInt(novoLivro.getIdCategoria(), novoLivro.getID()));
      }

      if (livroAntigo.getTitulo() != novoLivro.getTitulo()) {
        removerTitulosListaInvertida(livroAntigo.getTitulo(), livroAntigo.getID());
        adicionarTitulosListaInvertida(novoLivro.getTitulo(), novoLivro.getID());
      }

      // Atualiza o livro
      return super.update(novoLivro);
    }
    return false;
  }
}
