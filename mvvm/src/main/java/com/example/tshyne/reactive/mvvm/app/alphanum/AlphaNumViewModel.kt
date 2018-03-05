package com.example.tshyne.reactive.mvvm.app.alphanum

import android.arch.lifecycle.ViewModel
import com.example.tshyne.reactive.mvvm.app.dataprovider.DataProvider
import com.example.tshyne.reactive.mvvm.domain.AlphaNumUseCase
import com.example.tshyne.reactive.mvvm.log
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction

/**
 * Example d'un ViewModel qui contient un flux de donnée non partagé mais également un partagé
 *
 *  created on 20/02/2018
 *
 *  @version 1.0
 *  @author jpetit
 */
class AlphaNumViewModel : ViewModel() {

    private val useCase = AlphaNumUseCase()
    private val compositeDisposable = CompositeDisposable()
    private var currentData: Pair<Long, Observable<String>>? = null

    fun getAlphaNum(param: Long, forced: Boolean = false): Observable<String> {
        if (forced || currentData?.first != param) {
            compositeDisposable.clear()
            currentData = param to combineAlphaNum(param)
        }
        return currentData!!.second
    }

    private fun getAlphabet(param: Long) =
            DataProvider.instance.getAlphabet(param) {
                compositeDisposable.add(it)
            }

    private fun combineAlphaNum(param: Long) = Observable.combineLatest(useCase.getNumber(param),
            getAlphabet(param),
            BiFunction<Long, Char, Data> { t1, t2 -> Data(t1, t2) })
            .mapToString()
            .replay(1)
            .also { compositeDisposable.add(it.connect()) }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun Observable<Data>.mapToString() =
            map {
                log("map data to string")
                "${it.number}${it.letter}"
            }

    data class Data(val number: Long, val letter: Char)
}