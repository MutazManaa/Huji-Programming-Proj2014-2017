
#include "uthreads.h"
#include "Thread.h"
#include <list>
#include <map>
#include <queue>
#include <sys/time.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdexcept>

using namespace std;

#define uSecsInSec 1000000
#define STACK_SIZE 4096
#define FAILURE -1
#define SUCCESS 0
#define TERMINATED 1
#define TIMER_DONE 26
#define BLOCKED 2
#define EMPTY -1
#define NO_MORE_THREADS -1


#define LIB_NIG_ERROR "thread library error: quantum value must be grater than 0."
#define NO_SUCH_THREAD "thread library error: N0 such thread found."
#define SUS_MAIN_THREAD_ERROR "thread library error: suspend main thread NOT allowed."
#define MAX_THREADS_REACHED "thread library error: Maximum thread capacity reached."
#define SIG_ACTN_FAILED_ERROR "system error: Sigaction ERROR."
#define SETITIMER_FAILED_ERROR "system error: setitimer ERROR."
#define MEM_ERROR "thread library: memory allocation ERROR."
#define EMPTY_MASK_ERROR "system error: can not be empty mask."






int idArray[MAX_THREAD_NUM]={0};
int currentId;
int totalQuantums;
map<int, Thread*> getThreadFromId;

list<Thread*> readyRedList;
list<Thread*> readyOrangeList;
list<Thread*> readyGreenList;
list<Thread*> blockedList;


struct itimerval gTv;
struct sigaction gSa;
sigset_t gMask;




/*to get id for new hread*/
int getUniqueID()
{
	int i=0;
	for(i =0;i<MAX_THREAD_NUM;i++)
	{
		if(idArray[i] != 1)
		{
			return i;
		}

	}
	return FAILURE;
}

/**to check if we got more than max number of threads(100 threads)*/
bool checkIfMaxThreadsReached(){
	int i=0;
	bool flag=true;
	for(i=0;i<MAX_THREAD_NUM;i++)
	{
		if(idArray[i]==0)
		{
			flag=false;
			break;
		}
	}
	return flag;
}

void blockHandlerFromAction(int action) {
	sigprocmask(action, &gMask, NULL);
}

/*empty fuction to run main thread*/
void emptyFun(){}

/*return true if an id thread found in the color list, else false*/
bool  myFind(int id,list<Thread*> colorList)
{
	list<Thread*>::iterator it;
	for (it = colorList.begin(); it!=colorList.end(); it++)
	{
		if((*it)->getId() == id){
			return true;
		}
	}
	return false;
}


/*insert thread by it's id and it's priority to the suitable list*/
void insertToQueueFromPriority(int tid, Priority pr) {
	switch (pr) {
	case RED:
		readyRedList.push_back((new Thread(tid,*emptyFun,pr)));
		break;
	case ORANGE:
		readyOrangeList.push_back((new Thread(tid,*emptyFun,pr)));
		break;
	case GREEN:
		readyGreenList.push_back((new Thread(tid,*emptyFun,pr)));
		break;
	}
}



 /*function creat new thread to run , if no one to run return null*/

int getNewRunningThread() {
	int id;
	if (!readyRedList.empty())
	{
		id = (*readyRedList.front()).getId();
		readyRedList.pop_front();
		return id;
	}
	else
	{
		if (!readyOrangeList.empty())
		{
			id = (*readyOrangeList.front()).getId();
			readyOrangeList.pop_front();
			return id;
		}
		else
		{
			if (!readyGreenList.empty())
			{
				id = (*readyGreenList.front()).getId();
				readyGreenList.pop_front();
				return id;
			}
		}
		return EMPTY;
	}


}


