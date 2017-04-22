

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class Display {
    private Pane topDownPane;
    private Pane sideOnPane;

    final Paint clearwayColor = Color.YELLOW;
    final Paint stopwayColor = new Color(0.3, 0.3, 1, 0.98);
    final Paint obstacleColor = Color.RED;
    final Paint slopeColor = Color.TURQUOISE;
    final Paint areaColor = Color.LIGHTGRAY;

    final double arrowThickness = 0.8;

    public static final Paint CLEARWAY_COLOR = Color.YELLOW;
    public static final Paint STOPWAY_COLOR = new Color(0.3, 0.3, 1, 0.98);
    public static final Paint OBSTACLE_COLOR = Color.RED;
    public static final Paint SLOPE_COLOR = Color.TURQUOISE;

    private final double ARROW_THICKNESS = 0.8;
    private int runwayPixelWidth;

    private final Font INDICATOR_FONT = Font.font("Arial Black", FontWeight.BOLD, 18);

    private int paneHeight;
    private int paneWidth;

    private int centreLine;

    private Runway rw;
    private double scaleDir;
    private int xi;
    
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
        topDownPane.setStyle("-fx-background-color: #e0ebe0;");
        sideOnPane.setStyle("-fx-background-color: white;");

        this.rw = rw;

        setValues();

        drawArea(rw);
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

        drawCompass(rw, Color.BLACK);

        if (rw.getObstacle() != null) {
            double XObs = drawObstacle();
            if (rw.getTakeoffStrategy() != null) {
                drawLDADisplacement(LDAEnds, XObs);
                drawTakeoffDisplacement(TORAEnds, XObs);
                drawALSTOCS(XObs);
                sideOnPane.getChildren().get(sideOnPane.getChildren().size() - 1).toBack();
            }
        }
    }

    private double drawObstacle() {
        double x;

        Rectangle obs;
        if (scaleDir > 0)
            obs = new Rectangle(x = (rw.getObstacle().getDist1stThresh() + (rw.getOriginalTORA() - rw.getOriginalLDA())) * scaleDir + xi - 10, rw.getObstacle().centerlineDist + centreLine - 10, 20, 20);
        else
            obs = new Rectangle(x = (rw.getObstacle().getDist2ndThresh() + (rw.getOriginalTORA() - rw.getOriginalLDA())) * scaleDir + xi - 10, rw.getObstacle().centerlineDist + centreLine - 10, 20, 20);

        obs.setFill(OBSTACLE_COLOR);
        obs.setStroke(Color.BLACK);
        topDownPane.getChildren().add(obs);

        Rectangle obs2;
        if (scaleDir > 0)
            obs2 = new Rectangle(x, centreLine - rw.getObstacle().height, 20, rw.getObstacle().height);
        else
            obs2 = new Rectangle(x, centreLine - rw.getObstacle().height, 20, rw.getObstacle().height);

        obs2.setFill(OBSTACLE_COLOR);
        obs2.setStroke(Color.BLACK);
        sideOnPane.getChildren().add(obs2);
        return x + 10;
    }


    private void drawRunway(int concreteWidth) {
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


    private Group makeHArrow(double start, double end, double y, Color arrowColor) {
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
        main.setStrokeWidth(ARROW_THICKNESS);

        Line h1 = new Line(x1, y1, x3, y1 - 10);
        h1.setStroke(arrowColor);
        h1.setStrokeWidth(ARROW_THICKNESS);

        Line h2 = new Line(x1, y1, x3, y1 + 10);
        h2.setStroke(arrowColor);
        h2.setStrokeWidth(ARROW_THICKNESS);

        Line h3 = new Line(x2, y2, x4, y2 + 10);
        h3.setStroke(arrowColor);
        h3.setStrokeWidth(ARROW_THICKNESS);

        Line h4 = new Line(x2, y2, x4, y2 - 10);
        h4.setStroke(arrowColor);
        h4.setStrokeWidth(ARROW_THICKNESS);


        Group arrow = new Group();

        arrow.getChildren().addAll(main, h1, h2, h3, h4);
        return arrow;
    }

    private void drawDistance(double startX, double endX, double y, String text, Color color) {
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


    private void drawThreshold() {
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

    private void drawDirection() {
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

    private void drawIndicator() {
        double thresh;
        if (rw.getLandingStrategy() == Runway.AvoidanceStrategy.LANDINGOVER)
            thresh = xi + rw.getThreshold() * scaleDir;
        else
            thresh = xi + (rw.getOriginalTORA() - rw.getOriginalLDA()) * scaleDir;

        Text t1 = new Text(thresh - 10, centreLine + 40, rw.getName().substring(0, 2));
        Text t2;
        try {
            t2 = new Text(thresh - 5, centreLine + 55, rw.getName().substring(2, 3));
        } catch (Exception e) {
            t2 = new Text();
        }

        t1.setFont(INDICATOR_FONT);
        t2.setFont(INDICATOR_FONT);

        t1.setFill(Color.DARKGREEN);
        t2.setFill(Color.DARKGREEN);

        t1.setStrokeWidth(2);
        t2.setStrokeWidth(2);

        topDownPane.getChildren().addAll(t1, t2);

        t1 = new Text(thresh - 10, centreLine + 30, rw.getName().substring(0, 2));
        try {
            t2 = new Text(thresh - 5, centreLine + 45, rw.getName().substring(2, 3));
        } catch (Exception e) {
            t2 = new Text();
        }

        t1.setFont(INDICATOR_FONT);
        t2.setFont(INDICATOR_FONT);

        t1.setFill(Color.DARKGREEN);
        t2.setFill(Color.DARKGREEN);

        t1.setStrokeWidth(2);
        t2.setStrokeWidth(2);

        sideOnPane.getChildren().addAll(t1, t2);
    }


    private void drawClearway() {
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

        cl.setFill(CLEARWAY_COLOR);
        cl.setStroke(Color.BLACK);

        topDownPane.getChildren().add(cl);

        Rectangle cl2 = new Rectangle();
        cl2.setX(cl.getX());
        cl2.setWidth(cl.getWidth());
        cl2.setHeight(14);
        cl2.setY(cl.getY() + (cl.getHeight() - cl2.getHeight()) / 2);

        cl2.setFill(CLEARWAY_COLOR);
        cl2.setStroke(Color.BLACK);


        sideOnPane.getChildren().add(cl2);

    }

    private void drawStopway() {
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

        st.setFill(STOPWAY_COLOR);

        topDownPane.getChildren().add(st);

        Rectangle st2 = new Rectangle();
        st2.setX(st.getX());
        st2.setWidth(st.getWidth());
        st2.setHeight(8);
        st2.setY(st.getY() + (st.getHeight() - st2.getHeight()) / 2);

        st2.setFill(STOPWAY_COLOR);
        st2.setStroke(Color.BLACK);


        sideOnPane.getChildren().add(st2);

    }

    public void clearPanes() {
        sideOnPane.getChildren().clear();
        topDownPane.getChildren().clear();
    }

    private void setValues() {
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


    private double[] drawTORA() {
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
        return new double[]{end1, end2};
    }

    private void drawTODA() {
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

    private void drawASDA() {
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

    private double[] drawLDA() {
        double end1;
        double end2;

        if (rw.getLandingStrategy() == null || rw.getLandingStrategy() == Runway.AvoidanceStrategy.LANDINGOVER) {
            end1 = xi + rw.getThreshold() * scaleDir;
            end2 = end1 + rw.getLDA() * scaleDir;
        } else {
            end1 = xi + (rw.getOriginalTORA() - rw.getOriginalLDA()) * scaleDir;
            end2 = end1 + rw.getLDA() * scaleDir;
        }

        drawDistance(end1, end2, -30, "LDA = " + rw.getLDA(), Color.BLACK);
        return new double[]{end1, end2};
    }

    private void drawALSTOCS(double xObs) {
        Line w;
        if (rw.getLandingStrategy() == Runway.AvoidanceStrategy.LANDINGOVER) {
            w = new Line(xObs, centreLine - rw.getObstacle().height - 2, xObs + rw.getALSTOCSSlope() * rw.getObstacle().height * scaleDir, centreLine);
            w.setStroke(SLOPE_COLOR);
            w.setStrokeWidth(5);
            sideOnPane.getChildren().add(w);
        }
        if (rw.getTakeoffStrategy() == Runway.AvoidanceStrategy.TAKEOFFOVER) {
            w = new Line(xObs, centreLine - rw.getObstacle().height - 2, xObs - rw.getALSTOCSSlope() * rw.getObstacle().height * scaleDir, centreLine);
            w.setStroke(SLOPE_COLOR);
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

    public Polygon makeArea(double x1, double x2, double x3, double x4, double x5, double x6, double y1, double y2) {
        Polygon area = new Polygon();
        ArrayList<Double> points = new ArrayList<>();

        points.add(x1);
        points.add(centreLine - y1 / 2);

        points.add(x2);
        points.add(centreLine - y1 / 2);

        points.add(x3);
        points.add(centreLine - y2 / 2);

        points.add(x4);
        points.add(centreLine - y2 / 2);

        points.add(x5);
        points.add(centreLine - y1 / 2);

        points.add(x6);
        points.add(centreLine - y1 / 2);

        points.add(x6);
        points.add(centreLine + y1 / 2);

        points.add(x5);
        points.add(centreLine + y1 / 2);

        points.add(x4);
        points.add(centreLine + y2 / 2);

        points.add(x3);
        points.add(centreLine + y2 / 2);

        points.add(x2);
        points.add(centreLine + y1 / 2);

        points.add(x1);
        points.add(centreLine + y1 / 2);

        area.getPoints().addAll(points);

        area.setFill(areaColor);
        area.setStroke(Color.BLACK);
        area.setStrokeWidth(0.1);

        return area;
    }

    public void drawArea(Runway r) {
        //two ways this method can be used:

        // 1. Input lengths of segments here:
        double startX = xi - 60 * scaleDir; // (left) starting point of clear and grated area
        double length1 = (150 + 60) * scaleDir; // length of first segment
        double length2 = 150 * scaleDir; // length of second segment
        double length3 = rw.getOriginalTORA() * scaleDir - length2 * 4; // length of third segment
        double length4 = length2; // length of fourth segment
        double length5 = length1; // length of fifth segment

        //then widths:
        double width1 = 75 * 2; // small width
        double width2 = 105 * 2; // large width

        // 2. Input X coordinates of each segment beggining/end (first 6 values) and then the widths
        Polygon area = makeArea(startX, startX + length1, startX + length1 + length2, startX + length1 + length2 + length3, startX + length1 + length2 + length3 + length4, startX + length1 + length2 + length3 + length4 + length5, width1, width2);
        topDownPane.getChildren().add(area);
    }

    public void drawCompass(Runway r, Color arrowColor) {
        //draw compass relative to runway

        //get main line of arrow start and end points
        double angle = Math.toRadians((Integer.parseInt(r.getName().substring(0,2)) - 9) * 10);
        if ((Integer.parseInt(r.getName().substring(0,2))) > 18) {
            angle = angle - Math.toRadians(180);
        }
        int x1 = 700;
        int y1 = 60;
        //define main part of arrow using 2 lines
        double x2 = x1 - 20*Math.sin(angle);
        double y2 = y1 - 20*Math.cos(angle);
        double xhalf2 = x1 - 20*Math.sin(angle + Math.toRadians(180));
        double yhalf2 = y1 - 20*Math.cos(angle + Math.toRadians(180));


        //get end points of arrow wings
        double x3 =  x2 + 10*Math.sin(angle + 0.785398);
        double y3 =  y2 + 10*Math.cos(angle + 0.785398);
        double x4 =  x2 + 10*Math.sin(angle + 5.49779);
        double y4 =  y2 + 10*Math.cos(angle + 5.49779);

        //define line parameters
        Line main = new Line(x1, y1, x2, y2);
        main.setStroke(arrowColor);
        main.setStrokeWidth(ARROW_THICKNESS);

        Line mainHalf2 = new Line(x1, y1, xhalf2, yhalf2);
        mainHalf2.setStroke(arrowColor);
        mainHalf2.setStrokeWidth(ARROW_THICKNESS);

        Line wing1 = new Line(x2, y2, x3, y3);
        wing1.setStroke(arrowColor);
        wing1.setStrokeWidth(ARROW_THICKNESS);

        Line wing2 = new Line(x2, y2, x4, y4);
        wing2.setStroke(arrowColor);
        wing2.setStrokeWidth(ARROW_THICKNESS);

        //add text to tell the user that arrow points north
        Text t;
        t = new Text(650, 30, "Arrow Points North");

        //add arrow and text to panel
        topDownPane.getChildren().addAll(main, mainHalf2, wing1, wing2, t);
    }

    public void rotate() {
        if (topDownPane.getRotate() == 0) {
            int rotateAngle = (Integer.parseInt(rw.getName().substring(0,2)) - 9) * 10;
            if ((Integer.parseInt(rw.getName().substring(0,2))) > 18) {
                rotateAngle = rotateAngle - 180;
            }
            topDownPane.setRotate(rotateAngle);
        } else {
            topDownPane.setRotate(0);
        }
    }
}