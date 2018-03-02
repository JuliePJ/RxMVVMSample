package com.example.tshyne.reactive.mvvm.app.alphanum

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.tshyne.reactive.mvvm.R
import com.example.tshyne.reactive.mvvm.app.alphanum.alphabet.AlphabetActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class AlphaNumActivity: AppCompatActivity() {
    
    private val viewModel by lazy {
        ViewModelProviders.of(this)
                .get(AlphaNumViewModel::class.java)
    }
    private val compositeDisposable = CompositeDisposable()
    
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getAlphaNum(0)
        initButtons()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
    
    private fun initButtons() {
        button.setOnClickListener {
            getAlphaNum(0, true)
        }
        button2.setOnClickListener {
            startActivity(AlphabetActivity.newIntent(this))
        }
    }
    
    private fun showValue(value : String) {
        text.text = getString(R.string.value_text, value)
        Log.v("SubjectViewModel", "listener : $value")
    }
    
    private fun getAlphaNum(param : Long, force : Boolean = false) {
        compositeDisposable.clear()
        val disposable = viewModel.getAlphaNum(param, force)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { showValue(it) }
        compositeDisposable.add(disposable)
    }
    
    
}

