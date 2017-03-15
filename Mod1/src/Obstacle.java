import java.util.ArrayList;

public class Obstacle {

    static ArrayList<String> types;

    final String id;
    private Integer dist1stThresh;
    private Integer dist2ndThresh;
    final int centerlineDist;
    final int height;

    public Obstacle(String id, Integer dist1stThresh, Integer dist2ndThresh, int centerlineDist, int height) {
        this.id = id;
        this.dist1stThresh = dist1stThresh;
        this.dist2ndThresh = dist2ndThresh;
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
