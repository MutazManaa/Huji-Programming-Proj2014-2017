all: Thread.o uthreads.o
	ar rvs libuthreads.a Thread.o uthreads.o

Thread.o: Thread.cpp Thread.h
	g++ -std=c++11 -c -Wall -Wvla -g Thread.cpp

uthreads.o: uthreads.cpp uthreads.h BlackBox.h
	g++ -std=c++11 -c -Wall -Wvla -g uthreads.cpp

clean: 
	rm -f *.o *.a

tar:
	tar -cvf ex2.tar Thread.cpp Thread.h uthreads.cpp uthreads.h README Makefile
