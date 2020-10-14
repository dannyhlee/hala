package hala

import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.{MongoClient, MongoCollection, Observable}
import org.mongodb.scala.bson.codecs.Macros._
import scala.concurrent.Await
import scala.concurrent.duration.{Duration, SECONDS}

object DbRunner {

    // Mongo's scala boilerplate:
    // include classOf[T] for all of your classes

    // codecs are using default translations to convert data to bson

    val codecRegistry = fromRegistries(fromProviders(classOf[CommonLogEntry]), MongoClient.DEFAULT_CODEC_REGISTRY)
    var client = MongoClient()

    // gets db and converts from bson?
    val db = client.getDatabase("hala").withCodecRegistry(codecRegistry)

    // puts the data in a Scala collection from db
    val collection : MongoCollection[CommonLogEntry] = db.getCollection("CommonLogCollection")

    // helper function for access and printing
    def getResults[T](obs: Observable[T]): Seq[T] = {
        Await.result(obs.toFuture(), Duration(10, SECONDS))
    }

    def printResults[T](obs: Observable[T]): Unit = {
        getResults(obs).foreach(println(_))
    }

    def doIt(): Unit = {
        printResults(collection.find())

        printResults(collection.insertOne(CommonLogEntry("Test", Some(1000))))
    }


}