/*function to switch between threads*/
void replaceRunningThread(int status) {
	int oldThreadId = currentId;
	currentId = getNewRunningThread();
	if(sigsetjmp(*getThreadFromId.at(oldThreadId)->getEnv(),1)!=SUCCESS)
		return;

	if (currentId == NO_MORE_THREADS)
	{
		currentId = oldThreadId;
		totalQuantums++;
		getThreadFromId.at(currentId)->increaseTotalQuantums();
		setitimer(ITIMER_VIRTUAL, &gTv, NULL);
		siglongjmp(*getThreadFromId.at(currentId)->getEnv(), 1);

	}
	else
	{
		Priority pr=RED;
		if(status == TERMINATED)
		{
			idArray[oldThreadId]=0;
			delete (getThreadFromId.at(oldThreadId));
			getThreadFromId.erase(oldThreadId);
		}
		else if ( status == BLOCKED )
		{
			blockedList.push_back((new Thread(oldThreadId,*emptyFun,pr)));
		}
		else if( status == TIMER_DONE)
		{
			pr = getThreadFromId.at(oldThreadId)->getPriority();
			insertToQueueFromPriority(oldThreadId, pr);
		}

		totalQuantums++;
		getThreadFromId.at(currentId)->increaseTotalQuantums();
		setitimer(ITIMER_VIRTUAL, &gTv, NULL);
		siglongjmp(*getThreadFromId.at(currentId)->getEnv(),1);

	}
}


/**
 * Create a new thread whose entry point is f,
 * return new id of thread when succeed.
 * return -1 if failed
 */
int uthread_spawn(void (*f)(void), Priority pr) {

	blockHandlerFromAction(SIG_BLOCK);

	if(checkIfMaxThreadsReached())
	{
		cerr << MAX_THREADS_REACHED << endl;
		blockHandlerFromAction(SIG_UNBLOCK);
		return FAILURE;

	}
	else
	{

		try
		{
			int tid=getUniqueID();
			Thread * newThread = new Thread(tid,*f, pr);
			idArray[tid]=1;

			getThreadFromId.insert(pair<int, Thread*>(tid, newThread));

			if (tid != 0)
			{
				insertToQueueFromPriority(tid, pr);
			}

			blockHandlerFromAction(SIG_UNBLOCK);
			return tid;
		}
		catch (...)
		{
			cerr << MEM_ERROR << endl;
			blockHandlerFromAction(SIG_UNBLOCK);
			return FAILURE;
		}
	}
}



/** Get the number of thread quantums*/

int uthread_get_quantums(int tid)
{
	Thread * temp;
	if(tid<0 || tid>MAX_THREAD_NUM)
	{
		cerr << NO_SUCH_THREAD << endl;
		return -1;
	}
	try
	{
		temp = getThreadFromId.at(tid);

	}
	catch (...)
	{
		cerr << NO_SUCH_THREAD << endl;
		return -1;
	}
	return temp->getTotalQuantums();

}


/* Get the total number of library quantums */
int uthread_get_total_quantums() {
	return totalQuantums;
}

/*timer to mesure time of quantums */
void timer(int quantum_usecs)
{
	gTv.it_value.tv_sec = quantum_usecs / 1000000;
	gTv.it_value.tv_usec = quantum_usecs % 1000000;
	gTv.it_interval.tv_sec = quantum_usecs / 1000000;
	gTv.it_interval.tv_usec = quantum_usecs % 1000000;
	setitimer(ITIMER_VIRTUAL, &gTv, NULL);

}

void maskHandler()
{

	sigemptyset(&gMask);
	sigaddset(&gMask, SIGVTALRM);
	if(sigaction(SIGVTALRM, &gSa, NULL)==FAILURE)
	{
		cerr << SIG_ACTN_FAILED_ERROR << endl;
	}
}


/* Initialize the thread library */
int uthread_init(int quantum_usecs) {
	if (quantum_usecs <= 0) {
		cerr << LIB_NIG_ERROR << endl;
		return FAILURE;
	}


	if(uthread_spawn(0, ORANGE)==FAILURE)
	{
		return FAILURE;
	}

	gSa.sa_handler = &replaceRunningThread;
	maskHandler();
	timer(quantum_usecs);
	currentId = 0;
	totalQuantums = 0;
	replaceRunningThread(0);
	idArray[0]=1;
	return SUCCESS;
}



