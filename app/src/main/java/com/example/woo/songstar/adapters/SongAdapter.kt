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
import com.example.woo.songstar.activities.SongsActivity
import com.example.woo.songstar.models.Song
import java.util.*

class SongAdapter (context: Context, songs: List<Song>, favouriteSongs: List<Song>, parent: SongsActivity): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var context: Context? = null
    private var originalSong: List<Song> = emptyList()
    private var song: List<Song> = emptyList()
    private var favouriteSongs: List<Song> = emptyList()
    private var parent: SongsActivity? = null

    init {
        this.context = context
        this.song = songs
        this.originalSong = songs
        this.favouriteSongs = favouriteSongs
        this.parent = parent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(this.context).inflate(R.layout.adapter_song, parent, false)
        return SongViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.song.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val songViewHolder = holder as SongViewHolder
        songViewHolder.bind(this.song[position])
    }

    fun update(favouriteSongs: List<Song>){
        this.favouriteSongs = favouriteSongs
        this.notifyDataSetChanged()
    }

    fun filter(search: String) {
        if(search.isEmpty()) {
            song = emptyList()
            song = originalSong
        } else {
            song = originalSong.filter { it.name.toLowerCase(Locale.getDefault()).contains(search) }
        }
        notifyDataSetChanged()
    }

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView? = itemView.findViewById(R.id.tvSongName)
        private var albumName: TextView? = itemView.findViewById(R.id.tvAlbumName)
        private var favourite: ImageView? = itemView.findViewById(R.id.ivFavourite)

        fun bind(song: Song) {
            this.name?.text = song.name
            this.albumName?.text = song.album
            if(favouriteSongs.contains(song)) {
                this.favourite?.setImageDrawable(context?.let { ContextCompat.getDrawable(it, R.drawable.ic_favourite_selected) })
            } else {
                this.favourite?.setImageDrawable(context?.let { ContextCompat.getDrawable(it, R.drawable.ic_favourite) })
            }
            this.favourite?.setOnClickListener {
                parent?.sendAsFavourite(song)
            }
            this.name?.setOnClickListener {
                parent?.openSongDetails(song)
            }
        }
    }

}