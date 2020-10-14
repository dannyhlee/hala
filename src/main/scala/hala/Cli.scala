package hala

import java.io.FileNotFoundException

import scala.io.StdIn
import scala.util.matching.Regex

/** CLI that interacts with user  */
class Cli {

    val commandArgPattern: Regex = "(\\w+)\\s*(.*)".r

    def printWelcome(): Unit = {
        println("Welcome to HALA")
    }

    def printOptions(): Unit = {
        val optionText =
            """file [filename] : wordcount the contents of a file
            |exit : close WCCLI
            |please enter an option:"""
        .stripMargin

        println(optionText)
    }

    /** Runs the menu prompts and directs user input */
    def menu():Unit = {

        printWelcome()
        var continueMenuLoop = true

        /** This loop will continue to prompt, listen, run code
         * and repeat until the user exits */
        while (continueMenuLoop) {
            printOptions()
            StdIn.readLine() match {
                case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("file") => {
                    try {
                        FileUtil.getTextContent(arg)
                          .getOrElse("NoContent") // if none, return "NoContent" instead
                          .replaceAll("\\p{Punct}", "") // remove all punctuation
                          .toLowerCase()
                          .split("\\s") // split into words
                          .groupMapReduce(w => w)(w => 1)(_ + _) //
                          .toSeq
                          .sorted
                          .foreach { case (word, count) => println(s"$word: $count") }
                    } catch {
                        case fnf: FileNotFoundException => println(s"Failed to find $arg")
                    }
                }
                case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("exit") => {
                    continueMenuLoop = false
                }
                case notRecognized => println(s"$notRecognized not a valid command")
            }
        }
    }
}
