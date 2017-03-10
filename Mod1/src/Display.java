

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
	boolean low;
	int dir;
	int xi;
	
	
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
		topDownPane.setStyle("-fx-background-color: white;");
		sideOnPane.setStyle("-fx-background-color: white;");
		
		setValues(rw);
		
		drawRunwaySO();
		drawRunwayTD();
		
		drawThreshold(rw.getThreshold(),rw.getName());
		drawIndicator(rw);
		drawDirection();
		
		drawClearway(rw);
		drawStopway(rw);
		
		drawTORA(rw);
		drawTODA(rw);
		drawASDA(rw);
		
		
        displayLegend(topDownPane);
        displayLegend(sideOnPane);
        
	}
    

	
    public void drawRunwayTD() {
    	double x = (topDownPane.getWidth() / 2) - (runwayStripLength/2);
    	double y = (topDownPane.getHeight() / 2) - (runwayStripWidth/2);
    	
    	
    	Color runwayFill = new Color(0.2, 0.2, 0.2, 0.5);
  		
  		Rectangle runwayStrip = new Rectangle(x,y,runwayStripLength,runwayStripWidth); new Rectangle
  		runwayStrip.setFill(runwayFill);
  		runwayStrip.setStroke(Color.BLACK);
  		topDownPane.getChildren().add(runwayStrip);topDownPane.getChildren().add()
  		
  		Line centerLine = new Line(x, y + (runwayStripWidth/2) , x + runwayStripLength, y + (runwayStripWidth/2));
  		centerLine.setStroke(Color.WHITE);
  		centerLine.getStrokeDashArray().addAll(20.0,20.0);
  		topDownPane.getChildren().add(centerLine);
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

    private Rectangle Rectangle(double y,double end1,double end2, double height){
	    return new Rectangle((end1+end2)/2,y,Math.abs(end1-end2),height);
    }

   
	public Group makeHArrow(double start, double end, double y) {
	    	double x1;
	    	double x2;
	    	double y1 = y;
	    	double y2 = y;
	    	
	    	
	    	if (start<end) {
	    		x1 = start;
	    		x2 = end;
	    	} else {
	    		x1 = end;
	    		x2 = start;
	    	}
	    	
	    	
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
    public void drawDistance(double startX, double endX, double y, String text) {
    	Group arrow;
    	
    	arrow = makeHArrow(startX,endX,(paneHeight/2) + y);
    	
    	Text t;
    	t = new Text((startX+endX) / 2,(paneHeight / 2) + y - 5,text);
        t.setX(t.getX() - t.getText().length() *2.65);
    	
    	t.setStroke(Color.GRAY);
    	t.setStrokeWidth(0.7);
    	
    	arrow.getChildren().add(t);
    	topDownPane.getChildren().add(arrow);
    
    	Group arrow2;
    	
    	arrow2 = makeHArrow(startX,endX,(paneHeight/2) + y);
    	
    	Text t2;
    	t2 = new Text((startX+endX) / 2,(paneHeight / 2) + y - 5,text);
        t2.setX(t2.getX() - t2.getText().length() *2.65);
    	
    	t2.setStroke(Color.GRAY);
    	t2.setStrokeWidth(0.7);
    	
    	arrow2.getChildren().add(t2);
    	sideOnPane.getChildren().add(arrow2);
    
    }
 
    
    public void drawThreshold(int threshold, String rwnm) {
    	int x;
    	if (low)
	    x = runwayEndLeft +  (int) Math.round(threshold/scale);
    	else x = runwayEndRight -  (int) Math.abs(Math.round(threshold/scale));
	   
	   
    	thresholdPoint = x;
	   
    	Rectangle thresh = new Rectangle(x - 2,paneHeight / 2 - runwayStripWidth / 2, 5, runwayStripWidth);
    	thresh.setFill(Color.DARKSALMON);
	   
    	topDownPane.getChildren().add(thresh);
	   
    	Rectangle thresh2 = new Rectangle(x - 2,paneHeight / 2 - runwayStripWidth / 2, 5, runwayStripWidth);
    	
    	thresh2.setHeight(thresh.getHeight()/2);
    	thresh2.setY(thresh.getY() + thresh2.getHeight()/2);
    	thresh2.setFill(Color.DARKSALMON);
 	   
    	
    	sideOnPane.getChildren().add(thresh2);
    }    

    public void drawDirection() {
    	int tipX;
    	int wingX;
    	int centreLine = paneHeight / 2;
    	
    	if (low) {
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
    	
    	
    	Line w12 = new Line(tipX,centreLine,wingX,centreLine + 10);
    	w12.setStroke(Color.GREEN);
    	w12.setStrokeWidth(3);
    	
    	Line w22 = new Line(tipX,centreLine,wingX,centreLine - 10);
    	w22.setStroke(Color.GREEN);
    	w22.setStrokeWidth(3);
    		
    	
    	sideOnPane.getChildren().addAll(w12,w22);
    }   
    public void drawIndicator(Runway r) {
    	
    	String rwnm = r.getName();
    	Text t1 = new Text(thresholdPoint-10,centreLine+40,rwnm.substring(0,2));
    	Text t2 = new Text(thresholdPoint-5,centreLine+55,rwnm.substring(2,3));
    	
    	t1.setFont(indicatorFont);
    	t2.setFont(indicatorFont);
    	
    	t1.setFill(Color.DARKGREEN);
    	t2.setFill(Color.DARKGREEN);
    	
    	t1.setStrokeWidth(2);
    	t2.setStrokeWidth(2);
    	
    	topDownPane.getChildren().addAll(t1,t2);
    	

    	Text t1a = new Text(thresholdPoint-10,centreLine+30,rwnm.substring(0,2));
    	Text t2a = new Text(thresholdPoint-5,centreLine+45,rwnm.substring(2,3));
    	
    	t1a.setFont(indicatorFont);
    	t2a.setFont(indicatorFont);
    	
    	t1a.setFill(Color.DARKGREEN);
    	t2a.setFill(Color.DARKGREEN);
    	
    	t1a.setStrokeWidth(2);
    	t2a.setStrokeWidth(2);
    
    	sideOnPane.getChildren().addAll(t1a,t2a);
    }

    
    
    public void drawClearway(Runway r) {
    	 double size = r.getClearway() / scale;
    	 
    	 Rectangle cl = new Rectangle();
    	 
    	 if (low) {
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
    	 
    	 Rectangle cl2 = new Rectangle();
    	 cl2.setX(cl.getX());
    	 cl2.setWidth(cl.getWidth());
    	 cl2.setHeight(10);
    	 cl2.setY(cl.getY()+(cl.getHeight() - cl2.getHeight())/2);
    	 
    	 cl2.setFill(clearwayColor);
    	 cl2.setStroke(Color.BLACK);
    	 
    	 
    	 
    	 sideOnPane.getChildren().add(cl2);
    	 
    }        
    public void drawStopway(Runway r) {
      	 double size = r.getStopway() / scale;
      	 
      	 Rectangle st = new Rectangle();
      	 
      	 if (low) {
      		 st.setX(runwayEndRight);
      		 st.setY(centreLine - runwayStripWidth / 2);
      		 st.setWidth(size);
      		 st.setHeight(runwayStripWidth);
      	 }
      	 else {
      		 st.setX(runwayEndLeft - size);
      		 st.setY(centreLine - runwayStripWidth / 2);
      		 st.setWidth(size);
      		 st.setHeight(runwayStripWidth);	 
      	 }
      	 
      	 st.setFill(stopwayColor);
      	 
      	 topDownPane.getChildren().add(st);
      
    	 Rectangle st2 = new Rectangle();
    	 st2.setX(st.getX());
    	 st2.setWidth(st.getWidth());
    	 st2.setHeight(8);
    	 st2.setY(st.getY()+(st.getHeight() - st2.getHeight())/2);
    	 
    	 st2.setFill(stopwayColor);
    	 st2.setStroke(Color.BLACK);
    	 
    	 
    	 
    	 sideOnPane.getChildren().add(st2);
    
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

    public void clearPanes() {
    	sideOnPane.getChildren().clear();
    	topDownPane.getChildren().clear();
    }
    
    public void setValues(Runway r){
    	int x = Integer.parseInt(r.getName().substring(0, 2));
    	this.low = (x<=18);
    	this.scale = r.getOriginalTORA() / runwayStripLength;
    	if (low) {
    		dir = 1;
    	} else {
    		dir = -1;
    	}
    	
    	xi = runwayEndLeft;
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
    
    
    public void drawTORA(Runway r) {
    	double end1;
    	double end2;
    	
    	if (r.getAvoidanceStrategy()==Runway.AvoidanceStrategy.LANDINGOVER_TAKEOFFAWAY) {
    		end1 = (dir*r.getTakeoffThreshold()/scale) + xi;
    	} else {
    		end1 = xi;
    	}
    	
    	end2 = end1 + (r.getTORA() * dir / scale);
    	
    	System.out.println(end1);
    	System.out.println(end2);
    	
    	drawDistance(end1,end2,50,"TORA = " + r.getTORA());
    };  

    
    public void drawTODA( Runway r) {
    	double end1;
    	double end2;
    	
    	if (r.getAvoidanceStrategy()==Runway.AvoidanceStrategy.LANDINGOVER_TAKEOFFAWAY) {
    		end1 = (dir*r.getTakeoffThreshold()/scale) + xi;
    	} else {
    		end1 = xi;
    	}
    	
    	end2 = end1 + (r.getTODA() * dir / scale);
    	
    	System.out.println(end1);
    	System.out.println(end2);
    	
    	drawDistance(end1,end2,90,"TODA = " + r.getTODA());
    
    }
    
    public void drawASDA(Runway r) {
    	double end1;
    	double end2;
    	
    	if (r.getAvoidanceStrategy()==Runway.AvoidanceStrategy.LANDINGOVER_TAKEOFFAWAY) {
    		end1 = (dir*r.getTakeoffThreshold()/scale) + xi;
    	} else {
    		end1 = xi;
    	}
    	
    	end2 = end1 + (r.getASDA() * dir / scale);
    	
    	System.out.println(end1);
    	System.out.println(end2);
    	
    	drawDistance(end1,end2,70,"ASDA = " + r.getASDA());
    
    }
}