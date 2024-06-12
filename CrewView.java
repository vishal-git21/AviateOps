import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public abstract class CrewView {

    protected void showCrewWindow(String crewType, String crewID) {
        Stage crewStage = new Stage();
        crewStage.setTitle(crewType + " Member Window");

        // Create UI components for crew member window
        GridPane crewGrid = new GridPane();
        crewGrid.setAlignment(Pos.CENTER);
        crewGrid.setPadding(new Insets(20, 20, 20, 20));
        crewGrid.setVgap(10);
        crewGrid.setHgap(10);

        crewGrid.add(generateTitleText("AIRLINE MANAGEMENT SYSTEM"), 0, 0, 2, 1);

        Label crewLabel = new Label("Welcome " + crewType + " Member!");
        crewGrid.add(crewLabel, 0, 1);

        // Add a Button for viewing schedules
        Button viewScheduleButton = new Button("View Schedules");
        crewGrid.add(viewScheduleButton, 0, 2);
        // Attach event handler for viewing schedules
        viewScheduleButton.setOnAction(e -> viewScheduleAction(crewID));

        // Add a TextArea for displaying the schedule (initially hidden)
        TextArea crewScheduleTextArea = new TextArea();
        crewScheduleTextArea.setEditable(false); // Make it read-only
        crewScheduleTextArea.setVisible(false); // Initially hidden
        crewGrid.add(crewScheduleTextArea, 0, 3);

        // Add an "Accept Schedule" button (initially hidden)
        Button acceptScheduleButton = new Button("Accept Schedule");
        crewGrid.add(acceptScheduleButton, 0, 4);
        acceptScheduleButton.setVisible(false);
        // Attach event handler for accepting schedule
        acceptScheduleButton.setOnAction(e -> acceptScheduleAction(crewID));

        TextArea currentCrewScheduleTextArea = new TextArea();
        currentCrewScheduleTextArea.setEditable(false); // Make it read-only
        currentCrewScheduleTextArea.setVisible(false); // Initially hidden
        crewGrid.add(currentCrewScheduleTextArea, 0, 6);

        Button requestReliefButton = new Button("Request Relief");
        crewGrid.add(requestReliefButton, 0, 5);
        requestReliefButton.setOnAction(e -> requestReliefAction(crewID, currentCrewScheduleTextArea));        

        // Add components to crew grid
        Scene crewScene = new Scene(crewGrid, 400, 250);
        crewStage.setScene(crewScene);
        crewStage.show();

        // Set UI components
        setCrewScheduleTextArea(crewScheduleTextArea);
        setAcceptScheduleButton(acceptScheduleButton);
    }

    protected abstract void viewScheduleAction(String crewID);

    protected abstract void acceptScheduleAction(String crewID);

    protected abstract void requestReliefAction(String crewID, TextArea currentCrewScheduleTextArea);

    protected Text generateTitleText(String title) {
        Text titleText = new Text(title);
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        return titleText;
    }

    protected abstract void setCrewScheduleTextArea(TextArea textArea);

    protected abstract void setAcceptScheduleButton(Button button);
}
