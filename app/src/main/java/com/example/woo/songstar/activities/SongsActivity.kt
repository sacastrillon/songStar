package com.example.woo.songstar.activities

import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.woo.songstar.R
import com.example.woo.songstar.adapters.SongAdapter
import com.example.woo.songstar.database.AppDatabase
import com.example.woo.songstar.intefaces.ApiInterface
import com.example.woo.songstar.models.FavouriteSong
import com.example.woo.songstar.models.ItunesAPIResponse
import com.example.woo.songstar.models.Song
import com.example.woo.songstar.models.SongDetails
import com.example.woo.songstar.utils.*
import kotlinx.android.synthetic.main.activity_artists.bottomBarMenu
import kotlinx.android.synthetic.main.activity_songs.*
import kotlinx.android.synthetic.main.layout_top_bar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class SongsActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private var db: AppDatabase? = null
    private var songs: List<Song> = emptyList()
    private var listToShow: ArrayList<Song> = ArrayList()
    private var favouriteSongs: List<Song> = emptyList()
    private var adapter: SongAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private val upItems = listOf("5 Items", "10 Items", "15 Items")

    private var numItemsPage: Int = 5
    private var totalItems: Int = 0
    private var page: Int = 0
    private var totalPages: Int = 0
    private var isLoading: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songs)
        this.initView()
    }

    override fun onResume() {
        super.onResume()
        if(intent.getIntExtra(AppConstants.ARTIST_ID, -1) != -1) {
            this.getSongsByArtist(intent.getIntExtra(AppConstants.ARTIST_ID, 0))
        } else {
            this.getSongs()
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        this.adapter?.filter(newText!!)
        return false
    }

    private fun initView() {
        this.db = AppDatabase.getDatabase(this)
        tvTitle.text = getString(R.string.songs)
        CommonBottomNavigationBar.getInstance().setCommonBottomNavigationBar(this, bottomBarMenu, R.id.song)

        this.svSearchSongs.setOnQueryTextListener(this)

        val uri = Uri.fromFile(getFileStreamPath(AppSharedPreferences.getInstance().getString(this, AppSharedPreferences.USERNAME)))
        val picture = File(uri.path!!)
        if(picture.exists()) {
            doAsync {
                val image: ImageDecoder.Source = ImageDecoder.createSource(this@SongsActivity.contentResolver, uri)
                val bitmap = ImageDecoder.decodeBitmap(image)
                this@SongsActivity.ivProfile.setImageDrawable(CircleImage(bitmap))
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

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, this.upItems)
        this.spItems.adapter = arrayAdapter
        this.spItems.setSelection(0)

        this.spItems.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                this@SongsActivity.numItemsPage = (position + 1) * 5
                this@SongsActivity.setupPages()
            }
        }

        this.rvSongs.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount: Int = linearLayoutManager?.childCount!!
                val totalItemCount: Int = linearLayoutManager?.itemCount!!
                val firstVisibleItemPosition: Int =
                    linearLayoutManager!!.findFirstVisibleItemPosition()
                if (isLoading) {
                    if (page <= totalPages) {
                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                            this@SongsActivity.isLoading = false
                            page += 1
                            loadList(page)
                        }
                    }
                }

            }
        })
    }

    private fun setupPages() {
        this.listToShow = ArrayList()
        this.page = 0
        this.isLoading = false
        var value: Int = totalItems % numItemsPage
        value = if (value == 0) 0 else 1
        this.totalPages = totalItems / numItemsPage + value
        this.loadList(page)
    }

    private fun loadList(num: Int) {
        val start = num * numItemsPage
        for(i in start until start + numItemsPage) {
            if(i < songs.size) {
                this.listToShow.add(this.songs[i])
            } else {
                break
            }
        }

        this.isLoading = true
        if (this.page > 0) {
            /*this.rvSongs.post {
                this.adapter!!.notifyDataSetChanged()
            }*/
            this.adapter!!.notifyDataSetChanged()
        } else {
            setAdapter()
        }
    }

    private fun setAdapter() {
        this.linearLayoutManager = LinearLayoutManager(this)
        this.rvSongs.layoutManager = this.linearLayoutManager
        this.adapter = SongAdapter(this, this.listToShow, this.favouriteSongs, this)
        this.rvSongs.adapter = this.adapter
    }

    private fun getSongs() {
        doAsync {
            this@SongsActivity.songs = db?.songDao()?.getAll()!!
            this@SongsActivity.favouriteSongs = db?.favouriteSongDao()?.getFavouriteSongs(
                AppSharedPreferences.getInstance().getString(this@SongsActivity, AppSharedPreferences.USER_ID).toInt()
            )!!
            this@SongsActivity.totalItems = songs.size
            runOnUiThread{
                this@SongsActivity.setupPages()
            }
        }
    }

    private fun getSongsByArtist(artistId: Int) {
        doAsync {
            this@SongsActivity.songs = db?.songDao()?.getBySongsByArtist(artistId)!!
            this@SongsActivity.favouriteSongs = db?.favouriteSongDao()?.getFavouriteSongs(
                AppSharedPreferences.getInstance().getString(this@SongsActivity, AppSharedPreferences.USER_ID).toInt()
            )!!
            this@SongsActivity.totalItems = songs.size
            runOnUiThread{
                this@SongsActivity.setupPages()
            }
        }
    }

    fun openSongDetails(song: Song) {
        doAsync{
            val artist = db?.artistDao()?.getArtistById(song.artistId)!!.first()
            val param = artist.name.toLowerCase(Locale.getDefault()).replace(" ", "+") + "+" + song.name.toLowerCase(
                Locale.getDefault()).replace(" ", "+")
            val call = AppUtils.getRetrofit().create(ApiInterface::class.java).search(param).execute()
            uiThread {
                if(call.isSuccessful) {
                    val response = call.body() as ItunesAPIResponse
                    if(response.results.size > 0) {
                        this@SongsActivity.goToDetails(response.results.filter { it.artistName.isNotEmpty()
                                && it.trackName.isNotEmpty() && it.image.isNotEmpty() }.first())
                    } else {
                        Toast.makeText(this@SongsActivity, getString(R.string.not_found), Toast.LENGTH_LONG).show()
                    }
                } else {
                    try {
                        Toast.makeText(this@SongsActivity, call.errorBody().toString(), Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@SongsActivity, e.message, Toast.LENGTH_LONG).show()
                    }

                }
            }
        }
    }

    fun sendAsFavourite(song: Song) {
        doAsync{
            val songs = this@SongsActivity.db?.favouriteSongDao()?.getFavoriteSong(
                AppSharedPreferences.getInstance().getString(this@SongsActivity, AppSharedPreferences.USER_ID).toInt(), song.id)
            if(songs!!.isEmpty()) {
                val favouriteSong = FavouriteSong(song.id,
                    AppSharedPreferences.getInstance().getString(this@SongsActivity, AppSharedPreferences.USER_ID).toInt())
                this@SongsActivity.db?.favouriteSongDao()?.insert(favouriteSong)
                this@SongsActivity.favouriteSongs = db?.favouriteSongDao()?.getFavouriteSongs(
                    AppSharedPreferences.getInstance().getString(this@SongsActivity, AppSharedPreferences.USER_ID).toInt()
                )!!
                runOnUiThread {
                    this@SongsActivity.adapter?.update(this@SongsActivity.favouriteSongs)
                }
            } else {
                runOnUiThread {
                    Toast.makeText(this@SongsActivity, getString(R.string.song_already_on_your_favourites), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun goToDetails(songDetails: SongDetails) {
        val intent = Intent(this, SongDetailsActivity::class.java).apply {
            putExtra(AppConstants.SONG_DETAILS, songDetails)
        }
        startActivity(intent)
    }
}