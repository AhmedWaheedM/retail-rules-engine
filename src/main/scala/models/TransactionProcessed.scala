package models

final case class TransactionProcessed(
    originalTransaction: Transaction,
    discountApplied: List[Double],
    averageDiscount: Double,
    finalPrice: Double
)
