import infrastructure.DataLoader
import infrastructure.DataWriter
import infrastructure.Logger
import rules.QualifyingRules
import rules.Engine
import scala.util.Success
import scala.util.Failure
object Main extends App {
  val csvFilePath = "data\\TRX1000.csv"
  val pipeline = for {
    _ <- Logger.log(Logger.INFO, "Starting the transaction processing pipeline.")
    transactions <- DataLoader.loadTransactions(csvFilePath)
    _ <- Logger.log(Logger.INFO, s"Loaded ${transactions.size} transactions from $csvFilePath.")
    processed = Engine.processTransactions(transactions, QualifyingRules.allRules)
    _ <- DataWriter.writeResults(processed)
    _ <- Logger.log(Logger.INFO, s"Successfully processed and stored ${processed.size} transactions")
  } yield processed
  pipeline match {
    case Success(data) => Logger.log(Logger.INFO, s"PIPELINE COMPLETED: Processed ${data.size} transactions successfully.")
      println(s"SUCCESS! Processed ${data.size} transactions. Check rules_engine.log")
    case Failure(error) => Logger.log(Logger.ERROR, s"PIPELINE FAILED: ${error.getMessage}")
      println(s"CRITICAL ERROR: ${error.getMessage}")
  }
}
