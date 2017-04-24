import java.util.HashMap;
import java.util.Set;
/**
 * Created by tobyf on 18/02/2017.
 *
 * this class represents 'Model' in our MVC pattern
 */
public class Airport {

    private String name;
    private HashMap<String, Runway> runways = new HashMap<>();

    private HashMap<String, Obstacle> obstacles = new HashMap<>();

    public Airport(String name) {
        this.name = name;
        obstacles.put("Airliner",new Obstacle("Airliner", 13));
        obstacles.put("Helicopter",new Obstacle("Helicopter", 5));
        obstacles.put("Small Jet",new Obstacle("Small Jet", 6));
        obstacles.put("HGV",new Obstacle("HGV", 6));
        obstacles.put("Car",new Obstacle("Car", 2));
    }
    
    public String getName() {
		return name;
	}

	public Set<String> getRunwayIDs() {
        return runways.keySet();
    }

    public void addRunway(Runway runway) {
        runways.put(runway.getName(), runway);
    }

    public void addObstacle(Obstacle o) {
        obstacles.put(o.getId(), o);
    }

    public void removeRunway(String name) {
        runways.remove(name);
    }

    public Runway getRunway(String name) {
        return runways.get(name);
    }

    public Airport() {
        obstacles.put("Airliner",new Obstacle("Airliner", 13));
        obstacles.put("Helicopter",new Obstacle("Helicopter", 5));
        obstacles.put("Small Jet",new Obstacle("Small Jet", 6));
        obstacles.put("HGV",new Obstacle("HGV", 6));
        obstacles.put("Car",new Obstacle("Car", 2));
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setObstacles(HashMap<String, Obstacle> obstacles) {
        this.obstacles = obstacles;
    }

    public void setRunways(HashMap<String, Runway> runways) {
        this.runways = runways;
    }

    public HashMap<String, Runway> getRunways() {
        return runways;

    }

    public HashMap<String, Obstacle> getObstacles() {
        return obstacles;

    }
}