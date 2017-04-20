import java.util.ArrayList;

public class Obstacle {

    static ArrayList<String> types;

    private String runways = "";

    public String getRunways() {
        return runways;
    }

    public void setRunways(String runways) {
        this.runways = runways;
    }

    private String id;
    protected Integer dist1stThresh = null;

    public static ArrayList<String> getTypes() {
        return types;
    }

    public String getId() {
        return id;
    }

    public static void setTypes(ArrayList<String> types) {
        Obstacle.types = types;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCenterlineDist(Integer centerlineDist) {
        this.centerlineDist = centerlineDist;
    }

    public void setHeight(int height) {
        this.height = height;

    }

    public Integer getCenterlineDist() {
        return centerlineDist;

    }

    public int getHeight() {
        return height;
    }

    public Obstacle() {


    }

    protected Integer dist2ndThresh = null;
    public Integer centerlineDist = null;
    public int height;

    public Obstacle(String id, int height) {
        this.id = id;
        this.height = height;
    }

    public Obstacle(String id, Integer dist1stThresh, Integer dist2ndThresh, int centerlineDist, int height) {
        this.id = id;

        if (dist1stThresh == null) {
            this.dist1stThresh = 0;
        } else {
            this.dist1stThresh = dist1stThresh;
        }

        if (dist2ndThresh == null) {
            this.dist2ndThresh = 0;
        } else {
            this.dist2ndThresh = dist2ndThresh;
        }

        this.centerlineDist = centerlineDist;
        this.height = height;
    }

    public void setDist1stThresh(Integer dist1stThresh) {
        this.dist1stThresh = dist1stThresh;
    }

    public void setDist2ndThresh(Integer dist2ndThresh) {
        this.dist2ndThresh = dist2ndThresh;
    }

    public Integer getDist1stThresh() {
        return dist1stThresh;
    }

    public Integer getDist2ndThresh() {
        return dist2ndThresh;
    }
}
