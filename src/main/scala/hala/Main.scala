package hala

import org.mongodb.scala.MongoClient

object Main extends App {
  private val client = MongoClient()
  val Dao = new Dao(client)

  new Cli().menu()
}
