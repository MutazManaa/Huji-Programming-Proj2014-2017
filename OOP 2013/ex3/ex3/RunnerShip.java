import java.awt.Image;
import oop.ex3.*;

/**
 * this class represent a spaceship that attempts to run away from the fight.
 * It will constantly accelerate, and turn
 *away from the nearest ship. The runner has the spying ability and will attempt spying on each
 *round. It will try to get the nearest ship cannon angle from himself.
 *If that angle is smaller than 0.2
 *radians (in any direction) and the distance from the nearest ship is smaller than 0.2 units, the
 *runner will try to teleport.
 *
 * @author mutazmanaa
 *
 */
public class RunnerShip extends SpaceShip {
	
	public void doAction(SpaceWars game){
		double angle=getPhysics().angleTo(game.getClosestShipTo(this).getPhysics());
		double distance=getPhysics().distanceFrom(game.getClosestShipTo(this).getPhysics());
		if(Math.abs(angle)<STEP_ANGLE && Math.abs(distance)<STEP_ANGLE){
		teleport();
		}
		if(angle<ZERO_ANGLE){
		getPhysics().move(true,LEFT);
		}
		if(angle>ZERO_ANGLE){
		getPhysics().move(true,RIGHT);
		}
		if(angle==ZERO_ANGLE) {
		getPhysics().move(true,ZERO_ANGLE);
		}
		if(this.energyLevel<200)
		this.energyLevel++;
		}
	
	

	
	



}
