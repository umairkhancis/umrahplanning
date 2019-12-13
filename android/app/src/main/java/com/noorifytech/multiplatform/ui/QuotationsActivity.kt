package com.noorifytech.multiplatform.ui

import android.app.Activity
import android.os.Bundle
import com.noorifytech.multiplatform.R
import com.noorifytech.multiplatform.domain.Quote
import com.noorifytech.multiplatform.presenter.QuotationPresenter
import com.noorifytech.multiplatform.repository.QuotationRepositoryImpl
import com.noorifytech.multiplatform.view.QuotationView
import kotlinx.android.synthetic.main.activity_quotations.*

class QuotationsActivity : Activity(), QuotationView {

    private val quotationsRepo = QuotationRepositoryImpl()
    private val presenter = QuotationPresenter(this, quotationsRepo)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quotations)
        presenter.onStart()
        nextButton.setOnClickListener { presenter.onNext() }
    }

    override fun showQuote(quote: Quote) {
        textView.text = quote.text
        authorView.text = quote.person
    }
}