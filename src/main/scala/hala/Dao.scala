package hala

import scala.concurrent.Await
import scala.concurrent.duration.{Duration, SECONDS}
import org.mongodb.scala.bson.codecs.Macros._
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.{MongoClient, MongoCollection, Observable}
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.model.Sorts._
import org.mongodb.scala.model.Projections._

/** A Data Access Object for Comics.  The goal of this class is to encapsulate
 * all the mongoDB related parts of retrieving Comics, so the rest of our
 * application doesn't have to concern itself with mongo. */
class Dao(mongoClient : MongoClient) {
    // boilerplate code
    val codecRegistry = fromRegistries(
                            fromProviders(classOf[CommonLogEntry]),
                            MongoClient.DEFAULT_CODEC_REGISTRY
                        )
    val db = mongoClient.getDatabase("hala").withCodecRegistry(codecRegistry)
    val commonCollection : MongoCollection[CommonLogEntry] = db.getCollection("common")

    private def getResults[T](obs: Observable[T]): Seq[T] = {
        Await.result(obs.toFuture(), Duration(10, SECONDS))
    }

    def printResults[T](obs: Observable[T]): Unit = {
        getResults(obs).foreach(println(_))
    }

    def getCommonCollection(string: String) = getResults(commonCollection.find())

    def getByAttribute(attribute: String, whichAttribute: String) = commonCollection.find(equal(attribute, whichAttribute))

    /** Delete by title.  Returns true if deleted count is > 0, returns false otherwise.
     * This method is a little dangerous as is */
    def deleteByTitle(title: String) = {
        try {
            getResults(commonCollection.deleteMany(equal("title", title)))(0)
              .getDeletedCount > 0
        } catch {
            case e: Exception => {
                e.printStackTrace() //could be better
                false
            }
        }
    }

    def doIt(): Unit = {
        printResults(commonCollection.find())

        printResults(commonCollection.insertOne(CommonLogEntry("Test", Some(1000))))
    }
}
