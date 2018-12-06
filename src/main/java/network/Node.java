package network;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private String address;
    private static int count = 0;
    private int id;
    private int inbound = 0;
    private int outbound = 0;

    public Node(String address) {
        this.address = address;
        this.id = count++;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public int getInbound() {
        return inbound;
    }

    public int getOutbound() {
        return outbound;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setInbound(int inbound) {
        this.inbound = inbound;
    }

    public void setOutbound(int outbound) {
        this.outbound = outbound;
    }

    public void increaseInbound() {
        this.inbound++;
    }

    public void increaseOutbound() {
        this.outbound++;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Node) {
            Node node = (Node) obj;
            if(this.getAddress().equals(node.getAddress())) {
                return true;
            }
        }
        return false;
    }
}
