package net.taylorjm.markovchain

import scala.collection.immutable.Stream
import scala.collection.mutable
import scala.reflect.ClassTag

class MarkovChain[State : ClassTag](depth: Int) {
  import MarkovChain._

  private val _trainedData: mutable.Map[Seq[Node], Stats] = mutable.Map.empty
  /** Returns a copy of the trained data */
  def trainedData: Map[Seq[Node], Stats] = _trainedData.toMap

  /** Train the MarkovChain with training data
    * @param data A Seq of the training data states
    */
  def train(data: Seq[State]): MarkovChain[State] = {
    val dataWithSentinels: Seq[Node] = Seq.fill(depth)(StartNode) ++ data.map(StateNode[State]) :+ EndNode

    dataWithSentinels.sliding(depth + 1).map(s => (s.init, s.last))
      .foreach{ case (currentState, nextState) =>
        val stats = _trainedData.getOrInsertDefault(currentState, new Stats)
        stats.frequency += 1
        val nextFrequency = stats.nextStates.getOrElse(nextState, BigInt(0))
        stats.nextStates += nextState -> (nextFrequency + 1)
      }

    this
  }

  /** Generate a stream of States based on the training data and the starting state.
    * If there is no training data for the current state, (ex. if no training has been done) then end the stream.
    */
  def generate(startState: Seq[State] = Seq.empty): Stream[State] = {
    def generateHelper(state: Seq[Node]): Stream[State] = {
      val nextState = _trainedData.get(state)
        .map(_.nextStatesPercentage)
        .map(sample)
        .getOrElse(EndNode) // No training data!

      nextState match {
        case StartNode => throw new java.lang.AssertionError("nextState should never be StartNode")
        case EndNode => Stream.empty[State]
        case StateNode(s: State) => s #:: generateHelper(state.tail :+ nextState)
      }
    }

    generateHelper(Seq.fill(depth - startState.length)(StartNode) ++ startState.take(depth).map(StateNode[State]))
  }

  /** Remove all training data from this Markov Chain */
  def clear(): Unit = _trainedData.clear()

  /** The number of nextStates per state. A metric of how constrained the generate stream will be. */
  def averageOfNextStates: Float = _trainedData.values.map(state => state.nextStates.size).sum / _trainedData.size.toFloat


  override def toString: String = _trainedData
    .map{ case (start, next) => start.mkString(" ") + "... " + next }
    .mkString("MarkovChain(", ", ", ")")

}
object MarkovChain {

  /** A node in the markov chain which is either a start/end sentinel or a node with state */
  sealed trait Node
  /** Sentinel value for the start of a chain. */
  case object StartNode extends Node {
    override def toString: String = "<Start>"
  }
  /** Sentinel value for the end of a chain. */
  case object EndNode extends Node {
    override def toString: String = "<End>"
  }
  /** A state within the chain. */
  case class StateNode[State](state: State) extends Node {
    override def toString: String = state.toString
  }

  class Stats {
    var frequency: BigInt = 0
    val nextStates: mutable.Map[Node, BigInt] = mutable.Map.empty

    def nextStatesPercentage: Map[Node, Double] = nextStates.mapValues(v => v.doubleValue / frequency.doubleValue).toMap
    override def toString: String = nextStatesPercentage
      .mapValues(percentage => f"${percentage * 100}%.2f" + '%') //f"$percentage%.2f"
      .mkString("(", ", ", ")")
  }
}