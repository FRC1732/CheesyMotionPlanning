package src.main;

import src.lib.util.control.Lookahead;
import src.lib.util.control.Path;
import src.lib.util.control.PathFollower;
import src.lib.util.math.RigidTransform2d;
import src.lib.util.math.Twist2d;
import src.main.Drive.DriveControlState;

public class DrivePathFollower {

	private final Drive drive;
	private Path mCurrentPath = null;
	private DriveControlState mDriveControlState;
	private RobotState mRobotState = RobotState.getInstance();
	private PathFollower mPathFollower;

	public DrivePathFollower(Drive drive) {
		this.drive = drive;
	}

	public synchronized void setWantDrivePath(Path path, boolean reversed) {
		if (mCurrentPath != path || mDriveControlState != DriveControlState.PATH_FOLLOWING) {
			drive.configureTalonsForSpeedControl();
			RobotState.getInstance().resetDistanceDriven();
			mPathFollower = new PathFollower(path, reversed, new PathFollower.Parameters(
					new Lookahead(Constants.kMinLookAhead, Constants.kMaxLookAhead, Constants.kMinLookAheadSpeed,
							Constants.kMaxLookAheadSpeed),
					Constants.kInertiaSteeringGain, Constants.kPathFollowingProfileKp,
					Constants.kPathFollowingProfileKi, Constants.kPathFollowingProfileKv,
					Constants.kPathFollowingProfileKffv, Constants.kPathFollowingProfileKffa,
					Constants.kPathFollowingMaxVel, Constants.kPathFollowingMaxAccel,
					Constants.kPathFollowingGoalPosTolerance, Constants.kPathFollowingGoalVelTolerance,
					Constants.kPathStopSteeringDistance));
			mDriveControlState = DriveControlState.PATH_FOLLOWING;
			mCurrentPath = path;
		} else {
			drive.setVelocitySetpoint(0, 0);
		}
	}

	public synchronized boolean isDoneWithPath() {
		if (drive.getDriveControlState() == DriveControlState.PATH_FOLLOWING && mPathFollower != null) {
			return mPathFollower.isFinished();
		} else {
			System.out.println("Robot is not in path following mode");
			return true;
		}
	}

	public synchronized void forceDoneWithPath() {
		if (drive.getDriveControlState() == DriveControlState.PATH_FOLLOWING && mPathFollower != null) {
			mPathFollower.forceFinish();
		} else {
			System.out.println("Robot is not in path following mode");
		}
	}

	public synchronized boolean hasPassedMarker(String marker) {
		if (drive.getDriveControlState() == DriveControlState.PATH_FOLLOWING && mPathFollower != null) {
			return mPathFollower.hasPassedMarker(marker);
		} else {
			System.out.println("Robot is not in path following mode");
			return false;
		}
	}

	public void updatePathFollower(double timestamp) {
		RigidTransform2d robot_pose = mRobotState.getLatestFieldToVehicle().getValue();
		Twist2d command = mPathFollower.update(timestamp, robot_pose, RobotState.getInstance().getDistanceDriven(),
				RobotState.getInstance().getPredictedVelocity().dx);
		if (!mPathFollower.isFinished()) {
			Kinematics.DriveVelocity setpoint = Kinematics.inverseKinematics(command);
			drive.setVelocitySetpoint(setpoint.left, setpoint.right);
		} else {
			drive.setVelocitySetpoint(0, 0);
		}
	}

}
