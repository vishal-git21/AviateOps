import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginView {

    private GridPane grid;
    private TextField crewIDField;
    private PasswordField passwordField;
    private Label statusLabel;

    public void initUI(Stage primaryStage) {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        Text titleText = generateTitleText("AIRLINE MANAGEMENT SYSTEM");
        GridPane.setConstraints(titleText, 0, 0, 2, 1);

        Label crewIDLabel = new Label("Crew ID:");
        GridPane.setConstraints(crewIDLabel, 0, 1);
        crewIDField = new TextField();
        GridPane.setConstraints(crewIDField, 1, 1);

        Label passwordLabel = new Label("Password:");
        GridPane.setConstraints(passwordLabel, 0, 2);
        passwordField = new PasswordField();
        GridPane.setConstraints(passwordField, 1, 2);

        Button loginButton = new Button("Login");
        GridPane.setConstraints(loginButton, 1, 3);

        statusLabel = new Label();
        GridPane.setConstraints(statusLabel, 1, 4);

        grid.getChildren().addAll(titleText, crewIDLabel, crewIDField, passwordLabel, passwordField, loginButton, statusLabel);

        loginButton.setOnAction(e -> AuthController.login(crewIDField, passwordField, statusLabel , primaryStage));
    }

    private Text generateTitleText(String title) {
        Text titleText = new Text(title);
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        return titleText;
    }

    public GridPane getGrid() {
        return grid;
    }

    public TextField getCrewIDField() {
        return crewIDField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }
}
