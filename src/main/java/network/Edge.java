package network;

public class Edge {
    private Node from;
    private Node to;
    private int weight;

    public Edge(Node from, Node to) {
        this.from = from;
        this.to = to;
        this.weight = 1;
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }

    public int getWeight() {
        return weight;
    }

    public void setFrom(Node from) {
        this.from = from;
    }

    public void setTo(Node to) {
        this.to = to;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void increaseWeight() {
        this.weight++;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Edge) {
            Edge edge = (Edge) obj;
            if(this.getFrom().equals(edge.from) && this.getTo().equals(edge.to)){
                return true;
            }
        }
        return false;
    }
}
