all: random filter 


filter:
	javac Filter.java

random:
	javac EvaluateCFRandom.java

clean:
	rm *.class
