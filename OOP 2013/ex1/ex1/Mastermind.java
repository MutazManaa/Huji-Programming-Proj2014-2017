
 
import il.ac.huji.cs.oop.mastermind.*;
/**
 *@author mutazmanaa
 *@return Void 
 
 */

public class Mastermind {

	public static void main(String[] args){

		
	    MastermindUI ui = MastermindUIFactory.newMastermindUI();
	    
         //Variables belong to the options of the game(Inputs)          
		int codeLength=0,numOfValues=0,maxGusses=0;
		
		// Variables to calculate number of cows
		int cowsTemp1=0,cowsTemp2=0,count=0,value=0;
		
		//Variables belong to statics and results
		int cows=0,bulls=0,numOfGames=0,numOfTurns=0,numOfWins=0,numOfWinTurns=0;
		
		boolean result = true;


                    

		do {
			//resetr game paramaters to zero
			cows=0;bulls=0;numOfWins=0;numOfWinTurns=0;numOfTurns=0; 
			if (numOfGames > 0){
			
				result = ui.askYesNo("Do you want to change the game options?");
			}
			// if the user decide not to play again then clearing UI
			if (result == false){
				ui.clear();
			}
			
			//if number of games is zero when starting the game or the the player continue to play
			else{

				numOfGames=0;
				numOfValues=0;
				maxGusses=0;

				//get paramaters of the new game			
				codeLength = ui.askNumber("Enter code length:");
				while (codeLength < 1){
					ui.displayErrorMessage("Value must be positive:");
					codeLength = ui.askNumber("Enter code length:");

				}
				numOfValues = ui.askNumber("Enter number of values:");
				while (numOfValues < 1){
					ui.displayErrorMessage("Value must be positive:");
					numOfValues = ui.askNumber("Enter number of values:");
				}
				maxGusses = ui.askNumber("Enter max number of guesses:");
				while (maxGusses < 1){
					ui.displayErrorMessage("Value must be positive:");
					maxGusses = ui.askNumber("Enter max number of guesses:");

				}
				//update the paramater of the new game
				ui.reset(codeLength, numOfValues, maxGusses);

				Code code = CodeGenerator.newCode(codeLength,numOfValues);
			
				numOfGames++;
				

				//check the number of the cows and the bulls
				for (int i=1;i<=maxGusses ;i++){
					Code guess = ui.askGuess("Enter guess:", codeLength);
					numOfTurns++;

					//countig the bulls
					for(int b=1;b<=codeLength;b++){
						if (guess.getValue(b) == code.getValue(b)){
							bulls++;

						}

					}
					//end counting the pulls
					
					//counting the cows
					for(int j=1; j<= codeLength;j++){

						value = guess.getValue(j);
						if(j!=1){
							for(int k=1;k<j;k++){
								if(guess.getValue(k) == value){
									count ++;
								}
							}
						}

						if(count < 1){

							for(int c=1; c<= codeLength;c++){
								if(value == guess.getValue(c)){
									cowsTemp1++;


								}
								if(value == code.getValue(c)){
									cowsTemp2 ++;

								}
							}

							if(cowsTemp1 >= cowsTemp2){
								cows += cowsTemp2;
							}
							if(cowsTemp1 < cowsTemp2){
								cows += cowsTemp1;
							}
						}

						count= 0;
						cowsTemp1= 0; 
						cowsTemp2= 0;
					}
					cows = cows - bulls;
					if(cows <0){
						
						cows=0;
						numOfWins--;
					}	
					//end couting the cows


					//check if the player win and sho the siutable stats 

					if(guess.equals(code)){
					
						bulls=codeLength;
						ui.showGuessResult(guess, bulls, cows);
						numOfWinTurns=numOfWinTurns+numOfTurns;
						numOfWins++;
						ui.displayMessage("You won in "+(numOfTurns)+" turns!");
						ui.showStats(numOfGames, numOfWins, (double) (numOfWins)/(numOfGames),
								(double) (numOfWinTurns)/(numOfWins));
						break;

					}
					else {
						ui.showGuessResult(guess, bulls, cows);
						bulls=cows=0;
					}
				}
				
				// check if the player losse and show the siutable stats
				if (bulls!=codeLength){
					ui.displayMessage("You lost! You failed to find the code!");
					if (numOfWins!=0){
						ui.showStats(numOfGames, numOfWins, (double)(numOfWins)/(numOfGames), 
								(double)(numOfWinTurns)/(numOfWins));

					}
					else {
						ui.showStats(numOfGames, numOfWins, (double)(numOfWins)/(numOfGames), Double.NaN);


					}


					//bulls=cows=numOfTurns=0;
				}

			}
		}while (ui.askYesNo("Another game?"));

		//ending the game
		ui.close();



	}
}








