package main.logic;

import main.commons.core.EventsCenter;
import main.commons.core.Messages;
import main.commons.events.model.TaskTrackerChangedEvent;
import main.commons.events.ui.ShowHelpRequestEvent;
import main.logic.command.AddCommand;
import main.logic.command.CommandResult;
import main.logic.command.DeleteCommand;
import main.logic.command.DoneCommand;
import main.logic.command.EditCommand;
import main.logic.command.HelpCommand;
import main.logic.command.ListCommand;
import main.logic.command.ExitCommand;
import main.logic.command.ClearCommand;
import main.model.Model;
import main.model.ModelManager;
import main.model.ReadOnlyTaskTracker;
import main.model.TaskTracker;
import main.model.task.PriorityType;
import main.model.task.TaskType;
import main.model.task.ReadOnlyTask;
import main.model.task.Task;
import main.model.task.UniqueTaskList.DuplicateTaskException;
import main.storage.StorageManager;

import static main.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.*;
import org.junit.rules.TemporaryFolder;

import com.google.common.eventbus.Subscribe;


public class LogicManagerTest {
    
    @Rule
    public TemporaryFolder saveFolder = new TemporaryFolder();
        
    private Model model;
    private Logic logic;

    //These are for checking the correctness of the events raised
    private ReadOnlyTaskTracker latestSavedTaskTracker;
    private boolean helpShown;
    private int targetedJumpIndex;

    @Subscribe
    private void handleLocalModelChangedEvent(TaskTrackerChangedEvent abce) {
        latestSavedTaskTracker = new TaskTracker(abce.data);
    }

    @Subscribe
    private void handleShowHelpRequestEvent(ShowHelpRequestEvent she) {
        helpShown = true;
    }

    
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
        assertCommandBehavior(unknownCommand, String.format(MESSAGE_INVALID_COMMAND_FORMAT, Messages.MESSAGE_UNKNOWN_COMMAND));
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

    @Test
    public void execute_edit_invalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);
        assertCommandBehavior("edit wrong", expectedMessage);
        assertCommandBehavior("edit 16 Oct", expectedMessage);
        assertCommandBehavior("edit 0 ", expectedMessage);        

    }

    @Test
    public void execute_done_invalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DoneCommand.MESSAGE_USAGE);
        assertCommandBehavior("done wrong", expectedMessage);
        assertCommandBehavior("done", expectedMessage);
    }
    
    @Test
    public void execute_list_invalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(Messages.MESSAGE_INVALID_PARAMETERS,"ListCommand", ListCommand.MESSAGE_USAGE);
        assertCommandBehavior("list wrong", expectedMessage);
    }    
    
    @Test
    public void execute_delete_InvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);
        assertCommandBehavior("delete args", expectedMessage);
    }

    @Test
    public void execute_add_empty_description_invalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, Messages.MESSAGE_EMPTY_DESCRIPTION);
        assertCommandBehavior("add ", expectedMessage);
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
      public void execute_add_duplicate_notAllowed() throws Exception {
          // setup expectations
          TestDataHelper helper = new TestDataHelper();
          Task toBeAdded = helper.floating1();
          TaskTracker expectedAB = new TaskTracker();
          expectedAB.addTask(toBeAdded);

          // setup starting state
          model.addTask(toBeAdded); // task already in internal address book

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
            return new Task("floating1", PriorityType.NORMAL);
        }
        
        protected Task deadline_natural() {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 1);
            return new Task("deadline_natural", cal.getTime(), PriorityType.NORMAL);
        }
        
        protected Task deadline1() {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            return new Task("deadline1", cal.getTime(), PriorityType.NORMAL);
        }
        
        protected Task event1() {
            Calendar cal1 = Calendar.getInstance();
            cal1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            Calendar cal2 = Calendar.getInstance();
            cal2.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
            return new Task("event1", cal1.getTime(), cal2.getTime(), PriorityType.NORMAL);
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
        

        /**
         * Generates an TaskTracker with auto-generated tasks.
         */
        TaskTracker generateTaskTracker(int numGenerated) throws Exception{
            TaskTracker taskTracker = new TaskTracker();
            addToTaskTracker(taskTracker, numGenerated);
            return taskTracker;
        }

        /**
         * Generates an TaskTracker based on the list of Tasks given.
         */
        TaskTracker generateTaskTracker(List<Task> tasks) throws Exception{
            TaskTracker taskTracker = new TaskTracker();
            addToTaskTracker(taskTracker, tasks);
            return taskTracker;
        }

        /**
         * Adds auto-generated Task objects to the given TaskTracker
         * @param taskTracker The TaskTracker to which the Tasks will be added
         */
        void addToTaskTracker(TaskTracker taskTracker, int numGenerated) throws Exception {
            addToTaskTracker(taskTracker, generateTaskList(numGenerated));
        }

        /**
         * Adds the given list of Tasks to the given TaskTracker
         */
        void addToTaskTracker(TaskTracker taskTracker, List<Task> tasksToAdd) throws Exception {
            for (Task p: tasksToAdd) {
                taskTracker.addTask(p);
            }
        }
        

        TaskTracker addToTaskTracker(Task toBeAdded) throws DuplicateTaskException {
             TaskTracker expectedAB = new TaskTracker();
             expectedAB.addTask(toBeAdded);
             return expectedAB;
        }

        /**
         * Adds auto-generated Task objects to the given model
         * @param model The model to which the Tasks will be added
         */
        void addToModel(Model model, int numGenerated) throws Exception{
            addToModel(model, generateTaskList(numGenerated));
        }

        /**
         * Adds the given list of Tasks to the given model
         */
        void addToModel(Model model, List<Task> tasksToAdd) throws Exception{
            for (Task p: tasksToAdd) {
                model.addTask(p);
            }
        }
       
       /**
        * Generates a valid task using the given seed.
        * Running this function with the same parameter values guarantees the returned task will have the same state.
        * Each unique seed will generate a unique Task object.
        *
        * @param seed used to generate the task data field values
        */
       Task generateTask(int seed) throws Exception {
           return new Task("Task " + seed, PriorityType.NORMAL);
       }       
       
       /**
        * Generates a list of Tasks based on the flags.
        */
       List<Task> generateTaskList(int numGenerated) throws Exception{
           List<Task> tasks = new ArrayList<>();
           for (int i = 1; i <= numGenerated; i++) {
               tasks.add(generateTask(i));
           }
           return tasks;
       }

       List<Task> generateTaskList(Task... tasks) {
           return Arrays.asList(tasks);
       }

                    
    }

}
