package com.oxcoding.moviemvvm.ui.single_movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.oxcoding.moviemvvm.data.repository.SostoyanieSeti
import com.oxcoding.moviemvvm.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class SingleMovieViewModel (private val movieRepository : MovieDetailsRepository, movieId: Int)  : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val  movieDetails : LiveData<MovieDetails> by lazy {
        movieRepository.fetchSingleMovieDetails(compositeDisposable,movieId)
    }

    val sostoyanieSeti_peremennaya : LiveData<SostoyanieSeti> by lazy {
        movieRepository.getMovieDetailssostoyanieSeti_peremennaya()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }



}