import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by tobyf on 18/02/2017.
 *
 * this class represents 'Model' in our MVC pattern
 */
public class Airport {

    final String name;
    private HashMap<String, Runway> runways = new HashMap<String, Runway>();

    public Airport(String name) {
        this.name = name;
    }

    public Set<String> getRunwayIDs() {
        return runways.keySet();
    }

    public void addRunway(Runway runway) {
        runways.put(runway.name,runway);
    }

    public void removeRunway(String name) {
        runways.remove(name);
    }

    public Runway getRunway(String name){
        return runways.get(name);
    }

    public void addObstacle(String runwayName, Obstacle obstacle){
        runways.get(runwayName).setObstacle(obstacle);
    }
}
