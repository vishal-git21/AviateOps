import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class PilotView extends CrewView {

    @Override
    protected void viewScheduleAction(String crewID) {
        PilotController.viewPilotSchedule();
    }

    @Override
    protected void acceptScheduleAction(String crewID) {
        PilotController.acceptPilotSchedule(crewID);
    }

    @Override
    protected void requestReliefAction(String crewID, TextArea currentCrewScheduleTextArea) {
        PilotController.requestRelief(crewID, currentCrewScheduleTextArea);
    }

    @Override
    protected void setCrewScheduleTextArea(TextArea textArea) {
        PilotController.setPilotScheduleTextArea(textArea);
    }

    @Override
    protected void setAcceptScheduleButton(Button button) {
        PilotController.setAcceptScheduleButton(button);
    }
}
