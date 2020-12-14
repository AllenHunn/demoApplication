package com.commentsolddemo.awesomestore

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.commentsolddemo.awesomestore.handlers.ProductHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class ItemDetailActivity : AppCompatActivity() {
    private lateinit var fragment: ItemDetailFragment

    private var isNew: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        setSupportActionBar(findViewById(R.id.detail_toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            lifecycleScope.launch {
                val productHandler = ProductHandler()
                val product = fragment.getProduct()
                product?.let {
                    if (isNew)
                        productHandler.create(it)
                    else
                        productHandler.update(it)
                }
                navigateUpTo(
                    Intent(
                        fragment.activity?.applicationContext,
                        ItemListActivity::class.java
                    )
                )
            }
        }

        findViewById<FloatingActionButton>(R.id.delete).setOnClickListener {
            AlertDialog
                .Builder(this)
                .setTitle("Confirm")
                .setMessage("Are you sure you want to delete this product?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    lifecycleScope.launch {
                        val productHandler = ProductHandler()
                        val product = fragment.getProduct()
                        product?.let {
                            productHandler.delete(it)
                        }
                        navigateUpTo(
                            Intent(
                                fragment.activity?.applicationContext,
                                ItemListActivity::class.java
                            )
                        )
                    }
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            fragment = ItemDetailFragment().apply {
                if (intent.hasExtra(ItemDetailFragment.ARG_ITEM_ID)) {
                    arguments = Bundle().apply {
                        putInt(
                            ItemDetailFragment.ARG_ITEM_ID,
                            intent.getIntExtra(ItemDetailFragment.ARG_ITEM_ID, 0)
                        )
                    }
                } else {
                    findViewById<FloatingActionButton>(R.id.delete).visibility = View.INVISIBLE
                    isNew = true
                }
            }

            supportFragmentManager.beginTransaction()
                .add(R.id.item_detail_container, fragment)
                .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                navigateUpTo(Intent(this, ItemListActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}