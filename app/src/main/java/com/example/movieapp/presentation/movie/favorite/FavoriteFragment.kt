package com.example.movieapp.presentation.movie.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.example.movieapp.R
import com.example.movieapp.base.BaseFragment
import com.example.movieapp.data.models.MovieData
import com.example.movieapp.presentation.movie.list.MovieAdapter
import com.example.movieapp.utils.AppConstants
import com.example.movieapp.utils.AppPreferences
import com.example.movieapp.utils.PaginationListener
import org.koin.android.ext.android.inject

class FavoriteFragment : BaseFragment() {
    private val viewModel: FavoriteViewModel by inject()
    private lateinit var navController: NavController
    private lateinit var rvFavMovies: RecyclerView
    private lateinit var srlFavMovies: SwipeRefreshLayout

    private var currentPage = PaginationListener.PAGE_START
    private var isLastPage = false
    private var isLoading = false
    private var itemCount = 0

    private var accountId: Int? = null
    private var sessionId: String? = null


    private val onClickListener = object: MovieAdapter.ItemClickListener {
        override fun onItemClick(item: MovieData) {
            navController.navigate(
                R.id.action_favoriteFragment_to_movieDetailsFragment,
                bundleOf(
                    AppConstants.MOVIE_ID to item.id,
                    AppConstants.PARENT_FRAGMENT to "favorite_fragment"
                )
            )
        }
    }


    private val moviesAdapter by lazy {
        MovieAdapter(
            itemClickListener = onClickListener
        )
    }

    private fun setAdapter() {
        rvFavMovies.adapter = moviesAdapter
    }

    private fun initId() {
        accountId = activity?.applicationContext?.let { AppPreferences.getAccountId(it) }
        sessionId = activity?.applicationContext?.let { AppPreferences.getSessionId(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initId()
        bindViews(view)
        setAdapter()
        setData()
    }

    override fun bindViews(view: View) = with(view) {
        navController = Navigation.findNavController(this)
        srlFavMovies = view.findViewById(R.id.srlFavMovies)
        rvFavMovies = view.findViewById(R.id.rvFavMovies)
        val layoutManager = LinearLayoutManager(context)
        rvFavMovies.layoutManager = layoutManager
        rvFavMovies.addOnScrollListener(object: PaginationListener(layoutManager) {

            override fun loadMoreItems() {
                isLoading = true
                currentPage++
                viewModel.loadFavMovies(accountId, sessionId, page = currentPage)
            }

            override fun isLastPage(): Boolean = isLastPage

            override fun isLoading(): Boolean = isLoading
        })
        srlFavMovies.setOnRefreshListener {
            moviesAdapter.clearAll()
            itemCount = 0
            currentPage = PaginationListener.PAGE_START
            isLastPage = false
            viewModel.loadFavMovies(accountId, sessionId, page = currentPage)
        }
    }

    override fun setData() {
        viewModel.loadFavMovies(accountId, sessionId)
        moviesAdapter.clearAll()
        viewModel.liveData.observe(viewLifecycleOwner, Observer { result ->
            when(result) {
                is FavoriteViewModel.State.ShowLoading -> {
                    srlFavMovies.isRefreshing = true
                }
                is FavoriteViewModel.State.HideLoading -> {
                    srlFavMovies.isRefreshing = false
                }
                is FavoriteViewModel.State.Result -> {
                    moviesAdapter.clearAll()
                    itemCount = result.list.size
                    if (currentPage != PaginationListener.PAGE_START) {
                        moviesAdapter.removeLoading()
                    }
                    moviesAdapter.addItems(result.list)
                    if (currentPage < result.totalPages) {
                        moviesAdapter.addLoading()
                    } else {
                        isLastPage = true
                    }
                    isLoading = false
                }
                is FavoriteViewModel.State.Error -> {
                    Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                }
                is FavoriteViewModel.State.IntError -> {
                    Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}