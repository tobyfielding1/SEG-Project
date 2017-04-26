import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.print.PageLayout;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.TreeSet;

public class RunwayController extends Application {

    public static final String NAT_REGEX = "^[0-9]*$";
    public static final String INT_REGEX = "^-?[0-9]*$";
    private static final double SCALE_FACTOR = 1.1;
    public TabPane viewsTabs;

    public Button expObs;
    public Label obsLab;

    public TextArea rwLabel,rwLabel1;

    public TitledPane upper, lower;

    public Tab topDown, sideOn, calculations;
    public Label startLabel;

    public Pane tabMain;

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
    public ComboBox<String> obstacleList;

    // Output value text fields
    public TextField oldToraField, newToraField, oldTodaField, newTodaField, oldAsdaField, newAsdaField, oldLdaField, newLdaField;
    public Pane sidePane;
    @FXML
    public AnchorPane topDownPane, sideOnPane;

    @FXML
    public VBox leftPanel;

    public TextArea calculationsTextArea;
    public TextFlow calculationsTextFlow;

    private boolean alwaysShowLegend;
    private ArrayList<Object> legendItems = new ArrayList<Object>();

    public RunwayController(Runway rw, Controller parent) {
        this.rw = rw;
        this.parent = parent;

    }

    @FXML
    protected void openObstacleAction(){
        this.parent.configureFileChooser(this.parent.fileChooser);
        File file = this.parent.fileChooser.showOpenDialog(this.parent.primaryStage);
        if (file != null) {
            XMLDecoder decoder =
                    null;
            try {
                decoder = new XMLDecoder(new BufferedInputStream(
                        new FileInputStream(file)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Obstacle rw = (Obstacle) decoder.readObject();
                decoder.close();
                this.parent.airport.addObstacle(rw);
                this.parent.additionalInfoBar.setText("Obstacle imported successfully");

                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("A new obstacle has been added");
                alert.setContentText("Would you also like to place it on this runway and recalculate?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    loadObstaclesAction();
                    obstacleList.getSelectionModel().select(rw.getId());// ... user chose OK
                    submitButton.fire();
                } else {

                }
            }catch(Exception e1) {
                decoder.close();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("The file you want to import is of the wrong type");
                alert.setContentText("Airports files are named with the airport name only, runways use 'airport - designator', and obstacles use 'airport - obstacleName'.");
                alert.showAndWait();
                openObstacleAction();
            }

        }
    }

    @FXML
    protected void exportObstacleAction() {

        parent.configureFileChooser(parent.fileChooser);
        Obstacle r = null;
        try {
            r = getObstacleTextFields();


        if ((" "+r.getId().substring(r.getId().lastIndexOf(" ")+1)).equals(generateRunwayPairName()))
            r.setId(r.getId().substring(0, r.getId().lastIndexOf(" ")));
        this.parent.fileChooser.setInitialFileName(this.parent.airport.getName() + "-" + r.getId());
        File file = this.parent.fileChooser.showSaveDialog(this.parent.primaryStage);
        if (file != null) {
            try {
                FileOutputStream fos = new FileOutputStream(file);

                // Create XML encoder.
                XMLEncoder xenc = new XMLEncoder(new BufferedOutputStream(fos));

                // Write object.
                xenc.writeObject(r);
                xenc.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        this.parent.fileChooser.setInitialFileName("");
    } catch (IOException e) {
            displayInputError(e);
        }

    }

    @FXML
    protected void loadObstaclesAction() {
        obstacleList.getItems().setAll(new TreeSet(parent.airport.getObstacles().keySet()));
        obstacleList.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                if (parent.airport.getObstacles().containsKey(t1)) {
                    expObs.setVisible(true);
                    thresholdDistance.clear();
                    obstacleHeightInputField.clear();
                    distCentrelineInputField.clear();

                    if (rw.getDirection() <= 18 && !(String.valueOf(parent.airport.getObstacles().get(t1).getDist1stThresh()).equals("0") )) {
                        thresholdDistance.setText(String.valueOf(parent.airport.getObstacles().get(t1).getDist1stThresh()));
                    } else if (rw.getDirection() > 18 && !(String.valueOf(parent.airport.getObstacles().get(t1).getDist2ndThresh()).equals("0"))) {
                        thresholdDistance.setText(String.valueOf(parent.airport.getObstacles().get(t1).getDist2ndThresh()));
                    }

                    obstacleHeightInputField.setText(String.valueOf(parent.airport.getObstacles().get(t1).getHeight()));
                    distCentrelineInputField.setText(String.valueOf(parent.airport.getObstacles().get(t1).getCenterlineDist()));

                }
            }
        });
    }

    @FXML
    protected void submissionInit() {
        if (!initialized) {
            if (rw.getObstacle() != null ) {
                loadObstaclesAction();
                obstacleList.getSelectionModel().select(rw.getObstacle().getId());
            }

            submitButton.fire();
            initialized = true;
            if (rw.getObstacle() != null ) {
                submitButton.fire();
            }

            setAdvancedParameterFieldPompts();

            addFrontEndNumericInputValidation(tora, NAT_REGEX);
            addFrontEndNumericInputValidation(toda, NAT_REGEX);
            addFrontEndNumericInputValidation(asda, NAT_REGEX);
            addFrontEndNumericInputValidation(lda, NAT_REGEX);
            addFrontEndNumericInputValidation(resa, NAT_REGEX);
            addFrontEndNumericInputValidation(blast, NAT_REGEX);
            addFrontEndNumericInputValidation(stripEnd, NAT_REGEX);
            addFrontEndNumericInputValidation(alstocs, NAT_REGEX);

            addFrontEndNumericInputValidation(thresholdDistance, INT_REGEX);
            addFrontEndNumericInputValidation(distCentrelineInputField, INT_REGEX);
            addFrontEndNumericInputValidation(obstacleHeightInputField, NAT_REGEX);


        }
    }

    private void setAdvancedParameterFieldPompts() {
        tora.setPromptText(Integer.toString(rw.getOriginalTORA()) + "m");
        toda.setPromptText(Integer.toString(rw.getOriginalTODA()) + "m");
        asda.setPromptText(Integer.toString(rw.getOriginalASDA()) + "m");
        lda.setPromptText(Integer.toString(rw.getOriginalLDA()) + "m");
        resa.setPromptText(Integer.toString(rw.getRESA()) + "m");
        blast.setPromptText(Integer.toString(rw.getBlastAllowance()) + "m");
        stripEnd.setPromptText(Integer.toString(rw.getStripEnd()) + "m");
        alstocs.setPromptText(Integer.toString(rw.getALSTOCSSlope()));
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
        boolean temp = alwaysShowLegend;
        setAlwaysShowLegend(false);
        setAlwaysShowLegend(temp);
        rw.clearObstacle();
        obstacleList.setValue("");
        thresholdDistance.clear();
        distCentrelineInputField.clear();
        obstacleHeightInputField.clear();
        rw.setCalculations("");
        displayCalculations(rw);
        display();
        parent.additionalInfoBar.setText("Obstacle removed");
        obsLab.setText("Obstacle not set");
    }


    @FXML
    protected void submitButtonAction() {
boolean temp = alwaysShowLegend;
        setAlwaysShowLegend(false);
        setAlwaysShowLegend(temp);

        try {
            if (initialized) {
                rw.clearObstacle();
                rw.putObstacle(getObstacleTextFields());
                parent.additionalInfoBar.setText("Obstacle added");
                obsLab.setText("Obstacle set - " + rw.getObstacle().getId());
            }
            display();
        } catch (IOException e) {
            displayInputError(e);
        }
    }

    @FXML
    protected void advancedSubmitAction() {
        try {
            boolean temp1 = alwaysShowLegend;
            setAlwaysShowLegend(false);
            setAlwaysShowLegend(temp1);

            int[] newValues = getAdvancedTextFields();
            Obstacle temp = rw.getObstacle();
            rw = new Runway(rw.getName(), newValues[0], newValues[1], newValues[2], newValues[3]);
            rw.setRESA(newValues[4]);
            rw.setALSTOCSSlope(newValues[5]);
            rw.setStripEnd(newValues[6]);
            rw.setBlastAllowance(newValues[7]);
            rw.putObstacle(temp);
            setAdvancedParameterFieldPompts();
            parent.additionalInfoBar.setText("Runway " + rw.getName() + " values changed");
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

    @FXML
    protected void compressUpper(){
        upper.expandedProperty().setValue(false);
    }

    @FXML
    protected void compressLower(){
        lower.expandedProperty().setValue(false);
    }

    private void display() {
        startLabel.setText("");
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

        if (rw.getClearway() > 0 && alwaysShowLegend) {
            addLegendItem(Display.CLEARWAY_COLOR, "Clearway", currentY, true);
            currentY += 20;
        }

        if (rw.getStopway() > 0 && alwaysShowLegend) {
            addLegendItem(Display.STOPWAY_COLOR, "Stopway", currentY, true);
            currentY += 20;
        }

        if (rw.getObstacle() != null && alwaysShowLegend) {
            addLegendItem(Display.OBSTACLE_COLOR, "Obstacle", currentY, true);
            currentY += 20;
        }

        if (alwaysShowLegend) {
            addLegendItem(Color.GREEN, "Direction of Travel", currentY, true);
            currentY += 20;
        }
        if (alwaysShowLegend) {
            addLegendItem(Color.DARKSALMON, "Threshold", currentY, true);
            currentY += 20;
        }

        if (rw.getObstacle() != null && alwaysShowLegend) {
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

        legendItems.add(r1);
        legendItems.add(t1);
        legendItems.add(r2);
        legendItems.add(t2);
        if (bothPanels) tabMain.getChildren().addAll(r1, t1);
        sidePane.getChildren().addAll(r2, t2);
    }

    public void setAlwaysShowLegend(boolean alwaysShowLegend) {
        this.alwaysShowLegend = alwaysShowLegend;
        if (!alwaysShowLegend){
            for(Object o : legendItems) {
                tabMain.getChildren().remove(o);
                sidePane.getChildren().remove(o);
            }
            legendItems.clear();
        }
    }

    //Gets obstacle from text fields

    private Obstacle getObstacleTextFields() throws IOException {
        String obstacleType = (String) obstacleList.getValue();

        if (obstacleType.equals(""))
            throw new IOException("You must give a name for the obstacle in the box provided");


        Integer distLowerThreshold;
        Integer distUpperThreshold;
        Integer distCentreThreshold;
        Integer obstacleHeight;
        try {
            distCentreThreshold = Integer.parseInt(distCentrelineInputField.getText());
            obstacleHeight = Integer.parseInt(obstacleHeightInputField.getText());
        }catch(java.lang.NumberFormatException e){
            throw new IOException("Please enter a valid number for both the obstacle height and the distance from centreline");
        }

//		String intRegex = "-?[0-9]+";
//
//		if (!distLowerThreshInputField.getText().matches(intRegex) || !distUpperThreshInputField.getText().matches(intRegex) || !distCentrelineInputField.getText().matches(intRegex) || !distCentrelineInputField.getText().matches(intRegex)) {
//			throw new IOException("Invalid runway value: look for non-number characters in the Dist. Lower Threshold, Dist. Upper Threshold, Obstacle Height, and Dist. Centreline fields.");
//		}

        // Only parses input box that will be active
        if (rw.getDirection() <= 18) {
            try {
            distLowerThreshold = new Integer(thresholdDistance.getText());
        }catch(java.lang.NumberFormatException e){
            throw new IOException("Please enter a valid number for the threshold distance");
        }

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
            try {
            distUpperThreshold = new Integer(thresholdDistance.getText());
    }catch(java.lang.NumberFormatException e){
        throw new IOException("Please enter a valid number for the threshold distance");
    }

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

        if(parent.airport.getObstacles().containsKey(obstacleType) && (" "+obstacleType.substring(obstacleType.lastIndexOf(" ")+1)).equals(generateRunwayPairName())){
            if (rw.getDirection() <= 18)
                parent.airport.getObstacles().get(obstacleType).setDist1stThresh(distLowerThreshold);
            else
                parent.airport.getObstacles().get(obstacleType).setDist2ndThresh(distUpperThreshold);
            parent.airport.getObstacles().get(obstacleType).setHeight(obstacleHeight);
            parent.airport.getObstacles().get(obstacleType).setCenterlineDist(distCentreThreshold);
            return parent.airport.getObstacles().get(obstacleType);
        } else{
            Obstacle o = new Obstacle(obstacleType + generateRunwayPairName(), distLowerThreshold, distUpperThreshold, distCentreThreshold, obstacleHeight);
            o.setRunways(generateRunwayPairName());
            parent.airport.addObstacle(o);
            loadObstaclesAction();
            obstacleList.getSelectionModel().select(obstacleType + generateRunwayPairName());
            return o;
        }
    }

    private String generateRunwayPairName(){
        String suffix;
        String prefix = "";
        if (rw.getDirection() <= 27 && rw.getDirection() >= 22)
            prefix = "0";
        if(rw.getName().substring(rw.getName().length() - 1).equals("L"))
            suffix = "R";
        else if(rw.getName().substring(rw.getName().length() - 1).equals("R"))
            suffix = "L";
        else if (rw.getName().substring(rw.getName().length() - 1).equals("C"))
            suffix = "C";
        else
            suffix = "";

        if (rw.getDirection() <= 18) {
            return " ("+rw.getName() + "," + (rw.getDirection() + 18) + suffix+")";
        }else
            return " ("+prefix +(rw.getDirection() - 18) + suffix + "," + rw.getName()+")";
    }

    public int[] getAdvancedTextFields() throws Exception {
        TextField[] advancedInputFields = {tora, toda, asda, lda, resa, alstocs, stripEnd, blast};

        int[] initValues = {rw.getOriginalTORA(), rw.getOriginalTODA(), rw.getOriginalASDA(), rw.getOriginalLDA(), rw.getRESA(), rw.getALSTOCSSlope(), rw.getStripEnd(), rw.getBlastAllowance()};
        int[] newValues = new int[initValues.length];

        for (int i = 0; i < initValues.length; i++) {
            if (advancedInputFields[i].getText().equals("")) {
                newValues[i] = initValues[i];
            } else {
                newValues[i] = Integer.parseInt(advancedInputFields[i].getText());
            }
        }

        parent.checkRunwayValues(newValues[0], newValues[1], newValues[2], newValues[3]);

        return newValues;
    }

    public void print() {
		printToPrinter(viewsTabs.getSelectionModel().getSelectedItem().getContent());
		//saveFile(getTab());
    }



    public void saveFile() {
        saveFile(viewsTabs.getSelectionModel().getSelectedItem().getContent());
        //saveFile(getTab());
    }

    protected void saveFile(Node pan){
        BufferedImage bi = new BufferedImage(511, 640, BufferedImage.TYPE_INT_RGB);
        BufferedImage image = javafx.embed.swing.SwingFXUtils.fromFXImage(pan.snapshot(new SnapshotParameters(), null), bi);
        Graphics2D gd = (Graphics2D) image.getGraphics();
        FileChooser fc = new FileChooser();
        fc.setTitle("Save Image");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("png file",".png"), new FileChooser.ExtensionFilter("jpeg file",".jpg"), new FileChooser.ExtensionFilter("gif file",".gif"));
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
	    rwLabel.setVisible(true);
        rwLabel1.setVisible(true);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date date = new Date();
        rwLabel.setText("Airport: " + parent.airport.getName() + "\nRunway: "+ rw.getName() + "    Date: " + dateFormat.format(date));
        rwLabel1.setText("Airport: " + parent.airport.getName() + "\nRunway: "+ rw.getName() + "    Date: " + dateFormat.format(date));
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
        rwLabel.setVisible(false);
        rwLabel1.setVisible(false);
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

//        reCentre(p);
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
        p.setTranslateX(p.getTranslateX() + e.getX() - dragOldPos.getX());
        p.setTranslateY(p.getTranslateY() + e.getY() - dragOldPos.getY());

        dragOldPos.setLocation(e.getX(), e.getY());

//        reCentre(p);
    }

    private void reCentre(Pane p) {
//        double realWidth = p.getWidth() * p.getScaleX();
//        if (p.getTranslateX() + realWidth < 10) {
//            p.setTranslateX(-realWidth + 10);
//        } else if (p.getTranslateX() - p.getWidth() > 10) {
//            p.setTranslateX(p.getWidth() - 10);
//        }
//
//        double realHeight = p.getHeight() * p.getScaleY();
//        if (p.getTranslateY() + realHeight < 10) {
//            p.setTranslateY(-realHeight + 10);
//        } else if (p.getTranslateY() - p.getHeight() > 10) {
//            p.setTranslateY(p.getHeight() - 10);
//        }
        double realWidth = p.getWidth() * p.getScaleX();
        if (p.getTranslateX() + realWidth < 10) {
            p.setTranslateX(-realWidth + 10);
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

    @FXML
    protected void rotateButtonAction() {
        displayValues();
        displayCalculations(rw);
        Display screen = new Display(topDownPane, sideOnPane);
        screen.clearPanes();
        screen.drawRunway(rw);
        displayLegend();
        screen.rotate();
    }

    @FXML
    protected void topDownCentreButtonAction() {
        topDownPane.setTranslateX(0);
        topDownPane.setTranslateY(0);
        topDownPane.setScaleX(1);
        topDownPane.setScaleY(1);
    }

    @FXML
    protected void sideOnCentreButtonAction() {
        sideOnPane.setTranslateX(0);
        sideOnPane.setTranslateY(0);
        sideOnPane.setScaleX(1);
        sideOnPane.setScaleY(1);
    }
}