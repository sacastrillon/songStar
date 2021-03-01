package com.example.woo.songstar.activities

import android.app.SearchManager
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.woo.songstar.R
import com.example.woo.songstar.adapters.ArtistAdapter
import com.example.woo.songstar.database.AppDatabase
import com.example.woo.songstar.models.Artist
import com.example.woo.songstar.models.SearchUser
import com.example.woo.songstar.utils.AppConstants
import com.example.woo.songstar.utils.AppSharedPreferences
import com.example.woo.songstar.utils.CircleImage
import com.example.woo.songstar.utils.CommonBottomNavigationBar
import kotlinx.android.synthetic.main.activity_artists.*
import kotlinx.android.synthetic.main.layout_top_bar.*
import org.jetbrains.anko.doAsync
import java.io.File

class ArtistsActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private var db: AppDatabase? = null
    private var artists: List<Artist> = emptyList()
    private var adapter: ArtistAdapter? = null
    private var searchUser: List<SearchUser> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artists)
        this.initView()
    }

    override fun onResume() {
        super.onResume()
        this.getArtists()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        doAsync {
            val userSearch = SearchUser(query!!, AppSharedPreferences.getInstance().getString(this@ArtistsActivity, AppSharedPreferences.USER_ID).toInt())
            db?.searchUserDao()?.insert(userSearch)
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        this.adapter?.filter(newText!!)
        return false
    }

    private fun initView() {
        this.db = AppDatabase.getDatabase(this)
        tvTitle.text = getString(R.string.artists)
        CommonBottomNavigationBar.getInstance().setCommonBottomNavigationBar(this, bottomBarMenu, R.id.artist)

        this.svSearchArtist.setOnQueryTextListener(this)

        val uri = Uri.fromFile(getFileStreamPath(AppSharedPreferences.getInstance().getString(this, AppSharedPreferences.USERNAME)))
        val picture = File(uri.path!!)
        if(picture.exists()) {
            doAsync {
                val image: ImageDecoder.Source = ImageDecoder.createSource(this@ArtistsActivity.contentResolver, uri)
                val bitmap = ImageDecoder.decodeBitmap(image)
                this@ArtistsActivity.ivProfile.setImageDrawable(CircleImage(bitmap))
            }
        }

        this.ivProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java).apply{
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
            overridePendingTransition(0,0)
            this.finish()
        }

    }

    private fun setAdapter() {
        this.rvArtists.layoutManager = LinearLayoutManager(this)
        this.adapter = ArtistAdapter(this, this.artists, this)
        this.rvArtists.adapter = this.adapter
    }

    private fun getArtists() {
        doAsync {
            this@ArtistsActivity.artists = db?.artistDao()?.getAll()!!
            this@ArtistsActivity.searchUser = db?.searchUserDao()?.getByUserId(AppSharedPreferences.getInstance().getString(this@ArtistsActivity, AppSharedPreferences.USER_ID).toInt())!!
            runOnUiThread{
                this@ArtistsActivity.setAdapter()
                //this@ArtistsActivity.setSuggestions()
            }
        }
    }

    fun openSongs(artist: Artist) {
        val intent = Intent(this, SongsActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(AppConstants.ARTIST_ID, artist.id)
        }
        startActivity(intent)
        overridePendingTransition(0,0)
        this.finish()
    }

    fun setSuggestions() {
        val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val to = IntArray(android.R.id.text1)
        val cursorAdapter = SimpleCursorAdapter(
            this,
            android.R.layout.simple_list_item_1,
            null,
            from,
            to,
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        this.svSearchArtist.suggestionsAdapter =  cursorAdapter

    }

}