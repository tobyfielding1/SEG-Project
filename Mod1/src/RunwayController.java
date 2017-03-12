/**
 * Created by tobyf on 12/03/2017.
 */

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RunwayController extends Application {

    Runway rw;

    public TextField distUpperThreshInputField, distLowerThreshInputField, distCentrelineInputField, obstacleHeightInputField, resaInputField;

    // Combo box of obstacle types
    public ComboBox obstacleTypeComboBox;

    // Output value text fields
    public TextField oldToraField, newToraField, oldTodaField, newTodaField, oldAsdaField, newAsdaField, oldLdaField, newLdaField;

    // Graphical and calculations display panes
    public AnchorPane topDownPane, sideOnPane;
    public TextArea calculationsTextArea;


    public RunwayController(Runway rw) {
        this.rw = rw;
    }

    @Override
    public void start(Stage primaryStage) {

    }

    @FXML
    protected void drawButtonAction() throws IOException {

        // Create an airport
        Airport airport = new Airport("Airport");

        // Imports runway and obstacle values from text file
        Runway rw = getRunway();
        airport.addRunway(rw);
        if (!obstacleInputEmpty()) {
            rw.setObstacle(getObstacle());
        }

        // Displays runway
        displayValues();
        displayCalculations(rw);
        Display screen = new Display(topDownPane, sideOnPane);
        //////////screen.setAlwaysShowLegend(viewAlwaysShowLegend.isSelected());
        screen.clearPanes();
        screen.drawRunway(rw);
        ////////////additionalInfoBar.setText("Input successful");
        ////////////saveCalculations(calculationsTextArea, "calc", ".png");
    }

    @FXML
    protected void submitButtonAction() {

        // Create an airport
        Airport airport = new Airport("Airport");

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
