#include <stdio.h>
#include "osm.h"
#include <sys/time.h>
#include <stdlib.h>
#include <limits.h>
#include <unistd.h>
#include <string.h>

#define SUCCESS 0
#define FAILURE 1
#define FACTOR 15
#define MICROSECS_2_NANOSECS 1000L
#define SEC_TO_MICROSEC 1000000L
#define INIT_TO_0 0.0
#define DEFULT_NUM_OP 50000
static timeMeasurmentStructure timeStruct;

/**
 * this is the empty function call that we used to check the call time.
 */
void emptyFunctionCall(){
}


/* Initialization function that the user must call
 * before running any other library function.
 * Returns 0 uppon success and -1 on failure.
 */
int osm_init(){
	timeStruct.functionInstructionRatio = INIT_TO_0;
	timeStruct.functionTimeNanoSecond = INIT_TO_0;
	timeStruct.instructionTimeNanoSecond = INIT_TO_0;
	timeStruct.numberOfIterations = INIT_TO_0;
	timeStruct.trapInstructionRatio = INIT_TO_0;
	timeStruct.trapTimeNanoSecond = INIT_TO_0;

	return SUCCESS;
}
/**
 *this is a helper function thats will help us to round the number of iteration.
 */
unsigned int roundIt(const unsigned int osm_it){
      
	unsigned int newNumOfIt = osm_it-(osm_it % FACTOR);

	return newNumOfIt;
}


/**
 * this is the helper function thats will receives structs, befien and end and will
 * fill calculate the time that took in nano seconds
 */
double convertTimeToNanoSeconds(struct timeval begin, struct timeval end){
	return (double)
			((end.tv_sec * SEC_TO_MICROSEC + (double)end.tv_usec)-
			(begin.tv_sec * SEC_TO_MICROSEC + begin.tv_usec))
			* MICROSECS_2_NANOSECS;
}

/* Time measurement function for an empty function call.
   returns time in nano-seconds upon success,
   and -1 upon failure.
   Zero iterations number is invalid.
*/
double osm_function_time(unsigned int osm_iterations){
if( !(osm_iterations > 0)){
	osm_iterations=DEFULT_NUM_OP;
}

struct timeval start,end;
int beginTime = gettimeofday(&start, NULL);
if(beginTime!=SUCCESS){
	printf("ERROR in getting the start time.\n");
	return FAILURE;

}
int i,roundedIt;
roundedIt = (int) roundIt(osm_iterations);

for(i=0;i<=roundedIt;i++){
	emptyFunctionCall();
	emptyFunctionCall();
	emptyFunctionCall();
	emptyFunctionCall();
	emptyFunctionCall();
	emptyFunctionCall();
	emptyFunctionCall();
	emptyFunctionCall();
	emptyFunctionCall();
	emptyFunctionCall();
	emptyFunctionCall();
	emptyFunctionCall();
	emptyFunctionCall();
	emptyFunctionCall();
	emptyFunctionCall();
	emptyFunctionCall();

}

int finishTime = gettimeofday(&end,NULL);
	if(finishTime!=SUCCESS){
			printf("ERROR in getting the finish time.\n");
			return FAILURE;
		}

double  diffTimeInNano = convertTimeToNanoSeconds(start,end);
double timeForEmptyFunc = (diffTimeInNano/roundedIt) / FACTOR;
	return timeForEmptyFunc;
}



/* Time measurement function for an empty trap into the operating system.
   returns time in nano-seconds upon success,
   and -1 upon failure.
   Zero iterations number is invalid.
   */
