import Entities.Mail;
import POJO.Employee;
import POJO.Message;
import POJO.RecipientInfo;
import network.CreateNetwork;
import network.Edge;
import network.Network;
import network.Node;
import network.centrality.*;
import org.hibernate.Query;
import org.hibernate.Session;

import org.jblas.Eigen;
import persistent.HibernateUtil;
import weka.classifiers.meta.FilteredClassifier;
import weka.clusterers.*;
import weka.core.*;
import weka.experiment.InstanceQuery;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        /**
         * load network from json file.
         */

        JSONReader jsonReader = new JSONReader();
        Network network = jsonReader.readNodes(Paths.get("src/main/resources/java_persons.json").toAbsolutePath().toString());
        jsonReader.readAdjacencyMatrix(Paths.get("src/main/resources/java_network.json").toAbsolutePath().toString(),network);
        List<Mail> mails = jsonReader.readBodyOfMail(Paths.get("src/main/resources/java_mails_6.json").toAbsolutePath().toString());
        //System.out.println(mails.size());
        /**
         * print edges to txt-file.
         */
        /*
        try {
            PrintWriter writer = new PrintWriter("network.txt", "UTF-8");
            for (Edge edge:network.getEdges()) {
                writer.println(edge.getFrom().getId() + "," + edge.getTo().getId());
            }
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            System.err.println(e.getMessage());
        }

        /**
         * load data from sql database.
          */

        /*Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();
        Query query = session.createQuery("FROM Message group by body, sender, subject having count(*) < 2 ");
        query.setMaxResults(5);
        List<Message> messageList = query.list();

        List<Mail> mails = new ArrayList<Mail>();

        for (Message message : messageList) {
            Mail mail = new Mail((message));
            List<String> to = new ArrayList<String>();
            Query query3 = session.createQuery("FROM RecipientInfo WHERE mid = :mid");
            query3.setParameter("mid", mail.getMid());
            List<RecipientInfo> receivers = query3.list();
            for (RecipientInfo receiver : receivers) {
                to.add(receiver.getRvalue());
            }
            mail.setTo(to);
            mails.add(mail);
        }

        //Query query2 = session.createQuery("from Employee");
        //List<Employee> employeeList = query2.list();

        session.getTransaction().commit();
        session.getSessionFactory().close();
*/
        /*KNearestNeighbor kNearestNeighbor = new KNearestNeighbor(list.size());
        long[][] distanceMatrix = kNearestNeighbor.call(list);
        printMatrix(distanceMatrix);

        System.out.println("Network");
        CreateNetwork createNetwork = new CreateNetwork(mails,employeeList);
        Network network = createNetwork.call();

        */
        /*for (Edge edge:network.getEdges()) {
            System.out.println("From: " + edge.getFrom().getAddress() + " TO: " + edge.getTo().getAddress() + " Weight: " + edge.getWeight());
        }
        double[][] adjacencyMatrix = network.getAdjacencyMatrix();
        System.out.println("\nDegree Centrality");
        DegreeCentrality degreeCentrality = new DegreeCentrality();
        Map<Integer, Integer> inBoundDegree = degreeCentrality.getInBoundDegree(network);
        Map<Integer, Integer> outBoundDegree = degreeCentrality.getOutBoundDegree(network);
        System.out.println("\nInBound Degree");
        printDegreeMap(network, inBoundDegree);
        System.out.println("\nOutBound Degree");
        printDegreeMap(network, outBoundDegree);

        System.out.println("\nBetweenness Centrality:");
        BetweennessCentrality betweennessCentrality = new BetweennessCentrality();
        Map<Integer, Double> betweennessNetwork = betweennessCentrality.calculateBetweenness(network.getNodes(),adjacencyMatrix);
        printBetweennessMap(network, betweennessNetwork);

        System.out.println("\nCloseness Centrality:");
        ClosenessCentrality closenessCentrality = new ClosenessCentrality();
        Map<Integer, Double> closenessNetwork = closenessCentrality.calculateCloseness(network.getNodes(), adjacencyMatrix);
        printClosenessMap(network, closenessNetwork);

        System.out.println("\nEigenvector Centrality:");
        EigenvectorCentrality eigenvectorCentrality = new EigenvectorCentrality();
        Map<Integer, Double> principalEigenvector = eigenvectorCentrality.getPrincipalEigenvector(adjacencyMatrix);
        printEigenvectorMap(network, principalEigenvector);

        System.out.println("\nNormalised Eigenvector Centrality:");
        Map<Integer, Double> normalisedPrincipalEigenvector= eigenvectorCentrality.normalised(principalEigenvector);
        printEigenvectorMap(network, normalisedPrincipalEigenvector);
*/
        MailCluster mailCluster = new MailCluster();
        mailCluster.call(mails);

    }




    private static void printBetweennessMap(Network network, Map<Integer, Double> map) {
        for (int i = 0; i < network.getNodes().size(); i++) {
            System.out.printf("Employee: %s Betweenness: %.2f \n", network.getNodes().get(i).getAddress(), map.get(network.getNodes().get(i).getId()));
        }
    }

    private static void printClosenessMap(Network network, Map<Integer, Double> map) {
        for (int i = 0; i < network.getNodes().size(); i++) {
            System.out.printf("Employee: %s Closeness: %.2f \n", network.getNodes().get(i).getAddress(), map.get(network.getNodes().get(i).getId()));
        }
    }

    private static void printEigenvectorMap(Network network, Map<Integer, Double> map) {
        for (int i = 0; i < network.getNodes().size(); i++) {
            System.out.printf("Employee: %s Eigenvector: %.2f \n", network.getNodes().get(i).getAddress(), map.get(network.getNodes().get(i).getId()));
        }
    }

    private static void printDegreeMap(Network network, Map<Integer, Integer> map) {
        for (int i = 0; i < network.getNodes().size(); i++) {
            System.out.printf("Employee: %s Degree: %d \n", network.getNodes().get(i).getAddress(), map.get(network.getNodes().get(i).getId()));
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
