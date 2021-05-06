/**
 * Main class for GUI program that analyze employees from given text file.
 *
 * Uses JavaFX for GUI Build
 *
 * @author Vladimir Vladimirov
 */

package employeesAnalyzer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/main.fxml"));
        primaryStage.setTitle("Analyze employees");
        primaryStage.setMinHeight(456);
        primaryStage.setMinWidth(680);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
