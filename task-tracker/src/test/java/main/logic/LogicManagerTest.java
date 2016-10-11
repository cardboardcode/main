package main.logic;

import main.commons.core.Messages;
import main.model.Model;
import main.model.ModelManager;
import main.storage.StorageManager;

import static org.junit.Assert.assertEquals;

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
        String tempAddressBookFile = saveFolder.getRoot().getPath() + "TempAddressBook.xml";
        String tempPreferencesFile = saveFolder.getRoot().getPath() + "TempPreferences.json";
        logic = new LogicManager(model, new StorageManager(tempAddressBookFile, tempPreferencesFile));
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
    
//    @Test
//    public void execute_invalid() throws Exception {
//        String invalidCommand = "       ";
//        assertCommandBehavior(invalidCommand,String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
//    }
//    
//    @Test
//    public void execute_help() throws Exception {
//        assertCommandBehavior("help", HelpCommand.HELP_MESSAGE);
////        assertTrue(helpShown);
//    }

//    /**
//     * adapted from addressbook level 4
//     * 
//     * Executes the command and confirms that the result message is correct and
//     * also confirms that the following three parts of the LogicManager object's state are as expected:<br>
//     *      - the internal task list data are same as those in the {@code expected TaskList} <br>
//     *      - the backing list shown by UI matches the {@code shownList} <br>
//     *      - {@code expectedTaskList} was saved to the storage file. <br>
//     */
//    private void assertCommandBehavior(String inputCommand, String expectedMessage,
//                                       ReadOnlyTaskList expectedTaskList,
//                                       List<? extends ReadOnlyTask> expectedShownList) throws Exception {
//
//        //Execute the command
//        CommandResult result = logic.execute(inputCommand);
//
//        //Confirm the ui display elements should contain the right data
//        assertEquals(expectedMessage, result.feedbackToUser);
//        assertEquals(expectedShownList, model.getFilteredPersonList());
//
//        //Confirm the state of data (saved and in-memory) is as expected
//        assertEquals(expectedAddressBook, model.getAddressBook());
//        assertEquals(expectedAddressBook, latestSavedAddressBook);
//    }

}
