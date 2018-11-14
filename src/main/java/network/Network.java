package network;

import java.util.ArrayList;
import java.util.List;

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

    public boolean containsNode(Node node) {
        return this.nodes.contains(node);
    }

    public void addEdge(Edge edge) {
        if(!containsEdge(edge)) {
            this.edges.add(edge);
        }
    }

    public boolean containsEdge(Edge edge) {
        return this.edges.contains(edge);
    }
}
