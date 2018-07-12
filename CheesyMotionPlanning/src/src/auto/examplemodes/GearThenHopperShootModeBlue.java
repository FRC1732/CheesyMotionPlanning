package src.auto.examplemodes;

import edu.wpi.first.wpilibj.Timer;
import src.auto.AutoModeBase;
import src.auto.AutoModeEndedException;
import src.auto.exampleactions.CorrectPoseAction;
import src.auto.exampleactions.DrivePathAction;
import src.auto.exampleactions.ResetPoseFromPathAction;
import src.examplepaths.BoilerGearToHopperBlue;
import src.examplepaths.PathContainer;
import src.examplepaths.StartToBoilerGearBlue;
import src.examplepaths.profiles.PathAdapter;
import src.lib.util.math.RigidTransform2d;

/**
 * Scores the preload gear onto the boiler-side peg then deploys the hopper and
 * shoots all 60 balls (10 preload + 50 hopper).
 * 
 * This was the primary autonomous mode used at SVR, St. Louis Champs, and FOC.
 * 
 * @see AutoModeBase
 */
public class GearThenHopperShootModeBlue extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
		PathContainer gearPath = new StartToBoilerGearBlue();
		double start = Timer.getFPGATimestamp();
		runAction(new ResetPoseFromPathAction(gearPath));
		runAction(new DrivePathAction(gearPath));

		// runAction(new ParallelAction(
		// Arrays.asList(new Action[] { new DrivePathAction(gearPath), new
		// ActuateHopperAction(true), })));

		// runAction(new ParallelAction(Arrays.asList(new Action[] { new
		// SetFlywheelRPMAction(2900.0), // spin up flywheel
		// // to save time
		// new ScoreGearAction(), new DeployIntakeAction(true) })));
		runAction(new CorrectPoseAction(RigidTransform2d.fromTranslation(PathAdapter.getBlueGearCorrection())));
		runAction(new DrivePathAction(new BoilerGearToHopperBlue()));
		System.out.println("Shoot Time: " + (Timer.getFPGATimestamp() - start));
		// runAction(new BeginShootingAction());
		// runAction(new WaitAction(15));
	}
}
