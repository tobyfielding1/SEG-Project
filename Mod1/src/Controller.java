import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.PageOrientation;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class Controller extends Application {

    Airport airport;

    @FXML
	public TabPane runwayTabs;

    public MenuItem filePrintMenu;

    public Stage primaryStage;

    public TextField toraInputField;
    public TextField todaInputField;
    public TextField asdaInputField;
    public TextField ldaInputField;
    public TextField rNameInputField;
    public Button create;

    public Tab addRunway;

    // Input value text fields
    public TextField airportName;//ldaInputField;

    // Bottom info bar
    public TextField additionalInfoBar;

    // For interaction between controller and GUI
    @FXML
    private MenuBar menu;
    @FXML
    private CheckMenuItem viewAlwaysShowLegend;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
		Parent root = FXMLLoader.load(getClass().getResource("RRTGUI.fxml"));
		primaryStage.setTitle("Runway Redeclaration Tool");
		primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
		primaryStage.show();
	}

	private void createAndSelectNewTab(final TabPane tabPane, final String title) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RWTAB.fxml"));
        RunwayController rwController = new RunwayController(airport.getRunway(title), this);
        loader.setController(rwController);
        rwController.setAlwaysShowLegend(viewAlwaysShowLegend.isSelected());
        Tab tab = new Tab(title);
        try {
            tab.setContent(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        filePrintMenu.addEventHandler(ActionEvent.ACTION,
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if(tab.isSelected())
                            rwController.print();
                    }
                });

		final ObservableList<Tab> tabs = tabPane.getTabs();
		tab.closableProperty().setValue(true);
		tabs.add(tabs.size() - 1, tab);
		tabPane.getSelectionModel().select(tab);
    }

    /*
     Close option in MenuBar -> File -> Close
     */
    @FXML
    protected void fileCloseMenuAction() {
        // get a handle to the stage
        Stage stage = (Stage) menu.getScene().getWindow();
        // do what you have to do
        stage.close();
    }


    /*
     Shows ReadMe file to user
     */
    @FXML
    protected void helpViewHelpAction() throws IOException {
        Alert readme = new Alert(Alert.AlertType.INFORMATION);
        readme.setResizable(true);
        readme.setTitle("Help");
        readme.setHeaderText(null);

        TextArea textArea = new TextArea("Ensure that the computer running the redeclaration tool is running Java 1.8.\n" +
                "\n" +
                "To ensure the program runs with no errors two text files must first be created \"runway1.txt\" and \"obstacle1.txt\". The format of these text files must be in a comma separated values (csv) format.\n" +
                "\n" +
                "The layout for \"runway1.txt\" should be in the form :\n" +
                "\n" +
                "Runway Name: String , TORA : integer , TODA : integer, ASDA : integer, LDA : integer\n" +
                "\n" +
                "An example of \"runway1.txt\" would be:\n" +
                "\n" +
                "09L,3902,4500,4200,3595\n" +
                "\n" +
                "The layout for \"obstacle1.txt\" should be in the form:\n" +
                "\n" +
                "Obstacle Type : String, Distance from threshold 1 : integer , Distance from threshold 2 : integer , Distance from centre line : integer , Obstacle height : integer , Obstacle RESA : integer\n" +
                "\n" +
                "An example of \"obstacle1.txt\" would be: \n" +
                "\n" +
                "plane,50,3646,10,12,240\n" +
                "\n" +
                "Once these two files have been created, ensure they are in the same folder as the .jar file for the redeclaration application.\n" +
                "\n" +
                "Double click on the \"increment1.jar\" file to open the redeclaration tool application. \n" +
                "\n" +
                "Launch the application and to read the two text files and see a visualisation of the runway and its values, click the \"Draw\" button.   \n" +
                "\n");
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        readme.getDialogPane().setContent(textArea);
        readme.showAndWait();
    }

    @FXML
    protected void createAirportAction() throws IOException {
        airport = new Airport(airportName.getText());
        addRunway.disableProperty().setValue(false);
        runwayTabs.getTabs().remove(0);

        additionalInfoBar.setText("Airport created successfully");
    }

    //creates new runway
    @FXML
    protected void createAction(){
        try {
            String name = rNameInputField.getText();
            int tora = Integer.parseInt(toraInputField.getText());
            int toda = Integer.parseInt(todaInputField.getText());
            int asda = Integer.parseInt(asdaInputField.getText());
            int lda = Integer.parseInt(ldaInputField.getText());

            if (!Pattern.matches("^(0[1-9]|[1-2][0-9]|3[0-6])[ORLC]?$", name)) {
                throw new Exception("Invalid runway designator: name must be in the form of a number between 01 and 36, possibly followed by a L, R or C");
            }

            if (tora < 720) {
                throw new Exception("Invalid TORA value: must be at least 720.");
            }

            if (toda < tora) {
                throw new Exception("Invalid TODA value: must be at least as big as TORA (" + tora + ").");
            }

            if (asda < tora) {
                throw new Exception("Invalid ASDA value: must be at least as big as TORA (" + tora + ").");
            }

            if (asda > toda) {
                throw new Exception("Invalid ASDA value: must be at most as big as TODA (" + toda + ").");
            }

            if (lda > tora) {
                throw new Exception("Invalid LDA value: must be at most as big as TORA (" + tora + ").");
            }

            airport.addRunway(new Runway(name, tora, toda, asda, lda));
            createAndSelectNewTab(runwayTabs, rNameInputField.getText());
            rNameInputField.clear();
            toraInputField.clear();
            asdaInputField.clear();
            ldaInputField.clear();
            todaInputField.clear();

            additionalInfoBar.setText("Runway added successfully");
        } catch (NumberFormatException e) {
            additionalInfoBar.setText("Invalid runway value: look for non-number characters in the TORA, TODA, ASDA, and LDA fields.");
        } catch (Exception e) {
            additionalInfoBar.setText(e.getMessage());
        }
    }


    protected void saveToFile(Node pan, String fileName, String extension) {
        BufferedImage bi = new BufferedImage(511, 640, BufferedImage.TYPE_INT_RGB);
        BufferedImage image = javafx.embed.swing.SwingFXUtils.fromFXImage(pan.snapshot(new SnapshotParameters(), null), bi);
        Graphics2D gd = (Graphics2D) image.getGraphics();
        File file = new File(fileName + extension);
        try {
            ImageIO.write(image, extension.substring(1), file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void printToPrinter(Node pan) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            if (job.showPrintDialog(null)) {
                if (job.getJobSettings().getPageLayout().getPageOrientation().equals(PageOrientation.PORTRAIT)) {
                    pan.getTransforms().add(new Scale(0.45, 0.45));
                    pan.getTransforms().add(new Translate(-50, 0));
                } else {
                    pan.getTransforms().add(new Scale(0.75, 0.75));
                    pan.getTransforms().add(new Translate(150, 300));
                }
            }
            boolean printsucc = job.printPage(pan);
            if (printsucc) {
                job.endJob();
                pan.getTransforms().clear();
            }
        }
    }
}
