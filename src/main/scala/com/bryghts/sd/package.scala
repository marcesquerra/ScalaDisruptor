package com.bryghts

import scala.reflect.macros.Context
import java.util.concurrent.Executor
import com.lmax.disruptor.{EventHandler, RingBuffer, EventFactory}
import com.lmax.disruptor.dsl.{Disruptor => LDisruptor}

/**
 * Created by Marc Esquerrà on 08/06/2014.
 */
package object sd {

    implicit def factoryBuilder1[A]:       DisruptorFactory1[A]       = macro factoryBuilder1Impl[A]
    implicit def factoryBuilder2[A, B]:    DisruptorFactory2[A, B]    = macro factoryBuilder2Impl[A, B]
    implicit def factoryBuilder3[A, B, C]: DisruptorFactory3[A, B, C] = macro factoryBuilder3Impl[A, B, C]


    def factoryBuilder1Impl[A: c.WeakTypeTag](c: Context): c.Expr[DisruptorFactory1[A]] = {

        import c.universe._

        reify {

            new DisruptorFactory1[A] {

                def build(bufferSize: Int, realExecutor: Executor): Disruptor1[A] = {

                    class DisruptorEvent1() {
                        var a: A = _
                    }

                    class DisruptorEvent1Factory extends EventFactory[DisruptorEvent1] {
                        def newInstance: DisruptorEvent1 = new DisruptorEvent1()
                    }

                    val ldisruptor = new LDisruptor[DisruptorEvent1](new DisruptorEvent1Factory(), bufferSize, realExecutor)
                    val ringBuffer = ldisruptor.getRingBuffer();

                    class Disruptor1Impl(ldisruptor: LDisruptor[DisruptorEvent1], ringBuffer: RingBuffer[DisruptorEvent1]) extends Disruptor1[A] {

                        def apply(f: A => Unit): Unit = {
                            ldisruptor.handleEventsWith(new EventHandler[DisruptorEvent1]() {
                                override def onEvent(event: DisruptorEvent1, sequence: Long, endOfBatch: Boolean) =
                                    f(event.a)
                            })
                        }

                        def apply(a: A): Unit = {
                            val sequence = ringBuffer.next() // Grab the next sequence
                            try {
                                val event = ringBuffer.get(sequence) // Get the entry in the Disruptor
                                // for the sequence
                                event.a = a
                            }
                            finally {
                                ringBuffer.publish(sequence)
                            }
                        }

                        def start() = ldisruptor.start()

                        def shutdown() = ldisruptor.shutdown()
                    }

                    new Disruptor1Impl(ldisruptor, ringBuffer)
                }
            }
        }
    }


    def factoryBuilder2Impl[A: c.WeakTypeTag, B: c.WeakTypeTag](c: Context): c.Expr[DisruptorFactory2[A, B]] = {

        import c.universe._

        reify {

            new DisruptorFactory2[A, B] {

                def build(bufferSize: Int, realExecutor: Executor): Disruptor2[A, B] = {

                    class DisruptorEvent2() {
                        var a: A = _
                        var b: B = _
                    }

                    class DisruptorEvent2Factory extends EventFactory[DisruptorEvent2] {
                        def newInstance: DisruptorEvent2 = new DisruptorEvent2()
                    }

                    val ldisruptor = new LDisruptor[DisruptorEvent2](new DisruptorEvent2Factory(), bufferSize, realExecutor)
                    val ringBuffer = ldisruptor.getRingBuffer();

                    class Disruptor2Impl(ldisruptor: LDisruptor[DisruptorEvent2], ringBuffer: RingBuffer[DisruptorEvent2]) extends Disruptor2[A, B] {

                        def apply(f: (A, B) => Unit): Unit = {
                            ldisruptor.handleEventsWith(new EventHandler[DisruptorEvent2]() {
                                override def onEvent(event: DisruptorEvent2, sequence: Long, endOfBatch: Boolean) =
                                    f(event.a, event.b)
                            })
                        }

                        def apply(a: A, b: B): Unit = {
                            val sequence = ringBuffer.next() // Grab the next sequence
                            try {
                                val event = ringBuffer.get(sequence) // Get the entry in the Disruptor
                                // for the sequence
                                event.a = a
                                event.b = b
                            }
                            finally {
                                ringBuffer.publish(sequence)
                            }
                        }

                        def start() = ldisruptor.start()

                        def shutdown() = ldisruptor.shutdown()
                    }

                    new Disruptor2Impl(ldisruptor, ringBuffer)
                }
            }
        }
    }


    def factoryBuilder3Impl[A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag](c: Context): c.Expr[DisruptorFactory3[A, B, C]] = {

        import c.universe._

        reify {

            new DisruptorFactory3[A, B, C] {

                def build(bufferSize: Int, realExecutor: Executor): Disruptor3[A, B, C] = {

                    class DisruptorEvent3() {
                        var a: A = _
                        var b: B = _
                        var c: C = _
                    }

                    class DisruptorEvent3Factory extends EventFactory[DisruptorEvent3] {
                        def newInstance: DisruptorEvent3 = new DisruptorEvent3()
                    }

                    val ldisruptor = new LDisruptor[DisruptorEvent3](new DisruptorEvent3Factory(), bufferSize, realExecutor)
                    val ringBuffer = ldisruptor.getRingBuffer()

                    class Disruptor3Impl(ldisruptor: LDisruptor[DisruptorEvent3], ringBuffer: RingBuffer[DisruptorEvent3]) extends Disruptor3[A, B, C] {

                        def apply(f: (A, B, C) => Unit): Unit = {
                            ldisruptor.handleEventsWith(new EventHandler[DisruptorEvent3]() {
                                override def onEvent(event: DisruptorEvent3, sequence: Long, endOfBatch: Boolean) =
                                    f(event.a, event.b, event.c)
                            })
                        }

                        def apply(a: A, b: B, c: C): Unit = {
                            val sequence = ringBuffer.next() // Grab the next sequence
                            try {
                                val event = ringBuffer.get(sequence) // Get the entry in the Disruptor
                                // for the sequence
                                event.a = a
                                event.b = b
                                event.c = c
                            }
                            finally {
                                ringBuffer.publish(sequence)
                            }
                        }

                        def start() = ldisruptor.start()

                        def shutdown() = ldisruptor.shutdown()
                    }

                    new Disruptor3Impl(ldisruptor, ringBuffer)
                }
            }
        }
    }
}

package sd {

    import java.util.concurrent.Executor

    trait DisruptorFactory1[A] {

        def build(bufferSize: Int, realExecutor: Executor): Disruptor1[A]

    }

    trait DisruptorFactory2[A, B] {

        def build(bufferSize: Int, realExecutor: Executor): Disruptor2[A, B]

    }

    trait DisruptorFactory3[A, B, C] {

        def build(bufferSize: Int, realExecutor: Executor): Disruptor3[A, B, C]

    }

}