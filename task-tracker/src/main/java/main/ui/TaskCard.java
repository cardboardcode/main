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
import main.model.task.ReadOnlyTask;

/**
 * Display individual panels within TaskListPanel with the details of eacch specific task
 * "person" keyword check done
 * "addressbook" keyword check done
 * @param HBox cardPane
 * @param Label id
 * @param Label message
 * @param Label date
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
    	
        message.setText(task.getMessage());
        id.setText(displayedIndex + ". ");
        
        
        configureLayout();
        
        if (task.getDeadline()!=null)
        	deadline.setText(""+ task.getDeadline());
        else
        	deadline.setText("[NO DEADLINE SET]");
        if (task.getEndTime()!=null)
        	endtime.setText(""+ task.getEndTime());
        else
        	endtime.setText("[NO END TIME SET]");
        
        setPriorityTabColour();
        
    }
    
    /**
     * changed the if conditions to reflect on the different priority levels.
     */

    private void setPriorityTabColour() {
		if (task.getDeadline()==null){
			priorityTab.setFill(Color.RED);
		}
		else{
			priorityTab.setFill(Color.BLACK);
		}	
	}

	private void configureLayout() {
    	
    	cardPane.setSpacing(30.0);
        deadline.setMinWidth(300);
        endtime.setMinWidth(70);

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
