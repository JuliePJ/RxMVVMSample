package com.example.tshyne.reactive.mvvm.app.alphanum.number

import android.arch.lifecycle.ViewModel
import com.example.tshyne.reactive.mvvm.domain.AlphaNumUseCase
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/**
 * Example d'un ViewModel qui contient un flux de donnée non partagé
 *
 *  created on 20/02/2018
 *
 *  @version 1.0
 *  @author jpetit
 */
class NumberViewModel : ViewModel() {

    private var disposable: Disposable? = null
    private var currentData: Pair<Long, Observable<Long>>? = null
    private val useCase = AlphaNumUseCase()

    fun getNumber(param: Long): Observable<Long> = getCurrentObservable(param) ?: getNewObservable(param).also {
        dispose()
        disposable = it.connect()
        currentData = param to it
    }


    private fun getCurrentObservable(param: Long) = currentData?.takeIf { it.first == param }?.second

    private fun getNewObservable(param: Long) = useCase.getNumber(param).replay(1)

    override fun onCleared() {
        super.onCleared()
        dispose()
    }

    private fun dispose() = disposable?.takeIf { !it.isDisposed }?.dispose()
}