import Entities.Mail;
import POJO.Employee;
import POJO.Message;
import POJO.RecipientInfo;
import network.CreateNetwork;
import network.Edge;
import network.Network;
import network.centrality.BetweennessCentrality;
import org.hibernate.Query;
import org.hibernate.Session;

import persistent.HibernateUtil;

import java.util.*;

public class Main {

    public static void main( String[] args )
    {
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();
        Query query = session.createQuery("FROM Message group by body, sender, subject having count(*) < 2 ");
        query.setMaxResults(20);
        List<Message> messageList = query.list();

        List<Mail> mails = new ArrayList<Mail>();

        for (Message message : messageList) {
            Mail mail = new Mail((message));
            List<String> to = new ArrayList<String>();
            Query query3 = session.createQuery("FROM RecipientInfo WHERE mid = :mid");
            query3.setParameter("mid", mail.getMid());
            List<RecipientInfo> receivers = query3.list();
            for (RecipientInfo receiver:receivers) {
                to.add(receiver.getRvalue());
            }
            mail.setTo(to);
            mails.add(mail);
        }

        /*KNearestNeighbor kNearestNeighbor = new KNearestNeighbor(list.size());
        long[][] distanceMatrix = kNearestNeighbor.call(list);
        printMatrix(distanceMatrix);*/

        Query query2 = session.createQuery("from Employee");
        List<Employee> employeeList = query2.list();

        session.getTransaction().commit();
        session.getSessionFactory().close();

        System.out.println("Network");
        CreateNetwork createNetwork = new CreateNetwork(mails,employeeList);
        Network network = createNetwork.call();
        System.out.println(network.getNodes().size());
        System.out.println(network.getEdges().size());
        for (Edge edge:network.getEdges()) {
            System.out.println("From: " + edge.getFrom().getAddress() + " TO: " + edge.getTo().getAddress() + " Weight: " + edge.getWeight());
        }
        double[][] adjacencyMatrix = network.getAdjacencyMatrix();
        BetweennessCentrality betweennessCentrality = new BetweennessCentrality();
        Map<String, Double> betweennessNetwork = betweennessCentrality.calculateBetweenness(network.getNodes(),adjacencyMatrix);
        printMap(network, betweennessNetwork);
    }


    private TreeMap<String, Double> sortMap(Network network, Map<String, Double> map) {
        TreeMap<>
        for (int i = 0; i < network.getNodes().size() ; i++) {
            if(map.get(network.getNodes().get(i).getAddress()) != 0) {
                network.getNodes().get(i).getAddress(), map.get(network.getNodes().get(i).getAddress()));
            }
        }
    }


    private static void printMap(Network network, Map<String, Double> map) {
        for (int i = 0; i < network.getNodes().size() ; i++) {
            if(map.get(network.getNodes().get(i).getAddress()) != 0) {
                System.out.printf("Employee: %s Betweenness: %.2f \n",network.getNodes().get(i).getAddress(), map.get(network.getNodes().get(i).getAddress()));
            }
        }
    }

    private static void printMatrix(long[][] matrix) {
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[x].length; y++) {
                System.out.print("| " + matrix[x][y] + " |");
            }
            System.out.println();
        }
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
