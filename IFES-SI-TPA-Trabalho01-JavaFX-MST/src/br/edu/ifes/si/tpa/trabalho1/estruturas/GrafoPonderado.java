package br.edu.ifes.si.tpa.trabalho1.estruturas;

/*******************************************************************************
 *  Compilação:        javac GrafoPonderado.java
 *  Execução:          java GrafoPonderado dados.txt
 *  Dependências:      Aresta.java Vertice.java
 *  Arquivos de dados: GrafoPonderado1.txt
 *
 *  Um grafo ponderado de arestas, implementado utilizando listas de adjacências.
 *
 *  % java GrafoPonderado GrafoPonderado1.txt
 *  8 16
 *  0: 0-6 0,58000  0-2 0,26000  0-4 0,38000  0-7 0,16000  
 *  1: 1-3 0,29000  1-2 0,36000  1-7 0,19000  1-5 0,32000  
 *  2: 2-6 0,40000  2-7 0,34000  2-1 0,36000  2-0 0,26000  2-3 0,17000  
 *  3: 3-6 0,52000  3-1 0,29000  3-2 0,17000  
 *  4: 4-6 0,93000  4-0 0,38000  4-7 0,37000  4-5 0,35000  
 *  5: 5-1 0,32000  5-7 0,28000  5-4 0,35000  
 *  6: 6-4 0,93000  6-0 0,58000  6-3 0,52000  6-2 0,40000  
 *  7: 7-2 0,34000  7-1 0,19000  7-0 0,16000  7-5 0,28000  7-4 0,37000
 *
 ******************************************************************************/


import java.util.ArrayList;
import java.util.List;

/**
 * Esta classe implementa a representação do grafo ponderado com lista de adjacências.
 * Para documentação adicional, acesse:
 * <a href="http://algs4.cs.princeton.edu/43mst/">Section 4.3</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class GrafoPonderado {

    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;              // número de vértices no grafo
    private int A;                    // número de arestas no grafo
    private List<Aresta>[] adj;       // adj[v1] = lista de adjacência do vértice v1
    private List<Vertice> vertices;   // vértices com localizações de posição x e y

    /**
     * Inicializa um grafo com V vértices e 0 arestas.
     * @param  V o número de vértices
     * @throws IllegalArgumentException se V < 0
     */
    public GrafoPonderado(int V) {
        if (V < 0) {
            throw new IllegalArgumentException("Número de vértices no grafo deve ser não negativo");
        }
        this.V = V;
        this.A = 0;
        adj = new ArrayList[V];
        vertices = new ArrayList<>();
        for (int v = 0; v < V; v++) {
            adj[v] = new ArrayList<>();
        }
    }

    /**  
     * Inicializa um grafo à partir de um arquivo de dados.
     * O formato é o número de vértices V e o número de arestas A
     * seguido por pares de pares de vértices.
     * @param  in o arquivo de entrada de dados
     * @throws IndexOutOfBoundsException se os pontos finais de qualquer borda estão fora da área prescrita
     * @throws IllegalArgumentException se o número de vértices ou arestas for negativo
     */
    public GrafoPonderado(In in) {
        this(in.readInt());
        int A = in.readInt();
        if (A < 0) {
            throw new IllegalArgumentException("Número de arestas deve ser não negativo");
        }
        
        //Adicionando os vértices
        for (int i = 0; i < V; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            this.vertices.add(i, new Vertice(i, x, y));
        }
        
        //Adicionando as arestas
        for (int i = 0; i < A; i++) {
            int v1 = in.readInt();
            int v2 = in.readInt();
            double peso = in.readDouble();
            addAresta(new Aresta(v1, v2, peso));
        }
    }

    /**
     * Retorna o número de vértices do dígrafo.
     * @return o número de vértices do dígrafo
     */
    public int V() {
        return V;
    }

    /**
     * Retorna o número de arestas do dígrafo.
     * @return o número de arestas do dígrafo
     */
    public int A() {
        return A;
    }

    /**
     * Valida vértice do dígrafo.
     * @throws IndexOutOfBoundsException caso v não seja 0 <= v < V
     */
    private void validaVertice(int v) {
        if (v < 0 || v >= V)
            throw new IndexOutOfBoundsException("vértice " + v + " não está entre 0 e " + (V-1));
    }

    /**
     * Adiciona aresta direcionada a no dígrafo.
     * @param  a a aresta
     * @throws IndexOutOfBoundsException caso extremidades não estejam entre 0 e V-1
     */
    public void addAresta(Aresta a) {
        int v1 = a.umVertice();
        int v2 = a.outroVertice(v1);
        validaVertice(v1);
        validaVertice(v2);
        adj[v1].add(0, a);
        Aresta a2 = new Aresta(a.getV2(), a.getV1(), a.peso());
        adj[v2].add(0, a2);
        A++;
    }

    /**
     * Retorna as arestas incidentes no vértice v.
     * @param  v o vértice
     * @return as arestas incidentes no vértice v como um Iterable
     * @throws IndexOutOfBoundsException caso v não seja 0 <= v < V
     */
    public List<Aresta> adj(int v) {
        validaVertice(v);
        return adj[v];
    }

    /**
     * Retorna o grau do vértice v.
     * @param v o vértice
     * @return o grau do vértice v
     * @throws IndexOutOfBoundsException caso não seja 0 <= v < V
     */
    public int grau(int v) {
        validaVertice(v);
        return adj[v].size();
    }

    /**
     * Retorna todas as arestas neste grafo.
     * @return todas as arestas neste grafo, como um Iterable
     */
    public List<Aresta> arestas() {
        List<Aresta> lista = new ArrayList<Aresta>();
        for (int v = 0; v < V; v++) {
            int selfLoops = 0;
            for (Aresta a : adj(v)) {
                if (a.outroVertice(v) > v) {
                    lista.add(a);
                    lista.add(v, a);
                } // only add one copy of each self loop (self loops will be consecutive)
                else if (a.outroVertice(v) == v) {
                    if (selfLoops % 2 == 0) {
                        lista.add(a);
                    }
                    selfLoops++;
                }
            }
        }
        return lista;
    }
    
    /**
     * Retorna todos os vértices neste grafo.
     * @return todos os vértices neste grafo, como um List
     */
    public List<Vertice> vertices() {
        return this.vertices;
    }

    /**
     * Retorna uma representação String deste grafo.
     * @return uma representação String deste grafo
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " " + A + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (Aresta a : adj[v]) {
                s.append(a + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    /**
     * Testa a classe GrafoPonderado.
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        GrafoPonderado G = new GrafoPonderado(in);
        System.out.println(G);
    }

}
