package com.dlgsoft.freemarket.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.dlgsoft.freemarket.R
import com.dlgsoft.freemarket.data.db.entities.Site
import com.dlgsoft.freemarket.ui.splash.SplashActivity
import com.google.android.material.textfield.TextInputEditText
import org.koin.android.ext.android.inject

class ProfileFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var regionSpinner: Spinner
    private lateinit var birthdayText: TextInputEditText

    private val profileViewModel: ProfileViewModel by inject()

    lateinit var adapter: ArrayAdapter<Site>
    private var isUserInteracting = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        regionSpinner = view.findViewById(R.id.region_spinner)
        birthdayText = view.findViewById(R.id.input_text_birthday)
        birthdayText.setOnClickListener {
            showDatePickerDialog()
        }
        setupSpinner()

        profileViewModel.sites.observe(viewLifecycleOwner) { pair ->
            val sites = pair.first
            val currentSite = pair.second
            adapter.addAll(sites)
            adapter.notifyDataSetChanged()

            regionSpinner.setSelection(sites.indexOfFirst { site -> site.code == currentSite })
        }
        profileViewModel.getSites()
    }

    private fun setupSpinner() {
        adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayListOf())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        regionSpinner.adapter = adapter
        regionSpinner.onItemSelectedListener = this
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(requireActivity().supportFragmentManager, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        birthdayText.setText(getString(R.string.date, day, month, year))
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedSite = adapter.getItem(position)
        if (isUserInteracting) {
            AlertDialog.Builder(requireContext())
                .setCancelable(true)
                .setTitle(R.string.region)
                .setMessage(getString(R.string.region_message, selectedSite?.name))
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    profileViewModel.apply {
                        setRegion(selectedSite?.code)
                        setCurrency(selectedSite?.defaultCurrencyCode)
                    }
                    // La siguiente responsabilidad es del activity. Se realiza en el fragment solo para simplificar.
                    startActivity(Intent(activity, SplashActivity::class.java))
                    activity?.finish()
                }
                .show()
        } else {
            isUserInteracting = true
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Not implemented
    }
}