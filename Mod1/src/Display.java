

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Display {
    Pane topDownPane;
    Pane sideOnPane;

    final Paint clearwayColor = Color.YELLOW;
    final Paint stopwayColor = new Color(0.3, 0.3, 1, 0.98);
    final Paint obstacleColor = Color.RED;
    final Paint slopeColor = Color.TURQUOISE;
    
    
    final double arrowThickness = 0.8;
    int runwayPixelWidth;

    final Font indicatorFont = Font.font("Arial Black", FontWeight.BOLD, 18);

    int paneHeight;
    int paneWidth;

    int centreLine;

    Runway rw;
    double scaleDir;
    int xi;

    public Display(Pane td, Pane so) {
        this.topDownPane = td;
        this.sideOnPane = so;

        this.paneHeight = (int) Math.round(topDownPane.getHeight());
        if (this.paneHeight == 0)
            this.paneHeight = (int) Math.round(sideOnPane.getHeight());

        this.paneWidth = (int) Math.round(topDownPane.getWidth());
        if (this.paneWidth == 0)
            this.paneWidth = (int) Math.round(sideOnPane.getWidth());

        centreLine = paneHeight / 2;
    }


    public void drawRunway(Runway rw) {
        topDownPane.setStyle("-fx-background-color: white;");
        sideOnPane.setStyle("-fx-background-color: white;");

        this.rw = rw;

        setValues();

        drawRunway(runwayPixelWidth = 30);

        drawThreshold();
        drawIndicator();
        drawDirection();

        drawClearway();
        drawStopway();

        double TORAEnds[] = drawTORA();
        drawTODA();
        drawASDA();
        double LDAEnds[] = drawLDA();

        if (rw.getObstacle() != null) {
            double XObs = drawObstacle();
            if (rw.getTakeoffStrategy() != null) {
                drawLDADisplacement(LDAEnds, XObs);
                drawTakeoffDisplacement(TORAEnds, XObs);
                drawALSTOCS(XObs);
            }
        }

        displayLegend();
    }

    private double drawObstacle() {
        double x;

        Rectangle obs;
        if (scaleDir > 0)
            obs = new Rectangle(x = (rw.getObstacle().dist1stThresh + (rw.getOriginalTORA() - rw.getOriginalLDA())) * scaleDir + xi - 10, rw.getObstacle().centerlineDist + centreLine - 10, 20, 20);
        else
            obs = new Rectangle(x = (rw.getObstacle().dist2ndThresh + (rw.getOriginalTORA() - rw.getOriginalLDA())) * scaleDir + xi - 10, rw.getObstacle().centerlineDist + centreLine - 10, 20, 20);

        obs.setFill(obstacleColor);
        obs.setStroke(Color.BLACK);
        topDownPane.getChildren().add(obs);

        Rectangle obs2;
        if (scaleDir > 0)
            obs2 = new Rectangle(x, centreLine - rw.getObstacle().height, 20, rw.getObstacle().height);
        else
            obs2 = new Rectangle(x, centreLine - rw.getObstacle().height, 20, rw.getObstacle().height);

        obs2.setFill(obstacleColor);
        obs2.setStroke(Color.BLACK);
        sideOnPane.getChildren().add(obs2);
        return x + 10;
    }


    public void drawRunway(int concreteWidth) {
        Color runwayFill = new Color(0.2, 0.2, 0.2, 0.5);

        Rectangle runwayStrip = Rectangle(xi, centreLine - concreteWidth / 2, xi + rw.getOriginalTORA() * scaleDir, concreteWidth);
        runwayStrip.setFill(runwayFill);
        runwayStrip.setStroke(Color.BLACK);
        topDownPane.getChildren().add(runwayStrip);

        Line centerLine = new Line(xi, this.centreLine, xi + rw.getOriginalTORA() * scaleDir, this.centreLine);
        centerLine.setStroke(Color.WHITE);
        centerLine.getStrokeDashArray().addAll(20.0, 20.0);
        topDownPane.getChildren().add(centerLine);

        runwayStrip = Rectangle(xi, centreLine, xi + rw.getOriginalTORA() * scaleDir, 5);
        runwayStrip.setFill(runwayFill);
        runwayStrip.setStroke(Color.BLACK);
        sideOnPane.getChildren().add(runwayStrip);

        centerLine = new Line(xi, this.centreLine, xi + rw.getOriginalTORA() * scaleDir, this.centreLine);
        centerLine.setStroke(Color.WHITE);
        centerLine.getStrokeDashArray().addAll(20.0, 20.0);
        sideOnPane.getChildren().add(centerLine);
    }

    private Rectangle Rectangle(double end1, double y, double end2, double height) {
        return new Rectangle(Math.min(end1, end2), y, Math.abs(end1 - end2), height);
    }


    public Group makeHArrow(double start, double end, double y, Color arrowColor) {
        double x1;
        double x2;
        double x3;
        double x4;
        double y1 = y;
        double y2 = y;


        if (start < end) {
            x1 = start;
            x2 = end;
        } else {
            x1 = end;
            x2 = start;
        }

        if (scaleDir < 0) {
            x3 = x1 + 10;
            x4 = x2;
        } else {
            x3 = x1;
            x4 = x2 - 10;
        }

        Line main = new Line(x1, y1, x2, y2);
        main.setStroke(arrowColor);
        main.setStrokeWidth(arrowThickness);

        Line h1 = new Line(x1, y1, x3, y1 - 10);
        h1.setStroke(arrowColor);
        h1.setStrokeWidth(arrowThickness);

        Line h2 = new Line(x1, y1, x3, y1 + 10);
        h2.setStroke(arrowColor);
        h2.setStrokeWidth(arrowThickness);

        Line h3 = new Line(x2, y2, x4, y2 + 10);
        h3.setStroke(arrowColor);
        h3.setStrokeWidth(arrowThickness);

        Line h4 = new Line(x2, y2, x4, y2 - 10);
        h4.setStroke(arrowColor);
        h4.setStrokeWidth(arrowThickness);


        Group arrow = new Group();

        arrow.getChildren().addAll(main, h1, h2, h3, h4);
        return arrow;
    }

    public void drawDistance(double startX, double endX, double y, String text, Color color) {
        Group arrow;

        arrow = makeHArrow(startX, endX, (paneHeight / 2) + y, color);

        Text t;
        t = new Text((startX + endX) / 2, (paneHeight / 2) + y - 5, text);
        t.setX(t.getX() - t.getText().length() * 2.65);

        t.setStroke(Color.GRAY);
        t.setStrokeWidth(0.7);

        arrow.getChildren().add(t);
        topDownPane.getChildren().add(arrow);

        Group arrow2;

        arrow2 = makeHArrow(startX, endX, (paneHeight / 2) + y, color);

        Text t2;
        t2 = new Text((startX + endX) / 2, (paneHeight / 2) + y - 5, text);
        t2.setX(t2.getX() - t2.getText().length() * 2.65);

        t2.setStroke(Color.GRAY);
        t2.setStrokeWidth(0.7);

        arrow2.getChildren().add(t2);
        sideOnPane.getChildren().add(arrow2);

    }


    public void drawThreshold() {
        double thresh;
        if (rw.getLandingStrategy() == Runway.AvoidanceStrategy.LANDINGOVER)
            thresh = xi + rw.getThreshold() * scaleDir;
        else
            thresh = xi + (rw.getOriginalTORA() - rw.getOriginalLDA()) * scaleDir;

        Rectangle threshold = new Rectangle(thresh, paneHeight / 2 - runwayPixelWidth / 2, 5, runwayPixelWidth);
        threshold.setFill(Color.DARKSALMON);

        topDownPane.getChildren().add(threshold);

        Rectangle threshold2 = new Rectangle(thresh, paneHeight / 2 - runwayPixelWidth / 2, 5, runwayPixelWidth);

        threshold2.setHeight(threshold.getHeight() / 2);
        threshold2.setY(threshold.getY() + threshold2.getHeight() / 2);
        threshold2.setFill(Color.DARKSALMON);

        sideOnPane.getChildren().add(threshold2);
    }

    public void drawDirection() {
        int tipX;
        int wingX;
        int centreLine = paneHeight / 2;

        if (scaleDir > 0) {
            tipX = xi - 4;
            wingX = tipX - 15;
        } else {
            tipX = xi + 4;
            wingX = tipX + 20;
        }

        Line w1 = new Line(tipX, centreLine, wingX, centreLine + 30);
        w1.setStroke(Color.GREEN);
        w1.setStrokeWidth(3);

        Line w2 = new Line(tipX, centreLine, wingX, centreLine - 30);
        w2.setStroke(Color.GREEN);
        w2.setStrokeWidth(3);

        topDownPane.getChildren().addAll(w1, w2);


        Line w12 = new Line(tipX, centreLine, wingX, centreLine + 10);
        w12.setStroke(Color.GREEN);
        w12.setStrokeWidth(3);

        Line w22 = new Line(tipX, centreLine, wingX, centreLine - 10);
        w22.setStroke(Color.GREEN);
        w22.setStrokeWidth(3);


        sideOnPane.getChildren().addAll(w12, w22);
    }

    public void drawIndicator() {
        double thresh;
        if (rw.getLandingStrategy() == Runway.AvoidanceStrategy.LANDINGOVER)
            thresh = xi + rw.getThreshold() * scaleDir;
        else
            thresh = xi + (rw.getOriginalTORA() - rw.getOriginalLDA()) * scaleDir;

        Text t1 = new Text(thresh - 10, centreLine + 40, rw.getName().substring(0, 2));
        Text t2 = new Text(thresh - 5, centreLine + 55, rw.getName().substring(2, 3));

        t1.setFont(indicatorFont);
        t2.setFont(indicatorFont);

        t1.setFill(Color.DARKGREEN);
        t2.setFill(Color.DARKGREEN);

        t1.setStrokeWidth(2);
        t2.setStrokeWidth(2);

        topDownPane.getChildren().addAll(t1, t2);


        Text t1a = new Text(thresh - 10, centreLine + 30, rw.getName().substring(0, 2));
        Text t2a = new Text(thresh - 5, centreLine + 45, rw.getName().substring(2, 3));

        t1a.setFont(indicatorFont);
        t2a.setFont(indicatorFont);

        t1a.setFill(Color.DARKGREEN);
        t2a.setFill(Color.DARKGREEN);

        t1a.setStrokeWidth(2);
        t2a.setStrokeWidth(2);

        sideOnPane.getChildren().addAll(t1a, t2a);
    }


    public void drawClearway() {
        double size = rw.getClearway() * Math.abs(scaleDir);

        Rectangle cl = new Rectangle();

        if (scaleDir > 0) {
            cl.setX(xi + rw.getOriginalTORA() * scaleDir);
            cl.setY(centreLine - runwayPixelWidth / 2 - 3);
            cl.setWidth(size);
            cl.setHeight(runwayPixelWidth + 6);
        } else {
            cl.setX(xi + rw.getOriginalTORA() * scaleDir - size);
            cl.setY(centreLine - runwayPixelWidth / 2 - 3);
            cl.setWidth(size);
            cl.setHeight(runwayPixelWidth + 6);
        }

        cl.setFill(clearwayColor);
        cl.setStroke(Color.BLACK);

        topDownPane.getChildren().add(cl);

        Rectangle cl2 = new Rectangle();
        cl2.setX(cl.getX());
        cl2.setWidth(cl.getWidth());
        cl2.setHeight(10);
        cl2.setY(cl.getY() + (cl.getHeight() - cl2.getHeight()) / 2);

        cl2.setFill(clearwayColor);
        cl2.setStroke(Color.BLACK);


        sideOnPane.getChildren().add(cl2);

    }

    public void drawStopway() {
        double size = rw.getStopway() * Math.abs(scaleDir);

        Rectangle st = new Rectangle();

        if (scaleDir > 0) {
            st.setX(xi + rw.getOriginalTORA() * scaleDir);
            st.setY(centreLine - runwayPixelWidth / 2);
            st.setWidth(size);
            st.setHeight(runwayPixelWidth);
        } else {
            st.setX(xi + rw.getOriginalTORA() * scaleDir - size);
            st.setY(centreLine - runwayPixelWidth / 2);
            st.setWidth(size);
            st.setHeight(runwayPixelWidth);
        }

        st.setFill(stopwayColor);

        topDownPane.getChildren().add(st);

        Rectangle st2 = new Rectangle();
        st2.setX(st.getX());
        st2.setWidth(st.getWidth());
        st2.setHeight(8);
        st2.setY(st.getY() + (st.getHeight() - st2.getHeight()) / 2);

        st2.setFill(stopwayColor);
        st2.setStroke(Color.BLACK);


        sideOnPane.getChildren().add(st2);

    }


    public void displayLegend() {
        addLegendItem(clearwayColor,"Clearway",60,true);
        addLegendItem(stopwayColor,"Stopway",80,true);
        addLegendItem(obstacleColor,"Obstacle",100,true);
        addLegendItem(slopeColor,"Slope",120,false);
        
        
     }

    public void addLegendItem(Paint color, String name, int y, boolean bothPanels) {
    	Rectangle r1 = new Rectangle(50, y, 10, 10);
        r1.setFill(color);
        r1.setStroke(Color.BLACK);
        
        Rectangle r2 = new Rectangle(50, y, 10, 10);
        r2.setFill(color);
        r2.setStroke(Color.BLACK);


        Text t1 = new Text(70, y+10, name);
        Text t2 = new Text(70, y+10, name);

        if (bothPanels) topDownPane.getChildren().addAll(r1, t1);
        sideOnPane.getChildren().addAll(r2, t2);
    }
    
    public void clearPanes() {
        sideOnPane.getChildren().clear();
        topDownPane.getChildren().clear();
    }

    public void setValues() {
        double endSpace = Math.max(rw.getOriginalTODA() - rw.getOriginalTORA() + 200, 400);
        int x = Integer.parseInt(rw.getName().substring(0, 2));
        scaleDir = this.paneWidth / (rw.getOriginalTORA() + 2 * endSpace);
        xi = (int) (endSpace * scaleDir);
        if (x > 18) {
            scaleDir *= -1;
            xi = this.paneWidth - xi;
        }
    }

    /*
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
    */


    public double[] drawTORA() {
        double end1;
        double end2;

        if (rw.getTakeoffStrategy() == Runway.AvoidanceStrategy.TAKEOFFAWAY) {
            end1 = scaleDir * rw.getTakeoffThreshold() + xi;
            end2 = end1 + scaleDir * rw.getTORA();
        } else {
            end1 = xi;
            end2 = xi + rw.getTORA() * scaleDir;
        }

        drawDistance(end1, end2, 50, "TORA = " + rw.getTORA(), Color.BLACK);
        double a[] = {end1, end2};
        return a;
    }

    ;


    public void drawTODA() {
        double end1;
        double end2;

        if (rw.getTakeoffStrategy() == Runway.AvoidanceStrategy.TAKEOFFAWAY) {
            end1 = scaleDir * rw.getTakeoffThreshold() + xi;
            end2 = end1 + scaleDir * rw.getTODA();
        } else {
            end1 = xi;
            end2 = xi + rw.getTODA() * scaleDir;
        }

        drawDistance(end1, end2, 90, "TODA = " + rw.getTODA(), Color.BLACK);

    }

    public void drawASDA() {
        double end1;
        double end2;

        if (rw.getTakeoffStrategy() == Runway.AvoidanceStrategy.TAKEOFFAWAY) {
            end1 = scaleDir * rw.getTakeoffThreshold() + xi;
            end2 = end1 + scaleDir * rw.getASDA();
        } else {
            end1 = xi;
            end2 = xi + rw.getASDA() * scaleDir;
        }

        drawDistance(end1, end2, 70, "ASDA = " + rw.getASDA(), Color.BLACK);
    }

    public double[] drawLDA() {
        double end1;
        double end2;

        if (rw.getLandingStrategy() == null || rw.getLandingStrategy() == Runway.AvoidanceStrategy.LANDINGOVER) {
            end1 = xi + rw.getThreshold() * scaleDir;
            ;
            end2 = end1 + rw.getLDA() * scaleDir;
        } else {
            end1 = xi + (rw.getOriginalTORA() - rw.getOriginalLDA()) * scaleDir;
            end2 = end1 + rw.getLDA() * scaleDir;
        }

        drawDistance(end1, end2, -30, "LDA = " + rw.getLDA(), Color.BLACK);
        double a[] = {end1, end2};
        return a;
    }

    private void drawALSTOCS(double xObs) {
        Line w;
        if (rw.getLandingStrategy() == Runway.AvoidanceStrategy.LANDINGOVER) {
            w = new Line(xObs, centreLine - rw.getObstacle().height - 2, xObs + rw.getALSTOCSSlope() * rw.getObstacle().height * scaleDir, centreLine);
            w.setStroke(slopeColor);
            w.setStrokeWidth(5);
            sideOnPane.getChildren().add(w);
        }
        if (rw.getTakeoffStrategy() == Runway.AvoidanceStrategy.TAKEOFFOVER) {
            w = new Line(xObs, centreLine - rw.getObstacle().height - 2, xObs - rw.getALSTOCSSlope() * rw.getObstacle().height * scaleDir, centreLine);
            w.setStroke(slopeColor);
            w.setStrokeWidth(5);
            sideOnPane.getChildren().add(w);
        }
    }

    private void drawTakeoffDisplacement(double[] toraEnds, double xObs) {
        double end1;
        double end2;

        if (rw.getTakeoffStrategy() == Runway.AvoidanceStrategy.TAKEOFFAWAY) {
            end1 = xObs;
            end2 = toraEnds[0];
        } else {
            end1 = toraEnds[1];
            end2 = xObs;
        }

        drawDistance(end1, end2, 50, rw.getTakeoffThresholdLabel(), Color.RED);
    }

    private void drawLDADisplacement(double[] ldaEnds, double xObs) {
        double end1;
        double end2;

        if (rw.getLandingStrategy() == Runway.AvoidanceStrategy.LANDINGOVER) {
            end1 = xObs;
            end2 = ldaEnds[0];
        } else {
            end1 = ldaEnds[1];
            end2 = xObs;
        }

        drawDistance(end1, end2, -30, rw.getThresholdLabel(), Color.RED);
    }
}