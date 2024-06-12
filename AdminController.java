import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.DialogPane;




public class AdminController {

    public static void showAdminWindow(Stage primaryStage) {
        AdminView adminView = new AdminView();
        adminView.initUI();

        primaryStage.setScene(adminView.getAdminScene());
        primaryStage.setTitle("Admin Window");
        primaryStage.show();
    }
    public static Text generateTitleText(String title) {
        Text titleText = new Text(title);
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        return titleText;
    }
    public static void finalizeschedule() {
    // Connect to the database
    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/airlinemanagementsystem", "root", "password")) {
        // Query to select schedules with all crew members assigned
        String query = "SELECT * FROM flightschedule WHERE status = 'pending' AND pilotID IS NOT NULL AND coPilotID IS NOT NULL AND staffmember1 IS NOT NULL AND staffmember2 IS NOT NULL AND staffmember3 IS NOT NULL";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            // Display schedule details in TextArea
            TextArea scheduleTextArea = new TextArea();
            scheduleTextArea.setEditable(false);
            scheduleTextArea.setWrapText(true);

            // List to store schedule IDs
            ObservableList<Integer> scheduleIDs = FXCollections.observableArrayList();

            while (resultSet.next()) {
                int scheduleID = resultSet.getInt("scheduleID");
                String departureTime = resultSet.getString("departureTime");
                String arrivalTime = resultSet.getString("arrivalTime");
                String departure = resultSet.getString("departure");
                String arrival = resultSet.getString("arrival");

                // Append schedule details to TextArea
                scheduleTextArea.appendText("Schedule ID: " + scheduleID + "\n");
                scheduleTextArea.appendText("Departure Time: " + departureTime + "\n");
                scheduleTextArea.appendText("Arrival Time: " + arrivalTime + "\n");
                scheduleTextArea.appendText("Departure: " + departure + "\n");
                scheduleTextArea.appendText("Arrival: " + arrival + "\n\n");

                // Add schedule ID to the list
                scheduleIDs.add(scheduleID);
            }

            // Display schedules in the TextArea with confirmation options
            displayScheduleTextArea(scheduleTextArea, scheduleIDs);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}


// Method to display schedule details in TextArea
public static void displayScheduleTextArea(TextArea scheduleTextArea, ObservableList<Integer> scheduleIDs) {
    Stage scheduleStage = new Stage();
    scheduleStage.setTitle("Finalized Schedules");

    GridPane scheduleGrid = new GridPane();
    scheduleGrid.setAlignment(Pos.CENTER);
    scheduleGrid.setPadding(new Insets(20, 20, 20, 20));
    scheduleGrid.setVgap(10);
    scheduleGrid.setHgap(10);

    scheduleGrid.add(scheduleTextArea, 0, 0);

    // Add ComboBox with schedule IDs
    ComboBox<Integer> scheduleComboBox = new ComboBox<>(scheduleIDs);
    scheduleComboBox.setPromptText("Select Schedule ID");
    scheduleGrid.add(scheduleComboBox, 0, 1);

    // Add button to confirm finalization
    Button confirmButton = new Button("Confirm Finalization");
    confirmButton.setOnAction(e -> confirmFinalization(scheduleComboBox.getValue()));
    HBox buttonBox = new HBox(10);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().add(confirmButton);
    scheduleGrid.add(buttonBox, 0, 2);

    Scene scheduleScene = new Scene(scheduleGrid, 600, 450);
    scheduleStage.setScene(scheduleScene);
    scheduleStage.show();
}



public static void confirmFinalization(int selectedScheduleID) {
    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/airlinemanagementsystem", "root", "password")) {
        // Query to update the status of the selected schedule
        String updateQuery = "UPDATE flightschedule SET status = 'accepted' WHERE scheduleID = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setInt(1, selectedScheduleID);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                // Show confirmation message as a popup
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Schedule Finalization");
                alert.setHeaderText(null);
                alert.setContentText("Schedule ID: " + selectedScheduleID + "\nStatus updated to 'accepted'");
                alert.showAndWait();
            } else {
                // Show failure message as a popup
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Schedule Finalization");
                alert.setHeaderText(null);
                alert.setContentText("Failed to finalize schedule ID: " + selectedScheduleID);
                alert.showAndWait();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

public static void showCreateScheduleForm() {
        Stage createScheduleStage = new Stage();
        createScheduleStage.setTitle("Create Schedule");

        // Create UI components for create schedule form
        GridPane createScheduleGrid = new GridPane();
        createScheduleGrid.setAlignment(Pos.CENTER);
        createScheduleGrid.setPadding(new Insets(20, 20, 20, 20));
        createScheduleGrid.setVgap(10);
        createScheduleGrid.setHgap(10);

        createScheduleGrid.add(generateTitleText("AIRLINE MANAGEMENT SYSTEM"), 0, 0, 2, 1);

        Label departureLabel = new Label("Departure Date:");
        createScheduleGrid.add(departureLabel, 0, 1);
        DatePicker departurePicker = new DatePicker();
        createScheduleGrid.add(departurePicker, 1, 1);

        Label departureTimeLabel = new Label("Departure Time:");
        createScheduleGrid.add(departureTimeLabel, 0, 2);
        ComboBox<String> departureTimeComboBox = new ComboBox<>();
        departureTimeComboBox.getItems().addAll("00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00");
        createScheduleGrid.add(departureTimeComboBox, 1, 2);

        Label departureLocationLabel = new Label("Departure Location:");
        createScheduleGrid.add(departureLocationLabel, 0, 3);
        TextField departureLocationField = new TextField();
        createScheduleGrid.add(departureLocationField, 1, 3);

        Label arrivalLabel = new Label("Arrival Date:");
        createScheduleGrid.add(arrivalLabel, 0, 4);
        DatePicker arrivalPicker = new DatePicker();
        createScheduleGrid.add(arrivalPicker, 1, 4);

        Label arrivalTimeLabel = new Label("Arrival Time:");
        createScheduleGrid.add(arrivalTimeLabel, 0, 5);
        ComboBox<String> arrivalTimeComboBox = new ComboBox<>();
        arrivalTimeComboBox.getItems().addAll("00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00");
        createScheduleGrid.add(arrivalTimeComboBox, 1, 5);

        Label arrivalLocationLabel = new Label("Arrival Location:");
        createScheduleGrid.add(arrivalLocationLabel, 0, 6);
        TextField arrivalLocationField = new TextField();
        createScheduleGrid.add(arrivalLocationField, 1, 6);

        Label aircraftIDLabel = new Label("Aircraft ID:");
        createScheduleGrid.add(aircraftIDLabel, 0, 7);
        TextField aircraftIDField = new TextField();
        createScheduleGrid.add(aircraftIDField, 1, 7);

        Button confirmScheduleButton = new Button("Confirm Schedule");
        createScheduleGrid.add(confirmScheduleButton, 1, 8);
        confirmScheduleButton.setOnAction(e -> createSchedule(departurePicker.getValue(), departureTimeComboBox.getValue(), departureLocationField.getText(), arrivalPicker.getValue(), arrivalTimeComboBox.getValue(), arrivalLocationField.getText(), aircraftIDField.getText()));

        // Add components to create schedule grid
        Scene createScheduleScene = new Scene(createScheduleGrid, 400, 300);
        createScheduleStage.setScene(createScheduleScene);
        createScheduleStage.show();
    }

    public static void showPopup(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

public static void createSchedule(LocalDate departureDate, String departureTime, String departureLocation, LocalDate arrivalDate, String arrivalTime, String arrivalLocation, String aircraftID) {
    // Combine date and time into LocalDateTime objects
    LocalDateTime departureDateTime = LocalDateTime.of(departureDate, LocalTime.parse(departureTime));
    LocalDateTime arrivalDateTime = LocalDateTime.of(arrivalDate, LocalTime.parse(arrivalTime));

    // Establish database connection
    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/airlinemanagementsystem", "root", "password")) {
        // Prepare SQL statement
        String sql = "INSERT INTO FlightSchedule (scheduleID, departureTime, arrivalTime, status, aircraftID, departure, arrival, pilotID, coPilotID, staffMember1, staffMember2, staffMember3) VALUES (?, ?, ?, ?, ?, ?, ?, NULL, NULL, NULL, NULL, NULL)";
        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        // Set scheduleID parameter
        int scheduleID = 0;
        try (ResultSet rs = connection.createStatement().executeQuery("SELECT MAX(scheduleID) FROM FlightSchedule")) {
            if (rs.next()) {
                scheduleID = rs.getInt(1) + 1;
            } else {
                scheduleID = 1;
            }
        }
        statement.setInt(1, scheduleID);

        // Set other parameters
        statement.setObject(2, departureDateTime);
        statement.setObject(3, arrivalDateTime);
        statement.setString(4, "Pending");
        statement.setString(5, aircraftID);
        statement.setString(6, departureLocation);
        statement.setString(7, arrivalLocation);

        // Execute the query
        int rowsInserted = statement.executeUpdate();

        if (rowsInserted > 0) {
            showPopup("Schedule created successfully.");
            System.out.println("Schedule created successfully.");
        } else {
            System.out.println("Failed to create schedule.");
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/airlinemanagementsystem";
        String user = "root";
        String password = "password";
        return DriverManager.getConnection(url, user, password);
    }


public static void sendMaintenanceRequest() {
    try (Connection connection = getConnection()) {
        String query = "SELECT aircraftID, manufacturer, last_maintenance_check " +
                       "FROM aircraft " +
                       "ORDER BY last_maintenance_check ASC " +
                       "LIMIT 1";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            int aircraftID = resultSet.getInt("aircraftID");
            String manufacturer = resultSet.getString("manufacturer");
            String lastMaintenanceCheck = resultSet.getString("last_maintenance_check");

            String alertMessage = "Aircraft with ID " + aircraftID + " (" + manufacturer + ") "
                                + "had its last maintenance check on " + lastMaintenanceCheck + ". "
                                + "A maintenance request has been sent for this aircraft.";

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Maintenance Request Sent");
            alert.setHeaderText(null);
            alert.setContentText(alertMessage);

            // Enable text wrapping
            Label contentLabel = new Label(alertMessage);
            contentLabel.setWrapText(true);
            alert.getDialogPane().setContent(contentLabel);

            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No Aircraft Found");
            alert.setHeaderText(null);
            alert.setContentText("No aircraft data found in the database.");

            // Enable text wrapping
            Label contentLabel = new Label("No aircraft data found in the database.");
            contentLabel.setWrapText(true);
            alert.getDialogPane().setContent(contentLabel);

            alert.showAndWait();
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle database errors
    }
}



}
