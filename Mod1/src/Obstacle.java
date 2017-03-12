import java.util.ArrayList;

public class Obstacle {

    static ArrayList<String> types;

    final String id;
    final int dist1stThresh;
    final int dist2ndThresh;
    final int centerlineDist;
    final int height;

    public Obstacle(String id, int dist1stThresh, int dist2ndThresh, int centerlineDist, int height) {
        this.id = id;
        this.dist1stThresh = dist1stThresh;
        this.dist2ndThresh = dist2ndThresh;
        this.centerlineDist = centerlineDist;
        this.height = height;
    }
}