/*remove an thread by id from a list*/
void removeById(int tid,list<Thread*> * colorList){
	for (std::list<Thread*>::iterator itr = (*colorList).begin(); itr != (*colorList).end(); /*nothing*/)
	{
		if ((*itr) -> getId() == tid)
			itr = (*colorList).erase(itr);
		else
			++itr;
	}
}



/*remove all threads from the map */
void removeAllThreads()
{
	for (map<int, Thread*>::iterator it = getThreadFromId.begin();
			it != getThreadFromId.end(); ++it) {
		delete (it->second);
	}

}

/* Terminate a thread */
int uthread_terminate(int tid) {
	blockHandlerFromAction(SIG_BLOCK);

	if (tid == 0)
	{
		removeAllThreads();
		blockHandlerFromAction(SIG_UNBLOCK);
		exit(0);
	}


	else if (tid == currentId)
	{
		blockHandlerFromAction(SIG_UNBLOCK);
		replaceRunningThread(TERMINATED);
		return 0;
	}
	else
	{
		try
		{
			removeById(tid,&readyGreenList);
			removeById(tid,&readyRedList);
			removeById(tid,&readyOrangeList);
			removeById(tid,&blockedList);

			Thread * temp = getThreadFromId.at(tid);
			getThreadFromId.erase(tid);
			idArray[tid]=0;
			delete (temp);
			blockHandlerFromAction(SIG_UNBLOCK);
			return 0;
		}

		catch(...)
		{
			cerr << NO_SUCH_THREAD << endl;
			blockHandlerFromAction(SIG_UNBLOCK);
			return FAILURE;
		}
	}


}

/* Get the id of the calling thread */
int uthread_get_tid() {
	return currentId;
}

/* Suspend a thread */
int uthread_suspend(int tid) {
	blockHandlerFromAction(SIG_BLOCK);

	if (myFind(tid,blockedList)) {
		blockHandlerFromAction(1);
		return 0;
	}
	if (tid == 0) {
		cerr << SUS_MAIN_THREAD_ERROR << endl;
		blockHandlerFromAction(SIG_UNBLOCK);
		return -1;
	}
	//cases for threads

	if (tid == currentId) {
		blockHandlerFromAction(SIG_UNBLOCK);
		replaceRunningThread(BLOCKED);
		return 0;

	} else if (myFind(tid,readyRedList)) {
		removeById(tid,(&readyRedList));
		blockedList.push_back((new Thread(tid,*emptyFun,RED)));
		blockHandlerFromAction(SIG_UNBLOCK);
		return 0;

	} else if (myFind(tid,readyOrangeList)) {
		removeById(tid,&readyOrangeList);
		blockedList.push_back((new Thread(tid,*emptyFun,ORANGE)));

		blockHandlerFromAction(SIG_UNBLOCK);
		return 0;

	} else if (myFind(tid,readyGreenList)) {

		removeById(tid,&readyGreenList);
		blockedList.push_back((new Thread(tid,*emptyFun,GREEN)));

		blockHandlerFromAction(SIG_UNBLOCK);
		return 0;
	}

	else {
		cerr << NO_SUCH_THREAD << endl;
		blockHandlerFromAction(SIG_UNBLOCK);
		return FAILURE;
	}
}

/* Resume a thread */
int uthread_resume(int tid) {
	blockHandlerFromAction(SIG_BLOCK);

	if (myFind(tid,readyRedList) || myFind(tid,readyGreenList) || myFind(tid,readyOrangeList)
			|| currentId == tid) {
		blockHandlerFromAction(SIG_UNBLOCK);
		return SUCCESS;
	} else if (myFind(tid,blockedList)) {

		removeById(tid,&blockedList);
		Priority pr = getThreadFromId.at(tid)->getPriority();
		insertToQueueFromPriority(tid, pr);
		blockHandlerFromAction(SIG_UNBLOCK);
		return SUCCESS;
	}
	cerr << NO_SUCH_THREAD << endl;
	blockHandlerFromAction(SIG_UNBLOCK);
	return FAILURE;
}



