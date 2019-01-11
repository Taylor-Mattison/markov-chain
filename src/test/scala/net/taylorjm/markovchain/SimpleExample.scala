package net.taylorjm.markovchain

object SimpleExample extends App {

  val mc = new MarkovChain[String](1) // Create a new Markov Chain that predicts based on the previous 1 word
  val trainingData = Seq("A", "A", "A", "B")
  mc.train(trainingData)
  println(mc.trainedData)

  val result = mc.generate().toList.mkString(" ") // Generate words and combine them with spaces
  println(result)

}
