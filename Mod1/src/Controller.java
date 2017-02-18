/**
 * Created by tobyf on 18/02/2017.
 */
public class Controller {

    Airport model;
    View view;

    public static void main(String[]args){
        Controller controller = new Controller();
        controller.model = new Airport("Airport 1");
        controller.view = new View(controller.model);
        controller.model.addRunway(new Runway("09L",3902,3902,3902,3595));
        controller.model.addRunway(new Runway("27R",3884,3962,3884,3884));
        Obstacle o = new Obstacle("tree",2500,500, 60, 25,240);
        controller.view.update("09L");
        controller.view.update("27R");
        controller.model.addObstacle("09L", o);
        controller.model.addObstacle("27R", o);
        controller.view.update("09L");
        controller.view.update("27R");
    }
}
