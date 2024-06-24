package arquivos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

import aeds3.LZW;

public class ArquivoBackup {
  public static void compacta() throws Exception {
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    String nomeArquivo = "backup_" + timeStamp;
    Files.createDirectories(Paths.get("backup/"));
    RandomAccessFile arquivoBackup = new RandomAccessFile("backup/" + nomeArquivo, "rwd");

    Map<String, byte[]> dados = new Hashtable<>();

    File pastaDados = new File("dados");
    File[] arquivos = pastaDados.listFiles();

    int tamanhoDescompactado = 0;

    for (File arquivo : arquivos) {
      byte[] bytesArquivo = new FileInputStream(arquivo).readAllBytes();
      tamanhoDescompactado = tamanhoDescompactado + bytesArquivo.length;
      dados.put(arquivo.getName(), bytesArquivo);
    }

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

    objectOutputStream.writeObject(dados);
    objectOutputStream.flush();

    byte[] backupCompactado = LZW.codifica(byteArrayOutputStream.toByteArray());

    double compressao = (double) backupCompactado.length / tamanhoDescompactado;

    DecimalFormat df = new DecimalFormat("##.##%");

    System.out.println("Taxa de compressão alcançada: " + df.format(compressao));

    arquivoBackup.write(backupCompactado);
  }

  public static void descompacta(String backup) throws Exception {
    byte[] backupCompactado = Files.readAllBytes(Paths.get("backup/" + backup));
    byte[] backupDescompactado = LZW.decodifica(backupCompactado);

    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(backupDescompactado);
    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

    Map<String, byte[]> dados = (Map<String, byte[]>) objectInputStream.readObject();

    dados.forEach((key, value) -> {
      try {
        RandomAccessFile arquivo = new RandomAccessFile("dados/" + key, "rwd");
        arquivo.setLength(0);
        arquivo.write(value);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
}
