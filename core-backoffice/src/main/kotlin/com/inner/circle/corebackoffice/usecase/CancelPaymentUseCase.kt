package com.inner.circle.corebackoffice.usecase

import com.inner.circle.corebackoffice.service.dto.TransactionDto
import com.inner.circle.exception.BackofficeException
import java.math.BigDecimal

fun interface CancelPaymentUseCase {
    data class CancelPaymentRequest(
        val merchantId: Long,
        val paymentKey: String,
        val amount: BigDecimal
    ) {
        init {
            require(amount > BigDecimal.ZERO) {
                throw BackofficeException.BadCancelAmountException(
                    paymentKey = paymentKey,
                    amount = amount
                )
            }
        }
    }

    fun cancel(request: CancelPaymentRequest): TransactionDto
}
