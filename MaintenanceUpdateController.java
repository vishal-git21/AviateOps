import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class MaintenanceUpdateController {

    public static void updateLastMaintenanceCheck(Connection connection) {
        String updateQuery = "UPDATE aircraft SET last_maintenance_check = ? ORDER BY last_maintenance_check ASC LIMIT 1";
        LocalDateTime currentDate = LocalDateTime.now();
        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setObject(1, currentDate);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            // Handle the exception or throw it back to the caller
            e.printStackTrace();
        }
    }
}
