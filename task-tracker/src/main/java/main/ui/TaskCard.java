//@@author A0139422J
package main.ui;


import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.model.task.PriorityType;
import main.model.task.ReadOnlyTask;

/**
 * Display individual panels within TaskListPanel with the details of eacch specific task
 * "person" keyword check done
 * "addressbook" keyword check done
 * @param HBox cardPane
 * @param Label id
 * @param Label message
 * @param Label date
 * @param Rectangle priorityTab
 * @author bey
 *
 */
public class TaskCard extends UiPart{

    private static final String FXML = "TaskListCard.fxml";

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label message;
    @FXML
    private Label deadline;
    @FXML
    private Label endtime;
    @FXML
    private Label starttime;
    @FXML
    private Label recurring;
    
    @FXML
    private Rectangle priorityTab;

    private ReadOnlyTask task;
    private int displayedIndex;

    public TaskCard(){

    }

    public static TaskCard load(ReadOnlyTask task, int displayedIndex){
        TaskCard card = new TaskCard();
        card.task = task;
        card.displayedIndex = displayedIndex;
        return UiPartLoader.loadUiPart(card);
    }

    @FXML
    public void initialize() {
     
        configureLayout();     
        setTaskCardText();
        setPriorityTabColour();
        
    }
    
    private void setTaskCardText() {
    	
    	message.setText(task.getMessage());
        
        id.setText(displayedIndex + ". ");
        
        if (task.getDeadline()!=null)
        	deadline.setText("Deadline: "+ task.getDeadlineString());
        else
        	deadline.setText("");
        
        if (task.getEndTime()!=null)
        	endtime.setText("End: "+ task.getEndTimeString());
        else
        	endtime.setText("");
        
        if (task.getStartTime()!=null)
            starttime.setText("Start: "+ task.getStartTimeString());
        else
            starttime.setText("");
        
        if (task.getIsRecurring())
            recurring.setText("Recurring");
        else
            recurring.setText("No");
		
	}
    
    private void setPriorityTabColour() {
		if (task.getPriority().equals(PriorityType.HIGH)){
			priorityTab.setFill(Color.RED);
			cardPane.setStyle("-fx-background-color: #ff6666;");
		}
		else if (task.getPriority().equals(PriorityType.LOW)){
			priorityTab.setFill(Color.YELLOWGREEN);
			cardPane.setStyle("-fx-background-color: #ffffb3;");
		}
		else{
			priorityTab.setFill(Color.ORANGE);
			cardPane.setStyle("-fx-background-color: #ffa366;");
		}	
	}

	private void configureLayout() {
    	
    	cardPane.setSpacing(18.0);
        deadline.setMinWidth(300);
//        endtime.setMinWidth(300);
        starttime.setMinWidth(300);
        cardPane.setMinWidth(450);

	}

	public HBox getLayout() {
        return cardPane;
    }

    @Override
    public void setNode(Node node) {
        cardPane = (HBox)node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
}
