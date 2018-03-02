package com.example.tshyne.reactive.mvvm.domain

import com.example.tshyne.reactive.mvvm.log
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * class fournissant les cas d'utilisation
 *
 *  created on 15/02/2018
 *
 *  @version 1.0
 *  @author jpetit
 */
class AlphaNumUseCase {
    
    fun getNumber(start : Long) : Observable<Long> = intervalRange(start).map {
        (1 + it).also { log("emit : $it") }
    }
    
    fun getAlphabet(start : Long) : Observable<Char> = intervalRange(start).map {
        ('a'.toInt() + it).toChar()
                .also { log("emit letter : $it") }
    }
    
    private fun intervalRange(start : Long, end : Long = 10) = Observable.intervalRange(start, end, 0, 2, TimeUnit.SECONDS)
}