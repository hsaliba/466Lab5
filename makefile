all: random filter list


filter:
	javac Filter.java

random:
	javac EvaluateCFRandom.java

list:
	javac EvaluateCFList.java

clean:
	rm *.class
