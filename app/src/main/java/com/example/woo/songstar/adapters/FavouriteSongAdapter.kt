package com.example.woo.songstar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.woo.songstar.R
import com.example.woo.songstar.activities.FavouriteSongsActivity
import com.example.woo.songstar.models.Song

class FavouriteSongAdapter (context: Context, favouriteSongs: List<Song>, parent: FavouriteSongsActivity): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var context: Context? = null
    private var favouriteSongs: List<Song> = emptyList()
    private var parent: FavouriteSongsActivity? = null

    init {
        this.context = context
        this.favouriteSongs = favouriteSongs
        this.parent = parent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(this.context).inflate(R.layout.adapter_song, parent, false)
        return SongViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.favouriteSongs.size
    }

    fun update(favouriteSongs: List<Song>){
        this.favouriteSongs = favouriteSongs
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val songViewHolder = holder as SongViewHolder
        songViewHolder.bind(this.favouriteSongs[position])
    }

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView? = itemView.findViewById(R.id.tvSongName)
        private var albumName: TextView? = itemView.findViewById(R.id.tvAlbumName)
        private var favourite: ImageView? = itemView.findViewById(R.id.ivFavourite)

        fun bind(song: Song) {
            this.name?.text = song.name
            this.albumName?.text = song.album
            this.favourite?.setImageDrawable(context?.let { ContextCompat.getDrawable(it, R.drawable.ic_close) })
            this.name?.setOnClickListener {
                parent?.openSongDetails(song)
            }
            this.favourite?.setOnClickListener {
                parent?.removeFromFavourites(song)
            }
        }
    }

}