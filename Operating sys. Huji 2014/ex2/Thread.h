
#ifndef THREAD_H_
#define THREAD_H_
#include <iostream>
#include "uthreads.h"
#include <stdio.h>
#include <setjmp.h>
#include <signal.h>
#include <unistd.h>
using namespace std;
#define STACK_SIZE 4096
class Thread {

public:

	Thread(int id,void (*f)(void), Priority pr);
	int getId();
	void setId(int id);

	int getTotalQuantums();
	void increaseTotalQuantums();

	sigjmp_buf * getEnv();
	int setEnv();


	Priority getPriority();
	void setPriority(Priority pr);

private:
	int _id;
	char _stack[STACK_SIZE];
	sigjmp_buf _env;
	int _totalQuantums;
	Priority _pr;

};

#endif /* THREAD_H_ */
