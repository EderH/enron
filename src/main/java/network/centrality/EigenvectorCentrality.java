package network.centrality;

import org.jblas.ComplexDouble;
import org.jblas.ComplexDoubleMatrix;
import org.jblas.DoubleMatrix;
import org.jblas.Eigen;

import java.util.*;

//cited from 'http://www.markhneedham.com/blog/2013/08/05/javajblas-calculating-eigenvector-centrality-of-an-adjacency-matrix/'

public class EigenvectorCentrality {

    public EigenvectorCentrality() {
    }

    public Map<Integer, Double> getPrincipalEigenvector(double[][] adjacencyMarix) {
        DoubleMatrix matrix = new DoubleMatrix(adjacencyMarix);
        int maxIndex = getMaxIndex(matrix);
        ComplexDoubleMatrix eigenVectors = Eigen.eigenvectors(matrix)[0];
        return getEigenVector(eigenVectors, maxIndex);
    }

    private static int getMaxIndex(DoubleMatrix matrix) {
        ComplexDouble[] doubleMatrix = Eigen.eigenvalues(matrix).toArray();
        int maxIndex = 0;
        for (int i = 0; i < doubleMatrix.length; i++) {
            double newnumber = doubleMatrix[i].abs();
            if ((newnumber > doubleMatrix[maxIndex].abs())) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private static Map<Integer, Double> getEigenVector(ComplexDoubleMatrix eigenvector, int columnId) {
        ComplexDoubleMatrix column = eigenvector.getColumn(columnId);
        int count = 0;
        Map<Integer, Double> values = new HashMap<Integer, Double>();
        for (ComplexDouble value : column.toArray()) {
            values.put(count, value.abs());
            count++;
        }
        return values;
    }

    public Map<Integer, Double> normalised(Map<Integer, Double> principalEigenvector) {
        double total = sum(principalEigenvector);
        Map<Integer, Double> normalisedValues = new HashMap<Integer, Double>();
        Iterator it = principalEigenvector.keySet().iterator();
        while(it.hasNext()) {
            Integer next = (Integer)it.next();
            normalisedValues.put(next, principalEigenvector.get(next) / total);
        }
        return normalisedValues;
    }

    private static double sum(Map<Integer, Double> principalEigenvector) {
        double total = 0;
        for (Double aDouble : principalEigenvector.values()) {
            total += aDouble;
        }
        return total;
    }
}
