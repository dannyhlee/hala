package hala

import java.io.{File, FileNotFoundException, IOException}
import hala.FileUtil.getListOfFiles
import scala.io.StdIn
import scala.util.matching.Regex
import hala.LogEntry
import hala.Main.Dao
import org.mongodb.scala._

/** CLI that interacts with user  */
class Cli {

    val cmdArg: Regex = "(\\w+)\\s*(.*)".r

    // source: https://databricks-prod-cloudfront.cloud.databricks.com/public/4027ec902e239c93eaaa8714f173bcfc/2096468276406480/83365179015263/5175146168120750/latest.html
    // regex tester: https://regexr.com/5e9r6
    val logRegex = """^(\S+),(\S+),(\S+),\[(\S+ [+-]\S+)],"(\S+\s\S+\s\S+)",(\S+),(\S+)""".r

    // regex for search and replace access log common -> csv
    // search: ^(\S+) (\S+) (\S+) (\[\S+ [+-]\S+]) ("\S+\s\S+\s\S+") (\S+) (\S+)
    // replace: $1,$2,$3,$4,$5,$6,$7


    def printWelcome(): Unit = {
        val welcomeText =
        """|    __  __________________      __   ___
          |   / / / /_  __/_  __/ __ \____/ /  /   | _____________  __________
          |  / /_/ / / /   / / / /_/ / __  /  / /| |/ ___/ ___/ _ \/ ___/ ___/
          | / __  / / /   / / / ____/ /_/ /  / ___ / /__/ /__/  __(__  |__  )
          |/_/ /_/ /_/   /_/ /_/    \__,_/  /_/  |_\___/\___/\___/____/____/
          |    __                   ___                __
          |   / /   ____  ____ _   /   |  ____  ____ _/ /_  ______  ___  _____
          |  / /   / __ \/ __ `/  / /| | / __ \/ __ `/ / / / /_  / / _ \/ ___/
          | / /___/ /_/ / /_/ /  / ___ |/ / / / /_/ / / /_/ / / /_/  __/ /
          |/_____/\____/\__, /  /_/  |_/_/ /_/\__,_/_/\__, / /___/\___/_/
          |            /____/                        /____/
          |  ѧѦ ѧ  ︵͡︵  ̢ ̱ ̧̱ι̵̱̊ι̶̨̱ ̶̱   ︵ Ѧѧ  ︵͡ ︵   ѧ Ѧ    ̵̗̊o̵̖  ︵  ѦѦ ѧ """
          .stripMargin

        println(welcomeText)
    }

    def printHelpFile(): Unit = {
        val helpText =
        """
            |.==================================================================.
            || > command <parameter> : [command description]                    |
            |`=================================================================='
            || > import <logfile>    : import csv file from local disk          |
            || > show                : show entries in memory                   |
            || > clear               : clear entries in memory                  |
            || > save <collection>   : save an imported collection              |
            || > list                : list collections in database             |
            || > load <collection>   : load a collection from database          |
            || > report              : analyze and display report               |
            || > help                : show this menu                           |
            || > exit                : exit the application                     |
            |`==================================================================' """
              .stripMargin

        println(helpText)
    }

    def fnGiven(filename: String): String = {
        if (filename.length > 0) filename else "file"
    }

    def parseLogEntry(logEntry: String): Unit = {
        println(logEntry)
        try {
            val logRegex(hostIp, logname, remoteUser, time, tz, request, statusCode, size) = logEntry
            println(hostIp, logname, remoteUser, time, tz, request, statusCode, size)
        } catch {
            case e: Error => println(e)
        }
    }

    def clearScreen() {
        val ANSI_CLS: String = "\u001b[2J"
        val ANSI_HOME: String = "\u001b[H"
        System.out.print(ANSI_CLS + ANSI_HOME)
    }

    def printLogEntry(logEntry: LogEntry): Unit = {
//        println(s"ip: ${logEntry.hostIp} | logname: ${logEntry.logname} | remote user: ${logEntry.remoteUser} | time: ${logEntry.time} | request: ${logEntry.request} | status code: ${logEntry.statusCode} | size: ${logEntry.size}")
        printf("%-9s %-15s %-5s %-5s %-27s %-40.40s %-6s %-6s\n",
            logEntry._id, logEntry.hostIp, logEntry.logname, logEntry.remoteUser,
            logEntry.time, logEntry.request,logEntry.statusCode,logEntry.size)
    }

//    case class Log(hostIp: String, logname: String, remoteUser: String,
//                   time: String, request: String, statusCode: String, size :String)

    var logBuffer = scala.collection.mutable.ArrayBuffer.empty[LogEntry]

