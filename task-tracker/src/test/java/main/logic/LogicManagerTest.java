package main.logic;

import main.commons.core.EventsCenter;
import main.commons.core.Messages;
import main.logic.command.AddCommand;
import main.logic.command.CommandResult;
import main.logic.command.HelpCommand;
import main.logic.command.ExitCommand;
import main.logic.command.ClearCommand;
import main.model.Model;
import main.model.ModelManager;
import main.model.ReadOnlyTaskTracker;
import main.model.TaskTracker;
import main.model.task.ReadOnlyTask;
import main.model.task.Task;
import main.model.task.UniqueTaskList.DuplicateTaskException;
import main.storage.StorageManager;
import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.*;
import org.junit.rules.TemporaryFolder;


public class LogicManagerTest {
    
    @Rule
    public TemporaryFolder saveFolder = new TemporaryFolder();
    
    Logic logic;
    Model model;
    
    @Before
    public void setup() {
        model = new ModelManager();
        String tempTaskTrackerFile = saveFolder.getRoot().getPath() + "TempTaskTracker.xml";
        String tempPreferencesFile = saveFolder.getRoot().getPath() + "TempPreferences.json";
        logic = new LogicManager(model, new StorageManager(tempTaskTrackerFile, tempPreferencesFile));
    }
    
    @After
    public void teardown() {
        EventsCenter.clearSubscribers();
}
    
    private void assertCommandBehavior(String inputCommand, String expectedMessage) throws Exception {
//        assertCommandBehavior(inputCommand, expectedMessage, new TaskList(), Collections.emptyList());
        String result = logic.execute(inputCommand).feedbackToUser;
        assertEquals(expectedMessage,result);
    }
    

    @Test
    public void execute_unknownCommandWord() throws Exception {
        String unknownCommand = "uicfhmowqewca";
        assertCommandBehavior(unknownCommand, Messages.MESSAGE_UNKNOWN_COMMAND);
    }
    
    @Test
    public void execute_invalid() throws Exception {
        String invalidCommand = "       ";
        assertCommandBehavior(invalidCommand,String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
    }
    
    @Test
    public void execute_help() throws Exception {
        assertCommandBehavior("help", new HelpCommand().execute().feedbackToUser);
//        assertTrue(helpShown);
    }
    
    @Test
    public void execute_exit() throws Exception {
        assertCommandBehavior("exit", ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT);
    }

    @Test
    public void execute_clear() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        model.addTask(helper.generateTask(1));
        model.addTask(helper.generateTask(2));
        model.addTask(helper.generateTask(3));

        assertCommandBehavior("clear", ClearCommand.MESSAGE_SUCCESS, new TaskTracker(), Collections.emptyList());
    }

    
//    /**
//     * Executes the command and confirms that the result message is correct and
//     * also confirms that the following three parts of the LogicManager object's state are as expected:<br>
//     *      - the internal task list data are same as those in the {@code expected TaskList} <br>
//     *      - the backing list shown by UI matches the {@code shownList} <br>
//     *      - {@code expectedTaskList} was saved to the storage file. <br>
//     */
    private void assertCommandBehavior(String inputCommand, String expectedMessage,
                                       ReadOnlyTaskTracker expectedTaskTracker,
                                       List<? extends ReadOnlyTask> expectedShownList) throws Exception {

        //Execute the command
        CommandResult result = logic.execute(inputCommand);

        //Confirm the ui display elements should contain the right data
        System.out.println(inputCommand + "\n" + result.feedbackToUser);
        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedShownList, model.getFilteredTaskList());

        //Confirm the state of data (saved and in-memory) is as expected
        assertEquals(expectedTaskTracker, model.getTaskTracker());
//        assertEquals(expectedTaskTracker, latestSavedTaskTracker);
    }
    
  
      @Test
      public void execute_add_floating_successful() throws Exception {
          
          TestDataHelper helper = new TestDataHelper();
          Task toBeAdded = helper.floating1();
          TaskTracker expectedAB = helper.addToTaskTracker(toBeAdded);
          
          assertCommandBehavior(helper.generateAddCommand(toBeAdded),
                  String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded),
                  expectedAB,
                  expectedAB.getTaskList());
      }
     
      @Test
      public void execute_addDuplicate_notAllowed() throws Exception {
          // setup expectations
          TestDataHelper helper = new TestDataHelper();
          Task toBeAdded = helper.floating1();
          TaskTracker expectedAB = new TaskTracker();
          expectedAB.addTask(toBeAdded);

          // setup starting state
          model.addTask(toBeAdded); // person already in internal address book

          // execute command and verify result
          assertCommandBehavior(
                  helper.generateAddCommand(toBeAdded),
                  AddCommand.MESSAGE_DUPLICATE_TASK,
                  expectedAB,
                  expectedAB.getTaskList());

      }      
      
      /*
      @Test
      public void execute_add_deadline_natural_date_successful() throws Exception {
          TestDataHelper helper = new TestDataHelper();
          Task toBeAdded = helper.deadline_natural();
          TaskTracker expectedAB = helper.addToTaskTracker(toBeAdded);
          
          assertCommandBehavior(("add " + toBeAdded.getMessage() + " tmr"),
                  String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded),
                  expectedAB,
                  expectedAB.getTaskList());          
      }
      
     
      @Test
      public void execute_add_deadline_successful() throws Exception {
          
          TestDataHelper helper = new TestDataHelper();
          Task toBeAdded = helper.deadline1();
          TaskTracker expectedAB = helper.addToTaskTracker(toBeAdded);
          
          assertCommandBehavior(helper.generateAddCommand(toBeAdded),
                  String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded),
                  expectedAB,
                  expectedAB.getTaskList());
      }
    */

    
    /**
     * A utility class to generate test data.
     */
    class TestDataHelper{
        
        protected Task floating1() {
            return new Task("floating1");
        }
        
        protected Task deadline_natural() {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 1);
            return new Task("deadline_natural", cal.getTime());
        }
        
        protected Task deadline1() {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            return new Task("deadline1", cal.getTime());
        }
        
        protected Task event1() {
            Calendar cal1 = Calendar.getInstance();
            cal1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            Calendar cal2 = Calendar.getInstance();
            cal2.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
            return new Task("event1", cal1.getTime(), cal2.getTime());
        }
        
        /**
         * Generates a valid person using the given seed.
         * Running this function with the same parameter values guarantees the returned task will have the same state.
         * Each unique seed will generate a unique Task object.
         *
         * @param seed used to generate the task data field values
         */
        Task generateTask(int seed) throws Exception {
            return new Task("Task " + seed);
        }
        
        protected String generateAddCommand(Task toAdd) {
            if (toAdd.getIsFloating()) {
                return "add " + toAdd.getMessage();
            }
            else if (toAdd.getIsEvent()) {
                return "add " + toAdd.getMessage() + " " + toAdd.getStartTimeString() + " " + toAdd.getEndTimeString();
            }
            else {
                return "add " + toAdd.getMessage() + " " + toAdd.getDeadlineString(); 
            }
        }

       protected TaskTracker addToTaskTracker(Task toBeAdded) throws DuplicateTaskException {
            TaskTracker expectedAB = new TaskTracker();
            expectedAB.addTask(toBeAdded);
            return expectedAB;
        }
                    
    }

}
