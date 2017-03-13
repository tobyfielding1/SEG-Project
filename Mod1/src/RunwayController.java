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

    public RunwayController(Runway rw) {
        this.rw = rw;
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

    public void print() {
        System.out.print("hello");
    }
}
