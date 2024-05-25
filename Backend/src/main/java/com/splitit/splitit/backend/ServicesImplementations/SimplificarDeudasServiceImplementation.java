package com.splitit.splitit.backend.ServicesImplementations;

import com.splitit.splitit.backend.Services.SimplificarDeudasService;
import java.util.*;

import org.springframework.stereotype.Service;

import static java.lang.Math.min;
import com.splitit.splitit.backend.DTO.DtSaldo;
import com.splitit.splitit.backend.DTO.DtSaldoSimplificado;

@Service
public class SimplificarDeudasServiceImplementation implements SimplificarDeudasService{

	private static final long OFFSET = 1000000000L;
	private static Set<Long> visitedEdges;
	
	@Override
	public List<DtSaldoSimplificado> simplificarDeudas(List<DtSaldo> saldos) {
		List<Integer> personInt = new ArrayList<Integer>();
		for(DtSaldo saldo : saldos) {
			int idDeudor = saldo.getDeudor().getId();
			int idAcreedor = saldo.getAcreedor().getId();
			if(!personInt.contains(idDeudor) ) {
				personInt.add(idDeudor);
			}
			if(!personInt.contains(idAcreedor) ) {
				personInt.add(idAcreedor);
			}
		}
		//String[] person = {};
		List<String> personList = new ArrayList<String>();
		for(Integer pi : personInt) {
			personList.add(pi.toString());
		}
		String[] person = personList.toArray(new String[0]);
		 
	    
	    
	    int n = person.length;
	    //  Creating a graph with n vertices
	    Dinics solver = new Dinics(n, person);
	    //  Adding edges to the graph
	    solver = addAllTransactions(solver, saldos);

	    System.out.println();
	    System.out.println("Deudas simplificadas");
	    System.out.println("--------------------");
	    System.out.println();

	    //  Map to keep track of visited edges
	    visitedEdges = new HashSet<>();
	    Integer edgePos;

	    while((edgePos = getNonVisitedEdge(solver.getEdges())) != null) {
	      //  Force recomputation of subsequent flows in the graph
	      solver.recompute();
	      //  Set source and sink in the flow graph
	      Dinics.Edge firstEdge = solver.getEdges().get(edgePos);
	      solver.setSource(firstEdge.from);
	      solver.setSink(firstEdge.to);
	      //  Initialize the residual graph to be same as the given graph
	      List<Dinics.Edge>[] residualGraph = solver.getGraph();
	      List<Dinics.Edge> newEdges = new ArrayList<>();

	      for(List<Dinics.Edge> allEdges : residualGraph) {
	        for(Dinics.Edge edge : allEdges) {
	        double remainingFlow = ((edge.flow < 0) ? edge.capacity : (edge.capacity - edge.flow));
	          //  If there is capacity remaining in the graph, then add the remaining capacity as an edge
	          //  so that it can be used for optimizing other debts within the graph
	          if(remainingFlow > 0) {
	            newEdges.add(new Dinics.Edge(edge.from, edge.to, remainingFlow));
	          }
	        }
	      }

	      //  Get the maximum flow between the source and sink
	      long maxFlow = solver.getMaxFlow();
	      //  Mark the edge from source to sink as visited
	      int source = solver.getSource();
	      int sink = solver.getSink();
	      visitedEdges.add(getHashKeyForEdge(source, sink));
	      //  Create a new graph
	      solver = new Dinics(n, person);
	      //  Add edges having remaining capacity
	      solver.addEdges(newEdges);
	      //  Add an edge from source to sink in the new graph with obtained maximum flow as it's weight
	      solver.addEdge(source, sink, maxFlow);
	    }
	    //  Print the edges in the graph
	    solver.printEdges();
	    System.out.println();
	    
	    
	    return solver.getSaldosFinales();	    
	  }

