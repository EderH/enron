import java.util.Comparator;
import java.util.Map;

class EntryComparator implements Comparator<Map.Entry<String,Double>> {

    /**
     * Implements descending order.
     */
    @Override
    public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
        if (o1.getValue() < o2.getValue()) {
            return 1;
        } else if (o1.getValue() > o2.getValue()) {
            return -1;
        }
        return 0;
    }

}