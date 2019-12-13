package com.noorifytech.multiplatform.presenter

import com.noorifytech.multiplatform.repository.QuotationRepository
import com.noorifytech.multiplatform.view.QuotationView

class QuotationPresenter(
        private val view: QuotationView,
        private val repo: QuotationRepository
) {
    private var nextQuoteId = -1

    fun onStart() {
        onNext()
    }

    fun onNext() {
        nextQuoteId = (nextQuoteId + 1) % repo.quotesNumber
        val quote = repo.getQuote(nextQuoteId)
        view.showQuote(quote)
    }
}