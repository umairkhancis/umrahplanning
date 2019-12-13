package com.noorifytech.multiplatform.repository

import com.noorifytech.multiplatform.domain.Quote

interface QuotationRepository {
    val quotesNumber: Int
    fun getQuote(quoteId: Int): Quote
}