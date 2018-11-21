all:
	g++ -g -std=c++11 -Wextra -Wall -Wvla -pthread -DNDEBUG MatrixExceptions.h Matrix.hpp -o Matrix.hpp.gch
	
tar: 
	tar -cvf ex3.tar README MatrixExceptions.h Matrix.hpp Makefile extension.pdf  
	
clean:
	rm -f *.o *~ Matrix.hpp.gch
	
PHONY:
	all clean
	
