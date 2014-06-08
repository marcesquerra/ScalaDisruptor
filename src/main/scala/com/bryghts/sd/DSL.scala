package com.bryghts.sd

import java.util.concurrent.{Executors, Executor}
import scala.reflect.runtime.universe._

case object and

case class SettingBufferSize private[sd](copy: DisruptorConfiguration) {
    def slots(in: and.type): DisruptorConfiguration = copy
}

case class SettingExecutor private[sd](copy: DisruptorConfiguration) {
    def asExecutor(in: and.type): DisruptorConfiguration = copy
}

case class DisruptorConfiguration private[sd](bufferSize: Int = 1024, executor: Option[Executor] = None) extends CanBuildDisruptor
{
    protected def config:DisruptorConfiguration = this
}

class parameters1[A]()
class parameters2[A, B]()
class parameters3[A, B, C]()

object parameter {

    def apply[A: TypeTag] = new parameters1[A]()

}

object parameters {

    def apply[A: TypeTag, B: TypeTag]             = new parameters2[A, B]()
    def apply[A: TypeTag, B: TypeTag, C: TypeTag] = new parameters3[A, B, C]()

}


trait CanBuildDisruptor {

    def having(bufferSize: Int):SettingBufferSize = SettingBufferSize(config.copy(bufferSize = config.bufferSize))

    def having(executor: Executor):SettingExecutor = SettingExecutor(config.copy(executor = Some(executor)))

    def transmitting[A]      (p: parameters1[A])      (implicit factory: DisruptorFactory1[A]) =
        factory.build(config.bufferSize, realExecutor)

    def transmitting[A, B]   (p: parameters2[A, B])   (implicit factory: DisruptorFactory2[A, B]) =
        factory.build(config.bufferSize, realExecutor)

    def transmitting[A, B, C](p: parameters3[A, B, C])(implicit factory: DisruptorFactory3[A, B, C]) =
        factory.build(config.bufferSize, realExecutor)

    protected def config: DisruptorConfiguration

    private def realExecutor = config.executor.getOrElse(Executors.newCachedThreadPool())

}