	  private static Dinics addAllTransactions(Dinics solver, List<DtSaldo> saldos) {

		for(DtSaldo saldo : saldos) {
			//CONVERSION DE AVATAR ID A INDEX LOCAL
			int indexDeudor = 0;
			int indexAcreedor = 0;
			String[] vertexLabels = solver.vertexLabels;
			int index = 0;
			for(String vertex : vertexLabels) {
				if(vertex.equals(Integer.valueOf(saldo.getDeudor().getId()).toString())) {
					indexDeudor = index;
				}
				if(vertex.equals(Integer.valueOf(saldo.getAcreedor().getId()).toString())) {
					indexAcreedor = index;
				}
				index ++;
			}
			//solver.addEdge(saldo.getDeudor().getId(), saldo.getAcreedor().getId(), saldo.getMonto());	
			solver.addEdge(indexDeudor, indexAcreedor, saldo.getMonto());	
		}
		
	    return solver;
	  }

	  /**
	  * Get any non visited edge in the graph
	  * @param edges list of all edges in the graph
	  * @return index of a non visited edge
	  */
	  private static Integer getNonVisitedEdge(List<Dinics.Edge> edges) {
	    Integer edgePos = null;
	    int curEdge = 0;
	    for(Dinics.Edge edge : edges) {
	      if(!visitedEdges.contains(getHashKeyForEdge(edge.from, edge.to))) {
	        edgePos = curEdge;
	      }
	      curEdge++;
	    }
	    return edgePos;
	  }

	  /**
	  * Get a unique hash key for a given edge
	  * @param u the starting vertex in the edge
	  * @param v the ending vertex in the edge
	  * @return a unique hash key
	  */
	  private static Long getHashKeyForEdge(int u, int v) {
	    return u * OFFSET + v;
	  }
	
	/**
	 * Implementation of Dinic's network flow algorithm. The algorithm works by first constructing a
	 * level graph using a BFS and then finding augmenting paths on the level graph using multiple DFSs.
	 *
	 * <p>Time Complexity: O(EV²)
	 *
	 * @link https://github.com/williamfiset/Algorithms
	 */
	  class Dinics extends NetworkFlowSolverBase {

		  private int[] level;
	
		  /**
		   * Creates an instance of a flow network solver. Use the {@link #addEdge} method to add edges to
		   * the graph.
		   *
		   * @param n - The number of nodes in the graph including source and sink nodes.
		   */
		  public Dinics(int n, String[] vertexLabels) {
		    super(n, vertexLabels);
		    level = new int[n];
		  }
	
		  @Override
		  public void solve() {
		    // next[i] indicates the next unused edge index in the adjacency list for node i. This is part
		    // of the Shimon Even and Alon Itai optimization of pruning deads ends as part of the DFS phase.
		    int[] next = new int[n];
	
		    while (bfs()) {
		      Arrays.fill(next, 0);
		      // Find max flow by adding all augmenting path flows.
		      for (double f = dfs(s, next, INF); f != 0; f = dfs(s, next, INF)) {
		        maxFlow += f;
		      }
		    }
	
		    for (int i = 0; i < n; i++) if (level[i] != -1) minCut[i] = true;
		  }
	
		  // Do a BFS from source to sink and compute the depth/level of each node
		  // which is the minimum number of edges from that node to the source.
		  private boolean bfs() {
		    Arrays.fill(level, -1);
		    level[s] = 0;
		    Deque<Integer> q = new ArrayDeque<>(n);
		    q.offer(s);
		    while (!q.isEmpty()) {
		      int node = q.poll();
		      for (Edge edge : graph[node]) {
		    	double cap = edge.remainingCapacity();
		        if (cap > 0 && level[edge.to] == -1) {
		          level[edge.to] = level[node] + 1;
		          q.offer(edge.to);
		        }
		      }
		    }
		    return level[t] != -1;
		  }
	
		  private double dfs(int at, int[] next, double flow) {
		    if (at == t) return flow;
		    final int numEdges = graph[at].size();
	
		    for (; next[at] < numEdges; next[at]++) {
		      Edge edge = graph[at].get(next[at]);
		      double cap = edge.remainingCapacity();
		      if (cap > 0 && level[edge.to] == level[at] + 1) {
	
		    	  double bottleNeck = dfs(edge.to, next, min(flow, cap));
		        if (bottleNeck > 0) {
		          edge.augment(bottleNeck);
		          return bottleNeck;
		        }
		      }
		    }
		    return 0;
		  }
	}


