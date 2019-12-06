package com.example.movieapp.presentation.movie.cinemas.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.base.BaseViewHolder
import com.example.movieapp.data.room.Cinema
import com.example.movieapp.utils.AppConstants

class CinemaListAdapter (
    private val context: Context?,
    private val itemClickListener: ItemClickListener
): RecyclerView.Adapter<CinemaListAdapter.CinemaViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var cinemas = emptyList<Cinema>()

    inner class CinemaViewHolder(itemView: View): BaseViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvCinemaName)
        val tvAddress: TextView = itemView.findViewById(R.id.tvCinemaAddress)
        val ivPoster: ImageView = itemView.findViewById(R.id.ivCinemaPoster)

        fun setItemClick(item: Cinema) {
            itemView.setOnClickListener{
                itemClickListener.onItemClick(item)
            }
        }

        override fun clear() { }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CinemaViewHolder {
        val itemView = inflater.inflate(R.layout.row_item_cinema, parent, false)
        return CinemaViewHolder(itemView)
    }

    override fun getItemCount(): Int =
        cinemas.size

    override fun onBindViewHolder(holder: CinemaViewHolder, position: Int) {
        val current = cinemas[position]
        holder.tvName.text = current.name
        holder.tvAddress.text = current.address
        holder.setItemClick(current)


        val imageUrl = "${AppConstants.POSTER_CINEMA_BASE_URL}${current.poster}"
        Log.d("Cinema: imageUrl", imageUrl)

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.ivPoster)
    }

    internal fun setCinemas(cinemas: List<Cinema>) {
        this.cinemas = cinemas
        notifyDataSetChanged()
    }

    interface ItemClickListener {
        fun onItemClick(item: Cinema)
    }
}