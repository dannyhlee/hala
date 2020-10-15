package hala

import org.mongodb.scala.MongoClient

object Main extends App {
  private val client = MongoClient()
  val halaDao = new Dao(client)

  new Cli().menu()
}
