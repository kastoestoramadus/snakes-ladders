package org.kastoestoramadus.ladders

import org.joda.time.DateTime
import org.mongodb.scala.MongoClient
import org.mongodb.scala.bson.collection.immutable.Document

//FIXME
class SaveGameManger {
  val client: MongoClient = MongoClient("localhost:27017")
  val db = client.getDatabase("ladders")
  val collection = db.getCollection("logs")
  val doc: Document = Document("_id" -> DateTime.now().getMillis, "state" -> "21,34")
  collection.insertOne(doc)
}
