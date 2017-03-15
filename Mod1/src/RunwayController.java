import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
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
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class RunwayController extends Application {

    @FXML
    public Tab topDown, sideOn, calculations;

	private final Controller parent;
	private Runway rw;

	private boolean initialized = false;

	public Button submitButton,clearObstacle;

	public TextField thresholdDistance, distCentrelineInputField, obstacleHeightInputField,tora,toda,asda,lda,resa,blast,stripEnd,alstocs;

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
		thresholdDistance.clear();
		distCentrelineInputField.clear();
		obstacleHeightInputField.clear();
		rw.setCalculations("");
		displayCalculations(rw);
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
		if (rw.getDirection()<=18) {
			distLowerThreshold = new Integer(thresholdDistance.getText());

			int minDistLowerThreshold = -60;
			if (distLowerThreshold < minDistLowerThreshold) {
				throw new IOException("Obstacle's distance from the lower threshold is too low and does not require redeclaration (minimum: " + minDistLowerThreshold + ").");
			}

			int maxDistLowerThreshold = rw.getOriginalTORA() + 60;
			if (distLowerThreshold > maxDistLowerThreshold) {
				throw new IOException("Obstacle's distance from the lower threshold is too high and does not require redeclaration (maximum: " + maxDistLowerThreshold + ").");
			}

			distUpperThreshold = null;
		} else {
			distUpperThreshold = new Integer(thresholdDistance.getText());

			int minDistUpperThreshold = -60;
			if (distUpperThreshold < minDistUpperThreshold) {
				throw new IOException("Obstacle's distance from the upper threshold is too low and does not require redeclaration (minimum: " + minDistUpperThreshold + ").");
			}

			int maxDistUpperThreshold =  rw.getOriginalTORA() + 60;
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

		int maxObstacleHeight = 80;
		if (obstacleHeight > maxObstacleHeight) {
			throw new IOException("Obstacle too high (maximum: " + maxObstacleHeight + ").");
		}

		return new Obstacle(obstacleType, distLowerThreshold, distUpperThreshold, distCentreThreshold, obstacleHeight);
	}

	public void print() {
		printToPrinter(getTab());
	}

	protected void saveToFile(Node pan, String fileName, String extension) {
		BufferedImage bi = new BufferedImage(511, 640, BufferedImage.TYPE_INT_RGB);
		BufferedImage image = javafx.embed.swing.SwingFXUtils.fromFXImage(pan.snapshot(new SnapshotParameters(), null), bi);
		Graphics2D gd = (Graphics2D) image.getGraphics();
		File file = new File(fileName + extension);
		try {
			ImageIO.write(image, extension.substring(1), file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	protected void printToPrinter(Node pan) {
		PrinterJob job = PrinterJob.createPrinterJob();
		if (job != null) {
			if (job.showPrintDialog(null)) {
				pan.getTransforms().add(new Scale(0.60, 0.60));
				pan.getTransforms().add(new Translate(150, 200));
				boolean td = job.printPage(pan);
				if (td) {
					job.endJob();
					pan.getTransforms().clear();
				}
			}
		}
		pan.getTransforms().clear();
	}

	public void toggleLegend() {
		setAlwaysShowLegend(!alwaysShowLegend);
		display();
	}
}
