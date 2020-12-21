package com.dlgsoft.freemarket.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color.TRANSPARENT
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager.LayoutParams
import android.view.WindowManager.LayoutParams.MATCH_PARENT
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView
import com.dlgsoft.freemarket.R
import com.dlgsoft.freemarket.data.db.entities.SubCategory
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
open class FiltersDialog(
    context: Context
) : Dialog(context) {

    private val customDialogController by lazy { FiltersDialogController() }

    private val applyBtn: Button by lazy { findViewById(R.id.btn_apply) }
    private val cancelBtn: Button by lazy { findViewById(R.id.btn_cancel) }
    private val closeBtn: ImageButton by lazy { findViewById(R.id.btn_close) }
    private val container: EpoxyRecyclerView by lazy { findViewById(R.id.container) }

    fun init(
        subCategories: List<SubCategory>,
        currencyCode: String,
        currentFilter: Filter
    ) {
        setContentView(R.layout.dialog_filters)

        // Se hace el dialogo original transparente
        window?.apply {
            setBackgroundDrawable(ColorDrawable(TRANSPARENT))
            attributes = LayoutParams().apply {
                copyFrom(window?.attributes)
                width = MATCH_PARENT
                height = MATCH_PARENT
            }
        }

        container.apply {
            adapter = customDialogController.adapter
            layoutManager = LinearLayoutManager(context)
        }

        customDialogController.apply {
            this.subCategories = subCategories
            this.currencyCode = currencyCode
            setCurrentFilter(currentFilter)
            requestModelBuild()
        }

        cancelBtn.setOnClickListener { dismiss() }
        closeBtn.setOnClickListener { dismiss() }
    }

    fun setApplyButtonListener(listener: (Filter?) -> Unit) {
        applyBtn.setOnClickListener {
            listener.invoke(customDialogController.getAppliedFilter())
        }
    }
}