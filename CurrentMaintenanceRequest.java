public class CurrentMaintenanceRequest {
    private static CurrentMaintenanceRequest instance;
    private String maintenanceRequestInfo;

    private CurrentMaintenanceRequest() {
        // Initialize the maintenanceRequestInfo as empty initially
        maintenanceRequestInfo = "";
    }

    public static CurrentMaintenanceRequest getInstance() {
        if (instance == null) {
            instance = new CurrentMaintenanceRequest();
        }
        return instance;
    }

    public String getMaintenanceRequestInfo() {
        return maintenanceRequestInfo;
    }

    public void setMaintenanceRequestInfo(String info) {
        maintenanceRequestInfo = info;
    }
}