	abstract class NetworkFlowSolverBase {

	  // To avoid overflow, set infinity to a value less than Long.MAX_VALUE;
	  protected static final double INF = Long.MAX_VALUE / 2;

	  public static class Edge {
	    public int from, to;
	    public String fromLabel, toLabel;
	    public Edge residual;
	    public double flow, cost;
	    public final double capacity, originalCost;

	    public Edge(int from, int to, double capacity) {
	      this(from, to, capacity, 0 /* unused */);
	    }

	    public Edge(int from, int to, double capacity, double cost) {
	      this.from = from;
	      this.to = to;
	      this.capacity = capacity;
	      this.originalCost = this.cost = cost;
	    }

	    public boolean isResidual() {
	      return capacity == 0;
	    }

	    public double remainingCapacity() {
	      return capacity - flow;
	    }

	    public void augment(double bottleNeck) {
	      flow += bottleNeck;
	      residual.flow -= bottleNeck;
	    }

	    public String toString(int s, int t) {
	      String u = (from == s) ? "s" : ((from == t) ? "t" : String.valueOf(from));
	      String v = (to == s) ? "s" : ((to == t) ? "t" : String.valueOf(to));
	      return String.format(
	          "Edge %s -> %s | flow = %d | capacity = %d | is residual: %s",
	          u, v, flow, capacity, isResidual());
	    }
	  }

	  // Inputs: n = number of nodes, s = source, t = sink
	  protected int n, s, t;

	  protected long maxFlow;
	  protected long minCost;

	  protected boolean[] minCut;
	  protected List<Edge>[] graph;
	  protected String[] vertexLabels;
	  protected List<Edge> edges;

	  // 'visited' and 'visitedToken' are variables used for graph sub-routines to
	  // track whether a node has been visited or not. In particular, node 'i' was
	  // recently visited if visited[i] == visitedToken is true. This is handy
	  // because to mark all nodes as unvisited simply increment the visitedToken.
	  private int visitedToken = 1;
	  private int[] visited;

	  // Indicates whether the network flow algorithm has ran. We should not need to
	  // run the solver multiple times, because it always yields the same result.
	  protected boolean solved;

	  /**
	   * Creates an instance of a flow network solver. Use the {@link #addEdge} method to add edges to
	   * the graph.
	   *
	   * @param n - The number of nodes in the graph including source and sink nodes.
	   */
	  public NetworkFlowSolverBase(int n, String[] vertexLabels) {
	    this.n = n;
	    initializeGraph();
	    assignLabelsToVertices(vertexLabels);
	    minCut = new boolean[n];
	    visited = new int[n];
	    edges = new ArrayList<>();
	  }

	  // Construct an empty graph with n nodes including the source and sink nodes.
	  private void initializeGraph() {
	    graph = new List[n];
	    for (int i = 0; i < n; i++) graph[i] = new ArrayList<Edge>();
	  }

	  // Add labels to vertices in the graph.
	  private void assignLabelsToVertices(String[] vertexLabels) {
	    if(vertexLabels.length != n)
	      throw new IllegalArgumentException(String.format("You must pass %s number of labels", n));
	    this.vertexLabels = vertexLabels;
	  }

	  /**
	   * Adds a list of directed edges (and residual edges) to the flow graph.
	   *
	   * @param edges - A list of all edges to be added to the flow graph.
	   */
	  public void addEdges(List<Edge> edges) {
	    if (edges == null) throw new IllegalArgumentException("Edges cannot be null");
	    for(Edge edge : edges) {
	      addEdge(edge.from, edge.to, edge.capacity);
	    }
	  }

	  /**
	   * Adds a directed edge (and residual edge) to the flow graph.
	   *
	   * @param from - The index of the node the directed edge starts at.
	   * @param to - The index of the node the directed edge ends at.
	   * @param capacity - The capacity of the edge.
	   */
	  public void addEdge(int from, int to, double capacity) {
	    if (capacity < 0) throw new IllegalArgumentException("Capacity < 0");
	    Edge e1 = new Edge(from, to, capacity);
	    Edge e2 = new Edge(to, from, 0);
	    e1.residual = e2;
	    e2.residual = e1;
	    graph[from].add(e1);
	    graph[to].add(e2);
	    edges.add(e1);
	  }

