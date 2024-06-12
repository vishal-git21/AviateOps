public class MaintenanceCrewController {
    
    // Existing code

    public static void viewMaintenanceRequest(TextArea reportDetailsTextArea) {
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM aircraft ORDER BY last_maintenance_check ASC LIMIT 1";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int aircraftID = resultSet.getInt("aircraftID");
                String manufacturer = resultSet.getString("manufacturer");
                String dateOfManufacture = resultSet.getString("dateOfManufacture");
                double distanceTravelled = resultSet.getDouble("distanceTravelled");
                String lastMaintenanceCheck = resultSet.getString("last_maintenance_check");

                String maintenanceRequestInfo = "Aircraft ID: " + aircraftID + "\n"
                        + "Manufacturer: " + manufacturer + "\n"
                        + "Date of Manufacture: " + dateOfManufacture + "\n"
                        + "Distance Travelled: " + distanceTravelled + "\n"
                        + "Last Maintenance Check: " + lastMaintenanceCheck;

                // Update the currently viewed maintenance request
                CurrentMaintenanceRequest.getInstance().setMaintenanceRequestInfo(maintenanceRequestInfo);

                // Update the UI with the maintenance request info
                maintenanceRequestTextArea.setText(maintenanceRequestInfo);
                maintenanceRequestTextArea.setVisible(true);
                reportDetailsTextArea.setVisible(true);
                fileMaintenanceReportButton.setVisible(true);

                // Update last_maintenance_check to current date in aircraft table
                AircraftMaintenanceUpdater.updateLastMaintenanceCheck(connection);
            } else {
                // If no maintenance request found, clear the UI
                maintenanceRequestTextArea.setText("No maintenance requests found.");
                maintenanceRequestTextArea.setVisible(true);
            }
        } catch (SQLException e) {
            showErrorAlert("Error", "Failed to view maintenance request.", e.getMessage());
        }
    }

    // Existing code
}
