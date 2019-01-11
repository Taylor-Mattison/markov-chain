# Markov Chain

[Markov Chain on Wikipedia](https://en.wikipedia.org/wiki/Markov_chain)

### Sample Usage
```scala
  import net.taylorjm.markovchain.MarkovChain
  
  val mc = new MarkovChain[String](1) // Create a new Markov Chain that predicts based on the previous 1 word
  val trainingData = Seq("A", "A", "A", "B")
  mc.train(trainingData)
  println(mc.trainedData) // Map(List(B) -> (<End> -> 100.00%), List(<Start>) -> (A -> 100.00%), List(A) -> (A -> 66.67%, B -> 33.33%))

  val result = mc.generate().toList.mkString(" ") // Generate words and combine them with spaces
  println(result) // A A ... B (Number of A's is randomly chosen. 66% chance of A and 33% chance of B per the trained data.)
```

### Example Output
##### Trained using the first 10 chapters of Moby Dick, predicting based on the last 2 words:
```text
Some years ago—never mind how long each one had been a sailor and a full ship. 
Hurrah, boys; now we’ll have the satisfaction of knowing something about me in my shaggy jacket of the night, of the equator; yea, ye gods!
I saw the traces of a whaling captain had provided the chapel with a sober cannibal than a Nantucket craft, because there was something almost sublime in it.
```