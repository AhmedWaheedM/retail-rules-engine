package rules
import models.Transaction
import java.time.Month
object QualifyingRules {
    def createCategoryRule(targetCategory: String, discountRate: Double): Transaction => Double = {
        t => if (t.productName.toLowerCase.contains(targetCategory.toLowerCase)) discountRate else 0.0
}
    def createDateRule(targetMonth: Month, targetDay: Int, discountRate: Double): Transaction => Double = {
        t => {
            val date = t.timestamp.toLocalDate
            if (date.getMonth == targetMonth && date.getDayOfMonth == targetDay) discountRate else 0.0
        }
    }

    def createTieredQuantityRule(tiers: List[(Int, Double)]): Transaction => Double = {
        val sortedTiers = tiers.sortBy  {case (threshold, _) => -threshold}
        t => sortedTiers.find { case (threshold, _) => t.quantity >= threshold }.map(_._2).getOrElse(0.0) }
    
    // Defining rules 
    val expirationRule: Transaction => Double = t => {
        val daysLeft = t.daysToExpiry
        if (daysLeft >= 0 && daysLeft < 30) (30 - daysLeft) / 100.0 else 0.0    
        }
val allRules: List[Transaction => Double] = List(
    expirationRule,
    createCategoryRule("cheese", 0.10),
    createCategoryRule("wine", 0.05),
    createDateRule(Month.MARCH, 23, 0.50),
    createTieredQuantityRule(List( (15, 0.10), (10, 0.07), (6, 0.05) ))
    )
}
