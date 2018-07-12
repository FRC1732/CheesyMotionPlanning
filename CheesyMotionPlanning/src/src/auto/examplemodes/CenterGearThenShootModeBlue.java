package src.auto.examplemodes;

import src.auto.AutoModeBase;
import src.auto.AutoModeEndedException;
import src.auto.exampleactions.DrivePathAction;
import src.auto.exampleactions.ResetPoseFromPathAction;
import src.examplepaths.CenterGearToShootBlue;
import src.examplepaths.PathContainer;
import src.examplepaths.StartToCenterGearBlue;

/**
 * Scores the preload gear onto the center peg then shoots the 10 preloaded fuel
 * 
 * @see AutoModeBase
 */
public class CenterGearThenShootModeBlue extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
		PathContainer gearPath = new StartToCenterGearBlue();
		runAction(new ResetPoseFromPathAction(gearPath));
		runAction(new DrivePathAction(gearPath));
		// runAction(new DeployIntakeAction());
		// runAction(new ScoreGearAction());
		runAction(new DrivePathAction(new CenterGearToShootBlue()));
		// runAction(new BeginShootingAction());
		// runAction(new WaitAction(15));
	}
}
