package hala

import java.io.{FileNotFoundException, IOException}
import scala.io.StdIn
import scala.util.matching.Regex

/** CLI that interacts with user  */
class Cli {

    val cmdArg: Regex = "(\\w+)\\s*(.*)".r

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
            || > save <collection>   : save an imported collection              |
            || > list                : list collections in database             |
            || > load <collection>   : load a collection from database          |
            || > analyze             : display analysis of the collection       |
            || > help                : show this menu                           |
            || > exit                : exit the application                     |
            |`==================================================================' """
              .stripMargin

        println(helpText)
        print("Enter command=> ")
    }

    def fnGiven(filename: String): String = {
        if (filename.length > 0) filename else "file"
    }

    /** Runs the menu prompts and directs user input */
    def menu():Unit = {

        printWelcome()
        var keepRunning = true

        /* This loop will continue to prompt, listen, run code and repeat until the user exits */
        while (keepRunning) {
            printHelpFile()
            StdIn.readLine() match {
                // import a csv file from filesystem
                case cmdArg(cmd, arg) if cmd.equalsIgnoreCase("import") =>
                    try {
                        val lines = FileUtil.readFile(arg)
                        for (line <- lines) {
                            println(line)
                        }
                    }
                    catch {
                        case e : FileNotFoundException => println(s"Failed to find ${fnGiven(arg)}")
                        case e : IOException => println(s"There was an I/O exception: $e")
                        case _ => println("Error.  Exiting...")
                    }
                case cmdArg(cmd, arg) if cmd.equalsIgnoreCase("load") =>
                    try {
//                        FileUtil.readFile(arg)
//                          .getOrElse("No filename given")
//                          .replaceAll("\\p{Punct}", "") // remove all punctuation
//                          .toLowerCase()
//                          .split("\\s") // split into words
//                          .groupMapReduce(w => w)(w => 1)(_ + _) //
//                          .toSeq
//                          .sorted
//                          .foreach { case (word, count) => println(s"$word: $count") }
                    } catch {
                        case e : FileNotFoundException => println(s"Failed to find ${fnGiven(arg)}")
                        case e : IOException => println("There was an I/O exception")
                        case _ => println("Error.  Exiting...")
                    }
                case cmdArg(cmd, arg) if cmd.equalsIgnoreCase("analyze") =>
                    println("Analyzing")
                case cmdArg(cmd, arg) if cmd.equalsIgnoreCase("list") =>
                    println("Listing")
                case cmdArg(cmd, arg) if cmd.equalsIgnoreCase("save") =>
                    println(arg)
                case cmdArg(cmd, arg) if cmd.equalsIgnoreCase("help") =>
                    printHelpFile()
                case cmdArg(cmd, arg) if cmd.equalsIgnoreCase("exit") =>
                    keepRunning = false
                case notRecognized => println(s"$notRecognized not a valid command. Please try again.")
            }
        }
    }
}
