ScalaDisruptor
==============

An Scala wrapper around the LMAX Disruptor

Features:
 * Nice Scala like DSL to create a disruptor
 * Using macros, ScalaDisruptor generates in compile time all the boilerplate code for the consumer and producer
 * Uses Scala syntactic sugar and lambdas to nicely produce and consume form the disruptor
 
Example:

```scala

import com.bryghts.sd._
import scala.util.Random

object Example extends App {

    // Creates a Disruptor that will produce/consume an String and a Long
    val disruptor = Disruptor having 2048 slots and transmitting parameters [String, Long]

    // Add a first consumer that prints the received event
    disruptor { (a, b) =>
        println(s"a: $a, b: $b")
    }

    // Add a second consumer that prints the received event
    disruptor { (a, b) =>
        println(s"B a: $a, b: $b")
    }

    disruptor.start()

    (1 to 3) foreach { a =>

        // Produces one event and sends it to the Disruptor
        disruptor("Text: " + a, Random.nextLong())

        Thread.sleep(1000)
        if (a == 10) disruptor.shutdown
    }

}

```