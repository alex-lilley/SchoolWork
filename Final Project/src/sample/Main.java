package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    private static TabPane tabPane;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage note = new Stage();
        Parent noteRoot = FXMLLoader.load(getClass().getResource("popUp.fxml"));
        Scene noteScene = new Scene(noteRoot, 600, 400);
        note.setTitle("NOTE!");
        note.setScene(noteScene);

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene scene = new Scene(root, 1000, 600);

        primaryStage.setTitle("NarShark");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("file:narshark.png"));
        primaryStage.show();
        note.show();

        tabPane = (TabPane) scene.lookup("#tabPane");

        TabPane tabPane = Main.getTabPane();
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab)throws java.lang.NullPointerException {
                try{
                String id = newTab.getId();
                Controller.changeFile(id);
                }catch (java.lang.NullPointerException e){
                    //do nothing
                }
            }
        });
    }

    public static void createTab(Tab tab) {
        tabPane.getTabs().add(tab);
    }

    public static TabPane getTabPane() {
        return tabPane;
    }

    public static void main(String[] args) {
        launch(args);
    }
}