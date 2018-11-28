import Entities.Mail;
import POJO.Employee;
import POJO.Message;
import POJO.RecipientInfo;
import network.CreateNetwork;
import network.Edge;
import network.Network;
import network.centrality.*;
import org.hibernate.Query;
import org.hibernate.Session;

import org.jblas.Eigen;
import persistent.HibernateUtil;
import weka.classifiers.meta.FilteredClassifier;
import weka.clusterers.*;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.experiment.InstanceQuery;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static void main( String[] args )
    {

        JSONReader jsonReader = new JSONReader();
        Network network = jsonReader.readNodes(Paths.get("src/main/resources/java_persons.json").toAbsolutePath().toString());
        jsonReader.readAdjacencyMatrix(Paths.get("src/main/resources/java_network.json").toAbsolutePath().toString(),network);
        /*
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
            for (RecipientInfo receiver:receivers) {
                to.add(receiver.getRvalue());
            }
            mail.setTo(to);
            mails.add(mail);
        }

        session.getTransaction().commit();
        session.getSessionFactory().close();

        /*KNearestNeighbor kNearestNeighbor = new KNearestNeighbor(list.size());
        long[][] distanceMatrix = kNearestNeighbor.call(list);
        printMatrix(distanceMatrix);

        Query query2 = session.createQuery("from Employee");
        List<Employee> employeeList = query2.list();



        System.out.println("Network");
        CreateNetwork createNetwork = new CreateNetwork(mails,employeeList);
        Network network = createNetwork.call();
        System.out.println(network.getNodes().size());
        System.out.println(network.getEdges().size());
        */
        for (Edge edge:network.getEdges()) {
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

/*
        List<List<String>> docs = new ArrayList<>();
        for (int i = 0; i < mails.size(); i++) {
            StringTokenizer tokenizer = new StringTokenizer(mails.get(i).getBody(), ".,?! ");
            List<String> strings = new ArrayList<>();
            while (tokenizer.hasMoreElements()) {
                strings.add(tokenizer.nextToken());
            }
            docs.add(strings);
        }
        /*for (List <String> list: docs) {
            for (String string: list) {
                System.out.println(string + " ");
            }
            System.out.println();
        }*/
        /*TFIDFCalculator tfidfCalculator = new TFIDFCalculator();
        Map<String,Double> map = new HashMap<>();
        for (int i = 0; i < docs.size(); i++) {
            for(int j = 0; j < docs.get(i).size(); j++) {
                map.put(docs.get(i).get(j),tfidfCalculator.tfIdf(docs.get(i), docs, docs.get(i).get(j)));
            }
        }

        for (String string:map.keySet()) {
            System.out.println(string + ": " +map.get(string));
        }
        /*
        for(int i = 0; i < mails.size(); i++)
        {
            TFIDFCalculator tfidfCalculator = new TFIDFCalculator();
            tfidfCalculator.tfIdf(Arrays.asList(mails.get(i).getBody().split(" ")),)
        ArrayList<Attribute> atts = new ArrayList<Attribute>();
        atts.add(new Attribute("from"));
        atts.add(new Attribute("to"));
        atts.add(new Attribute("body"));
        Instances data = new Instances("mails", atts, 0);

        for(int i = 0; i < mails.size(); i++)
        {
            /*Instance instance = new DenseInstance(3);
            instance.setDataset(data);
            System.out.println(mails.get(i).getSender());
            Attribute from = new Attribute("from");
            Attribute to = new Attribute("to");
            Attribute body = new Attribute("body");

            instance.setValue(0,from.indexOfValue(mails.get(i).getSender()));
            instance.setValue(1,to.indexOfValue(mails.get(i).getTo().get(0)));
            instance.setValue(2,body.indexOfValue(mails.get(i).getBody()));

            data.add(instance);
            double[] vals = new double[data.numAttributes()];
            vals[0] = data.attribute(0).addStringValue(mails.get(i).getSender());
            System.out.println(vals[0]);
            vals[1] = data.attribute(1).addStringValue(mails.get(i).getTo().get(0));
            vals[2] = data.attribute(2).addStringValue(mails.get(i).getBody());
            data.add(new DenseInstance(1.0, vals));
        }
        System.out.println(data.size());
        System.out.println(data);

        try {
            /*ClusterEvaluation eval = new ClusterEvaluation();
                                        // build clusterer
            eval.setClusterer(clusterer);                                   // the cluster to evaluate
            //eval.evaluateClusterer(newData);                                // data to evaluate the clusterer on
            System.out.println("# of clusters: " + eval.getNumClusters());**/
            /*String[] options = new String[2];
            options[0] = "-I";                 // max. iterations
            options[1] = "100";
            EM clusterer = new EM();   // new instance of clusterer
            clusterer.setOptions(options);     // set the options
            clusterer.buildClusterer(data);*/
            /*Clusterer clusterer = new SimpleKMeans();
            ((SimpleKMeans) clusterer).setPreserveInstancesOrder(true);
            ((SimpleKMeans) clusterer).setNumClusters(3);
            clusterer.buildClusterer(data);
            int[] assignments  = ((SimpleKMeans) clusterer).getAssignments();
            int i = 0;
            for(int clusterNum : assignments) {
                System.out.printf("Instance %d -> Cluster %d\n", i, clusterNum);
                i++;
            }
            Clusterer clusterer = new SimpleKMeans();
            ((SimpleKMeans) clusterer).setPreserveInstancesOrder(true);
            ((SimpleKMeans) clusterer).setNumClusters(3);
            StringToWordVector filter = new StringToWordVector();
            filter.setIDFTransform(true);
            FilteredClusterer filteredClusterer = new FilteredClusterer();
            filteredClusterer.setFilter(filter);
            filteredClusterer.setClusterer(clusterer);
            filteredClusterer.buildClusterer(data);
            System.out.println(filteredClusterer);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
*/
    }


    private static void printBetweennessMap(Network network, Map<Integer, Double> map) {
        for (int i = 0; i < network.getNodes().size() ; i++) {
            System.out.printf("Employee: %s Betweenness: %.2f \n",network.getNodes().get(i).getAddress(), map.get(network.getNodes().get(i).getId()));
        }
    }

    private static void printClosenessMap(Network network, Map<Integer, Double> map) {
        for (int i = 0; i < network.getNodes().size() ; i++) {
                System.out.printf("Employee: %s Closeness: %.2f \n",network.getNodes().get(i).getAddress(), map.get(network.getNodes().get(i).getId()));
        }
    }

    private static void printEigenvectorMap(Network network, Map<Integer, Double> map) {
        for (int i = 0; i < network.getNodes().size() ; i++) {
            System.out.printf("Employee: %s Eigenvector: %.2f \n",network.getNodes().get(i).getAddress(), map.get(network.getNodes().get(i).getId()));
        }
    }

    private static void printDegreeMap(Network network, Map<Integer, Integer> map) {
        for (int i = 0; i < network.getNodes().size() ; i++) {
            System.out.printf("Employee: %s Degree: %d \n",network.getNodes().get(i).getAddress(), map.get(network.getNodes().get(i).getId()));
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
