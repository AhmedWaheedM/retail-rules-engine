package models

import java.time.temporal.ChronoUnit
import java.time.LocalDate
import java.time.LocalDateTime

final case class Transaction(
    timestamp: LocalDateTime,
    productName: String,
    expiryDate: LocalDate,
    quantity: Int,
    unitPrice: Double,
    channel: String,
    paymentMethod: String
){
    def daysToExpiry: Long = ChronoUnit.DAYS.between(LocalDate.now(), expiryDate)
}

