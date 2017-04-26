import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.Optional;
import java.util.regex.Pattern;

import static javafx.scene.control.Alert.AlertType;

public class Controller extends Application {

    Airport airport;
    ComboBox<String> expBox;

    final FileChooser fileChooser = new FileChooser();

    @FXML
	public TabPane runwayTabs;

    public MenuItem filePrintMenu,saveImage,m1,m2,m3,m4;


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

    public static void configureFileChooser(
            final FileChooser fileChooser) {
        fileChooser.setTitle("Choose an XML file");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.dir"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML", "*.xml")
        );
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

        tab.setOnCloseRequest(new EventHandler<javafx.event.Event>()
        {
            @Override
            public void handle(javafx.event.Event event)
            {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Close Runway");
                alert.setHeaderText("You have chosen to remove a Runway");
                alert.setContentText("Are you sure you wish to delete it from this Airport?");

                ButtonType buttonTypeTwo = new ButtonType("Remove Runway");
                ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(buttonTypeTwo,buttonTypeCancel);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonTypeTwo) {
                    tabPane.getTabs().remove(tab);
                    airport.removeRunway(tab.getText());
                }else{
                    event.consume();
                }
            }
        });

        filePrintMenu.addEventHandler(ActionEvent.ACTION,
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if(tab.isSelected())
                            rwController.print();
                    }
                });


        saveImage.addEventHandler(ActionEvent.ACTION,
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if(tab.isSelected())
                            rwController.saveFile();
                    }
                });

        viewAlwaysShowLegend.addEventHandler(ActionEvent.ACTION,
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                            rwController.toggleLegend();
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
        stage.fireEvent(
                new WindowEvent(
                        stage,
                        WindowEvent.WINDOW_CLOSE_REQUEST
                )
        );
    }


    /*
     Shows ReadMe file to user
     */
    @FXML
    protected void helpViewHelpAction() throws IOException {
        Alert readme = new Alert(AlertType.INFORMATION);
        readme.setResizable(true);
        readme.setTitle("Help");
        readme.setHeaderText(null);

        TextArea textArea = new TextArea();
        
        try {
        	FileReader fl = new FileReader("help.txt");
        	BufferedReader br = new BufferedReader(fl);
        	
        	String help = "";
        	String ln = br.readLine();
        	
        	
        	
        	while (ln != null) {
        		help += "\n";
        		help += ln;
        		ln = br.readLine();
        	}
        	
        	textArea.setText(help);
        	
        } catch (Exception e) {
        	textArea.setText("There was a problem loading the help file.");
        }
        
        
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
        enableMenuAction();
    }


    @FXML
    protected void enableMenuAction(){
        menu.setVisible(true);
        Stage stage = (Stage) menu.getScene().getWindow();
        // do what you have to do
        stage.setTitle(stage.getTitle() + " - " + airport.getName());
        stage.setOnCloseRequest(new EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent event)
            {
                event.consume();
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Quit");
                alert.setHeaderText("Any unsaved changes to your Airport will be lost");
                alert.setContentText("Do you wish to save before quitting?");

                ButtonType buttonTypeOne = new ButtonType("Save and Quit");
                ButtonType buttonTypeTwo = new ButtonType("Quit without saving");
                ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(buttonTypeOne,buttonTypeTwo,buttonTypeCancel);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonTypeOne) {
                    exportAirportAction();
                    stage.close();
                }else if (result.get() == buttonTypeTwo){stage.close();}
            }
        });
    }

    @FXML
    protected void switchAirportAction(){
        Stage stage = (Stage) menu.getScene().getWindow();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Switch Airport");
        alert.setHeaderText("You have chosen to switch Airports");
        alert.setContentText("Would you like to export this Airport before switching?");

        ButtonType buttonTypeOne = new ButtonType("Save and switch");
        ButtonType buttonTypeTwo = new ButtonType("Switch without saving");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne,buttonTypeTwo,buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonTypeOne) {
            exportAirportAction();
            stage.close();
            Platform.runLater(() -> {
                try {
                    new Controller().start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else if (result.get() == buttonTypeTwo){
            stage.close();
            Platform.runLater(() -> {
                try {
                    new Controller().start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }


    @FXML
    protected void openAirportAction(){
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(this.primaryStage);
        if (file != null) {
            XMLDecoder decoder =
                    null;
            try {
                decoder = new XMLDecoder(new BufferedInputStream(
                        new FileInputStream(file)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                if (airport == null){
                    airport = (Airport) decoder.readObject();
                    addRunway.disableProperty().setValue(false);
                    runwayTabs.getTabs().remove(0);
                }

                else if (runwayTabs.getTabs().size()-1 > 0){
                    airport = (Airport) decoder.readObject();
                    runwayTabs.getTabs().remove(0,runwayTabs.getTabs().size()-1);
                }

                for(Runway rw: airport.getRunways().values())
                    createAndSelectNewTab(runwayTabs, rw.getName());

                additionalInfoBar.setText("Airport imported successfully");

            }catch(Exception e1) {
                decoder.close();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("The file you want to import is of the wrong type");
                alert.setContentText("Airports files are named with the airport name only, runways use 'airport - designator', and obstacles use 'airport - obstacleName'.");
                alert.showAndWait();
                openAirportAction();
            }
            enableMenuAction();

        }
    }


    @FXML
    protected void openAction(){
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(this.primaryStage);
        if (file != null) {
            XMLDecoder decoder =
                    null;
            try {
                decoder = new XMLDecoder(new BufferedInputStream(
                        new FileInputStream(file)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Runway rw = (Runway) decoder.readObject();
                decoder.close();
                airport.addRunway(rw);
                createAndSelectNewTab(runwayTabs, rw.getName());
                additionalInfoBar.setText("Runway imported successfully");
            }catch(Exception e1) {
                decoder.close();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("The file you want to import is of the wrong type");
                alert.setContentText("Airports files are named with the airport name only, runways use 'airport - designator', and obstacles use 'airport - obstacleName'.");
                alert.showAndWait();
                openAction();
            }

        }
    }

    @FXML
    protected void exportAirportAction(){
        configureFileChooser(fileChooser);
        fileChooser.setInitialFileName(airport.getName());
        File file = fileChooser.showSaveDialog(this.primaryStage);
        if (file != null) {
            try {
                FileOutputStream fos = new FileOutputStream(file);

                // Create XML encoder.
                XMLEncoder xenc = new XMLEncoder(new BufferedOutputStream(fos));

                // Write object.
                xenc.writeObject(airport);
                xenc.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        fileChooser.setInitialFileName("");
    }

    @FXML
    protected void exportRunwayAction(){
        configureFileChooser(fileChooser);
        fileChooser.setInitialFileName(airport.getName() + "-" + runwayTabs.getSelectionModel().getSelectedItem().getText());
        File file = fileChooser.showSaveDialog(this.primaryStage);
        if (file != null) {
            try {
                FileOutputStream fos = new FileOutputStream(file);

                // Create XML encoder.
                XMLEncoder xenc = new XMLEncoder(new BufferedOutputStream(fos));

                Runway r = airport.getRunway(runwayTabs.getSelectionModel().getSelectedItem().getText());
                r.clearObstacle();

                // Write object.
                xenc.writeObject(r);
                xenc.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        fileChooser.setInitialFileName("");
    }

    @FXML
    protected void saveFile(Node pan){
        BufferedImage bi = new BufferedImage(511, 640, BufferedImage.TYPE_INT_RGB);
        BufferedImage image = javafx.embed.swing.SwingFXUtils.fromFXImage(pan.snapshot(new SnapshotParameters(), null), bi);
        Graphics2D gd = (Graphics2D) image.getGraphics();
        FileChooser fc = new FileChooser();
        fc.setTitle("Save Image");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("png file",".png"), new FileChooser.ExtensionFilter("jpeg file",".jpg"), new FileChooser.ExtensionFilter("gif file",".gif"));
        File file = fc.showSaveDialog(null);
        if (file != null) {
            try {
                ImageIO.write(image, "png", file);
            } catch (IOException ex) {
            }
        }
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

            if (!Pattern.matches("^(0[1-9]|[1-2][0-9]|3[0-6])[LRC]?$", name)) {
                throw new Exception("Invalid runway designator: name must be in the form of a number between 01 and 36, possibly followed by a L, R or C");
            }

            checkRunwayValues(tora, toda, asda, lda);

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
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("There was a problem with your input");
            alert.setContentText("Invalid runway value: look for non-number characters in the TORA, TODA, ASDA, and LDA fields.");
            alert.showAndWait();
        } catch (Exception e) {
            additionalInfoBar.setText(e.getMessage());
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("There was a problem with your input");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void checkRunwayValues(int tora, int toda, int asda, int lda) throws Exception {
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
    
    @FXML
    protected void enter1(KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER) {
			try {
				createAirportAction();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
    
    @FXML
    protected void enter2(KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER) {
				createAction();
			
		}
	}
}
