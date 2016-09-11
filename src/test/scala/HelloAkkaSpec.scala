import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}
import akka.actor.{Actor, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}

import scala.concurrent.Await
import scala.concurrent.duration._

class HelloAkkaSpec(_system: ActorSystem)
  extends TestKit(_system)
  with ImplicitSender
  with Matchers
  with FlatSpecLike
  with BeforeAndAfterAll {

  def this() = this(ActorSystem("HelloAkkaSpec"))

  val timeout: Duration = 10.seconds

  override def afterAll: Unit = {
    Await.result(system.terminate(), timeout)
  }

  "An HelloAkkaActor" should "be able to set a new greeting" in {
    val greeter = TestActorRef(Props[Greeter])
    greeter ! WhoToGreet("testkit")
    greeter.underlyingActor.asInstanceOf[Greeter].greeting should be("hello, testkit")
  }

  it should "be able to get a new greeting" in {
    val greeter = system.actorOf(Props[Greeter], "greeter")
    greeter ! WhoToGreet("testkit")
    greeter ! Greet
    expectMsgType[Greeting].message.toString should be("hello, testkit")
  }
}
