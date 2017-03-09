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

public class Display {
    private Pane topDownPane;
    private Pane sideOnPane;

    private final Paint ARROW_COLOR = Color.RED;
    private final Paint CLEARWAY_COLOR = Color.YELLOW;
    private final Paint STOPWAY_COLOR = new Color(0.3, 0.3, 1, 0.98);
    private final double ARROW_THICKNESS = 0.5;


    private final int RUNWAY_STRIP_LENGTH = 400;
    private final int RUNWAY_STRIP_WIDTH = 40;

    private final Font INDICATOR_FONT = Font.font("Arial Black", FontWeight.BOLD, 18);

    private int runwayEndLeft;
    private int runwayEndRight;

    private int paneHeight;
    private int paneWidth;

    private int thresholdPoint;
    private int centreLine;

    private double scale;


    public Display(Pane td, Pane so) {
        this.topDownPane = td;
        this.sideOnPane = so;

        this.paneHeight = (int) Math.round(topDownPane.getHeight());
        if (this.paneHeight == 0)
            this.paneHeight = (int) Math.round(sideOnPane.getHeight());

        this.paneWidth = (int) Math.round(topDownPane.getWidth());
        if (this.paneWidth == 0)
            this.paneWidth = (int) Math.round(sideOnPane.getWidth());

        this.runwayEndLeft = (int) Math.round((topDownPane.getWidth() / 2) - (RUNWAY_STRIP_LENGTH / 2));
        if (this.runwayEndLeft == 0)
            this.runwayEndLeft = (int) Math.round((sideOnPane.getWidth() / 2) - (RUNWAY_STRIP_LENGTH / 2));

        this.runwayEndRight = (int) Math.round((topDownPane.getWidth() / 2) + (RUNWAY_STRIP_LENGTH / 2));
        if (this.runwayEndRight == 0)
            this.runwayEndRight = (int) Math.round((sideOnPane.getWidth() / 2) + (RUNWAY_STRIP_LENGTH / 2));

        centreLine = paneHeight / 2;
    }

    /*
     Draws both views of runway
     */
    public void drawRunway(Runway rw) {
        scale = rw.getOriginalTORA() / RUNWAY_STRIP_LENGTH;

        drawRunwayTD(rw);
        drawRunwaySO(rw);

    }

    /*
     Draws side-on view of a runway with indicators
     */
    private void drawRunwaySO(Runway rw) {
        sideOnPane.setStyle("-fx-background-color: white;");

        drawRunwaySO();
        drawThresholdSO(rw.getThreshold(), rw.getName());
        drawDirectionSO(rw.getName());
        drawIndicatorSO(rw.getName());
        drawClearwaySO(rw);
        drawStopwaySO(rw);

        int dir = Integer.parseInt(rw.getName().substring(0, 2));


        if (dir <= 18) {
            drawDistanceSO(true, runwayEndLeft, 60, RUNWAY_STRIP_LENGTH, "TORA = " + rw.getTORA());
            drawDistanceSO(true, runwayEndLeft, 80, RUNWAY_STRIP_LENGTH + rw.getStopway() / scale, "ASDA = " + rw.getASDA());
            drawDistanceSO(true, runwayEndLeft, 100, RUNWAY_STRIP_LENGTH + rw.getClearway() / scale, "TODA = " + rw.getTODA());
            drawDistanceSO(true, thresholdPoint, -40, runwayEndRight - thresholdPoint, "LDA = " + rw.getLDA());
        } else {
            drawDistanceSO(true, runwayEndLeft + RUNWAY_STRIP_LENGTH - ((double) rw.getTORA() / (double) rw.getOriginalTORA()) * (double) RUNWAY_STRIP_LENGTH, 60, ((double) rw.getTORA() / (double) rw.getOriginalTORA()) * (double) RUNWAY_STRIP_LENGTH, "TORA = " + rw.getTORA());
            drawDistanceSO(true, runwayEndLeft - rw.getStopway() / scale + RUNWAY_STRIP_LENGTH - ((double) rw.getASDA() / (double) rw.getOriginalTORA()) * (double) RUNWAY_STRIP_LENGTH, 80, ((double) rw.getTORA() / (double) rw.getOriginalTORA()) * (double) RUNWAY_STRIP_LENGTH + rw.getStopway() / scale, "ASDA = " + rw.getASDA());
            drawDistanceSO(true, runwayEndLeft - rw.getClearway() / scale + RUNWAY_STRIP_LENGTH - ((double) rw.getTODA() / (double) rw.getOriginalTORA()) * (double) RUNWAY_STRIP_LENGTH, 100, ((double) rw.getTODA() / (double) rw.getOriginalTORA()) * (double) RUNWAY_STRIP_LENGTH + rw.getClearway() / scale, "TODA = " + rw.getTODA());
            drawDistanceSO(true, runwayEndLeft, -40, thresholdPoint - runwayEndLeft, "LDA = " + rw.getLDA());

        }
        displayLegend(sideOnPane, rw);
    }

