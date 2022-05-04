package com.oxcoding.moviemvvm.ui.popular_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.oxcoding.moviemvvm.data.api.POST_PER_PAGE
import com.oxcoding.moviemvvm.data.api.TheMovieDBInterface
import com.oxcoding.moviemvvm.data.repository.MovieDataSource
import com.oxcoding.moviemvvm.data.repository.MovieDataSourceFactory
import com.oxcoding.moviemvvm.data.repository.SostoyanieSeti
import com.oxcoding.moviemvvm.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class MoviePagedListRepository (private val apiService : TheMovieDBInterface) {

    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var moviesDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<Movie>> {
        moviesDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()

        return moviePagedList
    }

    fun getsostoyanieSeti_peremennaya(): LiveData<SostoyanieSeti> {
        return Transformations.switchMap<MovieDataSource, SostoyanieSeti>(
            moviesDataSourceFactory.moviesLiveDataSource, MovieDataSource::sostoyanieSeti_peremennaya)
    }
}