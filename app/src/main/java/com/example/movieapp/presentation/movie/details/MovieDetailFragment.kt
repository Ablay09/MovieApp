package com.example.movieapp.presentation.movie.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.base.BaseFragment
import com.example.movieapp.utils.AppConstants
import com.example.movieapp.utils.AppPreferences


class MovieDetailFragment : BaseFragment() {

    private lateinit var viewModel: MovieDetailViewModel

    private lateinit var progressBar: ProgressBar
    private lateinit var ivBackdrop: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvGenre: TextView
    private lateinit var tvOverview: TextView
    private lateinit var ivFavorite: ImageView

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
        viewModel = ViewModelProviders.of(this).get(MovieDetailViewModel::class.java)
        bindViews(view)
        setData()

        val accountId = activity?.applicationContext?.let { accId -> AppPreferences.getAccountId(accId) }
        val sessionId = activity?.applicationContext?.let { sessionId -> AppPreferences.getSessionId(sessionId) }

        val parentFragment = arguments?.getString(AppConstants.PARENT_FRAGMENT)

        ivFavorite.setOnClickListener{
            movieId?.let { movieId ->
                accountId?.let { accountId ->
                    sessionId?.let { sessionId ->
                        if (parentFragment.equals("list_fragment")) {
                            viewModel.setFavorite(accountId, movieId, sessionId, true)
                        } else if (parentFragment.equals("favorite_fragment")){
                            viewModel.setFavorite(accountId, movieId, sessionId, false)
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
        ivFavorite = view.findViewById(R.id.ivFavorite)

        movieId = arguments?.getInt(AppConstants.MOVIE_ID)
    }

    override fun setData() {
        movieId?.let { movieId ->
            viewModel.getMovie(movieId)
        }

        viewModel.liveData.observe(viewLifecycleOwner, Observer {result ->
            when(result) {
                is MovieDetailViewModel.State.ShowLoading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is MovieDetailViewModel.State.HideLoading -> {
                    progressBar.visibility = View.GONE
                }
                is MovieDetailViewModel.State.Result -> {
                    val imageUrl = "${AppConstants.BACKDROP_BASE_URL}${result.movie.backdropPath}"
                    Glide.with(this)
                        .load(imageUrl)
                        .into(ivBackdrop)
                    tvName.text = result.movie.title
                    tvRating.text = "${result.movie.voteAverage}/10"
                    tvGenre.text = result.movie.genres?.first()?.name
                    tvOverview.text = result.movie.overview
                }
                is MovieDetailViewModel.State.FavoriteMovie -> {
                    when(result.resultCode) {
                        1 -> Toast.makeText(context, "Successfully added to your favorite movies!", Toast.LENGTH_SHORT).show()
                        12 -> Toast.makeText(context, "The movie was updated successfully", Toast.LENGTH_SHORT).show()
                        13 -> Toast.makeText(context, "The movie was deleted from favorites", Toast.LENGTH_SHORT).show()
                    }
                }
                is MovieDetailViewModel.State.Error -> {
                    Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                }
                is MovieDetailViewModel.State.IntError -> {
                    Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}