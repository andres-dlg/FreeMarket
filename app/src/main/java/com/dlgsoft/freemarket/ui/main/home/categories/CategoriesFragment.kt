package com.dlgsoft.freemarket.ui.main.home.categories

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView
import com.dlgsoft.freemarket.R
import com.dlgsoft.freemarket.data.db.entities.Category
import org.koin.android.viewmodel.ext.android.viewModel

class CategoriesFragment : Fragment() {

    private val categoriesViewModel by viewModel<CategoriesViewModel>()

    private val categoriesController by lazy {
        CategoriesController {
            val action =
                CategoriesFragmentDirections.actionNavigationCategoriesToNavigationProducts(
                    it.id,
                    it.serverId,
                    it.name
                )
            findNavController().navigate(action)
        }
    }

    lateinit var categoriesList: EpoxyRecyclerView

    // Callbacks que se ocupan de recuperar la informacion ingresada en la barra de busqueda
    private val queryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            categoriesController.filterData(query)
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            categoriesController.filterData(newText)
            return true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoriesList = view.findViewById(R.id.categories_list)
        categoriesList.apply {
            adapter = categoriesController.adapter
            layoutManager = LinearLayoutManager(context)
        }
        categoriesController.setData(emptyList())

        categoriesViewModel.categories.observe(viewLifecycleOwner, dataObserver)
        categoriesViewModel.getCategories()
    }

    private val dataObserver = Observer<List<Category>> {
        categoriesController.isLoading = false
        categoriesController.originalData = it.toList()
        categoriesController.setData(it)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.categories_menu, menu)

        // Asocia la configuracion a la SearchView
        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            setOnQueryTextListener(queryTextListener)
        }
    }
}