import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.util.Scanner;

/**
 * Created by tobyf on 18/02/2017.
 */
public class Controller {
    // For interaction between controller and GUI
    @FXML
    private javafx.scene.control.MenuBar menu;
	
    Airport model;
    View view;

    public static void main(String[]args){
    	String logRun1,logRun2,obsType;
    	int lR1TORA,lR1TODA,lR1ASDA,lR1LDA,lR2TORA,lR2TODA,lR2ASDA,lR2LDA,dist1stThresh,dist2ndThresh,centerlineDist,objheight,objRESA;
    	Scanner scan = new Scanner(System.in);
        Controller controller = new Controller();
        controller.model = new Airport("Airport 1");
        controller.view = new View(controller.model);
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
        System.out.println("Enter the distance from Threshold 1 (Is this the logical runway ??) (m):");
        dist1stThresh = scan.nextInt();
        System.out.println("Enter the distance from Threshold 2 (Is this the logical runway ??) (m):");
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
        controller.model.addRunway(new Runway(logRun1,lR1TORA,lR1TODA,lR1ASDA,lR1LDA));
        controller.model.addRunway(new Runway(logRun2,lR2TORA,lR2TODA,lR2ASDA,lR2LDA));
        Obstacle o = new Obstacle(obsType,dist1stThresh,dist2ndThresh, centerlineDist, objheight,objRESA);
        controller.view.update(logRun1);
        controller.view.update(logRun2);
        controller.model.addObstacle(logRun1, o);
        controller.model.addObstacle(logRun2, o);
        controller.view.update(logRun1);
        controller.view.update(logRun2);
    }

    // Close option in MenuBar -> File -> Close
    @FXML
    protected void fileCloseMenuAction() {
        // get a handle to the stage
        Stage stage = (Stage) menu.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
}
