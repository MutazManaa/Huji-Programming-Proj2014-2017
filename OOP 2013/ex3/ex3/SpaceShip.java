import java.awt.Image;
import oop.ex3.*;

/**
 * The API spaceships need to implement for the SpaceWars game. 
 * It is your decision whether SpaceShip.java will be an interface, an abstract class,
 *  a base class for the other spaceships or any other option you will choose.
 *  
 * @author oop
 */

public abstract class SpaceShip{
	/**
	 * related to physics and movement
	 * 
	 */
	public static final int RIGHT=-1;
	public static final int LEFT=1;
	
	public static final int ZERO_ANGLE=0;
	public static final double STEP_ANGLE=0.2;
	
	
	
	
	
	/**
	 * variables for the related to health and
	 * energy
	 */

	protected int healthLevel,energyLevel,energySheild;
	protected SpaceWars game;
	protected boolean isShieldOn;
	protected Physics physics;
	protected int countPeriodOfRounds = 0;

	public SpaceShip() {
		this.healthLevel=10;
		this.energyLevel=200;
		this.energySheild=20;
		
		this.isShieldOn=false;
		physics=new SpaceShipPhysics();
		

	}


   
    /**
     * Does the actions of this ship for this round. 
     * This is called once per round by the SpaceWars game driver.
     * 
     * @param game the game object to which this ship belongs.
     */
    public void doAction(SpaceWars game) {

    }

    /**
     * This method is called every time a collision with this ship occurs 
     */
    public void collidedWithAnotherShip(){
    	if(isShieldOn==false)
    		this.healthLevel--;

    }

    /** 
     * This method is called whenever a ship has died. It resets the ship's 
     * attributes, and starts it at a new random position.
     */
    public void reset(){
    	this.healthLevel=20;
    	this.energyLevel=200;
    	this.isShieldOn=false;
    	physics=new SpaceShipPhysics();

    }

    /**
     * Checks if this ship is dead.
     * 
     * @return true if the ship is dead. false otherwise.
     */
    public boolean isDead() {
    	if(healthLevel==0){
    		return true;
    	}
    		
        return false;
    }

    /**
     * Gets the physics object that controls this ship.
     * 
     * @return the physics object that controls the ship.
     */
    public SpaceShipPhysics getPhysics() {
        return (SpaceShipPhysics) physics;
    }

    /**
     * This method is called by the SpaceWars game object when ever this ship
     * gets hit by a shot.
     */
    public void gotHit() {
    	if(isShieldOn==false)
    		this.healthLevel--;

    	

    }

    /**
     * Gets the image of this ship. This method should return the image of the
     * ship with or without the shield. This will be displayed on the GUI at
     * the end of the round.
     * 
     * @return the image of this ship.
     */
    public Image getImage(){
    	if(isShieldOn)
    		return GameGUI.ENEMY_SPACESHIP_IMAGE_SHIELD;
    		return GameGUI.ENEMY_SPACESHIP_IMAGE;
  	
    }

    /**
     * Attempts to fire a shot.
     * 
     * @param game the game object.
     */
    public void fire(SpaceWars game) {
    	
    	if(this.countPeriodOfRounds==8){
    		if(this.energyLevel>=20){
    		this.energyLevel-=20;
    		game.addShot((SpaceShipPhysics)physics);
    		}
    		this.countPeriodOfRounds=0;
    		}
    		this.countPeriodOfRounds++;
    }
       
    

    /**
     * Attempts to turn on the shield.
     */
    public void shieldOn() {
    	if(this.energyLevel>2){
    		this.isShieldOn=true;
    		energyLevel-=3;
    	}else{
    		this.isShieldOn=false;
    	}
    	
        
        
    }

    /**
     * Attempts to teleport.
     */
    public void teleport() {
    	if(this.energyLevel >=150){
    		this.energyLevel-=150;
    		this.physics=new SpaceShipPhysics();
    		}
    		
       
    }
    
}
