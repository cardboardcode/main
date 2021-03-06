//@@author A0142686X- reused
package main.commons.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertNotNull;

public class AppUtilTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getImage_exitingImage(){
        assertNotNull(AppUtil.getImage("/images/pp.png"));
    }

    @Test
    public void getImage_nullGiven_assertionError(){
        thrown.expect(AssertionError.class);
        AppUtil.getImage(null);
    }

}
