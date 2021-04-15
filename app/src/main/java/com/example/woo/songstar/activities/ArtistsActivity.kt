package com.example.woo.songstar.activities

import android.app.SearchManager
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.woo.songstar.R
import com.example.woo.songstar.adapters.ArtistAdapter
import com.example.woo.songstar.database.dao.ArtistDao
import com.example.woo.songstar.database.dao.SearchUserDao
import com.example.woo.songstar.models.Artist
import com.example.woo.songstar.models.SearchUser
import com.example.woo.songstar.utils.AppConstants
import com.example.woo.songstar.utils.AppSharedPreferences
import com.example.woo.songstar.utils.CircleImage
import com.example.woo.songstar.utils.CommonBottomNavigationBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_artists.*
import kotlinx.android.synthetic.main.layout_top_bar.*
import org.jetbrains.anko.doAsync
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class ArtistsActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    @Inject
    lateinit var artistDao: ArtistDao
    @Inject
    lateinit var searchUserDao: SearchUserDao

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
            val userSearch = SearchUser(query!!, AppSharedPreferences.getString(this@ArtistsActivity, AppSharedPreferences.USER_ID)!!.toInt())
            this@ArtistsActivity.searchUserDao.insert(userSearch)
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        this.adapter?.filter(newText!!)
        return false
    }

    private fun initView() {
        tvTitle.text = getString(R.string.artists)
        CommonBottomNavigationBar.setCommonBottomNavigationBar(this, bottomBarMenu, R.id.artist)

        this.svSearchArtist.setOnQueryTextListener(this)

        val uri = Uri.fromFile(getFileStreamPath(AppSharedPreferences.getString(this, AppSharedPreferences.USERNAME)))
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
            this@ArtistsActivity.artists = this@ArtistsActivity.artistDao.getAll()
            this@ArtistsActivity.searchUser = this@ArtistsActivity.searchUserDao.getByUserId(AppSharedPreferences.getString(this@ArtistsActivity, AppSharedPreferences.USER_ID)!!.toInt())
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