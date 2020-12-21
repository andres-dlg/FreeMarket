package com.dlgsoft.freemarket.ui.product

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.dlgsoft.freemarket.R
import com.github.chrisbanes.photoview.PhotoView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject

class ProductPhotoDetailActivity : AppCompatActivity() {

    private val image: PhotoView by lazy { findViewById(R.id.image) }
    private val progress: ProgressBar by lazy { findViewById(R.id.progress) }
    private val closeBtn: ImageButton by lazy { findViewById(R.id.close) }

    private val picasso: Picasso by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_photo_detail)

        val imageUrl = intent?.extras?.getString(EXTRA_IMAGE_URL)
        picasso.load(imageUrl)
            .placeholder(R.drawable.placeholder)
            .into(
                image,
                object : Callback {
                    override fun onSuccess() {
                        showViews()
                    }

                    override fun onError() {
                        showViews()
                    }
                }
            )

        closeBtn.setOnClickListener { finish() }
    }

    private fun showViews() {
        image.visibility = View.VISIBLE
        progress.visibility = View.GONE
    }

    companion object {
        const val EXTRA_IMAGE_URL = "image_url"
    }
}
