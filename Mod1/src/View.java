/**
 * Created by tobyf on 18/02/2017.
 */
public class View {
    Airport model;

    public View(Airport model) {
        this.model = model;
    }

    public void update(String runwayName){
        Runway runway = model.getRunway(runwayName);
        System.out.println("\nRunway: " + runway.name + "\n"+
                "TORA: " + runway.getTORA() + "\n"+
                "TODA: " + runway.getTODA() + "\n"+
                "ASDA: " + runway.getASDA() + "\n"+
                "LDA: " + runway.getLDA()
        );

        if (runway.getObstacle() != null){
            System.out.println(runway.getAvoidanceStrategy());
        }

    }
}
