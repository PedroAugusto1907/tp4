package aeds3;

import java.util.Arrays;

public class CifraCombinada {
    static final String CHAVE = "PedroAugustoPUC";

    // Cifra de Substituição
    public static byte[] cifraSubstituicao(byte[] dados, String chave) {
        int deslocamento = chave.hashCode() % 256;
        byte[] dadosCifrados = new byte[dados.length];
        for (int i = 0; i < dados.length; i++) {
            dadosCifrados[i] = (byte) ((dados[i] + deslocamento) % 256);
        }

        return dadosCifrados;
    }

    public static byte[] decifraSubstituicao(byte[] dados, String chave) {
        int deslocamento = chave.hashCode() % 256;
        byte[] dadosDecifrados = new byte[dados.length];
        for (int i = 0; i < dados.length; i++) {
            dadosDecifrados[i] = (byte) ((dados[i] - deslocamento + 256) % 256);
        }

        return dadosDecifrados;
    }

    // Cifra de Transposição
    public static byte[] cifraTransposicao(byte[] dados, String chave) {
        int[] chaveArray = gerarChave(chave);
        int n = chaveArray.length;
        int tamanhoPreenchido = dados.length + (n - dados.length % n) % n;
        byte[] dadosPreenchidos = Arrays.copyOf(dados, tamanhoPreenchido);
        byte[] dadosCifrados = new byte[tamanhoPreenchido];

        for (int i = 0; i < tamanhoPreenchido; i += n) {
            for (int j = 0; j < n; j++) {
                dadosCifrados[i + j] = dadosPreenchidos[i + chaveArray[j]];
            }
        }

        return dadosCifrados;
    }

    public static byte[] decifraTransposicao(byte[] dados, String chave) {
        int[] chaveArray = gerarChave(chave);
        int n = chaveArray.length;
        byte[] dadosDecifrados = new byte[dados.length];

        for (int i = 0; i < dados.length; i += n) {
            for (int j = 0; j < n; j++) {
                if (i + chaveArray[j] < dados.length) {
                    dadosDecifrados[i + chaveArray[j]] = dados[i + j];
                }
            }
        }

        int tamanhoOriginal = dadosDecifrados.length;
        for (int i = tamanhoOriginal - 1; i >= 0; i--) {
            if (dadosDecifrados[i] != 0) {
                tamanhoOriginal = i + 1;
                break;
            }
        }

        return Arrays.copyOf(dadosDecifrados, tamanhoOriginal);
    }

    private static int[] gerarChave(String chave) {
        int n = chave.length();
        int[] chaveArray = new int[n];
        for (int i = 0; i < n; i++) {
            chaveArray[i] = i;
        }

        for (int i = 0; i < n; i++) {
            int pos = (chave.charAt(i) % n);
            int temp = chaveArray[i];
            chaveArray[i] = chaveArray[pos];
            chaveArray[pos] = temp;
        }

        return chaveArray;
    }

    public static byte[] cifraCombinada(byte[] dados) {
        byte[] dadosCifrados = cifraSubstituicao(dados, CHAVE);
        return cifraTransposicao(dadosCifrados, CHAVE);
    }

    public static byte[] decifraCombinada(byte[] dados) {
        byte[] dadosDecifrados = decifraTransposicao(dados, CHAVE);
        return decifraSubstituicao(dadosDecifrados, CHAVE);
    }
}
