package com.example.movieapp.base

import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment: Fragment() {
    protected abstract fun bindViews(view: View)

    protected abstract fun setData()
}