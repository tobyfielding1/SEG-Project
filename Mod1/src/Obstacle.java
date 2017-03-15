import java.util.ArrayList;

public class Obstacle {

	static ArrayList<String> types;

	final String id;
	protected Integer dist1stThresh;
	protected Integer dist2ndThresh;
	final int centerlineDist;
	final int height;

	public Obstacle(String id, Integer dist1stThresh, Integer dist2ndThresh, int centerlineDist, int height) {
		this.id = id;
		if (dist1stThresh ==null){
			this.dist1stThresh=0;
		}else{
			this.dist1stThresh = dist1stThresh;
		}
		if (dist2ndThresh ==null){
			this.dist2ndThresh =0;
		}
		else{
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
