package com.dlgsoft.freemarket.ui.views

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.dlgsoft.freemarket.R
import com.squareup.picasso.Picasso
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ProductItemView(context: Context) : LinearLayout(context), KoinComponent {

    private val name: TextView
    private val price: TextView
    private val image: ImageView
    private val root: CardView
    private val picasso: Picasso by inject()

    @ModelProp
    fun setName(productName: String) {
        name.text = productName
    }

    @ModelProp
    fun setPrice(productPrice: String) {
        price.text = productPrice
    }

    @ModelProp
    fun setImage(imageUrl: String) {
        picasso.load(imageUrl).placeholder(R.drawable.placeholder)
            .into(image)
    }

    @CallbackProp
    fun setListener(listener: (() -> Unit)?) {
        root.setOnClickListener {
            listener?.invoke()
        }
    }

    init {
        inflate(context, R.layout.product_item_view, this)
        name = findViewById(R.id.name)
        image = findViewById(R.id.image)
        price = findViewById(R.id.price)
        root = findViewById(R.id.root)
    }
}