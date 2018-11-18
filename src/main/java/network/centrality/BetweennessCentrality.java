package network.centrality;

import network.Node;

import java.util.*;

public class BetweennessCentrality {

    public BetweennessCentrality() {

    }

    public Map<String,Double> calculateBetweenness(List<Node> nodeList, double[][] adjacencyMatrix) {


        //CB[v] ← 0, v ∈ V ;
        Map<Integer,Double> C = new HashMap<Integer, Double>();
        for(int i = 0; i < adjacencyMatrix.length; i++) {
            C.put(i,0.0);
        }

        //for s ∈ V do
        for (int s = 0; s < adjacencyMatrix.length; s++) {
            //S ← empty stack;
            Stack<Integer> S = new Stack<Integer>();

            //P[w] ← empty list, w ∈ V ;
            Map<Integer,List<Integer>> P = new HashMap<Integer, List<Integer>>();
            for (int i = 0; i < adjacencyMatrix.length; i++) {
                P.put(i, new LinkedList<Integer>());
            }

            //σ[t] ← 0, t ∈ V ; σ[s] ← 1;
            Map<Integer,Double> g = new HashMap<Integer, Double>();
            for (int i = 0; i < adjacencyMatrix.length; i++) {
                g.put(i,0.0);
            }
            g.put(s,1.0);

            //d[t] ← −1, t ∈ V ; d[s] ← 0;
            Map<Integer,Double> d = new HashMap<Integer, Double>();
            for (int i = 0; i < adjacencyMatrix.length; i++) {
                d.put(i,-1.0);
            }
            d.put(s,0.0);

            //Q ← empty queue;
            LinkedList<Integer> Q = new LinkedList<Integer>();

            //enqueue s → Q;
            Q.add(s);

            //while Q not empty do
            while (!Q.isEmpty()) {
                //dequeue v ← Q;
                int v = Q.remove();
                //push v → S;
                S.push(v);
                //foreach neighbor w of v do
                for (int w = 0; w < adjacencyMatrix[v].length; w++) {
                    //// w found for the first time?
                    if(adjacencyMatrix[v][w] != 0) {
                        //if d[w] < 0 then
                        if(d.get(w) < 0) {
                            //enqueue w → Q;
                            Q.add(w);
                            //d[w] ← d[v] + 1;
                            d.put(w, d.get(v) + 1);
                        }
                        //// shortest path to w via v?
                        //if d[w] = d[v] + 1 then
                        if(d.get(w) == (d.get(v) + 1)) {
                            //σ[w] ← σ[w] + σ[v];
                            g.put(w, g.get(w) + g.get(v));
                            //append v → P[w];
                            P.get(w).add(v);
                        }
                    }
                }
            }

            //δ[v] ← 0, v ∈ V ;
            Map<Integer,Double> delta = new HashMap<Integer, Double>();
            for(int i = 0; i < adjacencyMatrix.length; i++) {
                delta.put(i,0.0);
            }
            //// S returns vertices in order of non-increasing distance from s
            //while S not empty do
            while (!S.empty()) {
                //pop w ← S;
                int w = S.pop();
                //for v ∈ P[w] do δ[v] ← δ[v] + σ[v]/σ[w]· (1 + δ[w]);
                for(int i = 0; i < P.get(w).size(); i++) {
                    int v = P.get(w).get(i);
                    double temp = delta.get(v) + ((g.get(v)/(double)g.get(w)) * (1 + delta.get(w)));
                    delta.put(v, temp);
                }
                //if w != s then CB[w] ← CB[w] + δ[w];
                if(w != s) {
                    C.put(w, C.get(w) + delta.get(w));
                }
            }
        }

        Map<String, Double> resultMap = new HashMap<String, Double>();
        for (int i = 0; i < C.size(); i++) {
            resultMap.put(nodeList.get(i).getAddress(), C.get(i));
        }

        return resultMap;
    }
}
