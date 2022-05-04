package com.oxcoding.moviemvvm.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.oxcoding.moviemvvm.data.api.FIRST_PAGE
import com.oxcoding.moviemvvm.data.api.TheMovieDBInterface
import com.oxcoding.moviemvvm.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDataSource (private val apiService : TheMovieDBInterface, private val compositeDisposable: CompositeDisposable)
    : PageKeyedDataSource<Int, Movie>(){

    private var page = FIRST_PAGE

    val sostoyanieSeti_peremennaya: MutableLiveData<SostoyanieSeti> = MutableLiveData()


    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {

        sostoyanieSeti_peremennaya.postValue(SostoyanieSeti.LOADING)

        compositeDisposable.add(
            apiService.getPopularMovie(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.movieList, null, page+1)
                        sostoyanieSeti_peremennaya.postValue(SostoyanieSeti.LOADED)
                    },
                    {
                        sostoyanieSeti_peremennaya.postValue(SostoyanieSeti.ERROR)
                        Log.e("MovieDataSource", it.message)
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        sostoyanieSeti_peremennaya.postValue(SostoyanieSeti.LOADING)

        compositeDisposable.add(
            apiService.getPopularMovie(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if(it.totalPages >= params.key) {
                            callback.onResult(it.movieList, params.key+1)
                            sostoyanieSeti_peremennaya.postValue(SostoyanieSeti.LOADED)
                        }
                        else{
                            sostoyanieSeti_peremennaya.postValue(SostoyanieSeti.ENDOFLIST)
                        }
                    },
                    {
                        sostoyanieSeti_peremennaya.postValue(SostoyanieSeti.ERROR)
                        Log.e("MovieDataSource", it.message)
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {

    }
}