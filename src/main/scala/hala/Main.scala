package hala

import java.util.logging.{Level, Logger}
import org.mongodb.scala.MongoClient

object Main extends App {

  // turn off mongo logging in console
  val mongoLogger: Logger = Logger.getLogger("org.mongodb.driver")
  mongoLogger.setLevel(Level.SEVERE)

  private val client = MongoClient()
  val Dao = new Dao(client)

  new Cli().menu()
}
