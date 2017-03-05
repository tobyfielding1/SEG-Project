import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer.MarginType;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by tobyf on 18/02/2017.
 */
public class Controller extends Application {
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

	Airport model;
	View view;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("RRTGUI.fxml"));
		primaryStage.setTitle("Runway Redeclaration Tool");
		primaryStage.setScene(new Scene(root, 900, 590));
		primaryStage.show();
		//        primaryStage.setResizable(false);
	}

	// Close option in MenuBar -> File -> Close
	@FXML
	protected void fileCloseMenuAction() {
		// get a handle to the stage
		Stage stage = (Stage) menu.getScene().getWindow();
		// do what you have to do
		stage.close();
	}

	@FXML
	protected void drawButtonAction() throws IOException {
		Scanner scan = new Scanner(System.in);
		String runwayName, obsType;
		int TORA, TODA, ASDA, LDA, thresh1Distance, thresh2Distance, centerlineDistance, obsHeight, obsRESA;
		Airport airport;
		// Prompts user to enter values in command line
		additionalInfoBar.setText("Provide input via the command line");

		// Create an airport
		airport = new Airport("Airport");

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
		screen.clearPanes();
		screen.drawRunway(rw);
		PrinterJob job = PrinterJob.createPrinterJob();
		if (job != null) {
			if ( job.showPrintDialog(null)){
				if (job.getJobSettings().getPageLayout().getPageOrientation().equals(PageOrientation.PORTRAIT)){
					topDownPane.getTransforms().add(new Scale(0.45, 0.45));
					topDownPane.getTransforms().add(new Translate(-50, 0));
					sideOnPane.getTransforms().add(new Scale(0.45, 0.45));
					sideOnPane.getTransforms().add(new Translate(-50, 0));
				}
				else{
					topDownPane.getTransforms().add(new Scale(0.75, 0.75));
					topDownPane.getTransforms().add(new Translate(150, 300));
					sideOnPane.getTransforms().add(new Scale(0.75, 0.75));
					sideOnPane.getTransforms().add(new Translate(150, 300));
					calculationsTextArea.getTransforms().add(new Scale(0.75, 0.75));
					calculationsTextArea.getTransforms().add(new Translate(150, 300));
				}
			}
			boolean print3 = job.printPage(topDownPane);
			//boolean print3 = job.printPage(sideOnPane);
			//boolean print3 = job.printPage(calculationsTextArea);
			if (print3) {
				job.endJob();
				topDownPane.getTransforms().clear();
			}
		}


		additionalInfoBar.setText("Input successful");
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
}
