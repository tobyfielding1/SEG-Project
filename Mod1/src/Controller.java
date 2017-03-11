import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.PageOrientation;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by tobyf on 18/02/2017.
 */
public class Controller extends Application {

	// Input value text fields
	public TextField toraInputField, todaInputField, asdaInputField, ldaInputField, distLowerThreshInputField, rNameInputField;
	public TextField distUpperThreshInputField, distCentrelineInputField, obstacleHeightInputField, resaInputField;

	// Combo box of obstacle types
	public ComboBox obstacleTypeComboBox;

	// Output value text fields
	public TextField oldToraField, newToraField, oldTodaField, newTodaField, oldAsdaField, newAsdaField, oldLdaField, newLdaField;

	// Bottom info bar
	public TextField additionalInfoBar;

	// Graphical and calculations display panes
	public AnchorPane topDownPane, sideOnPane;
	public TextArea calculationsTextArea;

	// For interaction between controller and GUI
	@FXML
	private MenuBar menu;
	@FXML
	private CheckMenuItem viewAlwaysShowLegend;

	Airport model;
	View view;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("RRTGUI.fxml"));
		primaryStage.setTitle("Runway Redeclaration Tool");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
		primaryStage.setResizable(true);
	}

	/*
	 Close option in MenuBar -> File -> Close
	 */
	@FXML
	protected void fileCloseMenuAction() {
		// get a handle to the stage
		Stage stage = (Stage) menu.getScene().getWindow();
		// do what you have to do
		stage.close();
	}

	/*
	 Shows ReadMe file to user
	 */
	@FXML
	protected void helpViewReadMeAction() throws IOException {
		Alert readme = new Alert(Alert.AlertType.INFORMATION);
		readme.setResizable(true);
		readme.setTitle("View ReadMe");
		readme.setHeaderText(null);

		try {
			byte[] encoded = Files.readAllBytes(Paths.get("readme.txt"));
			TextArea textArea = new TextArea(new String(encoded, Charset.defaultCharset()));
			textArea.setEditable(false);
			textArea.setWrapText(true);
			textArea.setMaxWidth(Double.MAX_VALUE);
			textArea.setMaxHeight(Double.MAX_VALUE);

			readme.getDialogPane().setContent(textArea);
			readme.showAndWait();
		} catch (IOException noReadmeFile) {
			additionalInfoBar.setText("No ReadMe file found");
		}

	}

	@FXML
	protected void drawButtonAction() throws IOException {

		// Create an airport
		Airport airport = new Airport("Airport");

		// Imports runway and obstacle values from text file
		Runway rw = getRunway();
		airport.addRunway(rw);
		if (!obstacleInputEmpty()) {
			airport.addObstacle(rw.getName(), getObstacle());
		}

		// Displays runway
		displayValues(rw);
		displayCalculations(rw);
		Display screen = new Display(topDownPane, sideOnPane);
		//screen.setAlwaysShowLegend(viewAlwaysShowLegend.isSelected());
		screen.clearPanes();
		screen.drawRunway(rw);
		additionalInfoBar.setText("Input successful");
	}

	protected void saveToFile(Node pan,String fileName, String extension){
		BufferedImage bi = new BufferedImage(511,640, BufferedImage.TYPE_INT_RGB);
		BufferedImage image = javafx.embed.swing.SwingFXUtils.fromFXImage(pan.snapshot(new SnapshotParameters(), null), bi);
		Graphics2D gd = (Graphics2D) image.getGraphics();
		File file = new File(fileName + extension);
		try {
			ImageIO.write(image,extension.substring(1), file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void printToPrinter(Node pan){
		PrinterJob job = PrinterJob.createPrinterJob();
				if (job != null) {
					if ( job.showPrintDialog(null)){
						if (job.getJobSettings().getPageLayout().getPageOrientation().equals(PageOrientation.PORTRAIT)){
							pan.getTransforms().add(new Scale(0.45, 0.45));
							pan.getTransforms().add(new Translate(-50, 0));
						}
						else{
							pan.getTransforms().add(new Scale(0.75, 0.75));
							pan.getTransforms().add(new Translate(150, 300));
						}
					}
					boolean printsucc = job.printPage(pan);
					if (printsucc) {
						job.endJob();
						pan.getTransforms().clear();
					}
				}
	}
	/*
     Gets values from input text fields and draws runway
	 */
	@FXML
	protected void submitButtonAction() {

		// Create an airport
		Airport airport = new Airport("Airport");

		// Imports runway and obstacle values from text fields
		Runway rw = getRunwayTextFields();
		airport.addRunway(rw);
		airport.addObstacle(rw.getName(), getObstacleTextFields());

		// Displays runway
		displayValues(rw);
		displayCalculations(rw);
		Display screen = new Display(topDownPane, sideOnPane);
		//screen.setAlwaysShowLegend(viewAlwaysShowLegend.isSelected());
		screen.clearPanes();
		screen.drawRunway(rw);
	}

	/*
     Updates textboxes with runway values
	 */
	private void displayValues(Runway r) {
		newToraField.setText(Integer.toString(r.getTORA()));
		newTodaField.setText((Integer.toString(r.getTODA())));
		newAsdaField.setText(Integer.toString(r.getASDA()));
		newLdaField.setText(Integer.toString(r.getLDA()));
	}

	/*
     Updates calculations text box
	 */
	private void displayCalculations(Runway r) {
		calculationsTextArea.setText(r.getCalculations());
	}

	/*
     Displays chosen runway in runway panes
	 */
	private Runway getRunway() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("runway1.txt"));
		String input = br.readLine();
		String[] values = input.split(",");
		return new Runway(values[0], Integer.parseInt(values[1]), Integer.parseInt(values[2]), Integer.parseInt(values[3]), Integer.parseInt(values[4]));
	}

	/*
	 Gets Runway from text fields
	 */
	private Runway getRunwayTextFields() {
		String name = rNameInputField.getText();
		int tora = Integer.parseInt(toraInputField.getText());
		int toda = Integer.parseInt(todaInputField.getText());
		int asda = Integer.parseInt(asdaInputField.getText());
		int lda = Integer.parseInt(ldaInputField.getText());
		return new Runway(name, tora, toda, asda, lda);
	}

	private Obstacle getObstacle() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("obstacle1.txt"));
		String input = br.readLine();
		String[] values = input.split(",");
		return new Obstacle(values[0], Integer.parseInt(values[1]), Integer.parseInt(values[2]), Integer.parseInt(values[3]), Integer.parseInt(values[4]), Integer.parseInt(values[5]));
	}

	private boolean obstacleInputEmpty() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("obstacle1.txt"));
		return br.readLine() == null;
	}

	/*
	 Gets obstacle from text fields
	 */
	private Obstacle getObstacleTextFields() {
		String obstacleType = (String)obstacleTypeComboBox.getValue();
		int distLowerThreshold = Integer.parseInt(distLowerThreshInputField.getText());
		int distUpperThreshold = Integer.parseInt(distUpperThreshInputField.getText());
		int distCentreThreshold = Integer.parseInt(distCentrelineInputField.getText());
		int obstacleHeight = Integer.parseInt(obstacleHeightInputField.getText());
		int resa = Integer.parseInt(resaInputField.getText());
		return new Obstacle (obstacleType, distLowerThreshold, distUpperThreshold, distCentreThreshold, obstacleHeight, resa);
	}
}
