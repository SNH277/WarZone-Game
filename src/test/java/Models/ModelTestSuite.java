package Models;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * The type Model test suite.
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
        PlayerTest.class,
        TournamentTest.class
})

public class ModelTestSuite {
}