double osm_syscall_time(unsigned int osm_iterations){
if( osm_iterations == 0){
		osm_iterations=DEFULT_NUM_OP;
}
struct timeval start,end;
int beginTime = gettimeofday(&start, NULL);
if(beginTime!=SUCCESS){
		printf("ERROR in getting the start time.\n");
		return FAILURE;

	}
	int i,roundedIt;
	roundedIt = (int) roundIt(osm_iterations);
	
	for(i=0;i<=roundedIt;i++){
			OSM_NULLSYSCALL;
			OSM_NULLSYSCALL;
			OSM_NULLSYSCALL;
			OSM_NULLSYSCALL;
			OSM_NULLSYSCALL;
			OSM_NULLSYSCALL;
			OSM_NULLSYSCALL;
			OSM_NULLSYSCALL;
			OSM_NULLSYSCALL;
			OSM_NULLSYSCALL;
			OSM_NULLSYSCALL;
			OSM_NULLSYSCALL;
			OSM_NULLSYSCALL;
			OSM_NULLSYSCALL;
			OSM_NULLSYSCALL;
	}
int finishTime = gettimeofday(&end,NULL);
	if(finishTime!=SUCCESS){
			printf("ERROR in getting the finish time.\n");
			return FAILURE;
		}

	double  diffTimeInNano = convertTimeToNanoSeconds(start,end);
	double timeForNullFunc = (diffTimeInNano/roundedIt)/FACTOR;
	return timeForNullFunc;

}


/* Time measurement function for a simple arithmetic operation.
   returns time in nano-seconds upon success,
   and -1 upon failure.
   Zero iterations number is invalid.
   */
double osm_operation_time(unsigned int osm_iterations){
if( osm_iterations == 0){
			osm_iterations=DEFULT_NUM_OP;
	}
	struct timeval start,end;
	int beginTime = gettimeofday(&start, NULL);
	if(beginTime!=SUCCESS){
			printf("ERROR in getting the start time.\n");
			return FAILURE;
		}
	int i,roundedIt;
	roundedIt = (int) roundIt(osm_iterations);
	int simpleSum;
	for(i=0;i<=roundedIt;i++){
		simpleSum=1+2;
		simpleSum=3+4;
		simpleSum=5+6;
		simpleSum=7+8;
		simpleSum=9+10;
		simpleSum=11+12;
		simpleSum=13+14;
		simpleSum=15+16;
		simpleSum=17+18;
		simpleSum=19+20;
		simpleSum=21+22;
		simpleSum=23+24;
		simpleSum=25+25;
		simpleSum=26+27;
		simpleSum=28+29;
	}
	simpleSum+=0;
	int finishTime = gettimeofday(&end,NULL);
		if(finishTime!=SUCCESS){
				printf("ERROR in getting the finish time.\n");
				return FAILURE;
			}
	double  diffTimeInNano = convertTimeToNanoSeconds(start,end);
	double timeForSimpleOPFunc = (diffTimeInNano/roundedIt)/FACTOR;
	return timeForSimpleOPFunc;

}

timeMeasurmentStructure measureTimes(unsigned int numOfIterations){
	timeStruct.instructionTimeNanoSecond = osm_operation_time(numOfIterations);
	timeStruct.functionTimeNanoSecond = osm_function_time(numOfIterations);
	timeStruct.trapTimeNanoSecond = osm_syscall_time(numOfIterations);
	timeStruct.numberOfIterations=numOfIterations;
	timeStruct.functionInstructionRatio = timeStruct.functionTimeNanoSecond
	/timeStruct.instructionTimeNanoSecond;
	timeStruct.trapInstructionRatio = timeStruct.trapTimeNanoSecond
	/timeStruct.instructionTimeNanoSecond;
	char* machineName = (char*) malloc(HOST_NAME_MAX);
	int hostNameResult = gethostname(machineName, HOST_NAME_MAX);
	if(hostNameResult != SUCCESS){
		free(machineName);
	}
	else{
		strncpy(timeStruct.machineName,machineName,HOST_NAME_MAX);
	}
	return timeStruct;
}

/**
int main()
{
	timeMeasurmentStructure time = measureTimes(50000);

	//printf("results.numberOfIterations: %d\n",time.numberOfIterations);
	printf("results.instructionTimeNanoSecond: %f\n",time.instructionTimeNanoSecond);
	printf("results.functionTimeNanoSecond: %f\n",time.functionTimeNanoSecond);
	printf("results.trapTimeNanoSecond: %f\n",time.trapTimeNanoSecond);
	//printf("results.functionInstructionRatio: %f\n",time.functionInstructionRatio);
	//printf("results.trapInstructionRatio: %f\n",time.trapInstructionRatio);
	//printf("results.machineName: %s\n",time.machineName);

	return 0;
}
*/

