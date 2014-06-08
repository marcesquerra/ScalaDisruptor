package tests

import org.specs2.mutable.SpecificationWithJUnit
import com.bryghts.sd._
import scala.util.Random


/**
 * Created by Marc Esquerrà on 08/06/2014.
 */
class SDisruptor extends SpecificationWithJUnit {

    "The three disruptor examples should run without problems" in {

        DisruptorExamples.example1()
        DisruptorExamples.example2()
        DisruptorExamples.example3()

        true
    }

}



object DisruptorExamples  {

    def example1() {
        val disruptor = Disruptor having 2048 slots and transmitting parameter [String]

        disruptor { a =>
            println(s"a: $a")
        }

        disruptor.start()

        (1 to 3) foreach { a =>
            disruptor("Text: " + a)

            Thread.sleep(1000)
            if (a == 10) disruptor.shutdown
        }
    }

    def example2() {
        val disruptor = Disruptor having 2048 slots and transmitting parameters [String, Long]

        disruptor { (a, b) =>
            println(s"a: $a, b: $b")
        }

        disruptor { (a, b) =>
            println(s"B a: $a, b: $b")
        }

        disruptor.start()

        (1 to 3) foreach { a =>
            disruptor("Text: " + a, Random.nextLong())

            Thread.sleep(1000)
            if (a == 10) disruptor.shutdown
        }
    }

    def example3() {
        val disruptor = Disruptor having 2048 slots and transmitting parameters [String, Long, Int]

        disruptor { (a, b, c) =>
            println(s"a: $a, b: $b, c: $c")
        }

        disruptor.start()

        (1 to 3) foreach { a =>
            disruptor("Text: " + a, Random.nextLong(), a)

            Thread.sleep(1000)
            if (a == 10) disruptor.shutdown
        }
    }

}

