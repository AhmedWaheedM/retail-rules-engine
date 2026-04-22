import infrastructure.{DataLoader, DataWriter, Logger}
import rules.{QualifyingRules, Engine}
import scala.util.{Success, Failure}

object Main extends App {
  
  // Switch to TRX1000.csv for quick local smoke runs.
  val csvFilePath = "data\\TRX10M.csv" 
  
  Logger.log(Logger.INFO, "Starting the SCALED transaction processing pipeline.")

  val pipelineResult = DataLoader.loadTransactions(csvFilePath) { transactionStream =>
    
    val processedStream = Engine.processTransactions(transactionStream, QualifyingRules.allRules)
    
    DataWriter.writeResults(processedStream)
  }

  // Outer Try handles file stage; inner Try captures database stage failures.
  pipelineResult match {
  case Success(Success(totalSaved)) =>
    Logger.log(Logger.INFO, s"PIPELINE COMPLETED: Processed and saved $totalSaved transactions.")
    println(s"SUCCESS! Saved $totalSaved transactions. Check rules_engine.log")

  case Success(Failure(dbError)) =>
    Logger.log(Logger.ERROR, s"DATABASE FAILED: ${dbError.getMessage}")
    println(s"CRITICAL ERROR: Database failed - ${dbError.getMessage}")

  case Failure(fileError) =>
    Logger.log(Logger.ERROR, s"FILE LOAD FAILED: ${fileError.getMessage}")
    println(s"CRITICAL ERROR: File failed - ${fileError.getMessage}")
}
}