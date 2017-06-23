package org.kastoestoramadus.ladders

import com.mongodb.casbah.{MongoCollection, MongoConnection}
import com.mongodb.casbah.commons.MongoDBObject
import org.joda.time.DateTime
import org.kastoestoramadus.ladders.model.{BoardPosition, PlayerId}

// should not be a static object
// FIXME lack of tests // not ready for production
object SaveGameManger extends App{
  val DELIM = ","

  var mongo: MongoConnection = null
  var coll: MongoCollection = null
  try {
    mongo = MongoConnection()
    coll = mongo("ladder-game")("logs")
    println(s"last entry at mongo: $getLast")
  } catch {
    case _ => println("Something wrong with mongo connection. Tried on default port on localhost.")
  }
  // TODO serialization with play Reads/Writes
  def addEntry(entry: Entry) = {
    val builder = MongoDBObject.newBuilder
    builder += "players" -> entry.players.mkString(DELIM)
    builder += "next" -> entry.next
    builder += "positions" -> entry.positions.mkString(DELIM)
    builder += "timestamp" -> DateTime.now().getMillis
    coll += builder.result
  }

  def getLast(): Entry = {
    val eAsMap = coll.last.toMap

    Entry(
      eAsMap.get("players").asInstanceOf[String].split(DELIM).toSeq,
      eAsMap.get("positions").asInstanceOf[String]
        .split(DELIM).map(_.split("->").map(_.trim))
        .map(pair => pair(0) -> pair(1).toInt).toMap,
      eAsMap.get("next").asInstanceOf[String]
    )
  }

  def isMongoAvailable: Boolean = mongo != null
}

case class Entry(players: Seq[PlayerId], positions: Map[PlayerId, BoardPosition], next: PlayerId)