    /*
     Draws top-down view of a runway with indicators
     */
    private void drawRunwayTD(Runway rw) {
        topDownPane.setStyle("-fx-background-color: white;");


        drawRunwayTD();
        drawThresholdTD(rw.getThreshold(), rw.getName());
        drawDirectionTD(rw.getName());
        drawIndicatorTD(rw.getName());
        drawClearwayTD(rw);
        drawStopwayTD(rw);


        int dir = Integer.parseInt(rw.getName().substring(0, 2));

        if (dir <= 18) {
            drawDistanceTD(true, runwayEndLeft, 60, RUNWAY_STRIP_LENGTH, "TORA = " + rw.getTORA());
            drawDistanceTD(true, runwayEndLeft, 80, RUNWAY_STRIP_LENGTH + rw.getStopway() / scale, "ASDA = " + rw.getASDA());
            drawDistanceTD(true, runwayEndLeft, 100, RUNWAY_STRIP_LENGTH + rw.getClearway() / scale, "TODA = " + rw.getTODA());
            drawDistanceTD(true, thresholdPoint, -40, runwayEndRight - thresholdPoint, "LDA = " + rw.getLDA());
        } else {


            drawDistanceTD(true, runwayEndLeft + RUNWAY_STRIP_LENGTH - ((double) rw.getTORA() / (double) rw.getOriginalTORA()) * (double) RUNWAY_STRIP_LENGTH, 60, ((double) rw.getTORA() / (double) rw.getOriginalTORA()) * (double) RUNWAY_STRIP_LENGTH, "TORA = " + rw.getTORA());
            drawDistanceTD(true, runwayEndLeft - rw.getStopway() / scale + RUNWAY_STRIP_LENGTH - ((double) rw.getASDA() / (double) rw.getOriginalTORA()) * (double) RUNWAY_STRIP_LENGTH, 80, ((double) rw.getTORA() / (double) rw.getOriginalTORA()) * (double) RUNWAY_STRIP_LENGTH + rw.getStopway() / scale, "ASDA = " + rw.getASDA());
            drawDistanceTD(true, runwayEndLeft - rw.getClearway() / scale + RUNWAY_STRIP_LENGTH - ((double) rw.getTODA() / (double) rw.getOriginalTORA()) * (double) RUNWAY_STRIP_LENGTH, 100, ((double) rw.getTODA() / (double) rw.getOriginalTORA()) * (double) RUNWAY_STRIP_LENGTH + rw.getClearway() / scale, "TODA = " + rw.getTODA());
            drawDistanceTD(true, runwayEndLeft, -40, thresholdPoint - runwayEndLeft, "LDA = " + rw.getLDA());

        }
        displayLegend(topDownPane, rw);
    }

