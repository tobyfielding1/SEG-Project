import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;
/**
 * Created by tobyf on 18/02/2017.
 *
 * this class represents 'Model' in our MVC pattern
 */
public class Airport implements Serializable {

    private final String name;
    private HashMap<String, Runway> runways = new HashMap<>();

    public Airport(String name) {
        this.name = name;
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

    public void removeRunway(String name) {
        runways.remove(name);
    }

    public Runway getRunway(String name) {
        return runways.get(name);
    }

/*
    public void save(File file) {
// Create output stream.
        FileOutputStream fos = new FileOutputStream(file + ".xml");

        // Create XML encoder.
        XMLEncoder xenc = new XMLEncoder(fos);

        // Write object.
        xenc.writeObject(aFoo);
    }
*/
}