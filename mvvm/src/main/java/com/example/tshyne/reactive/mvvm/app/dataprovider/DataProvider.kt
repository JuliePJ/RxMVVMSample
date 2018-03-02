package com.example.tshyne.reactive.mvvm.app.dataprovider

import com.example.tshyne.reactive.mvvm.domain.AlphaNumUseCase
import com.example.tshyne.reactive.mvvm.log
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.observables.ConnectableObservable

/**
 * Class qui contient et fourni tout les flux de données partagées
 *
 *  created on 20/02/2018
 *
 *  @version 1.0
 *  @author jpetit
 */
class DataProvider(private val alphaNumUseCase: AlphaNumUseCase) {

    companion object {
        val instance: DataProvider by lazy { DataProvider(AlphaNumUseCase()) }
    }

    private val alphabetObservableMap: MutableMap<Long, Observable<Char>> = HashMap()

    /**
     * permet de récuperer le flux en cache en émettant le dernier élement émis
     * tant que la connection lors du premier abonnement n'a pas était déconnecter ou qu'il y a toujours des abonnées
     * le flux reste en cache
     */
    fun getAlphabet(param: Long, connection: ((Disposable) -> Unit)): Observable<Char> {
        return alphabetObservableMap.getOrPut(param) {
            getAlphabet(param).apply { connect(connection) }
        }
    }

    /**
     * on ajoute le flux en cache
     * lorsque la source est libérée alors on enleve le flux du cache
     */
    private fun getAlphabet(param: Long): ConnectableObservable<Char> {
        return alphaNumUseCase.getAlphabet(param)
                .doOnDispose {
                    alphabetObservableMap.remove(param)
                    log("remove param = $param")
                }
                .replay(1)
    }

}