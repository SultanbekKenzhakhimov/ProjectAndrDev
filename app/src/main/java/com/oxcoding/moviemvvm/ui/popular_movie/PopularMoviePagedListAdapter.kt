package com.oxcoding.moviemvvm.ui.popular_movie

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oxcoding.moviemvvm.R
import com.oxcoding.moviemvvm.data.api.POSTER_BASE_URL
import com.oxcoding.moviemvvm.data.repository.SostoyanieSeti
import com.oxcoding.moviemvvm.data.vo.Movie
import com.oxcoding.moviemvvm.ui.single_movie_details.SingleMovie
import kotlinx.android.synthetic.main.movie_list_item.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*
import android.widget.Toast




class PopularMoviePagedListAdapter(public val context: Context) : PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var sostoyanieSeti_peremennaya: SostoyanieSeti? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.movie_list_item, parent, false)
            return MovieItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return sostoyanieSeti_peremennayaItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position),context)
        }
        else {
            (holder as sostoyanieSeti_peremennayaItemViewHolder).bind(sostoyanieSeti_peremennaya)
        }
    }


    private fun hasExtraRow(): Boolean {
        return sostoyanieSeti_peremennaya != null && sostoyanieSeti_peremennaya != SostoyanieSeti.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }
    }




    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }


    class MovieItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        fun bind(movie: Movie?,context: Context) {
            itemView.cv_movie_title.text = movie?.title
            itemView.cv_movie_release_date.text =  movie?.releaseDate

            val moviePosterURL = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(moviePosterURL)
                .into(itemView.cv_iv_movie_poster);

            itemView.setOnClickListener{
                val intent = Intent(context, SingleMovie::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }

        }

    }

    class sostoyanieSeti_peremennayaItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        fun bind(sostoyanieSeti_peremennaya: SostoyanieSeti?) {
            if (sostoyanieSeti_peremennaya != null && sostoyanieSeti_peremennaya == SostoyanieSeti.LOADING) {
                itemView.progress_bar_item.visibility = View.VISIBLE;
            }
            else  {
                itemView.progress_bar_item.visibility = View.GONE;
            }

            if (sostoyanieSeti_peremennaya != null && sostoyanieSeti_peremennaya == SostoyanieSeti.ERROR) {
                itemView.error_msg_item.visibility = View.VISIBLE;
                itemView.error_msg_item.text = sostoyanieSeti_peremennaya.msg;
            }
            else if (sostoyanieSeti_peremennaya != null && sostoyanieSeti_peremennaya == SostoyanieSeti.ENDOFLIST) {
                itemView.error_msg_item.visibility = View.VISIBLE;
                itemView.error_msg_item.text = sostoyanieSeti_peremennaya.msg;
            }
            else {
                itemView.error_msg_item.visibility = View.GONE;
            }
        }
    }


    fun setsostoyanieSeti_peremennaya(newsostoyanieSeti_peremennaya: SostoyanieSeti) {
        val previousState = this.sostoyanieSeti_peremennaya
        val hadExtraRow = hasExtraRow()
        this.sostoyanieSeti_peremennaya = newsostoyanieSeti_peremennaya
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {                             //hadExtraRow is true and hasExtraRow false
                notifyItemRemoved(super.getItemCount())    //remove the progressbar at the end
            } else {                                       //hasExtraRow is true and hadExtraRow false
                notifyItemInserted(super.getItemCount())   //add the progressbar at the end
            }
        } else if (hasExtraRow && previousState != newsostoyanieSeti_peremennaya) { //hasExtraRow is true and hadExtraRow true and (sostoyanieSeti_peremennaya.ERROR or sostoyanieSeti_peremennaya.ENDOFLIST)
            notifyItemChanged(itemCount - 1)       //add the network message at the end
        }

    }




}