package com.dlgsoft.freemarket.ui.views

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.dlgsoft.freemarket.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class CategoryView(context: Context) : LinearLayout(context), KoinComponent {

    private val name: TextView
    private val image: ImageView
    private val progressBar: ProgressBar
    private val root: CardView
    private val picasso: Picasso by inject()

    @ModelProp
    fun setName(categoryName: String) {
        name.text = categoryName
    }

    @ModelProp
    fun setPicture(picture: String) {
        picasso.load(picture.replaceFirst("http", "https"))
            .into(image, object : Callback {
                override fun onSuccess() {
                    hideProgress()
                }

                override fun onError() {
                    hideProgress()
                }
            })
    }

    @CallbackProp
    fun setListener(listener: (() -> Unit)?) {
        root.setOnClickListener {
            listener?.invoke()
        }
    }

    private fun hideProgress() {
        progressBar.visibility = GONE
    }

    init {
        inflate(context, R.layout.category_item_view, this)
        name = findViewById(R.id.name)
        image = findViewById(R.id.image)
        progressBar = findViewById(R.id.progress)
        root = findViewById(R.id.root)
    }
}