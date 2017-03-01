

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Display{
	Pane topDownPane;
	Pane sideOnPane;
	
	final Paint arrowColor = Color.RED;
	final double arrowThickness = 0.5;
	
	
	final int runwayStripLength = 500;
	final int runwayStripWidth = 40;
	
	int runwayEndLeft;
	int runwayEndRight;
	
	int paneHeight;
	int paneWidth;
	
	int thresholdPoint;
	
	double scale;
	
	public Display (Pane td, Pane so) {
		this.topDownPane = td;
		this.sideOnPane = so;
		
		this.paneHeight = (int) Math.round(topDownPane.getHeight());
		this.paneWidth = (int) Math.round(topDownPane.getWidth());
	
		this.runwayEndLeft = (int) Math.round( (topDownPane.getWidth() / 2) - (runwayStripLength / 2));  
		this.runwayEndRight = (int) Math.round( (topDownPane.getWidth() / 2) + (runwayStripLength / 2));  
		
	}
	
	public void drawRunway(Runway rw) {
		scale = rw.getRunwaySize() / runwayStripLength;
		
		drawRunwayTD(rw);
		drawRunwaySO(rw);
	}
	
	public void drawRunwaySO(Runway rw) {
		sideOnPane.setStyle("-fx-background-color: white;");
    	
		drawRunwaySO();
		drawSlope(200, 100, 15);
	}
	
	public void drawRunwayTD(Runway rw) {
		topDownPane.setStyle("-fx-background-color: white;");
    	
    	
    	drawRunwayTD();
        drawThreshold(rw.threshold);
        drawDistance(true,thresholdPoint,-30,400,"even longer long text for test");
    	
    }
    
    public void drawDistance(Boolean dir, double startX, double startY, double length, String text) {
    	Group arrow;
    	if (dir) arrow = makeHArrow(startX,(paneHeight / 2) + startY,length);
    	else arrow = makeVArrow((paneWidth / 2) + startX,startY,length);
    	
    	Text t;
    	if (dir) {
    		t = new Text(startX + (length / 2),(paneHeight / 2) + startY - 5,text);
        	t.setX(t.getX() - t.getText().length() *2.65);
    	}
    	else t = new Text((paneWidth / 2) + startX + 3, startY + (length / 2), text);
    	
    	t.setStroke(Color.GRAY);
    	t.setStrokeWidth(0.7);
    	
    	arrow.getChildren().add(t);
    	topDownPane.getChildren().add(arrow);
    }
	
    public void drawRunwayTD() {
    	double x = (topDownPane.getWidth() / 2) - (runwayStripLength/2);
    	double y = (topDownPane.getHeight() / 2) - (runwayStripWidth/2);
    	
    	
    	Color runwayFill = new Color(0.2, 0.2, 0.2, 0.5);
  		
  		Rectangle runwayStrip = new Rectangle(x,y,runwayStripLength,runwayStripWidth);
  		runwayStrip.setFill(runwayFill);
  		runwayStrip.setStroke(Color.BLACK);
  		topDownPane.getChildren().add(runwayStrip);
  		
  		Line centerLine = new Line(x, y + (runwayStripWidth/2) , x + runwayStripLength, y + (runwayStripWidth/2));
  		centerLine.setStroke(Color.WHITE);
  		centerLine.getStrokeDashArray().addAll(20.0,20.0);
  		topDownPane.getChildren().add(centerLine);
    }
    
    public Group makeHArrow(double x1, double y1, double length) {
    	double x2 = x1 +length;
    	double y2 = y1;
    	
    	Line main = new Line(x1, y1, x2, y2);
    	main.setStroke(arrowColor);
    	main.setStrokeWidth(arrowThickness);
    	
    	Line h1 = new Line(x1,y1,x1+10,y1-10);
    	h1.setStroke(arrowColor);
    	h1.setStrokeWidth(arrowThickness);
    	
    	Line h2 = new Line(x1,y1,x1+10,y1+10);
    	h2.setStroke(arrowColor);
    	h2.setStrokeWidth(arrowThickness);
    	
    	Line h3 = new Line(x2,y2,x2-10,y2+10);
    	h3.setStroke(arrowColor);
    	h3.setStrokeWidth(arrowThickness);
    
    	Line h4 = new Line(x2,y2,x2-10,y2-10);
    	h4.setStroke(arrowColor);
    	h4.setStrokeWidth(arrowThickness);
    	
    	
    	Group arrow = new Group();
    	
    	arrow.getChildren().addAll(main,h1,h2,h3,h4);	
    	return arrow;
    }
    
    
    public Group makeVArrow(double x1, double y1, double length) {
    	double x2 = x1;
    	double y2 = y1 + length;
    	
    	Line main = new Line(x1, y1, x2, y2);
    	main.setStroke(arrowColor);
    	main.setStrokeWidth(arrowThickness);
    	
    	
    	Line h1 = new Line(x1,y1,x1-10,y1+10);
    	h1.setStroke(arrowColor);
    	h1.setStrokeWidth(arrowThickness);
    	
    	Line h2 = new Line(x1,y1,x1+10,y1+10);
    	h2.setStroke(arrowColor);
    	h2.setStrokeWidth(arrowThickness);
    	
    	Line h3 = new Line(x2,y2,x2+10,y2-10);
    	h3.setStroke(arrowColor);
    	h3.setStrokeWidth(arrowThickness);
    	
    	
    	Line h4 = new Line(x2,y2,x2-10,y2-10);
    	h4.setStroke(arrowColor);
    	h4.setStrokeWidth(arrowThickness);
    	

    	Group arrow = new Group();
    	
    	arrow.getChildren().addAll(main,h1,h2,h3,h4);
    	return arrow;
    
    }



	

    public void drawThreshold(int threshold) {
	   int x;
	   if (threshold > 0) x = runwayEndLeft +  (int) Math.round(threshold/scale);
	   else x = runwayEndRight +  (int) Math.round(threshold/scale);
	   
	   thresholdPoint = x;
	   
	   Rectangle thresh = new Rectangle(x - 2,paneHeight / 2 - runwayStripWidth / 2, 5, runwayStripWidth);
	   thresh.setFill(Color.DARKSALMON);
	   
	   topDownPane.getChildren().add(thresh);
   }

    public void drawRunwaySO() {
    	double x = (sideOnPane.getWidth() / 2) - (runwayStripLength/2);
    	double y = (sideOnPane.getHeight() / 2) - 2;
    	
    	
    	Color runwayFill = new Color(0.2, 0.2, 0.2, 0.5);
  		
  		Rectangle runwayStrip = new Rectangle(x,y,runwayStripLength,5);
  		runwayStrip.setFill(runwayFill);
  		runwayStrip.setStroke(Color.BLACK);
  		sideOnPane.getChildren().add(runwayStrip);
  		
  		Line centerLine = new Line(x, y + 2 , x + runwayStripLength, y + 2);
  		centerLine.setStroke(Color.WHITE);
  		centerLine.getStrokeDashArray().addAll(20.0,20.0);
  		sideOnPane.getChildren().add(centerLine);
    }

    public void drawSlope(int topX, int height, int angle) {
    	Polygon tri = new Polygon();
    	
    	
    	
    	double x1 = Math.round(topX/scale) + runwayEndLeft;
    	double y1 = paneHeight/2 - height/scale;
    	double x2 = (double) Math.round(topX/scale) + runwayEndLeft;;
    	double y2 = paneHeight/2;
    	double x3 = x1 + (height / Math.tan(Math.toRadians(angle))) / scale;
    	double y3 = paneHeight/2;
    	
    	tri.getPoints().addAll(new Double[] {x1,y1,x2,y2,x3,y3});
    	tri.setFill(Color.GREEN);
    	
    	tri.setVisible(true);
    	sideOnPane.getChildren().add(tri);
    }
}
