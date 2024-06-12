import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class StaffView extends CrewView {

    @Override
    protected void viewScheduleAction(String crewID) {
        StaffController.viewStaffSchedule();
    }

    @Override
    protected void acceptScheduleAction(String crewID) {
        StaffController.acceptStaffSchedule(crewID);
    }

    @Override
    protected void requestReliefAction(String crewID, TextArea currentCrewScheduleTextArea) {
        StaffController.requestRelief(crewID, currentCrewScheduleTextArea);
    }

    @Override
    protected void setCrewScheduleTextArea(TextArea textArea) {
        StaffController.setStaffScheduleTextArea(textArea);
    }

    @Override
    protected void setAcceptScheduleButton(Button button) {
        StaffController.setAcceptScheduleButton(button);
    }
}
