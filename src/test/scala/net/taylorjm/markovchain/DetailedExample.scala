package net.taylorjm.markovchain

import scala.io.Source

object DetailedExample extends App {
  val spacesAndLineReturns = "[ \\n]+" // spaces and newlines
  val allNonWordCharacters = "\\W+" // any non-word character. word characters are a-z, A-Z, 0-9, and _

  val mobyDick = Source.fromFile("./src/test/scala/net/taylorjm/markovchain/res/MobyDick10Chapters.txt")
    .getLines.mkString("\n").split(spacesAndLineReturns) // Split into words

  for (depth <- 1 to 4) {
    val mc = new MarkovChain[String](depth)
    mc.train(mobyDick)
//    println(mc.trainedData) // Show the complete trained data (very large)
    val genResult = mc.generate().take(200).mkString(" ")
    println(s"Depth: $depth, Average # of next states ${mc.averageOfNextStates}") // View the probability distributions of the training data
    println(genResult)
  }

}
