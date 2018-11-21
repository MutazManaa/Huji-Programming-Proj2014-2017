import java.awt.Image;
import oop.ex3.*;

/**
 * this ship attempts to collide with other ships. It will always accelerate, and turn towards
 *the closest ship. If it gets within a distance of 0.2 units from another ship,
 *it will turn on its shield.

 * @author mutazmanaa
 *
 */
public class BasherShip extends SpaceShip {
	
	private double angle,distance;

	
	public void doAction(SpaceWars game){
		distance=getPhysics().distanceFrom(game.getClosestShipTo(this).getPhysics());
		angle=getPhysics().angleTo(game.getClosestShipTo(this).getPhysics());
		if(Math.abs(distance)<STEP_ANGLE){
		shieldOn();
		}
		if(angle<ZERO_ANGLE){
		getPhysics().move(true,RIGHT);
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
