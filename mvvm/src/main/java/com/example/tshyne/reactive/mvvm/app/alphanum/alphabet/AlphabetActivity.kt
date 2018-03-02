package com.example.tshyne.reactive.mvvm.app.alphanum.alphabet

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.tshyne.reactive.mvvm.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_alphabet.*

class AlphabetActivity: AppCompatActivity() {
    
    private val viewModel by lazy {
        ViewModelProviders.of(this)
                .get(AlphabetViewModel::class.java)
    }
    private val compositeDisposable = CompositeDisposable()
    
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alphabet)
        subscribe()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSubscription()
    }

    private fun stopSubscription() {
        compositeDisposable.clear()
    }

    private fun subscribe() {
        val disposable = viewModel.getAlphabet(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    this.textView.text = getString(R.string.letter_text, it)
                })
        compositeDisposable.add(disposable)
    }
    
    companion object {
        fun newIntent(context : Context) : Intent = Intent(context, AlphabetActivity::class.java)
    }
}
