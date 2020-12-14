package com.commentsolddemo.awesomestore

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.paging.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.commentsolddemo.awesomestore.handlers.ProductHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ItemListActivity : AppCompatActivity() {
    // Hardcoded to limit of 50, this could be moved to userpreferences if desired down the road
    private val limit = 50

    private val products: Flow<PagingData<Product>> = Pager(PagingConfig(limit)) {
        ProductsPagingSource(ProductHandler())
    }.flow
        .cachedIn(lifecycleScope)

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            val intent = Intent(view.context, ItemDetailActivity::class.java)
            view.context.startActivity(intent)
        }

        setupRecyclerView(findViewById(R.id.item_list))
    }

    @ExperimentalCoroutinesApi
    private fun setupRecyclerView(recyclerView: RecyclerView) {
        val pla = ProductListAdapter()
        recyclerView.adapter = pla

        lifecycleScope.launch {
            products.collectLatest {
                pla.submitData(it)
            }
        }
    }

    private object ProductComparor : DiffUtil.ItemCallback<Product>() {
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

    }

    private class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.id_text)
        val contentView: TextView = view.findViewById(R.id.content)
    }

    private class ProductListAdapter :
        PagingDataAdapter<Product, ProductViewHolder>(ProductComparor) {
        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as Product
                val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                    putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id)
                }
                v.context.startActivity(intent)
            }
        }

        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            val item = getItem(position)
            holder.idView.text = item?.product_name
            holder.contentView.text = item?.description

            when (position % 2 == 0) {
                true -> {
                    holder.itemView.setBackgroundColor(Color.LTGRAY)
                }
                false -> {
                    holder.itemView.setBackgroundColor(Color.GRAY)
                }
            }

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_content, parent, false)
            val layoutParams = view.layoutParams
            layoutParams.width = parent.width
            layoutParams.height = parent.height / 4
            view.layoutParams = layoutParams
            return ProductViewHolder(view)
        }

    }
}

