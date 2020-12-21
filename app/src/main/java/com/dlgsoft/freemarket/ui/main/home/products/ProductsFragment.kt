package com.dlgsoft.freemarket.ui.main.home.products

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView
import com.dlgsoft.freemarket.R
import com.dlgsoft.freemarket.ui.dialogs.FiltersDialog
import com.dlgsoft.freemarket.ui.product.ProductDetailActivity
import com.dlgsoft.freemarket.ui.product.ProductDetailActivity.Companion.EXTRA_PRODUCT_ID
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ProductsFragment : Fragment() {

    private val productsController by lazy {
        ProductsController { productId ->
            startActivity(Intent(context, ProductDetailActivity::class.java).apply {
                putExtra(EXTRA_PRODUCT_ID, productId)
            })
        }
    }

    private val viewModel by viewModel<ProductsViewModel>()

    private val args: ProductsFragmentArgs by navArgs()

    private lateinit var productsList: EpoxyRecyclerView
    private var filterIcon: ImageView? = null
    private var badge: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productsList = view.findViewById(R.id.products_list)
        productsList.apply {
            adapter = productsController.adapter
            layoutManager = LinearLayoutManager(context)
        }
        productsController.setData(emptyList())

        viewModel.products.observe(viewLifecycleOwner, {
            productsController.isLoading = false
            productsController.setData(it)
        })
        viewModel.showError.observe(viewLifecycleOwner, {
            showErrorDialog(it)
        })
        viewModel.getProducts(args.categoryServerId, args.categoryId)
        viewModel.getFiltersData(args.categoryId)
    }

    private fun showErrorDialog(it: String?) {
        AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setTitle(R.string.error)
            .setMessage(it)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.products_menu, menu)

        // Recupero el menu
        val menuItem = menu.findItem(R.id.filter)

        // Guardo la vista
        val view = menuItem.actionView

        if (view != null) {
            filterIcon = view.findViewById(R.id.filter_icon)
            badge = view.findViewById(R.id.badge)
            view.setOnClickListener {
                onOptionsItemSelected(menuItem)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.filter) {
            showFiltersDialog()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showFiltersDialog() {
        FiltersDialog(requireContext()).apply {
            init(viewModel.subCategories, viewModel.currencyCode, viewModel.currentFilter)
            setApplyButtonListener {
                if (it != null) {
                    viewModel.getProductsForAppliedFilter(it)
                    updateFilterMenuView()
                    dismiss()
                } else {
                    Toast.makeText(requireContext(), R.string.filter_error, Toast.LENGTH_SHORT)
                        .show()
                }
            }
            show()
        }
    }

    /** Actualiza el icono del menu si hay algun filtro aplicado.
     *
     * En caso de haber un filtro aplicado, se mostrará el icono relleno de filtro junto con un
     * indicador rojo.
     *
     * En caso de no haber filtros aplicados, se mostrara el icono de filtro sin rellenar y desaparecerá
     * el indicador rojo. **/
    private fun updateFilterMenuView() {
        if (viewModel.currentFilter.isSet()) {
            filterIcon?.setImageResource(R.drawable.ic_filter_white_24dp)
            badge?.visibility = View.VISIBLE
        } else {
            filterIcon?.setImageResource(R.drawable.ic_filter_outline_white_24dp)
            badge?.visibility = View.INVISIBLE
        }
    }
}