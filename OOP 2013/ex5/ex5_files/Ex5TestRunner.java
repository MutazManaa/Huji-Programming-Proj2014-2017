import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * A runner for Ex5Tester
 * @author oop
 *
 */
public class Ex5TestRunner {
	public static void main(String[] args) {
		// Running tests
		Result result = JUnitCore.runClasses(Ex5Tester.class);
		
		// Iterating failures
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}

		// Summary
		if (result.wasSuccessful()) {
			System.out.println("Perfect!");
		} else {
			System.err.println(result.getFailureCount()+" tests failed");
			System.exit(result.getFailureCount());
		}
	}
} 
