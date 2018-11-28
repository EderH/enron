import com.mysql.cj.xdevapi.JsonArray;
import network.Edge;
import network.Network;
import network.Node;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JSONReader {
    private JSONParser jsonParser;
    public JSONReader() {
        jsonParser = new JSONParser();
    }

    public Network readNodes(String path) {

        Network network = new Network();
        try {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(new FileReader(path));

            for (int i = 0; i < jsonArray.size(); i++) {
                network.addNode(new Node(String.valueOf(jsonArray.get(i))));
            }

        } catch (IOException | ParseException e) {
            System.err.println(e.getMessage());
        }
        return network;
    }

    public void readAdjacencyMatrix(String path, Network network) {
        try {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(new FileReader(path));
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONArray nodes = (JSONArray) jsonArray.get(i);
                Node nodeFrom = network.getNode(i);
                for (int j = 0; j < nodes.size(); j++) {
                    JSONArray node = (JSONArray) nodes.get(j);
                    nodeFrom.increaseOutbound();
                    Node nodeTo = network.getNode(Math.toIntExact((Long)(node.get(0))));
                    nodeTo.increaseInbound();
                    Edge edge = new Edge(nodeFrom, nodeTo);
                    edge.setWeight(Math.toIntExact((Long)node.get(1)));
                    network.addEdge(edge);
                }
            }
        } catch (IOException | ParseException e) {
            System.err.println(e.getMessage());
        }
    }
}
