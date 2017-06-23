package org.kastoestoramadus.ladders

import com.mongodb.casbah.{MongoCollection, MongoConnection}
import com.mongodb.casbah.commons.MongoDBObject
import org.joda.time.DateTime
import org.kastoestoramadus.ladders.model._

import scala.util.Try

// should not be a static object
// FIXME lack of tests // not ready for production
// very ugly not ready for production
object SaveGameManger{
  val DELIM = ","

  var mongo: MongoConnection = null
  var coll: MongoCollection = null
  try {
    mongo = MongoConnection()
    coll = mongo("ladder-game")("logs")
    getLast().foreach(last => println(s"Last save game detected: $last"))
  } catch {
    case e => println("#### !Something wrong with mongo connection. Tried on default port on localhost."+e.getMessage)
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

  def getLast(): Option[Entry] = {
    if(coll.size > 0) {
      val eAsMap = coll.last.toMap
        Some(Entry(
          eAsMap.get("players").asInstanceOf[String].split(DELIM).toSeq,
          eAsMap.get("positions").asInstanceOf[String]
            .split(DELIM).map(_.split("->").map(_.trim))
            .map(pair => pair(0) -> pair(1).toInt).toMap,
          eAsMap.get("next").asInstanceOf[String]
        ))
    } else None
  }

  def isMongoAvailable: Boolean = mongo != null
}

case class Entry(players: Seq[PlayerId], positions: Map[PlayerId, BoardPosition], next: PlayerId) {
  def toGameState: GameState = {
    Try{FinishedGame(positions)}.getOrElse(GameInProgress(positions, players.indexOf(next), players))
  }
}
