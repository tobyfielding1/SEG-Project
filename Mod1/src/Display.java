

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
	private Pane topDownPane;
	private Pane sideOnPane;

	private final Paint arrowColor = Color.RED;
	private final double arrowThickness = 0.5;


	private final int runwayStripLength = 500;
	private final int runwayStripWidth = 40;

	private int runwayEndLeft;
	private int runwayEndRight;

	private int paneHeight;
	private int paneWidth;

	private int thresholdPoint;

	private double scale;

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

	/*
	 Draws side-on view of the runway as well as the slope
	 */
    private void drawRunwaySO(Runway rw) {
		sideOnPane.setStyle("-fx-background-color: white;");

		drawRunwaySO();
		drawSlope(200, 100, 15);
	}

	/*
	 Draws top-down view of the runway as well as necessary distances
	 */
    private void drawRunwayTD(Runway rw) {
		topDownPane.setStyle("-fx-background-color: white;");

    	drawRunwayTD();
        drawThreshold(rw.getThreshold());
        drawDistance(true,thresholdPoint,-30,400,"even longer long text for test");
    }

    /*
     Draws a labelled double-ended arrow between the two specified locations in the pane
     */
    private void drawDistance(Boolean dir, double startX, double startY, double length, String text) {
    	Group arrow;
    	if (dir) {
            arrow = makeHArrow(startX,(paneHeight / 2) + startY,length);
        } else {
            arrow = makeVArrow((paneWidth / 2) + startX,startY,length);
        }

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

    /*
     Draws top-down view of JUST the runway
     */
    private void drawRunwayTD() {
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

    private Group makeHArrow(double x1, double y1, double length) {
    	double x2 = x1 +length;

        Line main = new Line(x1, y1, x2, y1);
    	main.setStroke(arrowColor);
    	main.setStrokeWidth(arrowThickness);

    	Line h1 = new Line(x1,y1,x1+10,y1-10);
    	h1.setStroke(arrowColor);
    	h1.setStrokeWidth(arrowThickness);

    	Line h2 = new Line(x1,y1,x1+10,y1+10);
    	h2.setStroke(arrowColor);
    	h2.setStrokeWidth(arrowThickness);

    	Line h3 = new Line(x2, y1,x2-10, y1 +10);
    	h3.setStroke(arrowColor);
    	h3.setStrokeWidth(arrowThickness);

    	Line h4 = new Line(x2, y1,x2-10, y1 -10);
    	h4.setStroke(arrowColor);
    	h4.setStrokeWidth(arrowThickness);

    	Group arrow = new Group();

    	arrow.getChildren().addAll(main,h1,h2,h3,h4);
    	return arrow;
    }


    private Group makeVArrow(double x1, double y1, double length) {
        double y2 = y1 + length;

    	Line main = new Line(x1, y1, x1, y2);
    	main.setStroke(arrowColor);
    	main.setStrokeWidth(arrowThickness);

    	Line h1 = new Line(x1,y1,x1-10,y1+10);
    	h1.setStroke(arrowColor);
    	h1.setStrokeWidth(arrowThickness);

    	Line h2 = new Line(x1,y1,x1+10,y1+10);
    	h2.setStroke(arrowColor);
    	h2.setStrokeWidth(arrowThickness);

    	Line h3 = new Line(x1,y2, x1 +10,y2-10);
    	h3.setStroke(arrowColor);
    	h3.setStrokeWidth(arrowThickness);

    	Line h4 = new Line(x1,y2, x1 -10,y2-10);
    	h4.setStroke(arrowColor);
    	h4.setStrokeWidth(arrowThickness);

    	Group arrow = new Group();

    	arrow.getChildren().addAll(main,h1,h2,h3,h4);
    	return arrow;
    }

    private void drawThreshold(int threshold) {
	   int x;
	   if (threshold > 0) x = runwayEndLeft +  (int) Math.round(threshold/scale);
	   else x = runwayEndRight +  (int) Math.round(threshold/scale);

	   thresholdPoint = x;

	   Rectangle thresh = new Rectangle(x - 2,paneHeight / 2 - runwayStripWidth / 2, 5, runwayStripWidth);
	   thresh.setFill(Color.DARKSALMON);

	   topDownPane.getChildren().add(thresh);
   }

   /*
    Draws side-on view of JUST the runway
    */
   private void drawRunwaySO() {
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

    private void drawSlope(int topX, int height, int angle) {
    	Polygon tri = new Polygon();

    	double x1 = Math.round(topX/scale) + runwayEndLeft;
    	double y1 = paneHeight/2 - height/scale;
    	double x2 = (double) Math.round(topX/scale) + runwayEndLeft;;
    	double y2 = paneHeight/2;
    	double x3 = x1 + (height / Math.tan(Math.toRadians(angle))) / scale;
    	double y3 = paneHeight/2;

    	tri.getPoints().addAll(x1,y1,x2,y2,x3,y3);
    	tri.setFill(Color.GREEN);

    	tri.setVisible(true);
    	sideOnPane.getChildren().add(tri);
    }
}
