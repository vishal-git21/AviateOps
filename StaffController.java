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

public class StaffController {

    public static TextArea staffScheduleTextArea;
    public static Button acceptScheduleButton;

    // Setters for UI components
    public static void setStaffScheduleTextArea(TextArea textArea) {
        staffScheduleTextArea = textArea;
    }

    public static void setAcceptScheduleButton(Button button) {
        acceptScheduleButton = button;
    }

    public static void viewStaffSchedule() {
        if (staffScheduleTextArea == null || acceptScheduleButton == null) {
            System.err.println("UI components not set in StaffController");
            return;
        }

        StringBuilder scheduleBuilder = new StringBuilder();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/airlinemanagementsystem", "root", "password")) {
            String selectQuery = "SELECT * FROM FlightSchedule WHERE status = 'pending' AND (staffMember1 IS NULL OR staffMember2 IS NULL OR staffMember3 IS NULL) LIMIT 3";
            try (PreparedStatement statement = connection.prepareStatement(selectQuery);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    int scheduleID = resultSet.getInt("scheduleID");
                    LocalDateTime departureTime = resultSet.getTimestamp("departureTime").toLocalDateTime();
                    LocalDateTime arrivalTime = resultSet.getTimestamp("arrivalTime").toLocalDateTime();
                    String departureLocation = resultSet.getString("departure");
                    String arrivalLocation = resultSet.getString("arrival");

                    scheduleBuilder.append("Schedule ID: ").append(scheduleID).append("\n");
                    scheduleBuilder.append("Departure Time: ").append(departureTime).append("\n");
                    scheduleBuilder.append("Arrival Time: ").append(arrivalTime).append("\n");
                    scheduleBuilder.append("Departure Location: ").append(departureLocation).append("\n");
                    scheduleBuilder.append("Arrival Location: ").append(arrivalLocation).append("\n\n");
                }
            }

            staffScheduleTextArea.setText(scheduleBuilder.toString());
            staffScheduleTextArea.setVisible(true);
            acceptScheduleButton.setVisible(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void requestRelief(String crewID, TextArea currentstaffScheduleTextArea) {
        List<Integer> scheduleIDs = new ArrayList<>();
        StringBuilder scheduleDetails = new StringBuilder();
        String columnName = null;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/airlinemanagementsystem", "root", "password")) {
            String query = "SELECT scheduleID, departureTime, arrivalTime, departure, arrival, " +
                    "CASE " +
                    "  WHEN staffMember1 = ? THEN 'staffMember1' " +
                    "  WHEN staffMember2 = ? THEN 'staffMember2' " +
                    "  WHEN staffMember3 = ? THEN 'staffMember3' " +
                    "  ELSE NULL " +
                    "END AS columnName " +
                    "FROM FlightSchedule " +
                    "WHERE (staffMember1 = ? OR staffMember2 = ? OR staffMember3 = ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, crewID);
                statement.setString(2, crewID);
                statement.setString(3, crewID);
                statement.setString(4, crewID);
                statement.setString(5, crewID);
                statement.setString(6, crewID);
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

        currentstaffScheduleTextArea.setText(scheduleDetails.toString());
        currentstaffScheduleTextArea.setVisible(true);

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




    public static void acceptStaffSchedule(String crewIDString){
        // Fetch pending schedule IDs from the database
        List<Integer> pendingScheduleIDs_forstaff = fetchPendingScheduleIDs_forstaff();

        // Show choice dialog to select a schedule ID
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(null, pendingScheduleIDs_forstaff);
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

            // Determine which staff member slot is empty for the given schedule
            String emptyStaffMemberColumn = getEmptyStaffMemberColumn(scheduleID);

            if (emptyStaffMemberColumn != null) {
                // Update the corresponding slot with the crew ID
                updateStaffMemberColumn(scheduleID, emptyStaffMemberColumn, crewID);
            } else {
                // If all staff member slots are filled, show an error message
                Alert errorDialog = new Alert(Alert.AlertType.ERROR);
                errorDialog.setTitle("Error");
                errorDialog.setHeaderText("Staff Member Slots Full");
                errorDialog.setContentText("All staff member slots for schedule with ID " + scheduleID + " are already filled.");
                errorDialog.showAndWait();
            }
        });
    }

    public static String getEmptyStaffMemberColumn(int scheduleID) {
        // Query to check which staff member slot is empty for the given schedule
        String query = "SELECT CASE " +
                              "WHEN staffmember1 IS NULL THEN 'staffmember1' " +
                              "WHEN staffmember2 IS NULL THEN 'staffmember2' " +
                              "WHEN staffmember3 IS NULL THEN 'staffmember3' " +
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

        return null; // No empty staff member slot found
    }

    public static void updateStaffMemberColumn(int scheduleID, String columnName, Integer crewID) {
        // Query to update the staff member slot with the crew ID
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

    public static List<Integer> fetchPendingScheduleIDs_forstaff() {
        List<Integer> pendingScheduleIDs_forstaff = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/airlinemanagementsystem", "root", "password")) {
            String selectQuery = "SELECT scheduleID FROM FlightSchedule WHERE status = 'pending' AND (staffMember1 IS NULL OR staffMember2 IS NULL OR staffMember3 IS NULL)";
            try (PreparedStatement statement = connection.prepareStatement(selectQuery);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    pendingScheduleIDs_forstaff.add(resultSet.getInt("scheduleID"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return pendingScheduleIDs_forstaff;
    }
}
