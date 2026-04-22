package rules
import  models.Transaction
import models.TransactionProcessed

object Engine { 
    def processTransactions(transactions: List[Transaction], rules: List[Transaction => Double]): List[TransactionProcessed] = {
        transactions.map{
            t => 
                val averageDiscount = rules
                .map(ruleFunc => ruleFunc(t))
                .filter(_>0.0)
                .sortBy(d => -d)
                .take(2)
                match {
                    case topTwo if topTwo.nonEmpty => topTwo.sum / topTwo.size
                    case _ => 0.0
                }
                TransactionProcessed(
                    originalTransaction = t,
                    discountApplied = rules.map(_(t)).filter(_>0.0),
                    averageDiscount = averageDiscount,
                    finalPrice = t.unitPrice * t.quantity * (1 - averageDiscount)
                )
        }
    }
}