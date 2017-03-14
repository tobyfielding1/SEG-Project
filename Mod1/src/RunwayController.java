import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RunwayController extends Application {

    private final Controller parent;
    private Runway rw;

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
    public TextFlow calculationsTextFlow;

    private boolean alwaysShowLegend;

    public RunwayController(Runway rw, Controller parent) {
        this.rw = rw;
        this.parent = parent;
    }

    @FXML
    public void initialize() {
        topDownPane.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                if (!initialized)
                    submitButton.fire();
                initialized = true;
            }
        });

        leftPanel.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                if (!initialized)
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
        display();
    }


    @FXML
    protected void submitButtonAction() {

        try {
            if (initialized) {
                rw.setObstacle(getObstacleTextFields());
            }
            display();
        }catch (Exception e) {
            parent.additionalInfoBar.setText(e.getMessage());
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("There was a problem with your input");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    protected void display(){
        displayValues();
        displayCalculations(rw);
        Display screen = new Display(topDownPane, sideOnPane);
        screen.clearPanes();
        screen.drawRunway(rw);
        displayLegend();
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


    private void displayLegend() {
        int currentY = 60;

        if (rw.getClearway() > 0 || alwaysShowLegend) {
            addLegendItem(Display.CLEARWAY_COLOR, "Clearway", currentY, true);
            currentY += 20;
        }

        if (rw.getStopway() > 0 || alwaysShowLegend) {
            addLegendItem(Display.STOPWAY_COLOR, "Stopway", currentY, true);
            currentY += 20;
        }

        if (rw.getObstacle() != null || alwaysShowLegend) {
            addLegendItem(Display.OBSTACLE_COLOR, "Obstacle", currentY, true);
            currentY += 20;
        }

        if (rw.getObstacle() != null || alwaysShowLegend) {
            addLegendItem(Display.SLOPE_COLOR, "Slope", currentY, false);
            currentY += 20;
        }
    }

    private void addLegendItem(Paint color, String name, int y, boolean bothPanels) {
        Rectangle r1 = new Rectangle(50, y, 10, 10);
        r1.setFill(color);
        r1.setStroke(Color.BLACK);

        Rectangle r2 = new Rectangle(50, y, 10, 10);
        r2.setFill(color);
        r2.setStroke(Color.BLACK);


        Text t1 = new Text(70, y + 10, name);
        Text t2 = new Text(70, y + 10, name);

        if (bothPanels) topDownPane.getChildren().addAll(r1, t1);
        sideOnPane.getChildren().addAll(r2, t2);
    }

    public void setAlwaysShowLegend(boolean alwaysShowLegend) {
        this.alwaysShowLegend = alwaysShowLegend;
    }


    //Gets obstacle from text fields

    private Obstacle getObstacleTextFields() throws Exception{
        String obstacleType = (String) obstacleTypeComboBox.getValue();
        int distLowerThreshold = Integer.parseInt(distLowerThreshInputField.getText());
        int distUpperThreshold = Integer.parseInt(distUpperThreshInputField.getText());
        int distCentreThreshold = Integer.parseInt(distCentrelineInputField.getText());
        int obstacleHeight = Integer.parseInt(obstacleHeightInputField.getText());

        if (distLowerThreshInputField.getText().matches("[0-9]+") && distUpperThreshInputField.getText().matches("[0-9]+") && distCentrelineInputField.getText().matches("[0-9]+") && distCentrelineInputField.getText().matches("[0-9]+")) {
        } else {
            throw new IOException("Invalid runway value: look for non-number characters in the Dist. Lower Threshold, Dist. Upper Threshold, Obstacle Height, and Dist. Centreline fields.");
        }

        if (distLowerThreshold < -rw.getOriginalTORA()) {
            throw new IOException("Obstacle's distance from the lower threshold is too low (minimum: " + -rw.getOriginalTORA() + ").");
        }

        if (distLowerThreshold > 2 * rw.getOriginalTORA()) {
            throw new IOException("Obstacle's distance from the lower threshold is too high (maximum: " + 2 * rw.getOriginalTORA() + ").");
        }

        if (distUpperThreshold < -2 * rw.getOriginalTORA()) {
            throw new IOException("Obstacle's distance from the upper threshold is too low (minimum: " + -2 * rw.getOriginalTORA() + ").");
        }

        if (distUpperThreshold > rw.getOriginalTORA()) {
            throw new IOException("Obstacle's distance from the upper threshold is too high (maximum: " + rw.getOriginalTORA() + ").");
        }

        if (distCentreThreshold < -150) {
            throw new IOException("Obstacle's distance from the centreline is too low (minimum: " + -150 + ").");
        }

        if (distCentreThreshold > 150) {
            throw new IOException("Obstacle's distance from the centreline is too high (maximum: " + 150 + ").");
        }

        if (obstacleHeight > 80) {
            throw new IOException("Obstacle too high (maximum: " + 80 + ").");
        }

        return new Obstacle(obstacleType, distLowerThreshold, distUpperThreshold, distCentreThreshold, obstacleHeight);
    }

    public void print() {
        System.out.print("hello");
    }
    
    @FXML
    protected void enter(KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER) {
				submitButtonAction();
		}
	}
}
