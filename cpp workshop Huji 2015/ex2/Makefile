
all:MIR

MIR : MIR.o
	g++  -std=c++11 -Wextra -Wall InstrumentalSong.o LyricsSong.o ParametersParser.o QueriesParser.o Song.o SongParser.o  MIR.o -o MIR

MIR.o:MIR.cpp ParametersParser.o QueriesParser.o SongParser.o InstrumentalSong.o LyricsSong.o Song.o 
	g++  -c -std=c++11 -Wextra -Wall MIR.cpp
	
InstrumentalSong.o:InstrumentalSong.cpp InstrumentalSong.h
	g++  -c -std=c++11 -Wextra -Wall InstrumentalSong.cpp
	
LyricsSong.o: LyricsSong.cpp LyricsSong.h
	g++ -c -std=c++11 -Wextra -Wall LyricsSong.cpp
	
Song.o: Song.cpp Song.h
	g++  -c -w -std=c++11 -Wextra -Wall Song.cpp
	

ParametersParser.o:ParametersParser.cpp ParametersParser.h
	g++  -c -std=c++11 -Wextra -Wall ParametersParser.cpp
	
		
SongParser.o:SongParser.cpp SongParser.h
	g++  -c -std=c++11 -Wextra -Wall SongParser.cpp
	

QueriesParser.o:QueriesParser.cpp QueriesParser.h
	g++  -c -std=c++11 -Wextra -Wall QueriesParser.cpp
	
	
clean:
	rm -f MIR *.o
	
tar: 
	tar -cvf ex2.tar MIR.cpp InstrumentalSong.cpp InstrumentalSong.h LyricsSong.cpp LyricsSong.h Song.cpp Song.h ParametersParser.cpp ParametersParser.h QueriesParser.cpp QueriesParser.h SongParser.cpp SongParser.h  Makefile README extension.pdf

.PHONY: all clean
