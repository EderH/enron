package network.centrality;

import network.Network;
import network.Node;

import java.util.HashMap;
import java.util.Map;

public class DegreeCentrality {

    public DegreeCentrality() {

    }

    public Map<Integer, Integer> getInBoundDegree(Network network) {
        Map<Integer, Integer> inBoundMap = new HashMap<Integer, Integer>();
        for (Node node : network.getNodes()) {
            inBoundMap.put(node.getId(), node.getInbound());
        }
        return inBoundMap;
    }

    public Map<Integer, Integer> getOutBoundDegree(Network network) {
        Map<Integer, Integer> outBoundMap = new HashMap<Integer, Integer>();
        for (Node node : network.getNodes()) {
            outBoundMap.put(node.getId(), node.getOutbound());
        }
        return outBoundMap;
    }
}
