//
//  ViewController.swift
//  iOS
//
//  Created by Umair Khan on 11/10/19.
//  Copyright © 2019 NoorifyTech. All rights reserved.
//

import UIKit
import common

class ViewController: UIViewController, QuotationView {

    let quoteLabel = UILabel()
    let authorLabel = UILabel()
    let nextButton = UIButton()
    let quotationsRepo = QuotationRepositoryImpl()
    var presenter: QuotationPresenter? = nil
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        initView()
        
        presenter = QuotationPresenter(view:self, repo: quotationsRepo)
        presenter?.onStart()
    }

    func showQuote(quote: Quote) {
        // update UI

        quoteLabel.text = quote.text
        authorLabel.text = quote.person
    }
    
    func initView() {
        addQuoteLabel()
        addQuoteAuthorLabel()
        addNextButton()
    }
    
    @objc func buttonTapped() {
        presenter?.onNext()
    }
    
    func addQuoteLabel() {
        quoteLabel.frame = CGRect(x: 0, y: 0, width: self.view.frame.width - 30, height: 100)
        quoteLabel.center = view.center
        quoteLabel.numberOfLines = 3
        quoteLabel.textAlignment = NSTextAlignment.center
        if quoteLabel.superview == nil {
            view.addSubview(quoteLabel)
        }
    }
    
    func addQuoteAuthorLabel() {
        authorLabel.frame = CGRect(x: self.view.frame.width - 200, y: self.view.frame.height/2, width: 200, height: 100)
        authorLabel.numberOfLines = 1
        authorLabel.textColor = UIColor.gray
        authorLabel.textAlignment = NSTextAlignment.center
        if authorLabel.superview == nil {
            view.addSubview(authorLabel)
        }
    }
    
    func addNextButton() {
        nextButton.frame = CGRect(x: self.view.frame.width/2 - 50, y: self.view.frame.height - 100, width: 100, height:50)
        nextButton.setTitle("Next", for: .normal)
        nextButton.addTarget(self, action: #selector(buttonTapped), for: .touchUpInside)
        nextButton.backgroundColor = UIColor.red
        nextButton.titleLabel?.textColor = UIColor.white
        
        if nextButton.superview == nil {
            view.addSubview(nextButton)
        }
    }
}

