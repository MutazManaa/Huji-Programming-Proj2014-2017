import oop.ex3.*;

public class SpaceShipFactory {
	private static int count=0;
	private static final String HUMAN_CONTROLLED="h";
	private static final String DRUNKARD="d";
	private static final String RUNNER="r";
	private static final String AGRRESSIVE="a";
	private static final String BASHER="b";
	private static final String SPECIAL="s";
	private static final int ZERO=0;

    public static SpaceShip[] createSpaceShips(String[] args) {
    	SpaceShip[] array=new SpaceShip[args.length];
    	for(int i=ZERO;i<args.length;i++){
    	if(args[i].equals(HUMAN_CONTROLLED)){
    	count++;
    	array[i]=new HumanControlledShip();
    	continue;
    	}
    	if(args[i].equals(DRUNKARD)){
    	count++;
    	array[i]=new DrunkardShip();
    	continue;
    	}
    	if(args[i].equals(RUNNER)){
    	array[i]=new RunnerShip();
    	continue;
    	}
    	if(args[i].equals(AGRRESSIVE)){
    	array[i]=new AggressiveShip();
    	continue;
    	}
    	if(args[i].equals(BASHER)){
    	array[i]=new BasherShip();
    	continue;
    	}
    	if(args[i].equals(SPECIAL)){
    	array[i]=new SpecialShip();
    	continue;
    	}
    	}
    	if(count>1){
    		return null;
    	}
    	
    	return array;

        
    	
    }
}
