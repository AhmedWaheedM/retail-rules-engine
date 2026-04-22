package infrastructure

import java.time.LocalDate
import java.time.ZonedDateTime
import scala.util.Try
import models.Transaction
import scala.io.Source 
import scala.util.Using

object DataLoader {

  def loadTransactions[A](filePath: String)(process: Iterator[Transaction] => A): Try[A] = {
        Using(Source.fromFile(filePath)) { source =>
        val iterator = source.getLines().drop(1).flatMap(parseLine)
        process(iterator)
    }
  }

  def parseLine(line: String): Option[Transaction] = {
    val cols = line.split(",").map(_.trim)
    
    Try {
      Transaction(
        timestamp = ZonedDateTime.parse(cols(0)).toLocalDateTime, 
        productName = cols(1),
        expiryDate = LocalDate.parse(cols(2)),
        quantity = cols(3).toInt,
        unitPrice = cols(4).toDouble,
        channel = cols(5),
        paymentMethod = cols(6)
      )
    }.toOption
  }
}