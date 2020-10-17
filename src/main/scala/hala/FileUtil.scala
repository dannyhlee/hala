package hala

import scala.io.Source
import java.io.File

object FileUtil {
    def readFile(filename: String) : List[String] = {
        val source = Source.fromFile(filename)
        val lines = source.getLines().toList
        source.close()
        lines
    }

    def getListOfFiles(dir: File, extensions: List[String]): List[File] = {
        dir.listFiles.filter(_.isFile).toList.filter { file =>
            extensions.exists(file.getName.endsWith(_))
        }
    }
}
