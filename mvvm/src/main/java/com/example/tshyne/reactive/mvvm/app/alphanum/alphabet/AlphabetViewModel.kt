package com.example.tshyne.reactive.mvvm.app.alphanum.alphabet

import android.arch.lifecycle.ViewModel
import com.example.tshyne.reactive.mvvm.app.dataprovider.DataProvider
import io.reactivex.disposables.Disposable

/**
 * Example d'un ViewModel qui contient un flux de donnée partagée
 *
 *  created on 20/02/2018
 *
 *  @version 1.0
 *  @author jpetit
 */
class AlphabetViewModel : ViewModel() {

    private var disposable: Disposable? = null
    private val dataProvider = DataProvider.instance

    fun getAlphabet(param: Long) = dataProvider.getAlphabet(param) {
        dispose()
        disposable = it
    }

    override fun onCleared() {
        super.onCleared()
        dispose()
    }

    private fun dispose() = disposable?.takeIf { !it.isDisposed }?.dispose()
}