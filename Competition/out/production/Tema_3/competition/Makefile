JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Observer.java \
	Subject.java \
	Strategy.java \
	Player.java \
	Team.java \
	BasketballTeam.java \
	FootballTeam.java \
	HandballTeam.java \
	TeamFactory.java \
	Competition.java \
	CurrentObservedState.java \
	Game.java \
	RefereeVisitor.java \
	RefereeVisitorImpl.java \
	StrategyContext.java \
	Main.java


default: classes

run: classes
	java Main ${COMANDA} ${IN1} ${IN2} ${OUT}

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
