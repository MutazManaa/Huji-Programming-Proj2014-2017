import oop.ex3.*;
import static org.junit.Assert.*;

import org.junit.Test;
/**
 * Tests to the whole classes that programmed by this project.
 * 
 * @author mutazmanaa
 *
 */

public class Unit_tests {
	
	private SpaceShip[] Arr = new SpaceShip[6];
	@Test(timeout = 1000)
	public void ShipFactory(){
	this.Arr[0] = new BasherShip() {};
	this.Arr[1] = new AggressiveShip(){};
	this.Arr[2] = new DrunkardShip() {};
	this.Arr[3] = new HumanControlledShip(){};
	this.Arr[4] = new RunnerShip();
	this.Arr[5] = new SpecialShip(){};

	}
	
	@Test(timeout = 1000)
	public void testStart(){
	ShipFactory();
	for(int index = 0; index < Arr.length ; index++){
	assertEquals(20,Arr[index].healthLevel);
	assertEquals(200,Arr[index].energyLevel);
	}
	}
	
	@Test(timeout = 1000)
	public void testCollidedShip() {
	ShipFactory();
	for(int index = 0; index < Arr.length ; index++){
	int health = this.Arr[index].healthLevel;
	this.Arr[index].collidedWithAnotherShip();
	assertEquals("Error: Health decresment not legal",this.Arr[index].healthLevel,--health);
	}
	}
	
	@Test(timeout = 1000)
	public void testCollidedWithShield() {
	ShipFactory();
	for(int index = 0; index < Arr.length ; index++){
	int health = this.Arr[index].healthLevel;
	int energy = this.Arr[index].energyLevel;
	int Energy_boost =this.Arr[index].energyLevel;
	this.Arr[index].isShieldOn = true;
	this.Arr[index].collidedWithAnotherShip();
	assertEquals("Error: Health decresment not legal",this.Arr[index].healthLevel, health);
	assertEquals("Error: Health incresment not legal",this.Arr[index].energyLevel,energy= energy + Energy_boost);

	}
	}
	
	@Test
	public void testGotHit() {
	ShipFactory();
	for(int index = 0; index < Arr.length ; index++){
	int health = this.Arr[index].healthLevel;
	int maxEnergy = this.Arr[index].energyLevel;
	int EnergyDec = this.Arr[index].healthLevel;
	this.Arr[index].gotHit();
	assertEquals("Error: Health decresment not legal",this.Arr[index].healthLevel,--health);
	assertEquals("Error: Health decresment not legal",this.Arr[index].energyLevel,
	maxEnergy= maxEnergy - EnergyDec );
	}
	}
	
	@Test
	public void testIsDead() {
	ShipFactory();
	for(int i = 0; i < Arr.length ; i++){
	int health = this.Arr[i].healthLevel;
	for (int j =health ; j > 0 ; j--){
	this.Arr[i].gotHit();
	if(j == 1)
	{System.out.println(this.Arr[i].healthLevel);
	assertEquals("output Error of isDead function",this.Arr[i].healthLevel == 0,true);
	}
	}
	}
	}

	
	@Test
	public void testReset() {
	ShipFactory();
	for(int i = 0; i < Arr.length ; i++){
	SpaceShipPhysics physics = this.Arr[i].getPhysics();
	this.Arr[i].reset();
	assertEquals("randomise physics is not legal",physics != this.Arr[i].getPhysics() ,true);
	}
	}

	@Test
	public void testGetPhysics(){
	ShipFactory();
	for(int i = 0; i < Arr.length ; i++){
		SpaceShipPhysics physics = this.Arr[i].getPhysics();
		assertEquals("Wrong getPhysics output",physics == this.Arr[i].physics,true);


	}
	}

	@Test
	public void testShieldOn(){
	ShipFactory();
	for(int i = 0; i < Arr.length ; i++){
	this.Arr[i].shieldOn();
	assertEquals("Error condition for sheild",this.Arr[i].isShieldOn,true);
	}
	}

	

}
