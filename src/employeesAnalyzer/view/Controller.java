/**
 * Controller for JavaFX Scene
 *
 * @author Vladimir Vladimirov
 */

package employeesAnalyzer.view;

import employeesAnalyzer.data.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Window;

import java.io.File;
import java.net.URL;
import java.nio.file.FileSystem;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TableView<Entity> fileTable;
    @FXML
    private TableColumn<Entity, Long> fileEmployeeId;
    @FXML
    private TableColumn<Entity, Long> fileProjectId;
    @FXML
    private TableColumn<Entity, LocalDate> fileFrom;
    @FXML
    private TableColumn<Entity, LocalDate> fileTo;
    @FXML
    private TableView<EmplWorkedTogetherDTO> resTable;
    @FXML
    private TableColumn<EmplWorkedTogetherDTO, Long> resTableProjectId;
    @FXML
    private TableColumn<EmplWorkedTogetherDTO, Integer> resTableTotal;
    @FXML
    private TableColumn<EmplWorkedTogetherDTO, Integer> resTableWeekend;
    @FXML
    private TableColumn<EmplWorkedTogetherDTO, LocalDate> resTableStart;
    @FXML
    private TableColumn<EmplWorkedTogetherDTO, LocalDate> resTableEnd;
    @FXML
    private Label workedTogetherWeekend;
    @FXML
    private Label workedTogetherDays;
    @FXML
    private Label empl2Id;
    @FXML
    private Label empl1Id;
    @FXML
    private TextField path;

    private ObservableList<Entity> entityObservableList;
    private ObservableList<EmplWorkedTogetherDTO> emplWorkedTogetherDTOObservableList;
    private static File lastOpenDir;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fileEmployeeId.setCellValueFactory(new PropertyValueFactory<>("empId"));
        fileProjectId.setCellValueFactory(new PropertyValueFactory<>("projectId"));
        fileFrom.setCellValueFactory(new PropertyValueFactory<>("dateFrom"));
        fileTo.setCellValueFactory(new PropertyValueFactory<>("dateTo"));

        resTableProjectId.setCellValueFactory(new PropertyValueFactory<>("workedOnProject"));
        resTableTotal.setCellValueFactory(new PropertyValueFactory<>("workedDays"));
        resTableWeekend.setCellValueFactory(new PropertyValueFactory<>("weekendDays"));
        resTableStart.setCellValueFactory(new PropertyValueFactory<>("startPeriod"));
        resTableEnd.setCellValueFactory(new PropertyValueFactory<>("endPeriod"));

    }

    public void submitPath(javafx.event.ActionEvent actionEvent) {
        String pathString = this.path.getText();
        try {

            //show in table file entities
            List<Entity> entityList = EntitiesDAO.findEntitiesFromTextFile(pathString);
            entityObservableList = FXCollections.observableList(entityList);
            fileTable.setItems(entityObservableList);

            //SHOW RESULTS
            //in table
            List<EmplWorkedTogetherDTO> emplWorkedTogetherDTOs = EmployeesAnalyzer.findEmployeesWorkedMostTogether(entityList);
            emplWorkedTogetherDTOObservableList = FXCollections.observableList(emplWorkedTogetherDTOs);
            resTable.setItems(emplWorkedTogetherDTOObservableList);

            //in stats
            String empl1Id = Long.toString(emplWorkedTogetherDTOs.get(0).getEmployee1());
            this.empl1Id.setText(empl1Id);
            String empl2Id = Long.toString(emplWorkedTogetherDTOs.get(0).getEmployee2());
            this.empl2Id.setText(empl2Id);

            int workedTogether = 0;
            int weekendDays = 0;
            for(EmplWorkedTogetherDTO winnerEmpl : emplWorkedTogetherDTOs){
                workedTogether += winnerEmpl.getWorkedDays();
                weekendDays += winnerEmpl.getWeekendDays();
            }
            this.workedTogetherDays.setText(Integer.toString(workedTogether));
            this.workedTogetherWeekend.setText(Integer.toString(weekendDays));

        } catch (FileAnalyzerException e) {
            Alert alertDialog = new Alert(Alert.AlertType.ERROR);
            alertDialog.setTitle("Error");
            alertDialog.setHeaderText("Error with your file!");
            alertDialog.setContentText(e.getMessage());
            alertDialog.showAndWait();
        }
    }

    public void openDialog(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Text File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setInitialDirectory(lastOpenDir);
        Window openWindow = new Popup();
        File selectedFile = fileChooser.showOpenDialog(openWindow);
        this.path.setText(selectedFile.getAbsolutePath());
        String lastOpenPath = selectedFile.getAbsolutePath().substring(0, selectedFile.getAbsolutePath().lastIndexOf(File.separator));
        lastOpenDir = new File(lastOpenPath);
    }
}
