package com.example.movieapp.presentation.movie.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.base.BaseFragment
import com.example.movieapp.utils.AppConstants
import com.example.movieapp.utils.AppPreferences
import org.koin.android.ext.android.inject

class MovieDetailsFragment : BaseFragment() {

    private val viewModel: MovieDetailsViewModel by inject()

    private lateinit var progressBar: ProgressBar
    private lateinit var ivBackdrop: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvGenre: TextView
    private lateinit var tvOverview: TextView
    private lateinit var btnFavorite: Button
    private lateinit var group: Group

    private var movieId: Int? = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        setData()

        val accountId = activity?.applicationContext?.let { AppPreferences.getAccountId(it) }
        val sessionId = activity?.applicationContext?.let { AppPreferences.getSessionId(it) }

        val parentFragment = arguments?.getString(AppConstants.PARENT_FRAGMENT)

        btnFavorite.setOnClickListener{
            movieId?.let { movieId ->
                accountId?.let { accountId ->
                    sessionId?.let { sessionId ->
                        if (parentFragment.equals("list_fragment")) {
                            viewModel.setFavorite(accountId, movieId, sessionId, true)
                            viewModel.addToFavorites(movieId)
                        } else if (parentFragment.equals("favorite_fragment")) {
                            viewModel.setFavorite(accountId, movieId, sessionId, false)
                            viewModel.deleteFromFavorites(movieId)
                        }
                    }
                }
            }
        }
    }

    override fun bindViews(view: View) = with(view){
        progressBar = view.findViewById(R.id.progressBar)
        ivBackdrop = view.findViewById(R.id.ivBackdrop)
        tvName = view.findViewById(R.id.tvName)
        tvRating = view.findViewById(R.id.tvRating)
        tvGenre = view.findViewById(R.id.tvGenre)
        tvOverview = view.findViewById(R.id.tvOverview)
        btnFavorite = view.findViewById(R.id.btnFavorite)
        group = view.findViewById(R.id.group)
    }

    override fun setData() {
        movieId = arguments?.getInt(AppConstants.MOVIE_ID)
        movieId?.let { movieId ->
            viewModel.getMovie(movieId)
            viewModel.checkIfFavorite(movieId)
        }


        viewModel.liveData.observe(viewLifecycleOwner, Observer {result ->
            when(result) {
                is MovieDetailsViewModel.State.ShowLoading -> {
                    progressBar.visibility = View.VISIBLE
                    group.visibility = View.GONE
                }
                is MovieDetailsViewModel.State.HideLoading -> {
                    progressBar.visibility = View.GONE
                    group.visibility = View.VISIBLE
                }
                is MovieDetailsViewModel.State.Result -> {
                    val imageUrl = "${AppConstants.BACKDROP_BASE_URL}${result.movie.backdropPath}"
                    Glide.with(this)
                        .load(imageUrl)
                        .into(ivBackdrop)
                    tvName.text = result.movie.title
                    tvRating.text = "${result.movie.voteAverage}/10"
                    tvGenre.text = result.movie.genres?.first()?.name
                    tvOverview.text = result.movie.overview
                }
                is MovieDetailsViewModel.State.FavoriteMovie -> {
                    when(result.resultCode) {
                        1 -> {
                            btnFavorite.setBackgroundResource(R.drawable.like)
                            Toast.makeText(context, "Successfully added to your favorite movies!", Toast.LENGTH_SHORT).show()
                        }
                        12 -> {
                            Toast.makeText(context, "The movie was updated successfully", Toast.LENGTH_SHORT).show()
                        }
                        13 -> {
                            btnFavorite.setBackgroundResource(R.drawable.unlike)
                            Toast.makeText(context, "The movie was deleted from favorites", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                is MovieDetailsViewModel.State.IsFavorite -> {
                    if(result.favorite) {
                        btnFavorite.setBackgroundResource(R.drawable.like)
                    } else {
                        btnFavorite.setBackgroundResource(R.drawable.unlike)
                    }
                }
                is MovieDetailsViewModel.State.Error -> {
                    Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                }
                is MovieDetailsViewModel.State.IntError -> {
                    Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}