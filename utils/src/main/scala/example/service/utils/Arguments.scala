package example.service.utils

import scala.annotation.tailrec

class Arguments(private val argMaps: Map[String, List[String]]) {
  def envs: List[String] = valuesOf("evn")

  def valuesOf(argName: String): List[String] = argMaps.getOrElse(argName, Nil)
  def valueOf(argName: String): Option[String] = argMaps.get(argName).flatMap(_.headOption)
  def size: Int = argMaps.size
}

object Arguments {
  def parse(args: Array[String]): Arguments = {
    @tailrec def tailRecParse(
        unTrimmedKey: String,
        values: List[String],
        rest: List[String],
        map: Map[String, List[String]] = Map.empty
    ): Map[String, List[String]] = {
      val joinedMap = map + (trimKey(unTrimmedKey) -> values)
      if (rest.isEmpty) joinedMap
      else {
        val (key_, (values_, rest_)) = (rest.head, rest.tail.span(!isArgKey(_)))
        tailRecParse(key_, values_, rest_, joinedMap)
      }
    }

    if (args.nonEmpty) {
      val argsList = args.toList
      require(isArgKey(argsList.head))
      val (key, (values, rest)) = (argsList.head, argsList.tail.span(!isArgKey(_)))
      new Arguments(tailRecParse(key, values, rest))
    } else new Arguments(Map.empty)
  }

  private def isArgKey(str: String) = str.startsWith("--")
  private def trimKey(str: String) = str.substring(2)
}
