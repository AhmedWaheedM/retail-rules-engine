package infrastructure

import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import java.io.FileWriter 
import java.io.PrintWriter
import scala.util.Try 
import scala.util.Using

object Logger {
    trait LogLevel
    case object INFO extends LogLevel
    case object WARN extends LogLevel
    case object ERROR extends LogLevel
    val logFile = "rules_engine.log"
    val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    def log(level: LogLevel, message: String): Try[Unit] = {
        val logM = s"${LocalDateTime.now().format(format)} $level $message"
        Using(new PrintWriter(new FileWriter(logFile, true))) { writer =>
            writer.println(logM)
        }
    }
}
