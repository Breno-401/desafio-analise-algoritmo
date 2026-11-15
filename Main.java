import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class Main {

    static class Contador {
        long comparacoes;
        long trocas;
        long movimentos;
    }

    static int buscaLinear(int[] vetor, int valor, Contador c) {
        for (int i = 0; i < vetor.length; i++) {
            c.comparacoes++;
            if (vetor[i] == valor) {
                return i;
            }
        }
        return -1;
    }

    static int buscaBinaria(int[] vetor, int valor, Contador c) {
        int inicio = 0;
        int fim = vetor.length - 1;

        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;
            c.comparacoes++;

            if (vetor[meio] == valor) {
                return meio;
            } else if (vetor[meio] < valor) {
                inicio = meio + 1;
            } else {
                fim = meio - 1;
            }
        }
        return -1;
    }

    static void selectionSort(int[] vetor, Contador c) {
        for (int i = 0; i < vetor.length - 1; i++) {
            int menor = i;
            for (int j = i + 1; j < vetor.length; j++) {
                c.comparacoes++;
                if (vetor[j] < vetor[menor]) {
                    menor = j;
                }
            }

            if (menor != i) {
                int temp = vetor[i];
                vetor[i] = vetor[menor];
                vetor[menor] = temp;
                c.trocas++;
                c.movimentos += 3; // Uma troca usa tres atribuicoes.
            }
        }
    }

    static void mergeSort(int[] vetor, Contador c) {
        int[] auxiliar = new int[vetor.length];
        dividir(vetor, auxiliar, 0, vetor.length - 1, c);
    }

    static void dividir(int[] vetor, int[] auxiliar, int inicio, int fim, Contador c) {
        if (inicio >= fim) {
            return;
        }

        int meio = (inicio + fim) / 2;
        dividir(vetor, auxiliar, inicio, meio, c);
        dividir(vetor, auxiliar, meio + 1, fim, c);
        juntar(vetor, auxiliar, inicio, meio, fim, c);
    }

    static void juntar(int[] vetor, int[] auxiliar, int inicio, int meio, int fim, Contador c) {
        int i = inicio;
        int j = meio + 1;
        int k = inicio;

        while (i <= meio && j <= fim) {
            c.comparacoes++;
            if (vetor[i] <= vetor[j]) {
                auxiliar[k++] = vetor[i++];
            } else {
                auxiliar[k++] = vetor[j++];
            }
            c.movimentos++;
        }

        while (i <= meio) {
            auxiliar[k++] = vetor[i++];
            c.movimentos++;
        }
        while (j <= fim) {
            auxiliar[k++] = vetor[j++];
            c.movimentos++;
        }
        for (int pos = inicio; pos <= fim; pos++) {
            vetor[pos] = auxiliar[pos];
            c.movimentos++;
        }
    }

    static boolean ordenado(int[] vetor) {
        for (int i = 1; i < vetor.length; i++) {
            if (vetor[i - 1] > vetor[i]) {
                return false;
            }
        }
        return true;
    }

    static void testar() {
        int[] exemplo = {5, 2, 9, 1, 3};
        int[] esperado = {1, 2, 3, 5, 9};
        int[] copia = exemplo.clone();
        selectionSort(copia, new Contador());
        if (!Arrays.equals(copia, esperado)) throw new IllegalStateException("Erro no Selection Sort");
        copia = exemplo.clone();
        mergeSort(copia, new Contador());
        if (!Arrays.equals(copia, esperado)) throw new IllegalStateException("Erro no Merge Sort");
        if (buscaLinear(exemplo, 9, new Contador()) < 0) throw new IllegalStateException("Erro na busca linear");
        if (buscaBinaria(esperado, 9, new Contador()) < 0) throw new IllegalStateException("Erro na busca binaria");
        if (buscaLinear(exemplo, 8, new Contador()) != -1) throw new IllegalStateException("Erro na busca linear");
        if (buscaBinaria(esperado, 8, new Contador()) != -1) throw new IllegalStateException("Erro na busca binaria");
    }

    public static void main(String[] args) throws Exception {
        testar();
        new File("resultados").mkdirs();
        int[] tamanhos = {100, 1000, 5000, 10000, 50000, 100000};
        String[] algoritmos = {"Busca Linear", "Busca Binaria", "Selection Sort", "Merge Sort"};

        try (PrintWriter saida = new PrintWriter("resultados/dados_brutos.csv", "UTF-8")) {
            saida.println("algoritmo;tipo;n;execucao;warmup;tempo_ms;comparacoes;trocas;movimentos;valido");

            for (int n : tamanhos) {
                for (String tipo : new String[]{"Aleatorio", "Ordenado"}) {
                    int[] original = new int[n];
                    for (int i = 0; i < n; i++) original[i] = i;
                    if (tipo.equals("Aleatorio")) {
                        Random sorteio = new Random(1234 + n);
                        for (int i = n - 1; i > 0; i--) {
                            int j = sorteio.nextInt(i + 1);
                            int temp = original[i];
                            original[i] = original[j];
                            original[j] = temp;
                        }
                    }

                    for (String algoritmo : algoritmos) {
                        // A primeira execucao e descartada da media.
                        for (int execucao = 1; execucao <= 4; execucao++) {
                            int[] vetor = original.clone();
                            Contador c = new Contador();
                            boolean valido;
                            int posicao = -1;

                            // A busca binaria precisa de dados ordenados.
                            // Esta preparacao nao entra no tempo medido.
                            if (algoritmo.equals("Busca Binaria")) Arrays.sort(vetor);

                            long inicio = System.nanoTime();
                            if (algoritmo.equals("Busca Linear")) {
                                posicao = buscaLinear(vetor, n + 1, c);
                            } else if (algoritmo.equals("Busca Binaria")) {
                                posicao = buscaBinaria(vetor, n + 1, c);
                            } else if (algoritmo.equals("Selection Sort")) {
                                selectionSort(vetor, c);
                            } else {
                                mergeSort(vetor, c);
                            }
                            long fim = System.nanoTime();

                            // A verificacao nao faz parte do tempo medido.
                            if (algoritmo.startsWith("Busca")) {
                                valido = posicao == -1;
                            } else {
                                valido = ordenado(vetor);
                            }
                            if (!valido) throw new IllegalStateException("Resultado incorreto: " + algoritmo);

                            double tempoMs = (fim - inicio) / 1_000_000.0;
                            saida.printf(Locale.US, "%s;%s;%d;%d;%s;%.6f;%d;%d;%d;%s%n",
                                    algoritmo, tipo, n, execucao, execucao == 1 ? "Sim" : "Nao",
                                    tempoMs, c.comparacoes, c.trocas, c.movimentos, "Sim");
                        }
                        saida.flush();
                        System.out.println(n + " | " + tipo + " | " + algoritmo + " concluido");
                    }
                }
            }
        }
        System.out.println("Arquivo criado: resultados/dados_brutos.csv");
    }
}
