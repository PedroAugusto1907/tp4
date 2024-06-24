package entidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.text.NumberFormat;

import aeds3.CifraCombinada;
import aeds3.Registro;

public class Livro implements Registro {

  private int ID;
  private String isbn;
  private String titulo;
  private float preco;
  private int idCategoria;

  public Livro() {
    this(-1, "", "", 0F, -1);
  }

  public Livro(String i, String t, float p, int ic) {
    this(-1, i, t, p, ic);
  }

  public Livro(int id, String i, String t, float p, int ic) {
    this.ID = id;
    this.isbn = i;
    this.titulo = t;
    this.preco = p;
    this.idCategoria = ic;
  }

  public int getID() {
    return ID;
  }

  public void setID(int iD) {
    ID = iD;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public float getPreco() {
    return preco;
  }

  public void setPreco(float preco) {
    this.preco = preco;
  }

  public int getIdCategoria() {
    return idCategoria;
  }

  public void setIdCategoria(int idCategoria) {
    this.idCategoria = idCategoria;
  }

  public byte[] toByteArray() throws Exception {
    ByteArrayOutputStream ba_out = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(ba_out);
    dos.writeInt(this.ID);

    var isbn = this.isbn.getBytes();
    var tamanhoIsbn = isbn.length;

    dos.writeInt(tamanhoIsbn);
    dos.write(isbn);
    dos.writeUTF(this.titulo);
    dos.writeFloat(this.preco);
    dos.writeInt(this.idCategoria);

    byte[] dados = ba_out.toByteArray();
    byte[] dadosCifrados = CifraCombinada.cifraCombinada(dados);
    return dadosCifrados;
  }

  public void fromByteArray(byte[] ba) throws Exception {
    byte[] dadosDecifrados = CifraCombinada.decifraCombinada(ba);
    ByteArrayInputStream ba_in = new ByteArrayInputStream(dadosDecifrados);
    DataInputStream dis = new DataInputStream(ba_in);
    this.ID = dis.readInt();

    var tamanhoIsbn = dis.readInt();

    byte[] straux = new byte[tamanhoIsbn];

    dis.read(straux);
    this.isbn = new String(straux);
    this.titulo = dis.readUTF();
    this.preco = dis.readFloat();
    this.idCategoria = dis.readInt();
  }

  public String toString() {
    return "ID: " + this.ID +
        "\nISBN: " + this.isbn +
        "\nTítulo: " + this.titulo +
        "\nPreço: " + NumberFormat.getCurrencyInstance().format(this.preco) +
        "\nCategoria: " + this.idCategoria;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  @Override
  public int compareTo(Object b) {
    return this.getID() - ((Livro) b).getID();
  }
}
