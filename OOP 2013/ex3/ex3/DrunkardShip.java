import java.util.Random;

/**
 * The same as human controlled but in addition, in each turn there is a 2 percent
 * chance that it will try to teleport.

 */


public class DrunkardShip extends SpaceShip {
	
	public static final int TARGETNUMBER=2;
	private Random random;
	
	
	public DrunkardShip(){
		random=new Random();
	}

	public void doAction(SpaceWars game){
		super.doAction(game);
		switch ((random.nextInt(50))){
		case TARGETNUMBER:
		teleport();
		break;
		default:
		break;
		}
		}

}
