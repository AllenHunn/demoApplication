package com.commentsolddemo.awesomestore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.commentsolddemo.awesomestore.Constants.styles
import com.commentsolddemo.awesomestore.handlers.ProductHandler
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ItemDetailFragment : Fragment() {
    private var product: Product? = null

    private lateinit var productHandler: ProductHandler

    private fun bindUI() {
        val styleSpinner = view?.findViewById<Spinner>(R.id.product_style_spinner)
        styleSpinner?.let {
            ArrayAdapter<String>(
                view?.context!!,
                android.R.layout.simple_spinner_dropdown_item,
                styles
            ).also { arrayAdapter ->
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                it.adapter = arrayAdapter
                it.setSelection(arrayAdapter.getPosition(product?.style))
            }
        }

        product?.let {
            view?.findViewById<EditText>(R.id.product_name_field)?.setText(it.product_name)
            view?.findViewById<EditText>(R.id.product_description_field)?.setText(it.description)
            view?.findViewById<EditText>(R.id.product_brand_field)?.setText(it.brand)
            view?.findViewById<EditText>(R.id.product_url_field)?.setText(it.url)
            view?.findViewById<EditText>(R.id.product_type_field)?.setText(it.product_type)
            view?.findViewById<EditText>(R.id.product_shipping_price_field)
                ?.setText(it.shipping_price.toString())
            view?.findViewById<EditText>(R.id.product_note_field)?.setText(it.note)
            view?.findViewById<EditText>(R.id.product_admin_id_field)
                ?.setText(it.admin_id.toString())
        }
    }

    fun getProduct(): Product? {
        return product?.copy(
            product_name = view?.findViewById<EditText>(R.id.product_name_field)?.text.toString(),
            description = view?.findViewById<EditText>(R.id.product_description_field)?.text.toString(),
            style = view?.findViewById<Spinner>(R.id.product_style_spinner)?.selectedItem.toString(),
            brand = view?.findViewById<EditText>(R.id.product_brand_field)?.text.toString(),
            url = view?.findViewById<EditText>(R.id.product_url_field)?.text.toString(),
            product_type = view?.findViewById<EditText>(R.id.product_type_field)?.text.toString(),
            shipping_price = view?.findViewById<EditText>(R.id.product_shipping_price_field)?.text.toString()
                .toInt(),
            note = view?.findViewById<EditText>(R.id.product_note_field)?.text.toString(),
            admin_id = view?.findViewById<EditText>(R.id.product_admin_id_field)?.text.toString()
                .toInt()
        ) ?: run {
            return Product(
                product_name = view?.findViewById<EditText>(R.id.product_name_field)?.text.toString(),
                description = view?.findViewById<EditText>(R.id.product_description_field)?.text.toString(),
                style = view?.findViewById<Spinner>(R.id.product_style_spinner)?.selectedItem.toString(),
                brand = view?.findViewById<EditText>(R.id.product_brand_field)?.text.toString(),
                url = view?.findViewById<EditText>(R.id.product_url_field)?.text.toString(),
                product_type = view?.findViewById<EditText>(R.id.product_type_field)?.text.toString(),
                shipping_price = view?.findViewById<EditText>(R.id.product_shipping_price_field)?.text.toString()
                    .toInt(),
                note = view?.findViewById<EditText>(R.id.product_note_field)?.text.toString(),
                admin_id = view?.findViewById<EditText>(R.id.product_admin_id_field)?.text.toString()
                    .toInt(),
                id = null,
                // In my experience, it's best for the server to set these times for consistency
                created_at = null,
                updated_at = null
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.item_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        productHandler = ProductHandler()

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                val itemId = it.getInt(ARG_ITEM_ID, 0)
                val job = Job()
                val coroutineScope = CoroutineScope(job + Dispatchers.Main)
                coroutineScope.launch {
                    product = productHandler.get(itemId)
                    activity?.findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)?.title =
                        "Edit Product"
                    bindUI()
                }
            }
        } ?: run {
            activity?.findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)?.title =
                "Create New Product"
            bindUI()
        }
    }

    companion object {
        const val ARG_ITEM_ID = "item_id"
    }
}