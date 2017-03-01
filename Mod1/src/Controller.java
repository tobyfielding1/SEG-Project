import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by tobyf on 18/02/2017.
 */
public class Controller extends Application {
    // Output value text fields
    public TextField oldToraField, newToraField, oldTodaField, newTodaField, oldAsdaField, newAsdaField, oldLdaField, newLdaField;

    // Graphical and calculations display panes
    public AnchorPane topDownPane, sideOnPane;
    public TextArea calculationsTextArea;

    // For interaction between controller and GUI
    @FXML
    private MenuBar menu;
	
    Airport model;
    View view;

    public static void main(String[]args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
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
        String runwayName,obsType;
        int TORA,TODA,ASDA,LDA,thresh1Distance,thresh2Distance,centerlineDistance,obsHeight,obsRESA;
        Airport airport;

        // Create an airport
        System.out.println("Enter airport name:");
        String apName = scan.next();
        airport = new Airport(apName);

        // Ask if user wants to read from command line or text file
        System.out.println("Would you like to read data from a text file? (Y/N)");
        String input = scan.next();
        if (input.equals("Y") || input.equals("y")) {
            Runway rw = getRunway();
            airport.addRunway(rw);
            airport.addObstacle(rw.getName(), getObstacle());
        } else if (input.equals("N") || input.equals("n")) {
            System.out.println("Enter Logical Runway 1 :");
            runwayName = scan.next();
            System.out.println("Enter the TORA value for " + runwayName + " (m):");
            TORA = scan.nextInt();
            System.out.println("Enter the TODA value for " + runwayName + " (m):");
            TODA = scan.nextInt();
            System.out.println("Enter the ASDA value for " + runwayName + " (m):");
            ASDA = scan.nextInt();
            System.out.println("Enter the LDA value for " + runwayName + " (m):");
            LDA = scan.nextInt();
            System.out.println("------------------------------------------------------------------------------------------------");
            System.out.println("Enter type of obstacle on runway :");
            obsType = scan.next();
            System.out.println("Enter the distance from Threshold 1 (m):");
            thresh1Distance = scan.nextInt();
            System.out.println("Enter the distance from Threshold 2 (m):");
            thresh2Distance = scan.nextInt();
            System.out.println("Enter the distance from the centreline (m):");
            centerlineDistance = scan.nextInt();
            System.out.println("Enter the height of the object (m):");
            obsHeight = scan.nextInt();
            System.out.println("Enter the RESA value desired around the object (m):");
            obsRESA = scan.nextInt();
            System.out.println("------------------------------------------------------------------------------------------------");
            System.out.println("");
//        controller.model.addRunway(new Runway("09L",3902,3902,3902,3595));
//        controller.model.addRunway(new Runway("27R",3884,3962,3884,3884));
//        Obstacle o = new Obstacle("tree",2500,500, 60, 25,240);

            airport = new Airport(apName);
            airport.addRunway(new Runway(runwayName,TORA,TODA,ASDA,LDA));
            Obstacle o = new Obstacle(obsType,thresh1Distance,thresh2Distance, centerlineDistance, obsHeight,obsRESA);
            airport.addObstacle(runwayName, o);
        }

        // Displays chosen runway
        System.out.println("Enter chosen runway name:");
        String rwName = scan.next();
        Runway rw = airport.getRunway(rwName);
        displayValues(rw);
        displayCalculations(rw);
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
        FileReader fr = new FileReader("runway1.txt");
        BufferedReader br = new BufferedReader(fr);
        String input = br.readLine();
        String[] values = input.split(",");
        return new Runway(values[0], Integer.parseInt(values[1]), Integer.parseInt(values[2]), Integer.parseInt(values[3]), Integer.parseInt(values[4]));
    }

    private Obstacle getObstacle() throws IOException {
        FileReader fr = new FileReader("obstacle1.txt");
        BufferedReader br = new BufferedReader(fr);
        String input = br.readLine();
        String[] values = input.split(",");
        return new Obstacle(values[0], Integer.parseInt(values[1]), Integer.parseInt(values[2]), Integer.parseInt(values[3]), Integer.parseInt(values[4]), Integer.parseInt(values[5]));
    }
}
