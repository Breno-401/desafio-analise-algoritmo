# Desafio: A Corrida da Eficiencia

Trabalho da disciplina de Projeto e Analise de Algoritmos.

O programa compara quatro algoritmos usando vetores de inteiros:

- Busca Linear
- Busca Binaria
- Selection Sort
- Merge Sort

## Como executar

Precisa ter o Java instalado (JDK 21 ou compativel).

```bash
javac Main.java
java Main
```

O programa faz alguns testes simples antes de iniciar. Depois, testa vetores com 100, 1.000, 5.000, 10.000, 50.000 e 100.000 numeros, aleatorios e ordenados.

Cada caso e executado 4 vezes. A primeira execucao e descartada e as outras 3 sao usadas na media. Os resultados aparecem em `resultados/dados_brutos.csv`.

## O que foi medido

- Tempo em milissegundos, com `System.nanoTime()`.
- Numero de comparacoes entre valores.
- Trocas no Selection Sort (3 movimentos para cada troca).
- Movimentos no Merge Sort (escritas no vetor auxiliar e no original).

Nas buscas, usei um numero que nao esta no vetor (`n + 1`) para observar o caso de busca sem resultado. A busca binaria recebe uma copia ja ordenada; o tempo da ordenacao previa **nao entra** na medicao da busca.

As comparacoes entre algoritmos de ordenacao usam copias iguais dos vetores. Para os testes aleatorios, a semente foi fixada para repetir os mesmos dados.

## Arquivos

- `Main.java`: implementacao e execucao dos testes.
- `resultados/dados_brutos.csv`: medidas de cada execucao, inclusive o warm-up.
- `resultados/medias.csv`: media das tres execucoes validas de cada caso.
- `resultados/dados_brutos.xlsx`: planilha com todos os dados e as medias.
- `relatorio.pdf`: explicacao, resultados, graficos e conclusao.

**Observacao:** tempos podem mudar conforme computador, sistema e programas abertos.
