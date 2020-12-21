package com.dlgsoft.freemarket.ui.product

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.epoxy.EpoxyRecyclerView
import com.dlgsoft.freemarket.R
import com.dlgsoft.freemarket.data.db.entities.ProductPicture
import com.dlgsoft.freemarket.utils.AppBarStateChangeListener
import com.dlgsoft.freemarket.utils.ZoomOutPageTransformer
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ProductDetailActivity : AppCompatActivity() {

    private val viewModel by viewModel<ProductDetailViewModel>()

    private val toolbar: Toolbar by lazy { findViewById(R.id.toolbar) }
    private val appBar: AppBarLayout by lazy { findViewById(R.id.app_bar) }
    private val productDetail: EpoxyRecyclerView by lazy { findViewById(R.id.product_detail) }
    private val viewPager: ViewPager2 by lazy { findViewById(R.id.view_pager) }
    private val tabLayout: TabLayout by lazy { findViewById(R.id.tab_layout) }
    private val buyButton: FloatingActionButton by lazy { findViewById(R.id.fab) }

    lateinit var pagerAdapter: ProductPhotoPagerAdapter
    private val productDetailController by lazy { ProductDetailController() }

    private var isCollapsed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = null
        }

        appBar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                isCollapsed = state == State.COLLAPSED
                invalidateOptionsMenu()
            }
        })

        productDetail.apply {
            adapter = productDetailController.adapter
            layoutManager = LinearLayoutManager(this@ProductDetailActivity)
        }

        // The pager adapter, which provides the pages to the view pager widget.
        pagerAdapter = ProductPhotoPagerAdapter(this)
        viewPager.apply {
            adapter = pagerAdapter
            setPageTransformer(ZoomOutPageTransformer())
        }
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()

        buyButton.setOnClickListener { goToProductWebPage(viewModel.productLink) }

        setupViewModel()
    }

    private fun setupViewModel() {
        val productId = intent.getLongExtra(EXTRA_PRODUCT_ID, DEFAULT_PRODUCT_ID)
        if (productId != DEFAULT_PRODUCT_ID) {
            viewModel.product.observe(this) {
                pagerAdapter.pictures = if (!it.pictures.isNullOrEmpty()) {
                    // Máximo 10 fotos
                    if (it.pictures.size > 10) {
                        it.pictures.subList(0, 9)
                    } else {
                        it.pictures
                    }
                } else {
                    emptyList()
                }
                pagerAdapter.notifyDataSetChanged()
                productDetailController.setData(it)
            }
            viewModel.showError.observe(this) {
                showErrorDialog(it)
            }
            viewModel.getProduct(productId)
        } else {
            showErrorDialog(R.string.product_error)
        }
    }

    private fun showErrorDialog(stringResId: Int) {
        AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(R.string.error)
            .setMessage(stringResId)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                finish()
            }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.product_detail_menu, menu)
        menu?.findItem(R.id.buy)?.isVisible = isCollapsed

        val a = theme.obtainStyledAttributes(
            R.style.Theme_FreeMarket,
            intArrayOf(R.attr.homeAsUpIndicator)
        )
        val attributeResourceId = a.getResourceId(0, 0)
        val drawable = ContextCompat.getDrawable(this, attributeResourceId)
        if (isCollapsed) {
            drawable?.setTint(ContextCompat.getColor(this, R.color.white))
        } else {
            drawable?.setTint(ContextCompat.getColor(this, R.color.black))
        }
        supportActionBar?.setHomeAsUpIndicator(drawable)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.buy -> goToProductWebPage(viewModel.productLink)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToProductWebPage(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    inner class ProductPhotoPagerAdapter(
        fa: AppCompatActivity
    ) : FragmentStateAdapter(fa) {
        var pictures: List<ProductPicture> = emptyList()
        override fun getItemCount(): Int = pictures.size
        override fun createFragment(position: Int): Fragment =
            ProductPhotoSlideFragment.newInstance(pictures[position].url ?: "")
    }

    companion object {
        const val EXTRA_PRODUCT_ID = "productId"
        private const val DEFAULT_PRODUCT_ID = -1L
    }
}