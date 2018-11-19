package network.centrality;

import network.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClosenessCentrality {

    public ClosenessCentrality() {

    }

    public Map<String, Double> calculateCloseness(List<Node> nodeList, double[][] adjacencyMatrix) {
        double[][] floydWarshallMatrix = floydWarshall(adjacencyMatrix);

        double[] closeness = new double[floydWarshallMatrix.length];

        double sumOfDistances;
        for (int i = 0; i < floydWarshallMatrix.length; i++) {
            sumOfDistances = 0;
            for (int j = 0; j < floydWarshallMatrix.length; j++) {
                sumOfDistances += 1 / floydWarshallMatrix[i][j];
            }
            closeness[i] = sumOfDistances / (double)(floydWarshallMatrix.length - 1);
        }

        Map<String, Double> resultMap = new HashMap<String, Double>();
        for (int i = 0; i < closeness.length; i++) {
            resultMap.put(nodeList.get(i).getAddress(), closeness[i]);
        }

        return resultMap;
    }

    public double[][] floydWarshall(double[][] adjacencyMatrix) {
        double[][] dist = new double[adjacencyMatrix.length][adjacencyMatrix.length];

        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                if(adjacencyMatrix[i][j] >= 1) {
                    dist[i][j] = 1;
                } else {
                    dist[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }

        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                for(int l = 0; l < adjacencyMatrix.length; l++) {
                    if(dist[j][i] + dist[i][l] < dist[j][l]) {
                        dist[j][l] = dist[j][i] + dist[i][l];
                    }
                }
            }
        }

        return dist;
    }

    private static void printMatrix(double[][] matrix) {
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[x].length; y++) {
                System.out.print("| " + matrix[x][y] + " |");
            }
            System.out.println();
        }
    }
}
