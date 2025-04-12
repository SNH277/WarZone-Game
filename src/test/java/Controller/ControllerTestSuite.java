package Controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * The type Controller test suite.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        MainGameEngineTest.class,
        MapControllerTest.class,
        PlayerControllerTest.class
})
public class ControllerTestSuite {
}