	  /** Cost variant of {@link #addEdge(int, int, int)} for min-cost max-flow */
	  public void addEdge(int from, int to, double capacity, double cost) {
	    Edge e1 = new Edge(from, to, capacity, cost);
	    Edge e2 = new Edge(to, from, 0, -cost);
	    e1.residual = e2;
	    e2.residual = e1;
	    graph[from].add(e1);
	    graph[to].add(e2);
	    edges.add(e1);
	  }

	  // Marks node 'i' as visited.
	  public void visit(int i) {
	    visited[i] = visitedToken;
	  }

	  // Returns whether or not node 'i' has been visited.
	  public boolean visited(int i) {
	    return visited[i] == visitedToken;
	  }

	  // Resets all nodes as unvisited. This is especially useful to do
	  // between iterations of finding augmenting paths, O(1)
	  public void markAllNodesAsUnvisited() {
	    visitedToken++;
	  }

	  /**
	   * Returns the graph after the solver has been executed. This allow you to inspect the {@link
	   * Edge#flow} compared to the {@link Edge#capacity} in each edge. This is useful if you want to
	   * figure out which edges were used during the max flow.
	   */
	  public List<Edge>[] getGraph() {
	    execute();
	    return graph;
	  }

	  /**
	   * Returns all edges in this flow network
	   */
	  public List<Edge> getEdges() {
	    return edges;
	  }

	  // Returns the maximum flow from the source to the sink.
	  public long getMaxFlow() {
	    execute();
	    return maxFlow;
	  }

	  // Returns the min cost from the source to the sink.
	  // NOTE: This method only applies to min-cost max-flow algorithms.
	  public long getMinCost() {
	    execute();
	    return minCost;
	  }

	  // Returns the min-cut of this flow network in which the nodes on the "left side"
	  // of the cut with the source are marked as true and those on the "right side"
	  // of the cut with the sink are marked as false.
	  public boolean[] getMinCut() {
	    execute();
	    return minCut;
	  }

	  /**
	   * Used to set the source for this flow network 
	   */
	  public void setSource(int s) {
	    this.s = s;
	  }

	  /**
	   * Used to set the sink for this flow network 
	   */
	  public void setSink(int t) {
	    this.t = t;
	  }

	  /**
	   * Get source for this flow network 
	   */
	  public int getSource() {
	    return s;
	  }

	  /**
	   * Get sink for this flow network 
	   */
	  public int getSink() {
	    return t;
	  }

	  /**
	   * Set 'solved' flag to false to force recomputation of subsequent flows.
	   */
	  public void recompute() {
	    solved = false;
	  }

	  /**
	   * Print all edges.
	   */
	  public void printEdges() {
	    for(Edge edge : edges) {
	      System.out.println(String.format("%s ----%s----> %s", vertexLabels[edge.from], edge.capacity, vertexLabels[edge.to]));
	    }
	  }
	  
	  public List<DtSaldoSimplificado> getSaldosFinales(){
		  List<DtSaldoSimplificado> saldosFinales = new ArrayList<DtSaldoSimplificado>();
		  
		  for(Edge edge : edges) {
			  //CONVERSION DE INDEX LOCAL A AVATAR ID
			  int deudorAvatarId = Integer.parseInt(vertexLabels[edge.from]);
			  int acreedorAvatarId = Integer.parseInt(vertexLabels[edge.to]);
			  
			  
			  DtSaldoSimplificado saldoSimplificado = new DtSaldoSimplificado(deudorAvatarId, acreedorAvatarId, edge.capacity);
			  saldosFinales.add(saldoSimplificado);
		  }
		  return saldosFinales;		  
	  }

	  // Wrapper method that ensures we only call solve() once
	  private void execute() {
	    if (solved) return;
	    solved = true;
	    solve();
	  }

	  // Method to implement which solves the network flow problem.
	  public abstract void solve();

	}
}