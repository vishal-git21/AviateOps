import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import java.sql.*;
import javafx.stage.Stage;

public class AuthController {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/airlinemanagementsystem";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";



    public static void login(TextField crewIDField, PasswordField passwordField, Label statusLabel,Stage primaryStage) {
        String crewID = crewIDField.getText();
        String password = passwordField.getText();

        if (crewID.equals("admin") && password.equals("admin")) {
            AdminController.showAdminWindow(primaryStage);
            statusLabel.setText("Logged in as Admin");
        } else {
            try {
                Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                String positionQuery = "SELECT position FROM Crew WHERE crewID = ? AND password = ?";
                PreparedStatement positionStatement = connection.prepareStatement(positionQuery);
                positionStatement.setString(1, crewID);
                positionStatement.setString(2, password);
                ResultSet positionResult = positionStatement.executeQuery();

                if (positionResult.next()) {
                    String position = positionResult.getString("position");

                    if (position.equalsIgnoreCase("staff")) {
                        StaffView staffView = new StaffView();
                        staffView.showCrewWindow("Staff",crewID);
                        statusLabel.setText("You have logged in as Staff");
                    } else if (position.equalsIgnoreCase("flyer")) {
                        PilotView pilotView = new PilotView();
                        pilotView.showCrewWindow("Pilot",crewID);
                        statusLabel.setText("You have logged in as Pilot");
                    } else if (position.equalsIgnoreCase("maintenance")) {
                        MaintenanceCrewView maintenanceCrewView = new MaintenanceCrewView();
                        maintenanceCrewView.showMaintenanceCrewWindow(crewID);
                        statusLabel.setText("You have logged in as Maintenance Crew");
                    }
                } else {
                    statusLabel.setText("Invalid credentials");
                }

                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
