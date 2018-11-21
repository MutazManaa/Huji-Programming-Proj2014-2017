import java.awt.Image;
import oop.ex3.*;

/**
 * his spaceship is controlled by the user. The keys used to control the
 *spaceship are: left-arrow, right-arrow, up-arrow to turn left, right and accelerate.
 *s to fire a shot.
 *d to turn on the shield. t to teleport.

 * @author mutazmanaa
 *
 */
public class HumanControlledShip extends SpaceShip {
	public HumanControlledShip(){
		this.countPeriodOfRounds=1;
		}
	
	public void doAction(SpaceWars game){
		this.isShieldOn=false;
		if(game.getGUI().isTeleportPressed())
		teleport();
		if(game.getGUI().isUpPressed()==true &&
		game.getGUI().isRightPressed() && game.getGUI().isLeftPressed()==false)
		getPhysics().move(true, RIGHT);
		if(game.getGUI().isUpPressed()==true && game.getGUI().isLeftPressed() &&
		game.getGUI().isRightPressed()==false)
		getPhysics().move(true, LEFT);
		if(game.getGUI().isUpPressed()==true && game.getGUI().isLeftPressed()==false &&
		game.getGUI().isRightPressed()==false)
		getPhysics().move(true, ZERO_ANGLE);
		if(game.getGUI().isShieldsPressed())
		shieldOn();
		if(game.getGUI().isShotPressed())
		fire(game);
		if(game.getGUI().isUpPressed()==false && game.getGUI().isLeftPressed()==true &&
		game.getGUI().isRightPressed()==false)
		getPhysics().move(false, LEFT);
		if(game.getGUI().isUpPressed()==false && game.getGUI().isRightPressed()==true &&
		game.getGUI().isLeftPressed()==false)
		getPhysics().move(false, RIGHT);
		if(game.getGUI().isUpPressed()==false && game.getGUI().isRightPressed()==false &&
		game.getGUI().isLeftPressed()==false)
		getPhysics().move(false, ZERO_ANGLE);
		if(this.energyLevel<200)
		this.energyLevel++;
	}
		
	public Image getImag(){
		if(isShieldOn)
		return GameGUI.SPACESHIP_IMAGE_SHIELD;
		return GameGUI.SPACESHIP_IMAGE;
		
	}
	
	
}
