package com.example.movieapp.presentation.movie.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
    private lateinit var spinner: Spinner

    private var currentPage = PaginationListener.PAGE_START
    private var isLastPage = false
    private var isLoading = false
    private var itemCount = 0

    private var state: String = ""

    private val onClickListener = object:
        MovieAdapter.ItemClickListener {
        override fun onItemClick(item: MovieData) {
            val bundle = Bundle()
            item.id?.let { bundle.putInt(AppConstants.MOVIE_ID, it) }
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

        val moviesArr: Array<String> = resources.getStringArray(R.array.get_movies)
        val adapter = context?.let {
            ArrayAdapter<String>(it, R.layout.spinner_item, moviesArr)
        }
        adapter?.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (moviesArr[position]) {
                    "Top rated" -> {
                        state = "topRated"
                        moviesAdapter.clearAll()
                        viewModel.getTopRatedMovies()
                    }
                    "Popular" -> {
                        state = "popular"
                        moviesAdapter.clearAll()
                        viewModel.getPopularMovies()
                    }
                    "Upcoming" -> {
                        state = "upcoming"
                        moviesAdapter.clearAll()
                        viewModel.getUpcomingMovies()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        setAdapter()
        setData()
    }

    override fun bindViews(view: View) = with(view) {
        navController = Navigation.findNavController(this)
        srlMovies = findViewById(R.id.srlMovies)
        rvMovies = findViewById(R.id.rvMovies)
        spinner = findViewById(R.id.spinner)
        val layoutManager = LinearLayoutManager(context)
        rvMovies.layoutManager = layoutManager
        rvMovies.addOnScrollListener(object: PaginationListener(layoutManager) {

            override fun loadMoreItems() {
                isLoading = true
                currentPage++
                viewModel.getPopularMovies(page = currentPage)
            }

            override fun isLastPage(): Boolean = isLastPage

            override fun isLoading(): Boolean = isLoading
        })

        srlMovies.setOnRefreshListener {
            moviesAdapter.clearAll()
            itemCount = 0
            currentPage = PaginationListener.PAGE_START
            isLastPage = false
            when (state) {
                "topRated" -> viewModel.getTopRatedMovies(page = currentPage)
                "popular" -> viewModel.getPopularMovies(page = currentPage)
                "upcoming" -> viewModel.getUpcomingMovies(page = currentPage)
            }

        }
    }

    override fun setData() {
        moviesAdapter.clearAll()
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
                    if (currentPage < result.totalPages) {
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
