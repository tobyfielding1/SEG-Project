import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Scanner;

/**
 * Created by tobyf on 18/02/2017.
 */
public class Controller extends Application {
    // Output value text fields
    public TextField oldToraField, newToraField, oldTodaField, newTodaField, oldAsdaField, newAsdaField, oldLdaField, newLdaField;

    // Graphical and calculations display panes
    public AnchorPane topDownPane, sideOnPane, calculationsPane;

    private Airport airport;

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
    protected void drawButtonAction() {
        Scanner scan = new Scanner(System.in);
        String logRun1,logRun2,obsType;
        int lR1TORA,lR1TODA,lR1ASDA,lR1LDA,lR2TORA,lR2TODA,lR2ASDA,lR2LDA,dist1stThresh,dist2ndThresh,centerlineDist,objheight,objRESA;

        // Create an airport
        System.out.println("Enter airport name:");
        String apName = scan.next();
        System.out.println("Enter Logical Runway 1 :");
        logRun1 = scan.next();
        System.out.println("Enter the TORA value for " + logRun1 + " (m):");
        lR1TORA = scan.nextInt();
        System.out.println("Enter the TODA value for " + logRun1 + " (m):");
        lR1TODA = scan.nextInt();
        System.out.println("Enter the ASDA value for " + logRun1 + " (m):");
        lR1ASDA = scan.nextInt();
        System.out.println("Enter the LDA value for " + logRun1 + " (m):");
        lR1LDA = scan.nextInt();
        System.out.println("------------------------------------------------------------------------------------------------");
        System.out.println("");
        System.out.println("Enter Logical Runway 2 :");
        logRun2 = scan.next();
        System.out.println("Enter the TORA value for " + logRun2 + " (m):");
        lR2TORA = scan.nextInt();
        System.out.println("Enter the TODA value for " + logRun2 + " (m):");
        lR2TODA = scan.nextInt();
        System.out.println("Enter the ASDA value for " + logRun2 + " (m):");
        lR2ASDA = scan.nextInt();
        System.out.println("Enter the LDA value for " + logRun2 + " (m):");
        lR2LDA = scan.nextInt();
        System.out.println("------------------------------------------------------------------------------------------------");
        System.out.println("");
        System.out.println("Enter type of obstacle on runway :");
        obsType = scan.next();
        System.out.println("Enter the distance from Threshold 1 (m):");
        dist1stThresh = scan.nextInt();
        System.out.println("Enter the distance from Threshold 2 (m):");
        dist2ndThresh = scan.nextInt();
        System.out.println("Enter the distance from the centreline (m):");
        centerlineDist = scan.nextInt();
        System.out.println("Enter the height of the object (m):");
        objheight = scan.nextInt();
        System.out.println("Enter the RESA value desired around the object (m):");
        objRESA = scan.nextInt();
        System.out.println("------------------------------------------------------------------------------------------------");
        System.out.println("");
//        controller.model.addRunway(new Runway("09L",3902,3902,3902,3595));
//        controller.model.addRunway(new Runway("27R",3884,3962,3884,3884));
//        Obstacle o = new Obstacle("tree",2500,500, 60, 25,240);

        airport = new Airport(apName);
        airport.addRunway(new Runway(logRun1,lR1TORA,lR1TODA,lR1ASDA,lR1LDA));
        airport.addRunway(new Runway(logRun2,lR2TORA,lR2TODA,lR2ASDA,lR2LDA));
        Obstacle o = new Obstacle(obsType,dist1stThresh,dist2ndThresh, centerlineDist, objheight,objRESA);
        airport.addObstacle(logRun1, o);
        airport.addObstacle(logRun2, o);

        // Displays chosen runway
        System.out.println("Enter chosen runway name:");
        String rwName = scan.next();
        displayValues(airport.getRunway(rwName));
        drawRunway(airport.getRunway(rwName));
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
     Displays chosen runway in runway panes
     */
    public void drawRunway(Runway r) {

    }


}
