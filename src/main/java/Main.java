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
        /*
        JSONReader jsonReader = new JSONReader();
        Network network = jsonReader.readNodes(Paths.get("src/main/resources/java_persons.json").toAbsolutePath().toString());
        jsonReader.readAdjacencyMatrix(Paths.get("src/main/resources/java_network.json").toAbsolutePath().toString(),network);


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

        Session session = HibernateUtil.getSessionFactory().openSession();

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
        List<List<String>> docs = new ArrayList<>();
        for (int i = 0; i < mails.size(); i++) {
            StringTokenizer tokenizer = new StringTokenizer(mails.get(i).getBody(), "=*^.,?! ");
            List<String> strings = new ArrayList<>();
            while (tokenizer.hasMoreElements()) {
                String string = tokenizer.nextToken();
                string.replaceAll("/[^a-zA-Z0-9]+/", "");
                string.trim();
                strings.add(string);
            }
            docs.add(strings);
        }
        /*for (List <String> list: docs) {
            for (String string: list) {
                System.out.println(string + " ");
            }
            System.out.println();
        }*/

        TFIDFCalculator tfidfCalculator = new TFIDFCalculator();
        Map<String, Double> map = new HashMap<>();
        for (int i = 0; i < docs.size(); i++) {
            for (int j = 0; j < docs.get(i).size(); j++) {
                double tfdif = tfidfCalculator.tfIdf(docs.get(i), docs, docs.get(i).get(j));
                if (map.get(docs.get(i).get(j)) != null) {
                    if (map.get(docs.get(i).get(j)) < tfdif) {
                        map.put(docs.get(i).get(j), tfdif);
                    }
                } else {
                    map.put(docs.get(i).get(j), tfdif);
                }
            }
        }

        /*for (String string:map.keySet()) {
            System.out.println(string + ": " +map.get(string));
        }*/

        //List list = getTopKeysWithOccurences(map,10);

        /*for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }*/


        List<String> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);
        ArrayList<Attribute> atts = new ArrayList<Attribute>();
        for (int i = 0; i < keys.size(); i++) {
            atts.add(new Attribute(keys.get(i)));
        }
        Instances data = new Instances("words", atts, 0);
        for (int i = 0; i < map.size(); i++) {
            Instance instance = new DenseInstance(1);
            instance.setDataset(data);
            instance.setValue(0, map.get(atts.get(i).name()));
            data.add(instance);
        }

        for (Instance in : data) {
            System.out.println(in.attribute(0).name());
        }

        try {
            SimpleKMeans clusterer = new SimpleKMeans();   // new instance of clusterer
            clusterer.setNumClusters(3);
            clusterer.setSeed(10);

            clusterer.setMaxIterations(100);
            clusterer.setPreserveInstancesOrder(true);
            clusterer.buildClusterer(data);
            System.out.println(clusterer);
            int[] assignments = clusterer.getAssignments();

            Map<String, Double> cluster0 = new HashMap<>();
            Map<String, Double> cluster1 = new HashMap<>();
            Map<String, Double> cluster2 = new HashMap<>();

            Stopwords stopwords = new Stopwords();
            int instanceNr = 0;
            for (int clusterNum : assignments) {
                String word = atts.get(instanceNr).name();
                System.out.printf("Instance %d -> Cluster %d | Word: %s \n", instanceNr, clusterNum, word);
                if (clusterNum == 1) {
                    if (!stopwords.is(word)) {
                        cluster1.put(word, map.get(word));
                    }
                } else if (clusterNum == 2) {
                    if (!stopwords.is(word)) {
                        cluster2.put(word, map.get(word));
                    }
                } else {
                    if (!stopwords.is(word)) {
                        cluster0.put(word, map.get(word));
                    }
                }
                instanceNr++;
            }
            /*for (String word: cluster1) {
                System.out.println(word);
            }*/

            int k = 25;
            List cluster0TopK = getTopKeysWithOccurences(cluster0, cluster0.size() < k ? cluster0.size() : k);
            List cluster1TopK = getTopKeysWithOccurences(cluster1, cluster1.size() < k ? cluster1.size() : k);
            List cluster2TopK = getTopKeysWithOccurences(cluster2, cluster2.size() < k ? cluster2.size() : k);


            try {

                PrintWriter writer = new PrintWriter("outputCluster0.txt", "UTF-8");
                for (int i = 0; i < cluster0TopK.size(); i++) {
                    writer.println(cluster0TopK.get(i));
                }
                writer.close();
                writer = new PrintWriter("outputCluster1.txt", "UTF-8");
                for (int i = 0; i < cluster1TopK.size(); i++) {
                        writer.println(cluster1TopK.get(i));
                }
                writer.close();
                writer = new PrintWriter("outputCluster2.txt", "UTF-8");
            for (int i = 0; i < cluster2TopK.size(); i++) {
                        writer.println(cluster2TopK.get(i));
                }
                writer.close();
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                System.err.println(e.getMessage());
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }


    }

    public static List<Map.Entry<String, Double>> getTopKeysWithOccurences(Map<String, Double> hashmap, int top) {
        List<Map.Entry<String, Double>> results = new ArrayList<>(hashmap.entrySet());
        Collections.sort(results, new EntryComparator());
        return results.subList(0, top);
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
