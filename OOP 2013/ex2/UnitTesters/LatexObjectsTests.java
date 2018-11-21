import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({ BracketsMathTermTest.class, FractionMathTermTest.class,
		SimpleBinaryOpMathTermTest.class, SimpleMathTermTest.class,
		SumMathTermTest.class, TestScenarios.class })
public class LatexObjectsTests {

}
