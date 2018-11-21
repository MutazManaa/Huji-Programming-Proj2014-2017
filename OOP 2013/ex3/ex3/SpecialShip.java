import java.awt.Image;
import oop.ex3.*;

/**
 * special methods for this ship that teleporting over 200.
 * It will always stand still, turn around in the direction to the closest ship
 * . If its angle from the nearest ship is 0.2 radians or less (in any
 *direction) then it will open fire.

 * @author mutazmanaa
 *
 */
public class SpecialShip extends SpaceShip {

	private int countPeriodOfRounds;
	private int telportAfterTwoHunderSteps;
	
	public static final int INFINITE_ENERGY=200;
	
	public SpecialShip(){
		telportAfterTwoHunderSteps=0;
		this.countPeriodOfRounds=0;
		}

	
	public void doAction(SpaceWars game){
		double
		angle=getPhysics().angleTo(game.getClosestShipTo(this).getPhysics());
		if(telportAfterTwoHunderSteps==200){/*if it reaches 200 steps it will telport otherwise
		it will rotate and fire.*/
		telportAfterTwoHunderSteps=0;
		teleport();
		}
		if(Math.abs(angle)<=STEP_ANGLE){
		fire(game);
		}
		if(angle<ZERO_ANGLE){
		getPhysics().move(false,RIGHT);
		}
		if(angle>ZERO_ANGLE){
		getPhysics().move(false,LEFT);
		}
		if(angle==ZERO_ANGLE) {
		getPhysics().move(false,ZERO_ANGLE);
		}
		this.energyLevel=INFINITE_ENERGY;
		telportAfterTwoHunderSteps++;
		}

	public Image getImage(){
		return GameGUI.ENEMY_SPACESHIP_IMAGE;
		}


}
