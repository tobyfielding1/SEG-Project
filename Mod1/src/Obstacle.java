import java.util.ArrayList;

/**
 * Created by tobyf on 17/02/2017.
 */
public class Obstacle {

    static ArrayList<String> types;

    final String id;
    final int dist1stThresh;
    final int dist2ndThresh;
    final int centerlineDist;
    final int height;

    /**``
     * @param dist1stThresh
     * @param dist2ndThresh
     * @param centerlineDist
     * @param height
     */

    public Obstacle(String id, int dist1stThresh, int dist2ndThresh, int centerlineDist, int height) {
        this.id =  id;
        this.dist1stThresh = dist1stThresh;
        this.dist2ndThresh = dist2ndThresh;
        this.centerlineDist = centerlineDist;
        this.height = height;
    }
}
