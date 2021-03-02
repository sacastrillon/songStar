package com.example.woo.songstar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.woo.songstar.R
import com.example.woo.songstar.activities.FavouriteSongsActivity
import com.example.woo.songstar.models.Song

class MostListenedSongsAdapter (context: Context, mostListenedSongs: List<Song>, parent: FavouriteSongsActivity): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var context: Context? = null
    private var mostListenedSongs: List<Song> = emptyList()
    private var parent: FavouriteSongsActivity? = null

    init {
        this.context = context
        this.mostListenedSongs = mostListenedSongs
        this.parent = parent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(this.context).inflate(R.layout.adapter_most_listened_song, parent, false)
        return SongViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.mostListenedSongs.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val songViewHolder = holder as SongViewHolder
        songViewHolder.bind(this.mostListenedSongs[position])
    }

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView? = itemView.findViewById(R.id.tvSongName)
        private var artistName: TextView? = itemView.findViewById(R.id.tvArtistName)
        private var albumName: TextView? = itemView.findViewById(R.id.tvAlbumName)

        fun bind(song: Song) {
            this.name?.text = song.name
            this.artistName?.text = parent?.getArtistById(song.artistId)?.name
            this.albumName?.text = song.album
            this.name?.setOnClickListener {
                parent?.openSongDetails(song)
            }
        }
    }

}