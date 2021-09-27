package net.ykproperties.ykproperties

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide

class ProductDetails : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var ivProductDetail: ImageView
    lateinit var tvProductDetailsID: TextView
    lateinit var tvTitleProductDetails: TextView
    lateinit var tvPriceProductDetails: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        toolbar = findViewById(R.id.toolBarDetails)


        ivProductDetail = findViewById(R.id.ivProductDetail)
        tvProductDetailsID = findViewById(R.id.tvProductDetailsID)
        tvTitleProductDetails = findViewById(R.id.tvTitleProductDetails)
        tvPriceProductDetails = findViewById(R.id.tvPriceProductDetails)

        val bundle : Bundle? = intent.extras
        val id = bundle!!.getInt("id")
        val title = bundle.getString("title")
        val price = bundle.getString("price")
        val imgUrl = bundle.getString("imgUrl")
        val category = bundle.getString("category")

        val detail = "$id $title $price"

        tvProductDetailsID.text = detail
        tvTitleProductDetails.text = title
        tvPriceProductDetails.text = price
        Glide.with(this).load(imgUrl).into(ivProductDetail)

        toolbar.title = category
        setSupportActionBar(toolbar)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
        return true
    }
}