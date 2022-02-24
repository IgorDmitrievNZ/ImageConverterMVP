package com.example.android.imageconvertermvp.presenter

import com.example.android.imageconvertermvp.model.IConverter
import com.example.android.imageconvertermvp.view.MainView
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import moxy.MvpPresenter

class MainPresenter(private val converter: IConverter, private val mainThread: Scheduler) :
    MvpPresenter<MainView>() {

    val compositeDisposable: CompositeDisposable? = null

    fun startConversion() {
        viewState.startConversion()
    }

    fun convertImage(path: String) {
        val disposable = converter.convertImage(path)
            .observeOn(mainThread).doOnSubscribe { viewState.showDialog() }
            .subscribe({
                viewState.hideDialog()
                viewState.showResult()
            }, {
                viewState.hideDialog()
                viewState.showError()
            })

        compositeDisposable?.add(disposable)
    }

    fun dismiss() {
        compositeDisposable?.dispose()
        viewState.hideDialog()
        viewState.showInterrupted()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.dispose()
    }
}