    /*
     Draws an arrow of a given size on the side-on view
     */
    private void drawDistanceSO(Boolean dir, double startX, double startY, double length, String text) {
        Group arrow;
        if (dir) arrow = makeHArrow(startX, (paneHeight / 2) + startY, length);
        else arrow = makeVArrow((paneWidth / 2) + startX, startY, length);

        Text t;
        if (dir) {
            t = new Text(startX + (length / 2), (paneHeight / 2) + startY - 5, text);
            t.setX(t.getX() - t.getText().length() * 2.65);
        } else t = new Text((paneWidth / 2) + startX + 3, startY + (length / 2), text);

        t.setStroke(Color.GRAY);
        t.setStrokeWidth(0.7);

        arrow.getChildren().add(t);
        sideOnPane.getChildren().add(arrow);
    }

    /*
     Draws an arrow of a given size on the top-down view
     */
    private void drawDistanceTD(Boolean dir, double startX, double startY, double length, String text) {
        Group arrow;
        if (dir) arrow = makeHArrow(startX, (paneHeight / 2) + startY, length);
        else arrow = makeVArrow((paneWidth / 2) + startX, startY, length);

        Text t;
        if (dir) {
            t = new Text(startX + (length / 2), (paneHeight / 2) + startY - 5, text);
            t.setX(t.getX() - t.getText().length() * 2.65);
        } else t = new Text((paneWidth / 2) + startX + 3, startY + (length / 2), text);

        t.setStroke(Color.GRAY);
        t.setStrokeWidth(0.7);

        arrow.getChildren().add(t);
        topDownPane.getChildren().add(arrow);
    }

    /*
     Draws the side-on view of the runway strip
     */
    private void drawRunwaySO() {
        double x = (sideOnPane.getWidth() / 2) - (RUNWAY_STRIP_LENGTH / 2);
        double y = (sideOnPane.getHeight() / 2) - 2;


        Color runwayFill = new Color(0.2, 0.2, 0.2, 0.5);

        Rectangle runwayStrip = new Rectangle(x, y, RUNWAY_STRIP_LENGTH, 5);
        runwayStrip.setFill(runwayFill);
        runwayStrip.setStroke(Color.BLACK);
        sideOnPane.getChildren().add(runwayStrip);

        Line centerLine = new Line(x, y + 2, x + RUNWAY_STRIP_LENGTH, y + 2);
        centerLine.setStroke(Color.WHITE);
        centerLine.getStrokeDashArray().addAll(20.0, 20.0);
        sideOnPane.getChildren().add(centerLine);
    }

    /*
     Draws the top-down view of the runway strip
     */
    private void drawRunwayTD() {
        double x = (topDownPane.getWidth() / 2) - (RUNWAY_STRIP_LENGTH / 2);
        double y = (topDownPane.getHeight() / 2) - (RUNWAY_STRIP_WIDTH / 2);


        Color runwayFill = new Color(0.2, 0.2, 0.2, 0.5);

        Rectangle runwayStrip = new Rectangle(x, y, RUNWAY_STRIP_LENGTH, RUNWAY_STRIP_WIDTH);
        runwayStrip.setFill(runwayFill);
        runwayStrip.setStroke(Color.BLACK);
        topDownPane.getChildren().add(runwayStrip);

        Line centerLine = new Line(x, y + (RUNWAY_STRIP_WIDTH / 2), x + RUNWAY_STRIP_LENGTH, y + (RUNWAY_STRIP_WIDTH / 2));
        centerLine.setStroke(Color.WHITE);
        centerLine.getStrokeDashArray().addAll(20.0, 20.0);
        topDownPane.getChildren().add(centerLine);
    }

    /*
     Draws a threshold at a given distance on the side-on view of a given runway
     */
    private void drawThresholdSO(int threshold, String rwnm) {
        int dir = Integer.parseInt(rwnm.substring(0, 2));
        int x;
        if (dir <= 18) x = runwayEndLeft + (int) Math.round(threshold / scale);
        else x = runwayEndRight - (int) Math.abs(Math.round(threshold / scale));

        thresholdPoint = x;

        Rectangle thresh = new Rectangle(x - 2, paneHeight / 2 - 7, 5, 15);
        thresh.setFill(Color.DARKRED);

        sideOnPane.getChildren().add(thresh);
    }

