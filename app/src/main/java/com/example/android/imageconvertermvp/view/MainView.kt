package com.example.android.imageconvertermvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView : MvpView {
    fun startConversion()
    fun showDialog()
    fun hideDialog()
    fun showResult()
    fun showError()
    fun showInterrupted()

}