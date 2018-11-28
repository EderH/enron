package network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.toIntExact;

public class Network {
    private List<Node> nodes;
    private List<Edge> edges;

    public Network() {
        nodes = new ArrayList<Node>();
        edges = new ArrayList<Edge>();
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public void addNode(Node node) {
        if(!containsNode(node)) {
            this.nodes.add(node);
        }
    }

    public Node getNode(String address){
        for (Node node : nodes) {
            if(node.getAddress().equals(address)){
                return node;
            }
        }
        return null;
    }

    public Node getNode(int id) {
        for (Node node : nodes) {
            if(node.getId() == id){
                return node;
            }
        }
        return null;
    }

    public boolean containsNode(Node node) {
        return this.nodes.contains(node);
    }

    public void addEdge(Edge newEdge) {
        if(!containsEdge(newEdge)) {
            this.edges.add(newEdge);
        } else {
            for (Edge edge: this.edges) {
                if(edge.equals(newEdge)) {
                    edge.increaseWeight();
                }
            }
        }
    }

    public boolean containsEdge(Edge edge) {
        return this.edges.contains(edge);
    }

    public double[][] getAdjacencyMatrix() {
        double[][] adjacencyMatrix = new double[this.nodes.size()][this.nodes.size()];


        for (Edge edge : edges) {
            adjacencyMatrix[edge.getFrom().getId()][edge.getTo().getId()] = 1;
            adjacencyMatrix[edge.getTo().getId()][edge.getFrom().getId()] = 1;
        }

        return adjacencyMatrix;
    }
}