    /*
     Draws a threshold at a given distance on the top-down view of a given runway
     */
    private void drawThresholdTD(int threshold, String runwayName) {
        int dir = Integer.parseInt(runwayName.substring(0, 2));
        int x;
        if (dir < 18) x = runwayEndLeft + (int) Math.round(threshold / scale);
        else x = runwayEndRight - (int) Math.abs(Math.round(threshold / scale));


        thresholdPoint = x;

        Rectangle thresh = new Rectangle(x - 2, paneHeight / 2 - RUNWAY_STRIP_WIDTH / 2, 5, RUNWAY_STRIP_WIDTH);
        thresh.setFill(Color.DARKSALMON);

        topDownPane.getChildren().add(thresh);
    }

    /*
     Draws the arrow in the side-on view to show the direction of landing/take-off on a given runway
     */
    private void drawDirectionSO(String rwnm) {
        int dir = Integer.parseInt(rwnm.substring(0, 2));

        int tipX;
        int wingX;
        int centreLine = paneHeight / 2;

        if (dir <= 18) {
            tipX = runwayEndLeft - 7;
            wingX = tipX - 10;
        } else {
            tipX = runwayEndRight + 7;
            wingX = tipX + 10;
        }

        Line w1 = new Line(tipX, centreLine, wingX, centreLine + 15);
        w1.setStroke(Color.GREEN);
        w1.setStrokeWidth(3);

        Line w2 = new Line(tipX, centreLine, wingX, centreLine - 15);
        w2.setStroke(Color.GREEN);
        w2.setStrokeWidth(3);

        sideOnPane.getChildren().addAll(w1, w2);

    }

    /*
     Draws the arrow in the top-down view to show the direction of landing/take-off on a given runway
     */
    private void drawDirectionTD(String rwnm) {
        int dir = Integer.parseInt(rwnm.substring(0, 2));

        int tipX;
        int wingX;
        int centreLine = paneHeight / 2;

        if (dir <= 18) {
            tipX = runwayEndLeft - 4;
            wingX = tipX - 15;
        } else {
            tipX = runwayEndRight + 4;
            wingX = tipX + 20;
        }

        Line w1 = new Line(tipX, centreLine, wingX, centreLine + 30);
        w1.setStroke(Color.GREEN);
        w1.setStrokeWidth(3);

        Line w2 = new Line(tipX, centreLine, wingX, centreLine - 30);
        w2.setStroke(Color.GREEN);
        w2.setStrokeWidth(3);

        topDownPane.getChildren().addAll(w1, w2);

    }

    /*
     Draws the name of a threshold in the side-on view
     */
    private void drawIndicatorSO(String rwnm) {

        Text t1 = new Text(thresholdPoint - 10, centreLine + 30, rwnm.substring(0, 2));
        Text t2 = new Text(thresholdPoint - 5, centreLine + 45, rwnm.substring(2, 3));

        t1.setFont(INDICATOR_FONT);
        t2.setFont(INDICATOR_FONT);

        t1.setFill(Color.DARKGREEN);
        t2.setFill(Color.DARKGREEN);

        t1.setStrokeWidth(2);
        t2.setStrokeWidth(2);

        sideOnPane.getChildren().addAll(t1, t2);

    }

    /*
     Draws the name of a threshold in the top-down view
     */
    private void drawIndicatorTD(String rwnm) {

        Text t1 = new Text(thresholdPoint - 10, centreLine + 40, rwnm.substring(0, 2));
        Text t2 = new Text(thresholdPoint - 5, centreLine + 55, rwnm.substring(2, 3));

        t1.setFont(INDICATOR_FONT);
        t2.setFont(INDICATOR_FONT);

        t1.setFill(Color.DARKGREEN);
        t2.setFill(Color.DARKGREEN);

        t1.setStrokeWidth(2);
        t2.setStrokeWidth(2);

        topDownPane.getChildren().addAll(t1, t2);
    }

