JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES =	\
	Building.java \
	MinHeap.java \
	RBT.java \
	Main.java
default:classes

classes:$(CLASSES:.java=.class)

clean:
	$(RM) *.class
