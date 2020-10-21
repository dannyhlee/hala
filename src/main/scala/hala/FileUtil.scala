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

    def printLogEntry(logEntry: LogEntry): Unit = {
//        println(s"ip: ${logEntry.hostIp} | logname: ${logEntry.logname} | remote user: ${logEntry.remoteUser} | time: ${logEntry.time} | request: ${logEntry.request} | status code: ${logEntry.statusCode} | size: ${logEntry.size}")
        printf("%-20.20s %-18s %-4.4s %-4.4s %-27s %-35.35s %-5s %-5s\n",
            logEntry._id, logEntry.hostIp, logEntry.logname, logEntry.remoteUser,
            logEntry.time, logEntry.request,logEntry.statusCode,logEntry.size)
    }
}
