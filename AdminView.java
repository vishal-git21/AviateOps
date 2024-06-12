import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;


public class AdminView {

    private GridPane grid;
    private Scene adminScene;

    public void initUI() {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        Text titleText = generateTitleText("AIRLINE MANAGEMENT SYSTEM");
        GridPane.setConstraints(titleText, 0, 0, 2, 1);

        Label adminLabel = new Label("Welcome Admin!");
        GridPane.setConstraints(adminLabel, 0, 1);

        // Add buttons for admin functionality
        Button createScheduleButton = new Button("Create Schedule");
        GridPane.setConstraints(createScheduleButton, 0, 2);
        createScheduleButton.setOnAction(e -> AdminController.showCreateScheduleForm());

        Button finalizeScheduleButton = new Button("Finalize Schedule");
        GridPane.setConstraints(finalizeScheduleButton, 0, 3);
        finalizeScheduleButton.setOnAction(e -> AdminController.finalizeschedule());

        Button sendMaintenanceRequestButton = new Button("Send Maintenance Request");
        GridPane.setConstraints(sendMaintenanceRequestButton, 0, 4);
        sendMaintenanceRequestButton.setOnAction(e -> AdminController.sendMaintenanceRequest());


        grid.getChildren().addAll(titleText, adminLabel, createScheduleButton, finalizeScheduleButton , sendMaintenanceRequestButton);

        // Create the admin scene
        adminScene = new Scene(grid, 400, 300);
    }

    public Text generateTitleText(String title) {
        Text titleText = new Text(title);
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        return titleText;
    }

    public Scene getAdminScene() {
        return adminScene;
    }
}
