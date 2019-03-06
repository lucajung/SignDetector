package GUI;

import Backend.OneHotCoder;
import NeuralNetwork.Components.DataSets;
import NeuralNetwork.NeuralNetwork;
import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * SignDetectorController -
 * GUI controller
 *
 * @author Luca Jung
 * @version 2.2
 */
public class SignDetectorController {

    private int[][]                         canvasGrid;
    private int                             canvasResolution;
    private int                             canvasDimension;
    private int                             canvasBorder;
    private OneHotCoder oneHotCoder;
    private NeuralNetwork                   neuralNetwork;

    @FXML
    public javafx.scene.canvas.Canvas       GUICanvas;
    @FXML
    public javafx.scene.control.Button      GUIExitButton;
    @FXML
    public javafx.scene.control.Label       GUISignDetectedLabel;
    @FXML
    public javafx.scene.control.TextField   GUICorrespondingLabelTextField;

    @FXML
    public void initialize() {
        canvasDimension     = (int)GUICanvas.getWidth();
        canvasResolution    = 20;
        canvasBorder        = 2;
        oneHotCoder         = new OneHotCoder();
        clearCanvas();
    }

    //###### Start - Neural Network Methods ######

    //Setting up new Neural Network and train
    private void trainNeuralNetwork(DataSets dataSets){
        neuralNetwork = new NeuralNetwork(new int[]{ (int)Math.pow(canvasResolution, 2), oneHotCoder.getSizeOfOutputVector()});
        neuralNetwork.startTraining(dataSets, 10000,0.015, oneHotCoder.getSizeOfOutputVector() / 2);
        GUISignDetectedLabel.setText("Learning process finished!");
    }
    //###### End - Neural Network Methods ######

    //###### Start - GUI Methods ######

    //Updating canvas matrix and redraw canvas
    public void canvasOnMouseDraggedOrClicked(MouseEvent event){
        if(checkIfEventIsInBounds(event.getX(), event.getY())) {
            int x = (int) ((event.getX() - canvasBorder) / (getCanvasUpperBound() / canvasResolution));
            int y = (int) ((event.getY() - canvasBorder) / (getCanvasUpperBound() / canvasResolution));
            canvasGrid[x][y] = 1;
            markActiveFields();
        }
    }

    public void applyForTrainPressed(){
        String correspondingString = GUICorrespondingLabelTextField.getCharacters().toString();
        oneHotCoder.addNewPair(correspondingString, canvasGrid);
        clearCanvas();
    }

    public void trainButtonPressed(){
        DataSets dataSets = oneHotCoder.getDataSets();
        trainNeuralNetwork(dataSets);
    }

    public void predictButtonPressed(){
        double[] result = neuralNetwork.predict(oneHotCoder.get1DInputVector(canvasGrid));
        String str = oneHotCoder.getLabelOfOutputVector(oneHotCoder.normalizeVector(result));
        GUISignDetectedLabel.setText("Detected: " + str);
        clearCanvas();
    }

    public void clearButtonPressed(){
        clearCanvas();
        GUISignDetectedLabel.setText("Detected: -");
    }

    public void exitButtonPressed(){
        Stage stage = (Stage) GUIExitButton.getScene().getWindow();
        stage.close();
    }
    //###### End - GUI Methods ######

    //###### Start - GUI Helper Methods ######
    private void markField(int x, int y){
        GraphicsContext gc = getGraphicsContext();
        gc.setFill(Color.BLACK);
        gc.fillRect((x * (int)(getCanvasUpperBound() / canvasResolution)) + canvasBorder, (y * (int)(getCanvasUpperBound() / canvasResolution)) + canvasBorder, (int)(getCanvasUpperBound() / canvasResolution), (int)(getCanvasUpperBound() / canvasResolution));
    }

    public void markActiveFields(){
        for(int x = 0; x < canvasGrid.length; x++) {
            for (int y = 0; y < canvasGrid[x].length; y++) {
                if(canvasGrid[x][y] == 1) {
                    markField(x, y);
                }
            }
        }
    }

    private boolean checkIfEventIsInBounds(double x, double y){
        if(x > getCanvasLowerBound() && y > getCanvasLowerBound() && x < getCanvasUpperBound() && y < getCanvasUpperBound()){
            return true;
        }
        return false;
    }

    public void clearCanvas(){
        canvasGrid = new int[canvasResolution][canvasResolution];
        GraphicsContext gc = getGraphicsContext();
        gc.clearRect(0, 0, canvasDimension, canvasDimension);
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(canvasBorder * 2);
        gc.moveTo(0, 0);
        gc.lineTo(canvasDimension,0);
        gc.lineTo(canvasDimension, canvasDimension);
        gc.lineTo(0, canvasDimension);
        gc.lineTo(0, 0);
        gc.stroke();
    }

    private GraphicsContext getGraphicsContext(){
        return GUICanvas.getGraphicsContext2D();
    }

    private int getCanvasUpperBound(){
        return canvasDimension - canvasBorder;
    }

    private int getCanvasLowerBound(){
        return canvasBorder;
    }
    //###### End - GUI Helper Methods ######
}
