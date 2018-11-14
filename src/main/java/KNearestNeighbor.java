import POJO.Message;

import java.util.List;

public class KNearestNeighbor {

    private int n;
    private long[][] distanceMatrix;
    private double[][] confusionMatrix;

    public KNearestNeighbor(int n) {
        this.n = n;
        distanceMatrix = new long[n][n];
    }

    public long[][] call(List data) {
        for (int x = 0; x < data.size(); x++) {
            for (int y = x; y < data.size(); y++) {
                Message message1 = (Message)data.get(x);
                Message message2 = (Message)data.get(y);
                distanceMatrix[x][y] = getDistance(message1.getDate().getTime(), message2.getDate().getTime());
            }
        }
        return distanceMatrix;
    }

    private long getDistance(long x, long y) {
        return Math.abs(x - y);
    }

    private int determineK(List data) {

        return (int)(Math.sqrt(data.size()) / 2);
    }
}
