package Backend;

import NeuralNetwork.Components.DataSet;
import NeuralNetwork.Components.DataSets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * OneHotCoder -
 * Provides all Training sets
 * and is able to encode/ decode
 * the output.
 *
 * @author Luca Jung
 * @version 1.3
 */
public class OneHotCoder {

    private ArrayList<String>           correspondingLabels;
    private String[]                    correspondingOutput;
    private HashMap<String, int[][]>    pairs;

    public OneHotCoder(){
        correspondingLabels = new ArrayList<>();
        pairs = new HashMap<>();
    }

    public void addNewPair(String label, int[][] canvas){
        if(!correspondingLabels.contains(label)) {
            correspondingLabels.add(label);
        }
        pairs.put(label, canvas);
    }

    public DataSets getDataSets(){
        correspondingOutput = new String[correspondingLabels.size()];
        DataSets dataSets = new DataSets();
        for (Map.Entry<String, int[][]> pair: pairs.entrySet()) {
            int index = getIndexOfLabel(pair.getKey());
            dataSets.addDataSet(new DataSet(get1DInputVector(pair.getValue()), getOneHotEncodedArray(index)));
            correspondingOutput[index] = pair.getKey();
        }
        return dataSets;
    }

    private int getIndexOfLabel(String label){
        return correspondingLabels.indexOf(label);
    }

    private String getLabelOfIndex(int index){
        return correspondingOutput[index];
    }

    public double[] normalizeVector(double[] vector){
        int closedToOne = 0;
        for (int i = 0; i < vector.length; i++) {
            if (vector[i] > vector[closedToOne]){
                closedToOne = i;
            }
        }
        for (int i = 0; i < vector.length; i++) {
            if(i == closedToOne){
                vector[i] = 1.0;
            }
            else {
                vector[i] = 0.0;
            }
        }
        return vector;
    }

    public String getLabelOfOutputVector(double[] outputVector){
        for (int i = 0; i < outputVector.length; i++) {
            if((int)outputVector[i] == 1){
                return getLabelOfIndex(i);
            }
        }
        throw new IllegalArgumentException();
    }

    private double[] getOneHotEncodedArray(int hotIndex){
        double[] arr = new double[correspondingLabels.size()];
        arr[hotIndex] = 1.0;
        return arr;
    }

    public double[] get1DInputVector(int[][] canvas){
        double[] arr = new double[canvas.length * canvas.length];
        int h = 0;
        for (int i = 0; i < canvas.length; i++) {
            for (int j = 0; j < canvas[i].length; j++) {
                arr[h] = canvas[i][j] == 1 ? 1.0 : 0.0;
                h++;
            }
        }
        return arr;
    }

    public int getSizeOfOutputVector(){
        return correspondingLabels.size();
    }

}
