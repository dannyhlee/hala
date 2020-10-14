package hala

import scala.io.{ BufferedSource, Source }

object FileUtil {

    def getTextContent(filename: String) : Option[String] = {
        // the way we open file, we use Source.fromFile
        // you can write very short version of opening and reading
        // from a file. So we can close the file.

        var openedFile: BufferedSource = null

        try {
            openedFile = Source.fromFile(filename)
            Some(openedFile.getLines().mkString(" "))
        } finally {
            if (openedFile != null) openedFile.close
        }
    }
}
