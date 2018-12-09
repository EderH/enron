import Entities.Mail;
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
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
                    Node nodeTo = network.getNode(Math.toIntExact((Long) (node.get(0))));
                    nodeTo.increaseInbound();
                    Edge edge = new Edge(nodeFrom, nodeTo);
                    edge.setWeight(Math.toIntExact((Long) node.get(1)));
                    network.addEdge(edge);
                }
            }
        } catch (IOException | ParseException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<Mail> readBodyOfMail(String path) {
        List<Mail> mails = new ArrayList<>();
        try {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(new FileReader(path));
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    Mail mail = new Mail();
                    mail.setMid(Math.toIntExact((Long) jsonObject.get("mid")));
                    mail.setSender((String) jsonObject.get("sender"));
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date parsedDate = (java.sql.Date) sdf.parse((String)jsonObject.get("date"));
                    Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                    mail.setDate( timestamp);
                } catch(Exception e) { //this generic but you can control another types of exception
                    // look the origin of excption
                }
                    mail.setMessageID((String) jsonObject.get("message_id"));
                    mail.setSubject((String) jsonObject.get("subject"));
                    mail.setBody((String) jsonObject.get("body"));
                    mail.setFolder((String) jsonObject.get("folder"));
                    mails.add(mail);
            }
        } catch (IOException | ParseException e) {
            System.err.println(e.getMessage());
        }
        return mails;
    }
}
