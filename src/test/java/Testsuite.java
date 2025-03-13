import Controller.MainGameEngineTest;
import Controller.MapControllerTest;
import Controller.PlayerControllerTest;
import Models.*;
import Services.GameServiceTest;
import Utils.CommandHandlerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * The type TestSuite - Runs all unit tests in the project.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        // Controller Tests
        MainGameEngineTest.class,
        MapControllerTest.class,
        PlayerControllerTest.class,

        // Model Tests
        AdvanceOrderTest.class,
        CardAirliftTest.class,
        CardBlockadeTest.class,
        CardBombTest.class,
        ContinentTest.class,
        CountryTest.class,
        CurrentStateTest.class,
        DeployTest.class,
        MapTest.class,
        OrdersTest.class,
        OrderExecutionPhaseTest.class,
        PhaseTest.class,
        PlayerTest.class,

        // Service Tests
        GameServiceTest.class,

        // Utility Tests
        CommandHandlerTest.class,

        // Tournament Tests
        TournamentTest.class,
})
public class TestSuite {
}
