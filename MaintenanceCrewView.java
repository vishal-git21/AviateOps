import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MaintenanceCrewView {

    private TextArea reportDetailsTextArea; // Declare as class-level variable

    public void showMaintenanceCrewWindow(String crewID) {
        Stage maintenanceCrewStage = new Stage();
        maintenanceCrewStage.setTitle("Maintenance Crew Member Window");

        // Create UI components for maintenance crew member window
        GridPane maintenanceCrewGrid = new GridPane();
        maintenanceCrewGrid.setAlignment(Pos.CENTER);
        maintenanceCrewGrid.setPadding(new Insets(20, 20, 20, 20));
        maintenanceCrewGrid.setVgap(10);
        maintenanceCrewGrid.setHgap(10);

        maintenanceCrewGrid.add(generateTitleText("AIRLINE MANAGEMENT SYSTEM"), 0, 0, 2, 1);

        Label maintenanceCrewLabel = new Label("Welcome Maintenance Crew Member!");
        maintenanceCrewGrid.add(maintenanceCrewLabel, 0, 1);

        // Add a Button for viewing Maintenance requests
        Button viewMaintenanceRequestsButton = new Button("View Maintenance Request");
        maintenanceCrewGrid.add(viewMaintenanceRequestsButton, 0, 2);
        viewMaintenanceRequestsButton.setOnAction(e -> MaintenanceCrewController.viewMaintenanceRequest(reportDetailsTextArea));

        // Initialize maintenanceRequestTextArea and make it visible
        TextArea maintenanceRequestTextArea = new TextArea();
        maintenanceRequestTextArea.setEditable(false);
        maintenanceRequestTextArea.setVisible(true); // Ensure it's visible initially
        maintenanceCrewGrid.add(maintenanceRequestTextArea, 0, 3);

        // User input area for entering report details
        reportDetailsTextArea = new TextArea(); // Assign to class-level variable
        reportDetailsTextArea.setPromptText("Enter report details here...");
        maintenanceCrewGrid.add(reportDetailsTextArea, 0, 4);
        reportDetailsTextArea.setVisible(false); // Initially invisible

        Button fileMaintenanceReportButton = new Button("Log Maintenance Report");
        maintenanceCrewGrid.add(fileMaintenanceReportButton, 0, 5);
        fileMaintenanceReportButton.setVisible(false);
        fileMaintenanceReportButton.setOnAction(e -> MaintenanceCrewController.fileMaintenanceReport(crewID, reportDetailsTextArea));

        Scene maintenanceCrewScene = new Scene(maintenanceCrewGrid, 400, 300);
        maintenanceCrewStage.setScene(maintenanceCrewScene);
        maintenanceCrewStage.show();

        // Set the maintenanceRequestTextArea in the controller
        MaintenanceCrewController.setmaintenanceRequestTextArea(maintenanceRequestTextArea);
        MaintenanceCrewController.setfileMaintenanceReportButton(fileMaintenanceReportButton);
    }

    public static Text generateTitleText(String title) {
        Text titleText = new Text(title);
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        return titleText;
    }
}