    /*
     Draws a runway's clearway in the side-on view
     */
    private void drawClearwaySO(Runway r) {
        int dir = Integer.parseInt(r.getName().substring(0, 2));
        double size = r.getClearway() / scale;

        Rectangle cw = new Rectangle();

        if (dir <= 18) {
            cw.setX(runwayEndRight);
            cw.setY(centreLine - 2.5 - 2);
            cw.setWidth(size);
            cw.setHeight(6 + 4);
        } else {
            cw.setX(runwayEndLeft - size);
            cw.setY(centreLine - 2.5 - 2);
            cw.setWidth(size);
            cw.setHeight(6 + 4);
        }

        cw.setFill(CLEARWAY_COLOR);
        cw.setStroke(Color.BLACK);

        sideOnPane.getChildren().add(cw);
    }

    /*
     Draws a runway's stopway in the side-on view
     */
    private void drawStopwaySO(Runway r) {
        int dir = Integer.parseInt(r.getName().substring(0, 2));
        double size = r.getStopway() / scale;

        Rectangle sw = new Rectangle();

        if (dir <= 18) {
            sw.setX(runwayEndRight);
            sw.setY(centreLine - 2.5);
            sw.setWidth(size);
            sw.setHeight(6);
        } else {
            sw.setX(runwayEndLeft - size);
            sw.setY(centreLine - 2.5);
            sw.setWidth(size);
            sw.setHeight(6);
        }

        sw.setFill(STOPWAY_COLOR);

        sideOnPane.getChildren().add(sw);
    }

    /*
     Draws a runway's clearway in the top-down view
     */
    private void drawClearwayTD(Runway r) {
        int dir = Integer.parseInt(r.getName().substring(0, 2));
        double size = r.getClearway() / scale;

        Rectangle cw = new Rectangle();

        if (dir <= 18) {
            cw.setX(runwayEndRight);
            cw.setY(centreLine - RUNWAY_STRIP_WIDTH / 2 - 3);
            cw.setWidth(size);
            cw.setHeight(RUNWAY_STRIP_WIDTH + 6);
        } else {
            cw.setX(runwayEndLeft - size);
            cw.setY(centreLine - RUNWAY_STRIP_WIDTH / 2 - 3);
            cw.setWidth(size);
            cw.setHeight(RUNWAY_STRIP_WIDTH + 6);
        }

        cw.setFill(CLEARWAY_COLOR);
        cw.setStroke(Color.BLACK);

        topDownPane.getChildren().add(cw);
    }

    /*
     Draws a runway's stopway in the top-down view
     */
    private void drawStopwayTD(Runway r) {
        int dir = Integer.parseInt(r.getName().substring(0, 2));
        double size = r.getStopway() / scale;

        Rectangle sw = new Rectangle();

        if (dir <= 18) {
            sw.setX(runwayEndRight);
            sw.setY(centreLine - RUNWAY_STRIP_WIDTH / 2);
            sw.setWidth(size);
            sw.setHeight(RUNWAY_STRIP_WIDTH);
        } else {
            sw.setX(runwayEndLeft - size);
            sw.setY(centreLine - RUNWAY_STRIP_WIDTH / 2);
            sw.setWidth(size);
            sw.setHeight(RUNWAY_STRIP_WIDTH);
        }

        sw.setFill(STOPWAY_COLOR);

        topDownPane.getChildren().add(sw);
    }

