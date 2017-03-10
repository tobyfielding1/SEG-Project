import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Test extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hello World!");
        Pane topDownPane = new Pane();
        Pane sideOnPane = new Pane();
        
        Stage w2 = new Stage();
        w2.setScene(new Scene(sideOnPane, 630, 540));
        w2.show();
        
        primaryStage.setX(20);
        primaryStage.setY(50);
        
        w2.setX(680);
        w2.setY(50);
        
       	
        primaryStage.setScene(new Scene(topDownPane, 630, 540));
        Airport testAir = new Airport("TEST");
        Runway rw = getRunway();
        testAir.addRunway(rw);
        Obstacle ob = getObstacle();
        testAir.addObstacle("09L", ob);
        
        Display test = new Display(topDownPane,sideOnPane);
        test.drawRunway(rw);
        primaryStage.show();
		
	}
	
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
