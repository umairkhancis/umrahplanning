package com.noorifytech.multiplatform.view

import com.noorifytech.multiplatform.domain.Quote

interface QuotationView {
    fun showQuote(quote: Quote)
}