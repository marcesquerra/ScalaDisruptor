package com.bryghts.sd

trait BaseDisruptor {
    def start(): Unit
    def shutdown: Unit
}

trait Disruptor1[A] extends BaseDisruptor{

    def apply(f: A => Unit): Unit
    def apply(a: A): Unit

}

trait Disruptor2[A, B] extends BaseDisruptor {

    def apply(f: (A, B) => Unit): Unit
    def apply(a: A, b: B): Unit

}

trait Disruptor3[A, B, C] extends BaseDisruptor {

    def apply(f: (A, B, C) => Unit): Unit
    def apply(a: A, b: B, c: C): Unit

}


/**
 * Created by Marc Esquerrà on 08/06/2014.
 */
object Disruptor  extends CanBuildDisruptor  {

    protected def config = DisruptorConfiguration()

}