/**
 * Created by tobyf on 12/03/2017.
 */

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RunwayController extends Application {

    Runway rw;

    private boolean initialized = false;

    public Button submitButton,clearObstacle;

    public TextField distUpperThreshInputField, distLowerThreshInputField, distCentrelineInputField, obstacleHeightInputField,tora,toda,asda,lda,resa,blast,stripEnd,alstocs;

    // Combo box of obstacle types
    public ComboBox obstacleTypeComboBox;

    // Output value text fields
    public TextField oldToraField, newToraField, oldTodaField, newTodaField, oldAsdaField, newAsdaField, oldLdaField, newLdaField;

    @FXML
    public AnchorPane topDownPane, sideOnPane;

    @FXML
    public VBox leftPanel;

    public TextArea calculationsTextArea;


    public RunwayController(Runway rw) {
        this.rw = rw;
    }

    @FXML
    public void initialize() {
        topDownPane.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                if (initialized==false)
                    submitButton.fire();
                initialized = true;
            }
        });

        leftPanel.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                if (initialized==false)
                    submitButton.fire();
                initialized = true;
            }
        });
    }

    public void start(Stage primaryStage){
    }

    @FXML
    protected void editAction(){

    }

    @FXML
    protected void clearObstacleAction(){
        rw.clearObstacle();
        obstacleTypeComboBox.setValue(new String());
        distUpperThreshInputField.clear();
        distLowerThreshInputField.clear();
        distCentrelineInputField.clear();
        obstacleHeightInputField.clear();
        submitButtonAction();
    }


    @FXML
    protected void submitButtonAction() {

        rw.setObstacle(getObstacleTextFields());

        // Displays runway
        displayValues();
        displayCalculations(rw);
        Display screen = new Display(topDownPane, sideOnPane);
        ////////////screen.setAlwaysShowLegend(viewAlwaysShowLegend.isSelected());
        screen.clearPanes();
        screen.drawRunway(rw);
    }


    //Updates textboxes with runway values

    private void displayValues() {
        oldToraField.setText(Integer.toString(rw.getOriginalTORA()));
        oldTodaField.setText((Integer.toString(rw.getOriginalTODA())));
        oldAsdaField.setText(Integer.toString(rw.getOriginalASDA()));
        oldLdaField.setText(Integer.toString(rw.getOriginalLDA()));
        newToraField.setText(Integer.toString(rw.getTORA()));
        newTodaField.setText((Integer.toString(rw.getTODA())));
        newAsdaField.setText(Integer.toString(rw.getASDA()));
        newLdaField.setText(Integer.toString(rw.getLDA()));
    }


    //Updates calculations text box

    private void displayCalculations(Runway r) {
        calculationsTextArea.setText(r.getCalculations());
    }


    //Displays chosen runway in runway panes

    private Runway getRunway() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("runway1.txt"));
        String input = br.readLine();
        String[] values = input.split(",");
        return new Runway(values[0], Integer.parseInt(values[1]), Integer.parseInt(values[2]), Integer.parseInt(values[3]), Integer.parseInt(values[4]));
    }

    private Obstacle getObstacle() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("obstacle1.txt"));
        String input = br.readLine();
        String[] values = input.split(",");
        return new Obstacle(values[0], Integer.parseInt(values[1]), Integer.parseInt(values[2]), Integer.parseInt(values[3]), Integer.parseInt(values[4]));
    }

    private boolean obstacleInputEmpty() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("obstacle1.txt"));
        return br.readLine() == null;
    }


    //Gets obstacle from text fields

    private Obstacle getObstacleTextFields() {
        try {
            String obstacleType = (String) obstacleTypeComboBox.getValue();
            int distLowerThreshold = Integer.parseInt(distLowerThreshInputField.getText());
            int distUpperThreshold = Integer.parseInt(distUpperThreshInputField.getText());
            int distCentreThreshold = Integer.parseInt(distCentrelineInputField.getText());
            int obstacleHeight = Integer.parseInt(obstacleHeightInputField.getText());
            return new Obstacle(obstacleType, distLowerThreshold, distUpperThreshold, distCentreThreshold, obstacleHeight);
        }catch (Exception e){
            return null;
        }
    }
}
