package src.main;

import java.util.function.Supplier;

import src.lib.util.math.Rotation2d;
import src.loops.Loop;
import src.loops.Looper;

// see cheesypoof drivetrain code. Implementation of motor controllers and sensors left to the user
public class Drive extends Subsystem {

	private static Drive mInstance = new Drive();

	public static Drive getInstance() {
		return mInstance;
	}

	// The robot drivetrain's various states.
	public enum DriveControlState {
		OPEN_LOOP, // open loop voltage control
		VELOCITY_SETPOINT, // velocity PID control
		PATH_FOLLOWING, // used for autonomous driving
	}

	public Drive() {
		// insert actual sensors here
		leftEncoder = () -> 1.0;
		rightEncoder = () -> 2.0;
		leftEncoderRate = () -> 3.0;
		rightEncoderRate = () -> 4.0;
		// get yaw from navx board (use cheesypoof wrapper, it is a lot better than what
		// kuai labs makes
		// see their 2017 code)
		gyro = () -> Rotation2d.fromDegrees(90);
		// configure motors in he
		// see cheesypoof code
		mPathFollower = new DrivePathFollower(this);
	}

	private DriveControlState mDriveControlState;
	public final DrivePathFollower mPathFollower;

	private Supplier<Double> leftEncoder;
	private Supplier<Double> rightEncoder;
	private Supplier<Double> leftEncoderRate;
	private Supplier<Double> rightEncoderRate;
	private Supplier<Rotation2d> gyro;

	private final Loop mLoop = new Loop() {
		@Override
		public void onStart(double timestamp) {
			synchronized (Drive.this) {
				// initialize drive
			}
		}

		@Override
		public void onLoop(double timestamp) {
			synchronized (Drive.this) {
				switch (mDriveControlState) {
				case OPEN_LOOP:
					return;
				case VELOCITY_SETPOINT:
					return;
				case PATH_FOLLOWING:
					if (mPathFollower != null) {
						mPathFollower.updatePathFollower(timestamp);
					}
					return;
				default:
					System.out.println("Unexpected drive control state: " + mDriveControlState);
					break;
				}
			}
		}

		@Override
		public void onStop(double timestamp) {
			stop();
		}
	};

	public DriveControlState getDriveControlState() {
		return mDriveControlState;
	}

	public double getLeftDistanceInches() {
		return leftEncoder.get();
	}

	public double getRightDistanceInches() {
		return rightEncoder.get();
	}

	public double getLeftVelocityInchesPerSec() {
		return leftEncoderRate.get();
	}

	public double getRightVelocityInchesPerSec() {
		return rightEncoderRate.get();
	}

	// following 2 methods are necessary to be synchronized if using cheesypoof navx
	// class

	public synchronized Rotation2d getGyroAngle() {
		return gyro.get();
	}

	public synchronized void setGyroAngle(Rotation2d angle) {
		// reset gyro
		gyro = () -> Rotation2d.fromDegrees(0);
	}

	public void configureTalonsForSpeedControl() {
		if (!usesTalonVelocityControl(mDriveControlState)) {
			// configure motor controllers for speed control here
		}
	}

	public synchronized void setVelocitySetpoint(double left_inches_per_sec, double right_inches_per_sec) {
		configureTalonsForSpeedControl();
		mDriveControlState = DriveControlState.VELOCITY_SETPOINT;
		updateVelocitySetpoint(left_inches_per_sec, right_inches_per_sec);
	}

	private synchronized void updateVelocitySetpoint(double left_inches_per_sec, double right_inches_per_sec) {
		if (usesTalonVelocityControl(mDriveControlState)) {
			// final double max_desired = Math.max(Math.abs(left_inches_per_sec),
			// Math.abs(right_inches_per_sec));
			// final double scale = max_desired > Constants.kDriveHighGearMaxSetpoint
			// ? Constants.kDriveHighGearMaxSetpoint / max_desired
			// : 1.0;

			// set velocity setpoint here
		} else {
			System.out.println("Hit a bad velocity control state");

			// set velocity setpoint to 0
		}
	}

	protected static boolean usesTalonVelocityControl(DriveControlState state) {
		if (state == DriveControlState.VELOCITY_SETPOINT || state == DriveControlState.PATH_FOLLOWING) {
			return true;
		}
		return false;
	}

	@Override
	public synchronized void stop() {
		// stop the robot
	}

	@Override
	public void registerEnabledLoops(Looper in) {
		in.register(mLoop);
	}

	@Override
	public void zeroSensors() {
		// zero the sensors
	}
}
