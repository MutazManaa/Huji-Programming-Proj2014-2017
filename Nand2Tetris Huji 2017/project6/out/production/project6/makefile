JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
        $(JC) $(JFLAGS) $*.java

CLASSES = Code.java CommandType.java MainDriver.java Parser.java SymbolTable.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
        $(RM) *.class