package src.examplepaths;

import src.auto.examplemodes.GearThenHopperShootModeBlue;
import src.examplepaths.profiles.PathAdapter;
import src.lib.util.control.Path;
import src.lib.util.math.RigidTransform2d;

/**
 * Path from the blue alliance wall to the blue boiler peg.
 * 
 * Used in GearThenHopperShootModeBlue
 * 
 * @see GearThenHopperShootModeBlue
 * @see PathContainer
 */
public class StartToBoilerGearBlue implements PathContainer {

	@Override
	public Path buildPath() {
		return PathAdapter.getBlueGearPath();
	}

	@Override
	public RigidTransform2d getStartPose() {
		return PathAdapter.getBlueStartPose();
	}

	@Override
	public boolean isReversed() {
		return true;
	}
}