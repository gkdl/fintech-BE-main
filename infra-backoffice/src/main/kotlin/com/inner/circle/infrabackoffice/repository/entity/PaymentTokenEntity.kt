package com.inner.circle.infrabackoffice.repository.entity

import com.inner.circle.exception.PaymentJwtException

data class PaymentTokenEntity(
    val merchantId: Long,
    val orderId: String,
    val generatedToken: String,
    val signature: String
) {
    companion object {
        fun fromToken(tokenData: MutableMap<String, String>): PaymentTokenEntity {
            val notFoundMessage = "tokenData not found"
            return PaymentTokenEntity(
                generatedToken =
                    tokenData["token"]
                        ?: throw PaymentJwtException.TokenNotFoundException(notFoundMessage),
                merchantId =
                    tokenData["merchantId"]?.toLong()
                        ?: throw PaymentJwtException.TokenNotFoundException(notFoundMessage),
                orderId =
                    tokenData["orderId"]
                        ?: throw PaymentJwtException.TokenNotFoundException(notFoundMessage),
                signature =
                    tokenData["signature"]
                        ?: throw PaymentJwtException.TokenNotFoundException(notFoundMessage)
            )
        }
    }

    override fun toString(): String =
        "token = $generatedToken, merchantId = $merchantId, orderId = $orderId}"
}
