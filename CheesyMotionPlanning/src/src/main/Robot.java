package src.main;

import java.util.Arrays;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import src.auto.AutoModeExecuter;
import src.examplepaths.profiles.PathAdapter;
import src.lib.util.math.RigidTransform2d;
import src.loops.Looper;
import src.loops.RobotStateEstimator;

/**
 * The main robot class, which instantiates all robot parts and helper classes
 * and initializes all loops. Some classes are already instantiated upon robot
 * startup; for those classes, the robot gets the instance as opposed to
 * creating a new object
 * 
 * After initializing all robot parts, the code sets up the autonomous and
 * teleoperated cycles and also code that runs periodically inside both
 * routines.
 * 
 * This is the nexus/converging point of the robot code and the best place to
 * start exploring.
 * 
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	// Get subsystem instances
	private Drive mDrive = Drive.getInstance();
	private RobotState mRobotState = RobotState.getInstance();
	private AutoModeExecuter mAutoModeExecuter = null;

	// Create subsystem manager
	private final SubsystemManager mSubsystemManager = new SubsystemManager(Arrays.asList(Drive.getInstance()));

	private Looper mEnabledLooper = new Looper();

	public Robot() {
	}

	public void zeroAllSensors() {
		mSubsystemManager.zeroSensors();
		mRobotState.reset(Timer.getFPGATimestamp(), new RigidTransform2d());
		mDrive.zeroSensors();
	}

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {
		mSubsystemManager.registerEnabledLoops(mEnabledLooper);
		mEnabledLooper.register(RobotStateEstimator.getInstance());

		// Pre calculate the paths we use for auto.
		PathAdapter.calculatePaths();
		zeroAllSensors();
	}

	/**
	 * Initializes the robot for the beginning of autonomous mode (set drivebase,
	 * intake and superstructure to correct states). Then gets the correct auto mode
	 * from the AutoModeSelector
	 * 
	 * @see AutoModeSelector.java
	 */
	@Override
	public void autonomousInit() {
		System.out.println("Auto start timestamp: " + Timer.getFPGATimestamp());

		if (mAutoModeExecuter != null) {
			mAutoModeExecuter.stop();
		}

		zeroAllSensors();

		mAutoModeExecuter = null;

		mEnabledLooper.start();
		mAutoModeExecuter = new AutoModeExecuter();
		// mAutoModeExecuter.setAutoMode(AutoModeSelector.getSelectedAutoMode());
		mAutoModeExecuter.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		allPeriodic();
	}

	/**
	 * Initializes the robot for the beginning of teleop
	 */
	@Override
	public void teleopInit() {
		// Start loopers
		mEnabledLooper.start();
		zeroAllSensors();
	}

	@Override
	public void teleopPeriodic() {

	}

	@Override
	public void disabledInit() {
		if (mAutoModeExecuter != null) {
			mAutoModeExecuter.stop();
		}
		mAutoModeExecuter = null;

		mEnabledLooper.stop();

		// Call stop on all our Subsystems.
		mSubsystemManager.stop();

		PathAdapter.calculatePaths();
	}

	@Override
	public void disabledPeriodic() {
		zeroAllSensors();
		allPeriodic();
	}

	@Override
	public void testPeriodic() {
	}

	/**
	 * Helper function that is called in all periodic functions
	 */
	public void allPeriodic() {
		mRobotState.outputToSmartDashboard();
		mSubsystemManager.writeToLog();
		mEnabledLooper.outputToSmartDashboard();
	}
}
