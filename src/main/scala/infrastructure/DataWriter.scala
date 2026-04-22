package infrastructure
import models.TransactionProcessed
import scala.util.Try
import scala.util.Using
import java.sql.DriverManager
import java.sql.Timestamp
object DataWriter {
    val url = sys.env.getOrElse("DB_URL", "jdbc:postgresql://localhost:5432/retail_db")
    val user = sys.env.getOrElse("DB_USER", "postgres")
    val password = sys.env.getOrElse("DB_PASSWORD", "password")
    def writeResults(results: List[TransactionProcessed]): Try[Unit] = {
        Using.Manager {
            use => 
                val conn = use(DriverManager.getConnection(url, user, password))
                val sql = "INSERT INTO processed_transactions (transaction_timestamp, product_name, quantity, base_price, discount_applied, final_price) VALUES (?, ?, ?, ?, ?, ?)"
                val stmt = use(conn.prepareStatement(sql))
                results.foreach {
                    result => 
                        val transaction = result.originalTransaction
                        stmt.setTimestamp(1, Timestamp.valueOf(transaction.timestamp))
                        stmt.setString(2, transaction.productName)
                        stmt.setInt(3, transaction.quantity)
                        stmt.setDouble(4, transaction.unitPrice * transaction.quantity) 
                        stmt.setDouble(5, result.averageDiscount)
                        stmt.setDouble(6, result.finalPrice)
                        stmt.addBatch()
                }
                stmt.executeBatch()
    }
  }
}