package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var imageurl : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()
    }
    private fun loadMeme() {
        imageurl = "https://meme-api.herokuapp.com/gimme"
        progress_circular.visibility = View.VISIBLE
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, imageurl, null,
            { response ->
                val imageUrl = response.get("url").toString()
                Glide.with(this).load(imageUrl).listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress_circular.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress_circular.visibility = View.GONE
                        return false
                    }
                }).into(memeImageView)
            },
            { error ->
                Toast.makeText(
                    this,
                    "something went wrong",
                    Toast.LENGTH_LONG
                ).show()
            }
        )
//    queue.add(jsonObjectRequest)
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey, checkout this cool meme from  reddit! $imageurl" )
        val chooser = Intent.createChooser(intent, "Share meme...")
        startActivity(intent)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}