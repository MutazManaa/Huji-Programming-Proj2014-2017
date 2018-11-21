import java.awt.Image;

import oop.ex3.*;

/**
 * This ship pursues other ships and tries to fire at them. It will always accelerate, and
 *turn towards the nearest ship. If its angle from the nearest ship is 0.2 radians or less (in any
 *direction) then it will open fire.

 * @author mutazmanaa
 *
 */
public class AggressiveShip extends BasherShip {
	private double angle;
	private int countPeriodOfRounds;
	public AggressiveShip(){
	countPeriodOfRounds=0;
	}
	
	/**
	* Does the actions of this ship for this round.
	* This is called once per round by the SpaceWars game driver.
	*
	* @param game the game object to which this ship belongs.
	*/

	
	public void doAction(SpaceWars game){
		angle=getPhysics().angleTo(game.getClosestShipTo(this).getPhysics());
		if(Math.abs(angle)<=STEP_ANGLE){
		fire(game);
		}
		if(angle<ZERO_ANGLE){
		getPhysics().move(true, RIGHT);
		}
		if(angle>ZERO_ANGLE){
		getPhysics().move(true,LEFT);
		}
		if(angle==ZERO_ANGLE) {
		getPhysics().move(true,ZERO_ANGLE);
		}
		if(this.energyLevel<200)
		this.energyLevel++;
		}


}
