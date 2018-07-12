package src.auto.exampleactions;

import edu.wpi.first.wpilibj.Timer;
import src.examplepaths.PathContainer;
import src.lib.util.math.RigidTransform2d;
import src.main.Drive;
import src.main.RobotState;

/**
 * Resets the robot's current pose based on the starting pose stored in the
 * pathContainer object.
 * 
 * @see PathContainer
 * @see Action
 * @see RunOnceAction
 */
public class ResetPoseFromPathAction extends RunOnceAction {

	protected PathContainer mPathContainer;

	public ResetPoseFromPathAction(PathContainer pathContainer) {
		mPathContainer = pathContainer;
	}

	@Override
	public synchronized void runOnce() {
		RigidTransform2d startPose = mPathContainer.getStartPose();
		RobotState.getInstance().reset(Timer.getFPGATimestamp(), startPose);
		Drive.getInstance().setGyroAngle(startPose.getRotation());
	}
}
