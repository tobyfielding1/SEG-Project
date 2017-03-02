

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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Display{
	Pane topDownPane;
	Pane sideOnPane;
	
	final Paint arrowColor = Color.RED;
	final Paint clearwayColor = Color.YELLOW;
	final Paint stopwayColor = new Color(0.3,0.3,1,0.98);
	final double arrowThickness = 0.5;
	
	
	final int runwayStripLength = 400;
	final int runwayStripWidth = 40;
	
	final Font indicatorFont = Font.font("Arial Black",FontWeight.BOLD,18);
	
	int runwayEndLeft;
	int runwayEndRight;
	
	int paneHeight;
	int paneWidth;
	
	int thresholdPoint;
	int centreLine;
	
	double scale;
	
	
	public Display (Pane td, Pane so) {
		this.topDownPane = td;
		this.sideOnPane = so;
		
		this.paneHeight = (int) Math.round(topDownPane.getHeight());
		if (this.paneHeight == 0) 
			this.paneHeight = (int) Math.round(sideOnPane.getHeight());
		
		this.paneWidth = (int) Math.round(topDownPane.getWidth());
		if (this.paneWidth == 0) 
			this.paneWidth = (int) Math.round(sideOnPane.getWidth());
		
		this.runwayEndLeft = (int) Math.round( (topDownPane.getWidth() / 2) - (runwayStripLength / 2)); 
		if (this.runwayEndLeft == 0) this.runwayEndLeft = (int) Math.round( (sideOnPane.getWidth() / 2) - (runwayStripLength / 2)); 
		 
		this.runwayEndRight = (int) Math.round( (topDownPane.getWidth() / 2) + (runwayStripLength / 2));  
		if (this.runwayEndRight == 0) this.runwayEndRight = (int) Math.round( (sideOnPane.getWidth() / 2) + (runwayStripLength / 2));  

		centreLine = paneHeight /2;
	}
	
	public void drawRunway(Runway rw) {
		scale = rw.getRunwaySize() / runwayStripLength;
		
		drawRunwayTD(rw);
		drawRunwaySO(rw);
		
	}
	
	public void drawRunwaySO(Runway rw) {
		sideOnPane.setStyle("-fx-background-color: white;");
    	
		drawRunwaySO();
		drawThresholdSO(rw.getThreshold(),rw.getName());
		drawDirectionSO(rw.getName());
		drawIndicatorSO(rw.getName());
		drawClearwaySO(rw);
		drawStopwaySO(rw);
		
	    drawDistanceSO(true,runwayEndLeft,60,runwayStripLength,"TORA = " + rw.getTORA());
        drawDistanceSO(true,runwayEndLeft,80,runwayStripLength+rw.getStopway()/scale,"ASDA = " + rw.getASDA());
        drawDistanceSO(true,runwayEndLeft,100,runwayStripLength+rw.getClearway()/scale, "TODA = " + rw.getTODA());
        drawDistanceSO(true,thresholdPoint,-40,runwayEndRight-thresholdPoint,"LDA = " + rw.getLDA());
	
        displayLegend(sideOnPane);
	}
	
	public void drawRunwayTD(Runway rw) {
		topDownPane.setStyle("-fx-background-color: white;");
    	
    	
    	drawRunwayTD();
        drawThreshold(rw.getThreshold(),rw.getName());
        drawDirectionTD(rw.getName());
        drawIndicatorTD(rw.getName());
        drawClearwayTD(rw);
        drawStopwayTD(rw);
        
        
        drawDistance(true,runwayEndLeft,60,runwayStripLength,"TORA = " + rw.getTORA());
        drawDistance(true,runwayEndLeft,80,runwayStripLength+rw.getStopway()/scale,"ASDA = " + rw.getASDA());
        drawDistance(true,runwayEndLeft,100,runwayStripLength+rw.getClearway()/scale, "TODA = " + rw.getTODA());
        drawDistance(true,thresholdPoint,-40,runwayEndRight-thresholdPoint,"LDA = " + rw.getLDA());
	
        displayLegend(topDownPane);
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
    
    public void drawDistanceSO(Boolean dir, double startX, double startY, double length, String text) {
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
    	sideOnPane.getChildren().add(arrow);
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

    public void drawThreshold(int threshold, String rwnm) {
	   int dir = Integer.parseInt(rwnm.substring(0, 2));
	   int x;
	   if (dir < 18) x = runwayEndLeft +  (int) Math.round(threshold/scale);
	   else x = runwayEndRight -  (int) Math.abs(Math.round(threshold/scale));
	   
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
    
    public void drawThresholdSO(int threshold, String rwnm) {
    	int dir = Integer.parseInt(rwnm.substring(0, 2));
 	   int x;
 	   if (dir <= 18) x = runwayEndLeft +  (int) Math.round(threshold/scale);
 	   else x = runwayEndRight -  (int) Math.abs(Math.round(threshold/scale));
 	   
 	   thresholdPoint = x;
 	   
 	   Rectangle thresh = new Rectangle(x - 2,paneHeight / 2 - 7, 5, 15);
 	   thresh.setFill(Color.DARKRED);
 	   
 	   sideOnPane.getChildren().add(thresh);
    }
    
    public void drawDirectionTD(String rwnm) {
    	int dir = Integer.parseInt(rwnm.substring(0, 2));
    	
    	int tipX;
    	int wingX;
    	int centreLine = paneHeight / 2;
    	
    	if (dir<=18) {
    		tipX = runwayEndLeft-4;
    		wingX = tipX - 15;
    	} else {
    		tipX = runwayEndRight+4;
    		wingX = tipX + 20;
    	}
    	
    	Line w1 = new Line(tipX,centreLine,wingX,centreLine + 30);
    	w1.setStroke(Color.GREEN);
    	w1.setStrokeWidth(3);
    	
    	Line w2 = new Line(tipX,centreLine,wingX,centreLine - 30);
    	w2.setStroke(Color.GREEN);
    	w2.setStrokeWidth(3);
    		
    	topDownPane.getChildren().addAll(w1,w2);
    	
    }
    
    public void drawDirectionSO(String rwnm) {
    	int dir = Integer.parseInt(rwnm.substring(0, 2));
    	
    	int tipX;
    	int wingX;
    	int centreLine = paneHeight / 2;
    	
    	if (dir<=18) {
    		tipX = runwayEndLeft -7;
    		wingX = tipX - 10;
    	} else {
    		tipX = runwayEndRight+7;
    		wingX = tipX + 10;
    	}
    	
    	Line w1 = new Line(tipX,centreLine,wingX,centreLine + 15);
    	w1.setStroke(Color.GREEN);
    	w1.setStrokeWidth(3);
    	
    	Line w2 = new Line(tipX,centreLine,wingX,centreLine - 15);
    	w2.setStroke(Color.GREEN);
    	w2.setStrokeWidth(3);
    	
    	sideOnPane.getChildren().addAll(w1,w2);
    	
    }
    
    public void drawIndicatorTD(String rwnm) {

    	Text t1 = new Text(thresholdPoint-10,centreLine+40,rwnm.substring(0,2));
    	Text t2 = new Text(thresholdPoint-5,centreLine+55,rwnm.substring(2,3));
    	
    	t1.setFont(indicatorFont);
    	t2.setFont(indicatorFont);
    	
    	t1.setFill(Color.DARKGREEN);
    	t2.setFill(Color.DARKGREEN);
    	
    	t1.setStrokeWidth(2);
    	t2.setStrokeWidth(2);
    
    	topDownPane.getChildren().addAll(t1,t2);
    }

    public void drawIndicatorSO(String rwnm) {

    	Text t1 = new Text(thresholdPoint-10,centreLine+30,rwnm.substring(0,2));
    	Text t2 = new Text(thresholdPoint-5,centreLine+45,rwnm.substring(2,3));
    	
    	t1.setFont(indicatorFont);
    	t2.setFont(indicatorFont);
    	
    	t1.setFill(Color.DARKGREEN);
    	t2.setFill(Color.DARKGREEN);
    	
    	t1.setStrokeWidth(2);
    	t2.setStrokeWidth(2);
    
    	sideOnPane.getChildren().addAll(t1,t2);

    }
    
    public void drawClearwayTD(Runway r) {
    	 int dir = Integer.parseInt(r.getName().substring(0, 2));
    	 double size = r.getClearway() / scale;
    	 System.out.println(size);
    	 
    	 Rectangle cl = new Rectangle();
    	 
    	 if (dir<=18) {
    		 cl.setX(runwayEndRight);
    		 cl.setY(centreLine - runwayStripWidth / 2 - 3);
    		 cl.setWidth(size);
    		 cl.setHeight(runwayStripWidth + 6);
    	 }
    	 else {
    		 cl.setX(runwayEndLeft - size);
    		 cl.setY(centreLine - runwayStripWidth / 2 - 3);
    		 cl.setWidth(size);
    		 cl.setHeight(runwayStripWidth + 6);	 
    	 }
    	 
    	 cl.setFill(clearwayColor);
    	 cl.setStroke(Color.BLACK);
    	 
    	 topDownPane.getChildren().add(cl);
    }
    
    public void drawStopwayTD(Runway r) {
   	 int dir = Integer.parseInt(r.getName().substring(0, 2));
   	 double size = r.getStopway() / scale;
   	 
   	 Rectangle cl = new Rectangle();
   	 
   	 if (dir<=18) {
   		 cl.setX(runwayEndRight);
   		 cl.setY(centreLine - runwayStripWidth / 2);
   		 cl.setWidth(size);
   		 cl.setHeight(runwayStripWidth);
   	 }
   	 else {
   		 cl.setX(runwayEndLeft - size);
   		 cl.setY(centreLine - runwayStripWidth / 2);
   		 cl.setWidth(size);
   		 cl.setHeight(runwayStripWidth);	 
   	 }
   	 
   	 cl.setFill(stopwayColor);
   	 
   	 topDownPane.getChildren().add(cl);
   }
    
    public void drawClearwaySO(Runway r) {
   	 int dir = Integer.parseInt(r.getName().substring(0, 2));
   	 double size = r.getClearway() / scale;
   	 
   	 Rectangle cl = new Rectangle();
   	 
   	 if (dir<=18) {
   		 cl.setX(runwayEndRight);
   		 cl.setY(centreLine - 2.5 -2);
   		 cl.setWidth(size);
   		 cl.setHeight(6 + 4);
   	 }
   	 else {
   		 cl.setX(runwayEndLeft - size);
   		 cl.setY(centreLine - 2.5 - 2);
   		 cl.setWidth(size);
   		 cl.setHeight(6 + 4);	 
   	 }
   	 
   	 cl.setFill(clearwayColor);
   	 cl.setStroke(Color.BLACK);
   	 
   	 sideOnPane.getChildren().add(cl);
   }
    
    public void drawStopwaySO(Runway r) {
      	 int dir = Integer.parseInt(r.getName().substring(0, 2));
      	 double size = r.getStopway() / scale;
      	 
      	 Rectangle cl = new Rectangle();
      	 
      	 if (dir<=18) {
      		 cl.setX(runwayEndRight);
      		 cl.setY(centreLine - 2.5);
      		 cl.setWidth(size);
      		 cl.setHeight(6);
      	 }
      	 else {
      		 cl.setX(runwayEndLeft - size);
      		 cl.setY(centreLine - 2.5);
      		 cl.setWidth(size);
      		 cl.setHeight(6);	 
      	 }
      	 
      	 cl.setFill(stopwayColor);
      	 
      	 sideOnPane.getChildren().add(cl);
      }

    public void displayLegend(Pane target) {
    	Rectangle r1 = new Rectangle(50,50,10,10);
    	r1.setFill(stopwayColor);
    	r1.setStroke(Color.BLACK);
    	
    	Rectangle r2 = new Rectangle(50,80,10,10);
    	r2.setFill(clearwayColor);
    	r2.setStroke(Color.BLACK);
    	
    	
    	Text t1 = new Text(70,60,"Stopway");
    	Text t2 = new Text(70,90,"Clearway");
    	
    	target.getChildren().addAll(r1,r2,t1,t2);
    }
}
