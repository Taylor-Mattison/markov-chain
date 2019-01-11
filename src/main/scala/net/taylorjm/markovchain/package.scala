package net.taylorjm

import scala.collection.mutable

package object markovchain {

  implicit class RichMap[A,B](map: mutable.Map[A,B]) {
    def getOrInsertDefault(key: A, defaultValue: B): B = map.getOrElse(key,{
      map += key -> defaultValue
      defaultValue
    })
  }

  /** Select (sample) a key from distribution with the value's percent chance of being chosen */
  def sample[A](distribution: Map[A, Double]): A = {
    val p = scala.util.Random.nextDouble
    var seenItemP = 0d
    distribution.find{ case (_, itemProb) =>
      seenItemP += itemProb
      seenItemP >= p
    }.getOrElse(throw new IllegalArgumentException("distribution summed to less than 1.0"))._1
  }

}