    /*
     Draws the landing/take-off slope on the side-on view
     */
    private void drawSlope(int topX, int height, int angle) {
        Polygon tri = new Polygon();


        double x1 = Math.round(topX / scale) + runwayEndLeft;
        double y1 = paneHeight / 2 - height / scale;
        double x2 = (double) Math.round(topX / scale) + runwayEndLeft;
        ;
        double y2 = paneHeight / 2;
        double x3 = x1 + (height / Math.tan(Math.toRadians(angle))) / scale;
        double y3 = paneHeight / 2;

        tri.getPoints().addAll(new Double[]{x1, y1, x2, y2, x3, y3});
        tri.setFill(Color.GREEN);

        tri.setVisible(true);
        sideOnPane.getChildren().add(tri);
    }

    /*
     Draws the legend on a given pane, showing what different indicators mean
     */
    private void displayLegend(Pane target, Runway rw) {

        if (rw.getStopway() > 0) {
            Rectangle r1 = new Rectangle(50, 50, 10, 10);
            r1.setFill(STOPWAY_COLOR);
            r1.setStroke(Color.BLACK);
            Text t1 = new Text(70, 60, "Stopway");
            target.getChildren().addAll(r1, t1);
        }

        if (rw.getClearway() > 0) {
            Rectangle r2 = new Rectangle(50, 80, 10, 10);
            r2.setFill(CLEARWAY_COLOR);
            r2.setStroke(Color.BLACK);
            Text t2 = new Text(70, 90, "Clearway");
            target.getChildren().addAll(r2, t2);
        }
    }

    /*
     Returns a group of lines making the shape of a horizontal arrow with the given parameters
     */
    private Group makeHArrow(double x1, double y1, double length) {
        double x2 = x1 + length;

        Line main = new Line(x1, y1, x2, y1);
        main.setStroke(ARROW_COLOR);
        main.setStrokeWidth(ARROW_THICKNESS);

        Line h1 = new Line(x1, y1, x1 + 10, y1 - 10);
        h1.setStroke(ARROW_COLOR);
        h1.setStrokeWidth(ARROW_THICKNESS);

        Line h2 = new Line(x1, y1, x1 + 10, y1 + 10);
        h2.setStroke(ARROW_COLOR);
        h2.setStrokeWidth(ARROW_THICKNESS);

        Line h3 = new Line(x2, y1, x2 - 10, y1 + 10);
        h3.setStroke(ARROW_COLOR);
        h3.setStrokeWidth(ARROW_THICKNESS);

        Line h4 = new Line(x2, y1, x2 - 10, y1 - 10);
        h4.setStroke(ARROW_COLOR);
        h4.setStrokeWidth(ARROW_THICKNESS);


        Group arrow = new Group();

        arrow.getChildren().addAll(main, h1, h2, h3, h4);
        return arrow;
    }

    /*
     Returns a group of lines making the shape of a vertical arrow with the given parameters
     */
    private Group makeVArrow(double x1, double y1, double length) {
        double y2 = y1 + length;

        Line main = new Line(x1, y1, x1, y2);
        main.setStroke(ARROW_COLOR);
        main.setStrokeWidth(ARROW_THICKNESS);


        Line h1 = new Line(x1, y1, x1 - 10, y1 + 10);
        h1.setStroke(ARROW_COLOR);
        h1.setStrokeWidth(ARROW_THICKNESS);

        Line h2 = new Line(x1, y1, x1 + 10, y1 + 10);
        h2.setStroke(ARROW_COLOR);
        h2.setStrokeWidth(ARROW_THICKNESS);

        Line h3 = new Line(x1, y2, x1 + 10, y2 - 10);
        h3.setStroke(ARROW_COLOR);
        h3.setStrokeWidth(ARROW_THICKNESS);


        Line h4 = new Line(x1, y2, x1 - 10, y2 - 10);
        h4.setStroke(ARROW_COLOR);
        h4.setStrokeWidth(ARROW_THICKNESS);


        Group arrow = new Group();

        arrow.getChildren().addAll(main, h1, h2, h3, h4);
        return arrow;

    }

    /*
     Clears the side-on and top-down panes
     */
    public void clearPanes() {
        sideOnPane.getChildren().clear();
        topDownPane.getChildren().clear();
    }

}
