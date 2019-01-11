package net.taylorjm.markovchain

import scala.io.Source

object SimpleExample extends App {

  val spacesAndLineReturns = "[ \\n]+"
  val trumpStateOfTheUnion = Source.fromFile("./src/test/scala/net/taylorjm/markovchain/res/TrumpStateOfTheUnion.txt")
    .getLines.mkString("\n").split(spacesAndLineReturns) // Split into words

  val mc = new MarkovChain[String](2) // Create a new Markov Chain that predicts based on the 2 previous words
  mc.train(trumpStateOfTheUnion)
  val genResult = mc.generate().take(200).mkString(" ") // Generate 200 words and combine them with spaces
  println(genResult)

}
