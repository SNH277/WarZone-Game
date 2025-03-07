package Models;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test Suite for Model-related Tests.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        AdvanceOrderTest.class,
        CardAirliftTest.class,
        CardBlockadeTest.class,
        CardBombTest.class,
        CardNegotiateTest.class,
        ContinentTest.class,
        CountryTest.class,
        CurrentStateTest.class,
        DeployTest.class,
        MapTest.class,
        OrderExecutionPhaseTest.class,
        OrdersTest.class,
        PhaseTest.class,
        PlayerTest.class
})
public class ModelTestSuite {
    // No additional logic needed, the Suite will execute all included tests
}
