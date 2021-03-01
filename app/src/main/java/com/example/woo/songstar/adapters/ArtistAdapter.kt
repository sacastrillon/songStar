package com.example.woo.songstar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.woo.songstar.R
import com.example.woo.songstar.activities.ArtistsActivity
import com.example.woo.songstar.models.Artist
import java.util.*

class ArtistAdapter (context: Context, artist: List<Artist>, parent: ArtistsActivity): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var context: Context? = null
    private var originalArtist: List<Artist> = emptyList()
    private var artist: List<Artist> = emptyList()
    var parent: ArtistsActivity? = null

    init {
        this.context = context
        this.artist = artist
        this.originalArtist = artist
        this.parent = parent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(this.context).inflate(R.layout.adapter_artist, parent, false)
        return ArtistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.artist.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val artistViewHolder = holder as ArtistViewHolder
        artistViewHolder.bind(this.artist[position])
    }

    fun filter(search: String) {
        if(search.isEmpty()) {
            artist = emptyList()
            artist = originalArtist
        } else {
            artist = originalArtist.filter { it.name.toLowerCase(Locale.getDefault()).contains(search) }
        }
        notifyDataSetChanged()
    }

    inner class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView? = itemView.findViewById(R.id.tvArtistName)
        private var cardArtist: CardView? = itemView.findViewById(R.id.cvArtistInfo)

        fun bind(artist: Artist) {
            this.name?.text = artist.name
            this.cardArtist?.setOnClickListener {
                parent?.openSongs(artist)
            }
        }
    }
}