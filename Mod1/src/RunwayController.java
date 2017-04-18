import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.print.PageLayout;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class RunwayController extends Application {

    public static final String NUMERIC_REGEX = "^[0-9]*$";
    private static final double SCALE_FACTOR = 1.1;

    @FXML
    public Tab topDown, sideOn, calculations;

    private final Controller parent;
    private Runway rw;

    private boolean initialized = false;

    private Point dragOldPos;

    public Button submitButton, clearObstacle;

    // Obstacle input value text fields
    public TextField thresholdDistance, distCentrelineInputField, obstacleHeightInputField;

    // Advanced parameter input fields
    public TextField tora, toda, asda, lda, resa, blast, stripEnd, alstocs;

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
    protected void submissionInit() {
        TextField[] obstacleInputFields = {thresholdDistance, distCentrelineInputField, obstacleHeightInputField};
        TextField[] advancedInputFields = {tora, toda, asda, lda, resa, blast, stripEnd, alstocs};

        if (!initialized) {
            submitButton.fire();
            initialized = true;

            // It's a bit of a roundabout way to do it, but it keeps things separated nicely
            for (int i = 0; i < advancedInputFields.length - 1; i++) {
                addFrontEndNumericInputValidation(advancedInputFields[i], NUMERIC_REGEX);
            }

            for (int i = 0; i < obstacleInputFields.length - 1; i++) {
                addFrontEndNumericInputValidation(obstacleInputFields[i], NUMERIC_REGEX);
            }
        }
    }

    public void addFrontEndNumericInputValidation(TextField t, String regex) {
        t.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue.matches(regex)) {
                        ((StringProperty) observable).setValue(newValue);
                    } else {
                        ((StringProperty) observable).setValue(oldValue);
                    }
                }
        );
    }

    public void start(Stage primaryStage) {
    }

    @FXML
    protected void clearObstacleAction() {
        rw.clearObstacle();
        obstacleTypeComboBox.setValue("");
        thresholdDistance.clear();
        distCentrelineInputField.clear();
        obstacleHeightInputField.clear();
        rw.setCalculations("");
        displayCalculations(rw);
        display();
        parent.additionalInfoBar.setText("Obstacle removed");
    }


    @FXML
    protected void submitButtonAction() {

        try {
            if (initialized) {
                rw.clearObstacle();
                rw.setObstacle(getObstacleTextFields());
                parent.additionalInfoBar.setText("Obstacle added");
            }
            display();
        } catch (Exception e) {
            displayInputError(e);
        }
    }

    @FXML
    protected void advancedSubmitAction() {
        try {
            int[] newValues = getAdvancedTextFields();
            Obstacle temp = rw.getObstacle();
            rw = new Runway(rw.getName(), newValues[0], newValues[1], newValues[2], newValues[3]);
            rw.setRESA(newValues[4]);
            rw.setALSTOCSSlope(newValues[5]);
            rw.setStripEnd(newValues[6]);
            rw.setBlastAllowance(newValues[7]);
            rw.setObstacle(temp);
            display();
        } catch (Exception e) {
            displayInputError(e);
        }
    }

    private void displayInputError(Exception e) {
        parent.additionalInfoBar.setText(e.getMessage());
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("There was a problem with your input");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    private void display() {
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

    private Obstacle getObstacleTextFields() throws Exception {
        String obstacleType = (String) obstacleTypeComboBox.getValue();

        Integer distLowerThreshold;
        Integer distUpperThreshold;
        int distCentreThreshold = Integer.parseInt(distCentrelineInputField.getText());
        int obstacleHeight = Integer.parseInt(obstacleHeightInputField.getText());

//		String intRegex = "-?[0-9]+";
//
//		if (!distLowerThreshInputField.getText().matches(intRegex) || !distUpperThreshInputField.getText().matches(intRegex) || !distCentrelineInputField.getText().matches(intRegex) || !distCentrelineInputField.getText().matches(intRegex)) {
//			throw new IOException("Invalid runway value: look for non-number characters in the Dist. Lower Threshold, Dist. Upper Threshold, Obstacle Height, and Dist. Centreline fields.");
//		}

        // Only parses input box that will be active
        if (rw.getDirection() <= 18) {
            distLowerThreshold = new Integer(thresholdDistance.getText());

            int minDistLowerThreshold = -60 - rw.getOriginalThreshold();
            if (distLowerThreshold < minDistLowerThreshold) {
                throw new IOException("Obstacle's distance from the lower threshold is too low and does not require redeclaration (minimum: " + minDistLowerThreshold + ").");
            }

            int maxDistLowerThreshold = rw.getOriginalTORA() + 60 - rw.getOriginalThreshold();
            if (distLowerThreshold > maxDistLowerThreshold) {
                throw new IOException("Obstacle's distance from the lower threshold is too high and does not require redeclaration (maximum: " + maxDistLowerThreshold + ").");
            }

            distUpperThreshold = null;
        } else {
            distUpperThreshold = new Integer(thresholdDistance.getText());

            int minDistUpperThreshold = -60 - rw.getOriginalThreshold();
            if (distUpperThreshold < minDistUpperThreshold) {
                throw new IOException("Obstacle's distance from the upper threshold is too low and does not require redeclaration (minimum: " + minDistUpperThreshold + ").");
            }

            int maxDistUpperThreshold = rw.getOriginalTORA() + 60 - rw.getOriginalThreshold();
            if (distUpperThreshold > maxDistUpperThreshold) {
                throw new IOException("Obstacle's distance from the upper threshold is too high and does not require redeclaration (maximum: " + maxDistUpperThreshold + ").");
            }
            distLowerThreshold = null;
        }

        int minDistCentreThreshold = -75;
        if (distCentreThreshold < -75) {
            throw new IOException("Obstacle's distance from the centreline is too low and does not require redeclaration (minimum: " + minDistCentreThreshold + ").");
        }

        int maxDistCentreThreshold = 75;
        if (distCentreThreshold > maxDistCentreThreshold) {
            throw new IOException("Obstacle's distance from the centreline is too high and does not require redeclaration (maximum: " + maxDistCentreThreshold + ").");
        }

        int minObstacleHeight = 1;
        if (obstacleHeight < minObstacleHeight) {
            throw new IOException("Obstacle too short (minimum: " + minObstacleHeight + ").");
        }

        int maxObstacleHeight = 80;
        if (obstacleHeight > maxObstacleHeight) {
            throw new IOException("Obstacle too tall (maximum: " + maxObstacleHeight + ").");
        }

        return new Obstacle(obstacleType, distLowerThreshold, distUpperThreshold, distCentreThreshold, obstacleHeight);
    }

    public int[] getAdvancedTextFields() throws Exception {
        TextField[] advancedInputFields = {tora, toda, asda, lda, resa, blast, stripEnd, alstocs};

        int[] initValues = {rw.getOriginalTORA(), rw.getOriginalTODA(), rw.getOriginalASDA(), rw.getOriginalLDA(), rw.getRESA(), rw.getALSTOCSSlope(), rw.getStripEnd(), rw.getBlastAllowance()};
        int[] newValues = new int[initValues.length];

        for (int i = 0; i < initValues.length; i++) {
            if (advancedInputFields[i].getText().equals("")) {
                newValues[i] = initValues[i];
            } else {
                newValues[i] = Integer.parseInt(advancedInputFields[i].getText());
            }
        }

        return newValues;
    }

    public void print() {
		printToPrinter(getTab());
		//saveFile(getTab());
    }

    protected void saveFile(Node pan){
    	BufferedImage bi = new BufferedImage(511, 640, BufferedImage.TYPE_INT_RGB);
        BufferedImage image = javafx.embed.swing.SwingFXUtils.fromFXImage(pan.snapshot(new SnapshotParameters(), null), bi);
        Graphics2D gd = (Graphics2D) image.getGraphics();
    	 FileChooser fc = new FileChooser();
         fc.setTitle("Save Image");
         fc.getExtensionFilters().addAll(new ExtensionFilter("png file",".png"), new ExtensionFilter("jpeg file",".jpg"), new ExtensionFilter("gif file",".gif"));
         File file = fc.showSaveDialog(null);
         if (file != null) {
             try {
                 ImageIO.write(image, "png", file);
             } catch (IOException ex) {
             }
         }
     }
    
	protected Node getTab(){
		if (topDown.isSelected()){
			return topDownPane;
		}
		if (sideOn.isSelected()){
			return sideOnPane;
		}
		if (calculations.isSelected()){
			return calculationsTextFlow;
		}
		return null;
	}

	private void printToPrinter(Node pan) {
		double xTranslate = pan.getTranslateX();
		double yTranslate = pan.getTranslateY();
		double xScale = pan.getScaleX();
		double yScale = pan.getScaleY();
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            if (job.showPrintDialog(null)) {
            	pan.setTranslateX(0);
             	pan.setTranslateY(0);
             	pan.setScaleX(1);
             	pan.setScaleY(1);
            	PageLayout pl = job.getPrinter().getDefaultPageLayout();
                double XShift = pl.getPrintableWidth() / pan.getBoundsInParent().getWidth();
                double YShift = pl.getPrintableHeight() / pan.getBoundsInParent().getHeight();
                double transformValue = Math.min(XShift, YShift);
                Scale scale = new Scale(transformValue, transformValue);
                  pan.getTransforms().add(scale);
                boolean td = job.printPage(pan);
                parent.additionalInfoBar.setText("Current view sucessfully printed");
                if (td) {
                    job.endJob();
                    pan.getTransforms().clear();
                    pan.setTranslateX(xTranslate);
                 	pan.setTranslateY(yTranslate);
                 	pan.setScaleX(xScale);
                 	pan.setScaleY(yScale);
                }
            }
        }
        pan.getTransforms().clear();
    }

    public void toggleLegend() {
        setAlwaysShowLegend(!alwaysShowLegend);
        display();
    }

    @FXML
    protected void topDownScroll(ScrollEvent e) {
        e.consume();
        mouseScroll(e, topDownPane);
    }

    @FXML
    protected void sideOnScroll(ScrollEvent e) {
        e.consume();
        mouseScroll(e, sideOnPane);
    }

    private void mouseScroll(ScrollEvent e, Pane p) {
        double scaling = getScale(e);

        p.setScaleX(p.getScaleX() * scaling);
        p.setScaleY(p.getScaleY() * scaling);

        reCentre(p);
    }

    @FXML
    protected void displayMousePressed(MouseEvent e) {
        e.consume();
        dragOldPos = new Point((int) e.getX(), (int) e.getY());
    }

    @FXML
    protected void topDownMouseDrag(MouseEvent e) {
        e.consume();
        mouseDrag(e, topDownPane);
    }

    @FXML
    protected void sideOnMouseDrag(MouseEvent e) {
        e.consume();
        mouseDrag(e, sideOnPane);
    }

    private void mouseDrag(MouseEvent e, Pane p) {
        p.setTranslateX(sideOnPane.getTranslateX() + e.getX() - dragOldPos.getX());
        p.setTranslateY(sideOnPane.getTranslateY() + e.getY() - dragOldPos.getY());

        reCentre(p);
    }

    private void reCentre(Pane p) {
        double realWidth = p.getWidth() * p.getScaleX();
        if (p.getTranslateX() + realWidth < 10) {
            p.setTranslateX(10);
        } else if (p.getTranslateX() - p.getWidth() > 10) {
            p.setTranslateX(p.getWidth() - 10);
        }

        double realHeight = p.getHeight() * p.getScaleY();
        if (p.getTranslateY() + realHeight < 10) {
            p.setTranslateY(-realHeight + 10);
        } else if (p.getTranslateY() - p.getHeight() > 10) {
            p.setTranslateY(p.getHeight() - 10);
        }
    }

    private double getScale(ScrollEvent e) {
        if (e.getDeltaY() == 0) {
            return 1;
        } else if (e.getDeltaY() > 0) {
            return SCALE_FACTOR;
        } else {
            return 1 / SCALE_FACTOR;
        }

    }


    @FXML
    protected void enter(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            submitButtonAction();

        }
    }

}