package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import guitests.guihandles.CommandBoxHandle;

public class HotKeysTest extends TaskTrackerGuiTest {

	@Test
	public void pressHotKeys() {
		//minimizes window when Esc key is pressed
	    assertWindowIsMinimized(commandBox.minimizeWindow());
	    System.out.println(stage.getStyle());
//		commandBox.runStaticCommand("f");
//		assertEquals(commandBox.getCommandInput(), "T.T");

	}

	private void assertWindowIsMinimized(CommandBoxHandle commandBox){
		assertTrue(stage.isIconified());
		
	}
//	private void assertCorrectAutoComplete(CommandBoxHandle commandBox){
//		assertEquals(commandBox.getCommandInput(), "find");
//	}
}
