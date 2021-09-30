package net.ykproperties.ykproperties

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide

class OtherDetails : AppCompatActivity() {

    lateinit var toolbar: Toolbar

    lateinit var ivProductDetail: ImageView
    lateinit var tvProductDetailsID: TextView
    lateinit var tvTitleProductDetails: TextView
    lateinit var tvPriceProductDetails: TextView
    lateinit var tvDescription: TextView
    lateinit var tvItemType: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_details)

        toolbar = findViewById(R.id.toolBarDetails)

        ivProductDetail = findViewById(R.id.ivProductDetail)
        tvProductDetailsID = findViewById(R.id.tvProductDetailsID)
        tvTitleProductDetails = findViewById(R.id.tvTitleProductDetails)
        tvPriceProductDetails = findViewById(R.id.tvPriceProductDetails)
        tvDescription = findViewById(R.id.tvDescription)
        tvItemType = findViewById(R.id.tvItemType)

        val bundle : Bundle? = intent.extras
        val id = bundle!!.getInt("id")
        val title = bundle.getString("title")
        val price = bundle.getString("price")
        val imgUrl = bundle.getString("imgUrl")
        val category = bundle.getString("category")
        val description = bundle.getString("description")

        val detail = "$id $title $price"

        tvProductDetailsID.text = detail
        tvTitleProductDetails.text = title
        tvPriceProductDetails.text = price
        tvDescription.text = description
        tvItemType.text = title
        Glide.with(this).load(imgUrl).into(ivProductDetail)

//        toolbar.title = category
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setTitle(category)
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        toolbar.setNavigationOnClickListener {
            finish();
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
        return true
    }
}