package com.example.movieapp.presentation.movie.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.example.movieapp.R
import com.example.movieapp.base.BaseFragment
import com.example.movieapp.data.models.MovieData
import com.example.movieapp.utils.AppConstants
import com.example.movieapp.utils.PaginationListener
import org.koin.android.ext.android.inject

class MovieListFragment : BaseFragment() {

    private lateinit var navController: NavController
    private val viewModel: MovieListViewModel by inject()
    private lateinit var rvMovies: RecyclerView
    private lateinit var srlMovies: SwipeRefreshLayout

    private var currentPage = PaginationListener.PAGE_START
    private var isLastPage = false
    private var isLoading = false
    private var itemCount = 0

    private val onClickListener = object:
        MovieAdapter.ItemClickListener {
        override fun onItemClick(item: MovieData) {
            val bundle = Bundle()
            bundle.putInt(AppConstants.MOVIE_ID, item.id!!)
            bundle.putString(AppConstants.PARENT_FRAGMENT, "list_fragment")
            navController.navigate(
                R.id.action_moviesListFragment_to_movieDetailsFragment,
                bundle
            )
        }
    }

    private val moviesAdapter by lazy {
        MovieAdapter(
            itemClickListener = onClickListener
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        setAdapter()
        setData()
    }

    override fun bindViews(view: View) = with(view) {
        navController = Navigation.findNavController(this)
        srlMovies = findViewById(R.id.srlMovies)
        rvMovies = findViewById(R.id.rvMovies)
        val layoutManager = LinearLayoutManager(context)
        rvMovies.layoutManager = layoutManager
        rvMovies.addOnScrollListener(object: PaginationListener(layoutManager) {

            override fun loadMoreItems() {
                isLoading = true
                currentPage++
                viewModel.loadMovies(page = currentPage)
            }

            override fun isLastPage(): Boolean = isLastPage

            override fun isLoading(): Boolean = isLoading
        })

        srlMovies.setOnRefreshListener {
            moviesAdapter.clearAll()
            itemCount = 0
            currentPage = PaginationListener.PAGE_START
            isLastPage = false
            viewModel.loadMovies(page = currentPage)
        }
    }

    override fun setData() {
        viewModel.liveData.observe(viewLifecycleOwner, Observer { result ->
            when(result) {
                is MovieListViewModel.State.ShowLoading -> {
                    srlMovies.isRefreshing = true
                }
                is MovieListViewModel.State.HideLoading -> {
                    srlMovies.isRefreshing = false
                }
                is MovieListViewModel.State.Result -> {
                    itemCount = result.list.size
                    if (currentPage != PaginationListener.PAGE_START) {
                        moviesAdapter.removeLoading()
                    }
                    moviesAdapter.addItems(result.list)
                    if (currentPage < result.totalPage) {
                        moviesAdapter.addLoading()
                    } else {
                        isLastPage = true
                    }
                    isLoading = false
                }
                is MovieListViewModel.State.Error -> {
                    Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                }
                is MovieListViewModel.State.IntError -> {
                    Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setAdapter() {
        rvMovies.adapter = moviesAdapter
    }
}
