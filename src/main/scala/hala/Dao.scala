package hala

import scala.concurrent.Await
import scala.concurrent.duration.{Duration, SECONDS}
import org.mongodb.scala.bson.codecs.Macros._
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.{FindObservable, MongoClient, MongoCollection, MongoDatabase, Observable}
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.model.Sorts._
import org.mongodb.scala.model.Projections._


class Dao(mongoClient : MongoClient) {
    val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[LogEntry]), MongoClient.DEFAULT_CODEC_REGISTRY) : CodecRegistry
    val db: MongoDatabase = mongoClient.getDatabase("hala").withCodecRegistry(codecRegistry) : MongoDatabase
    val commonCollection : MongoCollection[LogEntry] = db.getCollection("common") : MongoCollection[LogEntry]

    private def getResults[T](obs: Observable[T]): Seq[T] = {
        Await.result(obs.toFuture(), Duration(10, SECONDS))
    }

    def printResults[T](obs: Observable[T]): Unit = {
        getResults(obs).foreach(println(_))
    }

    def getCommonCollection(string: String): Seq[LogEntry] = getResults(commonCollection.find())

    def getByAttribute(attribute: String, whichAttribute: String):FindObservable[LogEntry] = commonCollection.find(equal(attribute, whichAttribute))

    /** Delete by title.  Returns true if deleted count is > 0, returns false otherwise.
     * This method is a little dangerous as is */
    def deleteByTitle(title: String): Boolean = {
        try getResults(commonCollection.deleteMany(equal("title", title))).head
          .getDeletedCount > 0 catch {
            case e: Exception =>
                e.printStackTrace() //could be better
                false
        }
    }

    def doIt(): Unit = {
        printResults(commonCollection.find())

//        printResults(commonCollection.insertOne(LogEntry("Test", Some(1000))))
    }
}
