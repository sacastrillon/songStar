package com.example.woo.songstar.activities

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.woo.songstar.R
import com.example.woo.songstar.models.SongDetails
import com.example.woo.songstar.utils.AppConstants
import kotlinx.android.synthetic.main.activity_song_details.*
import kotlinx.android.synthetic.main.layout_top_bar.*
import org.jetbrains.anko.doAsync
import java.net.URL


class SongDetailsActivity : AppCompatActivity() {
    private var songDetails: SongDetails? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_details)

        this.tvTitle.text = getString(R.string.song_details)
        this.ivProfile.visibility = View.GONE
        this.ivBack.visibility = View.VISIBLE

        this.ivBack.setOnClickListener {
            this.finish()
        }

        this.songDetails = intent.getSerializableExtra(AppConstants.SONG_DETAILS) as SongDetails

        this.tvSongTitle.text = songDetails!!.trackName
        this.tvAlbumName.text = songDetails!!.album
        this.tvArtistName.text = songDetails!!.artistName
        this.tvGenderName.text = songDetails!!.gender
    }

    override fun onPostResume() {
        super.onPostResume()
        doAsync {
            val url = URL(songDetails?.image)
            val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            runOnUiThread {
                this@SongDetailsActivity.ivAlbumPic.setImageBitmap(bmp)
            }
        }
    }
}