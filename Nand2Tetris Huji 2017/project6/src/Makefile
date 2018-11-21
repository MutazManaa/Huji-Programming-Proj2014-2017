###############################################################################
#
# Makefile for Java project
#
# Students:
# Anass AlZurba, ID 20649999-8, Anass.Alzurba@mail.huji.ac.il
# Mutaz Manaa,   ID 20075045-3, Mutaz.manaa@mail.huji.aci 
#
###############################################################################

JAVAC=javac
JAVACFLAGS=

SRCS=*.java
EXEC=Assembler

TAR=tar
TARFLAGS=-cvf
TARNAME=project6.tar
TARSRCS=$(SRCS) README Makefile

all: compile

compile:
	$(JAVAC) $(JAVACFLAGS) $(SRCS)
	chmod +x $(EXEC)

tar:
	$(TAR) $(TARFLAGS) $(TARNAME) $(TARSRCS)

clean:
	rm -f *.class *~

