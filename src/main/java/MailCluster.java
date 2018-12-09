import Entities.Mail;
import weka.clusterers.SimpleKMeans;
import weka.core.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class MailCluster {

    public MailCluster() {
    }

    public void call(List<Mail> mails) {
        Stopwords stopwords = new Stopwords();
        List<List<String>> docs = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            StringTokenizer tokenizer = new StringTokenizer(mails.get(i).getBody(), "=*^.,?! ");
            List<String> strings = new ArrayList<>();
            while (tokenizer.hasMoreElements()) {
                String string = tokenizer.nextToken();
                string.replaceAll("/[^a-zA-Z0-9]+/", "");
                string.trim();
                if (!stopwords.is(string)) {
                    strings.add(string);
                }
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
/*
        for (Instance in : data) {
            System.out.println(in.attribute(0).name());
        }
*/
        try {
            SimpleKMeans clusterer = new SimpleKMeans();   // new instance of clusterer
            clusterer.setNumClusters(3);
            clusterer.setSeed(10);
            clusterer.setMaxIterations(100);
            clusterer.setPreserveInstancesOrder(true);
            clusterer.buildClusterer(data);
            //System.out.println(clusterer);

            int[] assignments = clusterer.getAssignments();

            Map<String, Double> cluster0 = new HashMap<>();
            Map<String, Double> cluster1 = new HashMap<>();
            Map<String, Double> cluster2 = new HashMap<>();


            int instanceNr = 0;
            for (int clusterNum : assignments) {
                String word = atts.get(instanceNr).name();
                //System.out.printf("Instance %d -> Cluster %d | Word: %s \n", instanceNr, clusterNum, word);
                if (clusterNum == 1) {
                    cluster1.put(word, map.get(word));
                } else if (clusterNum == 2) {
                    cluster2.put(word, map.get(word));
                } else {
                    cluster0.put(word, map.get(word));
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

}
