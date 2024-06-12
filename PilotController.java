import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class PilotController {

    public static TextArea pilotScheduleTextArea;
    public static Button acceptScheduleButton;

    // Setters for UI components
    public static void setPilotScheduleTextArea(TextArea textArea) {
        pilotScheduleTextArea = textArea;
    }

    public static void setAcceptScheduleButton(Button button) {
        acceptScheduleButton = button;
    }

    public static void viewPilotSchedule() {
        // Check if UI components are set
        if (pilotScheduleTextArea == null || acceptScheduleButton == null) {
            System.err.println("UI components not set in PilotController");
            return;
        }

        StringBuilder scheduleBuilder = new StringBuilder();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/airlinemanagementsystem", "root", "password")) {
            String selectQuery = "SELECT * FROM FlightSchedule WHERE status = 'pending' AND (pilotID IS NULL OR copilotID IS NULL) LIMIT 3";
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int scheduleID = resultSet.getInt("scheduleID");
                LocalDateTime departureTime = resultSet.getTimestamp("departureTime").toLocalDateTime();
                LocalDateTime arrivalTime = resultSet.getTimestamp("arrivalTime").toLocalDateTime();
                String departureLocation = resultSet.getString("departure");
                String arrivalLocation = resultSet.getString("arrival");

                // Append schedule details to the StringBuilder
                scheduleBuilder.append("Schedule ID: ").append(scheduleID).append("\n");
                scheduleBuilder.append("Departure Time: ").append(departureTime).append("\n");
                scheduleBuilder.append("Arrival Time: ").append(arrivalTime).append("\n");
                scheduleBuilder.append("Departure Location: ").append(departureLocation).append("\n");
                scheduleBuilder.append("Arrival Location: ").append(arrivalLocation).append("\n\n");
            }

            // Set the text of the TextArea with the schedule details
            pilotScheduleTextArea.setText(scheduleBuilder.toString());
            pilotScheduleTextArea.setVisible(true); // Show the TextArea

            // Show the "Accept Schedule" button
            acceptScheduleButton.setVisible(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void requestRelief(String crewID, TextArea currentpilotScheduleTextArea) {
        List<Integer> scheduleIDs = new ArrayList<>();
        StringBuilder scheduleDetails = new StringBuilder();
        String columnName = null;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/airlinemanagementsystem", "root", "password")) {
            String query = "SELECT scheduleID, departureTime, arrivalTime, departure, arrival, " +
                    "CASE " +
                    "  WHEN pilotID = ? THEN 'pilotID' " +
                    "  WHEN copilotID = ? THEN 'copilotID' " +
                    "  ELSE NULL " +
                    "END AS columnName " +
                    "FROM FlightSchedule " +
                    "WHERE (pilotID = ? OR copilotID = ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, crewID);
                statement.setString(2, crewID);
                statement.setString(3, crewID);
                statement.setString(4, crewID);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int scheduleID = resultSet.getInt("scheduleID");
                        LocalDateTime departureTime = resultSet.getTimestamp("departureTime").toLocalDateTime();
                        LocalDateTime arrivalTime = resultSet.getTimestamp("arrivalTime").toLocalDateTime();
                        String departureLocation = resultSet.getString("departure");
                        String arrivalLocation = resultSet.getString("arrival");
                        columnName = resultSet.getString("columnName");

                        scheduleIDs.add(scheduleID);

                        scheduleDetails.append("Schedule ID: ").append(scheduleID).append("\n");
                        scheduleDetails.append("Departure Time: ").append(departureTime).append("\n");
                        scheduleDetails.append("Arrival Time: ").append(arrivalTime).append("\n");
                        scheduleDetails.append("Departure Location: ").append(departureLocation).append("\n");
                        scheduleDetails.append("Arrival Location: ").append(arrivalLocation).append("\n\n");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        currentpilotScheduleTextArea.setText(scheduleDetails.toString());
        currentpilotScheduleTextArea.setVisible(true);

        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(null, scheduleIDs);
        dialog.setTitle("Request Relief");
        dialog.setHeaderText("Select a Schedule ID for Relief Request");
        dialog.setContentText("Schedule ID:");
        Optional<Integer> result = dialog.showAndWait();
        String finalColumnName = columnName;
        result.ifPresent(selectedScheduleID -> {
            sendReliefRequest(finalColumnName, selectedScheduleID);
        });
    }

    public static void sendReliefRequest(String columnName, int selectedScheduleID) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/airlinemanagementsystem", "root", "password")) {
            String query = "UPDATE FlightSchedule SET " + columnName + " = NULL WHERE scheduleID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, selectedScheduleID);
                statement.executeUpdate();
            }
            Alert confirmationDialog = new Alert(Alert.AlertType.INFORMATION);
            confirmationDialog.setTitle("Relief Request Granted");
            confirmationDialog.setHeaderText("Relief Request Granted");
            confirmationDialog.setContentText("You have been granted relief from the selected schedule.");
            confirmationDialog.showAndWait();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }







    public static void acceptPilotSchedule(String crewIDString){
        // Fetch pending schedule IDs from the database
        List<Integer> pendingScheduleIDs_forpilot = fetchPendingScheduleIDs_forpilot();

        // Show choice dialog to select a schedule ID
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(null, pendingScheduleIDs_forpilot);
        dialog.setTitle("Accept Schedule");
        dialog.setHeaderText("Select a Schedule ID to Accept");
        dialog.setContentText("Schedule ID:");

        Optional<Integer> result = dialog.showAndWait();
        result.ifPresent(scheduleID -> {
            // Show confirmation dialog
            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationDialog.setTitle("Confirmation");
            confirmationDialog.setHeaderText("Schedule Confirmed");
            confirmationDialog.setContentText("Schedule with ID " + scheduleID + " has been confirmed.");
            confirmationDialog.showAndWait();


            int crewID = Integer.parseInt(crewIDString);

            // Determine which pilot member slot is empty for the given schedule
            String emptyPilotMemberColumn = getEmptyPilotMemberColumn(scheduleID);

            if (emptyPilotMemberColumn != null) {
                // Update the corresponding slot with the crew ID
                updatePilotMemberColumn(scheduleID, emptyPilotMemberColumn, crewID);
            } else {
                // If all pilot member slots are filled, show an error message
                Alert errorDialog = new Alert(Alert.AlertType.ERROR);
                errorDialog.setTitle("Error");
                errorDialog.setHeaderText("Pilot Member Slots Full");
                errorDialog.setContentText("All pilot member slots for schedule with ID " + scheduleID + " are already filled.");
                errorDialog.showAndWait();
            }
        });
    }

    public static String getEmptyPilotMemberColumn(int scheduleID) {
        // Query to check which pilot member slot is empty for the given schedule
        String query = "SELECT CASE " +
                              "WHEN pilotID IS NULL THEN 'pilotID' " +
                              "WHEN copilotID IS NULL THEN 'copilotID' " +
                              "ELSE NULL " +
                       "END AS emptyColumn " +
                       "FROM FlightSchedule " +
                       "WHERE scheduleID = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/airlinemanagementsystem", "root", "password");
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, scheduleID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("emptyColumn");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null; // No empty pilot member slot found
    }

    public static void updatePilotMemberColumn(int scheduleID, String columnName, int crewID) {
        // Query to update the pilot member slot with the crew ID
        String query = "UPDATE FlightSchedule SET " + columnName + " = ? WHERE scheduleID = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/airlinemanagementsystem", "root", "password");
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, crewID);
            statement.setInt(2, scheduleID);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static List<Integer> fetchPendingScheduleIDs_forpilot() {
        List<Integer> pendingScheduleIDs_forpilot = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/airlinemanagementsystem", "root", "password")) {
            String selectQuery = "SELECT scheduleID FROM FlightSchedule WHERE status = 'pending' AND (pilotID IS NULL OR copilotID IS NULL)";
            try (PreparedStatement statement = connection.prepareStatement(selectQuery);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    pendingScheduleIDs_forpilot.add(resultSet.getInt("scheduleID"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return pendingScheduleIDs_forpilot;
    }
}
