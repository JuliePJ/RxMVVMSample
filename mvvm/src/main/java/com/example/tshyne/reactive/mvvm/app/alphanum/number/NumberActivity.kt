package com.example.tshyne.reactive.mvvm.app.alphanum.number

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.tshyne.reactive.mvvm.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_alphabet.*

class NumberActivity: AppCompatActivity() {
    
    private val viewModel by lazy {
        ViewModelProviders.of(this)
                .get(NumberViewModel::class.java)
    }
    private val compositeDisposable = CompositeDisposable()
    
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alphabet)
        val disposable = viewModel.getNumber(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    this.textView.text = getString(R.string.num_text, it)
                })
        compositeDisposable.add(disposable)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
    
}
