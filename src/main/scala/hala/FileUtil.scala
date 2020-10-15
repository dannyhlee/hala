package hala

import scala.io.Source

object FileUtil {

    def readFile(filename: String) : List[String] = {
        val source = Source.fromFile(filename)
        val lines = source.getLines().toList
        source.close()
        lines
    }
}