    /** Runs the menu prompts and directs user input */
    def menu():Unit = {

        printWelcome()
        printHelpFile()
        var keepRunning = true

        /* This loop will continue to prompt, listen, run code and repeat until the user exits */
        while (keepRunning) {
            print("Enter command=> ")
            StdIn.readLine() match {
                // import a csv file from filesystem
                case cmdArg(cmd, arg) if cmd.equalsIgnoreCase("import") =>
                    try {
                        val lines = FileUtil.readFile(arg)
                        var errors = 0
                        var lineNum = 1
                        lines.foreach(line => {
                            try {
                                val logRegex(hostIp, logname, remoteUser, time, request, statusCode, size) = line
                                logBuffer += LogEntry(null, hostIp, logname, remoteUser, time, request, statusCode, size)
                            }  catch {
                                case e: FileNotFoundException => println(s"Failed to find ${fnGiven(arg)}")
                                case e: IOException => println(s"There was an I/O exception: $e")
                                case matchError: MatchError => println(Console.RED + "Malformed entry on Line #" +lineNum + Console.RESET + ": "+ matchError.toString); errors += 1
                                case other : Throwable => println("Error: " + other.toString)
                            } finally {
                                lineNum += 1
                            }
                        })
                        println(Console.BOLD + Console.GREEN + "Processed entries: " + Console.RESET + logBuffer.length + " lines")
                        println(Console.BOLD + Console.RED + "Malformed entries: " + Console.RESET + errors)
                    } catch {
                        case e: FileNotFoundException => {
                            println("File not found error!")
                            println(s"\nFailed to find ${fnGiven(arg)}")
                            val okFileExtensions = List("log", "csv")
                            val files = getListOfFiles(new File(System.getProperty("user.dir")), okFileExtensions)
                            println("Files in current directory with .log and .csv extensions:");
                            println("========================================================");
                            files.map(file => file.getName).foreach((file : String) => println(s"---> ${file}"))
                            println()
                        }
                        case e: IOException => println(s"There was an I/O exception: $e")
                        case other : Throwable => println("Error: " + other.toString)
                    }
                case cmdArg(cmd, arg) if cmd.equalsIgnoreCase("load") =>
                    try {
                        println("Loading from database collection:")
                        var results : Seq[Document] = Dao.getResults(Dao.collection.find())
                        results.foreach (result => {
                            val source = result.toJson
                            println(result)
                        })
                        println("=============================")
                        val logEntryTupple = results.map(doc => {
                            (doc("_id").asObjectId().getValue, doc("hostIp").asString.getValue, doc("logname").asString.getValue,
                              doc("remoteUser").asString.getValue, doc("time").asString.getValue, doc("request").asString.getValue,
                              doc("statusCode").asString.getValue, doc("size").asString.getValue)
                        })
                        logEntryTupple.foreach(item => {
                            logBuffer += LogEntry(item._1, item._2, item._3, item._4, item._5, item._6, item._7, item._8)
                        })
                        println(logBuffer)
                    } catch {
                        case e : FileNotFoundException => println(s"Failed to find ${fnGiven(arg)}")
                        case e : IOException => println("There was an I/O exception")
                        case other : Throwable => println("Error: " + other.toString)
                    }
                case cmdArg(cmd, arg) if cmd.equalsIgnoreCase("report") => {
                    if (logBuffer.length == 0)
                        println("Memory is empty!  Nothing to analyze!  Please load data first.")
                    else {
                        println("Analyzing..." + logBuffer.length + " entries")
                        logBuffer.foreach(item => {
                            println(item.hostIp)
                            println(Dao.ipTest(item.hostIp))
                        })
                    }
                }

                case cmdArg(cmd, arg) if cmd.equalsIgnoreCase("list") => {
                    println("Listing database collections:")
                    var results = Dao.getResults(Dao.db.listCollectionNames()).toList
                    results.sorted.foreach(println)

                }
                case cmdArg(cmd, arg) if cmd.equalsIgnoreCase("save") =>
                    println(arg)
                case cmdArg(cmd, arg) if cmd.equalsIgnoreCase("help") =>
                    clearScreen()
                    printHelpFile()
                case cmdArg(cmd, arg) if cmd.equalsIgnoreCase("show") =>
                    if (logBuffer.length > 0)
                        logBuffer.foreach(printLogEntry)
                    else
                        println("No log entries in memory.")
                case cmdArg(cmd, arg) if cmd.equalsIgnoreCase("clear") =>
                    if (logBuffer.length == 0)
                        println("Memory is empty!")
                    else {
                        println(logBuffer.length + " entries cleared")
                        logBuffer.clear
                    }
                case cmdArg(cmd, arg) if cmd.equalsIgnoreCase("exit") =>
                    println("Goodbye.")
                    keepRunning = false
                case notRecognized => println(s"$notRecognized not a valid command. Please try again.")
            }
        }
    }
}
