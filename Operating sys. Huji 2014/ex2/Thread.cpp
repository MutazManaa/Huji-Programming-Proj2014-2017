
#include "Thread.h"
#include <signal.h>
#include "BlackBox.h"

using namespace std;


Thread::Thread(int id ,void (*f)(void), Priority pr) {
	this->_id = id;
	this->_totalQuantums = 0;
	this->_pr = pr;
	address_t sp, pc;
	sp = (address_t)_stack + STACK_SIZE - sizeof(address_t);
	pc = (address_t)f;
	sigsetjmp(_env, 1);
	(_env->__jmpbuf)[JB_SP] = translate_address(sp);
	(_env->__jmpbuf)[JB_PC] = translate_address(pc);
	sigemptyset(&_env->__saved_mask);


}


int Thread::getId()
{
	return _id;
}

void Thread::setId(int id)
{
	this->_id=id;
}

int Thread::getTotalQuantums() {
	return _totalQuantums;
}

void Thread::increaseTotalQuantums() {
	_totalQuantums++;
}


sigjmp_buf * Thread::getEnv()
{
	return &_env;
}

int Thread::setEnv()
{
	return sigsetjmp(_env, 1);
}

Priority Thread::getPriority(){
	return _pr;
}

void Thread::setPriority(Priority pr)
{
	this->_pr=pr;
}

