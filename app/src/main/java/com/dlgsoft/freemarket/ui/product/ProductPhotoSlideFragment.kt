package com.dlgsoft.freemarket.ui.product

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.dlgsoft.freemarket.R
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject

private const val ARG_IMAGE_URL = "imageUrl"

class ProductPhotoSlideFragment : Fragment() {
    private var imageUrl: String? = null

    private val picasso: Picasso by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageUrl = it.getString(ARG_IMAGE_URL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_photo_slide, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val image = view.findViewById<ImageView>(R.id.image)
        picasso.load(imageUrl).placeholder(R.drawable.placeholder).into(image)
        image.setOnClickListener {
            startActivity(Intent(activity, ProductPhotoDetailActivity::class.java).apply {
                putExtra(ProductPhotoDetailActivity.EXTRA_IMAGE_URL, imageUrl)
            })
        }
    }

    companion object {
        fun newInstance(url: String) =
            ProductPhotoSlideFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMAGE_URL, url)
                }
            }
    }
}