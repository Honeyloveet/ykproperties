package net.ykproperties.ykproperties

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.NumberFormat
import java.util.*

class CarDetails : AppCompatActivity() {

    private companion object {
        private const val TAG = "SAMUEL"
    }

    lateinit var toolbar: Toolbar

    private lateinit var btnCallSeller: Button
    private lateinit var btnMessageSeller: Button

    private lateinit var ivProductDetail: ImageSlider
    private lateinit var tvProductDetailsID: TextView
    private lateinit var tvTitleProductDetails: TextView
    private lateinit var tvPriceProductDetails: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvCarForSaleOr: TextView
    private lateinit var tvYearDetails: TextView
    private lateinit var tvTransDetails: TextView
    private lateinit var tvFuelDetails: TextView
    private lateinit var tvColorDetails: TextView
    private lateinit var tvSeenProductDetails: TextView
    private lateinit var tvPlateNoTitleDetails: TextView
    private lateinit var tvPlateNoDetails: TextView
    private lateinit var tvKmDetails: TextView
    private lateinit var tvKmTitleDetails: TextView
    private lateinit var tvPostedDateProductDetails: TextView
    private lateinit var tvCarOwnerOr: TextView

    private lateinit var auth: FirebaseAuth

    private val db = Firebase.firestore

    private var sellerPhone: Long = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_car_details)

        val imageList = ArrayList<SlideModel>()

        toolbar = findViewById(R.id.toolBarDetails)

        auth = Firebase.auth

        btnCallSeller = findViewById(R.id.btnCallSeller)
        btnMessageSeller = findViewById(R.id.btnMessageSeller)

        ivProductDetail = findViewById(R.id.ivProductDetail)
        tvProductDetailsID = findViewById(R.id.tvProductDetailsID)
        tvTitleProductDetails = findViewById(R.id.tvTitleProductDetails)
        tvPriceProductDetails = findViewById(R.id.tvPriceProductDetails)
        tvDescription = findViewById(R.id.tvDescription)
        tvCarForSaleOr = findViewById(R.id.tvCarForSaleOr)
        tvYearDetails = findViewById(R.id.tvYearDetails)
        tvTransDetails = findViewById(R.id.tvTransDetails)
        tvFuelDetails = findViewById(R.id.tvFuelDetails)
        tvColorDetails = findViewById(R.id.tvColorDetails)
        tvSeenProductDetails = findViewById(R.id.tvSeenProductDetails)
        tvPlateNoTitleDetails = findViewById(R.id.tvPlateNoTitleDetails)
        tvPlateNoDetails = findViewById(R.id.tvPlateNoDetails)
        tvKmDetails = findViewById(R.id.tvKmDetails)
        tvKmTitleDetails = findViewById(R.id.tvKmTitleDetails)
        tvPostedDateProductDetails = findViewById(R.id.tvPostedDateProductDetails)
        tvCarOwnerOr = findViewById(R.id.tvCarOwnerOr)

        val bundle : Bundle? = intent.extras
        val id = bundle!!.getString("id")
        val make = bundle.getString("make")
        val model = bundle.getString("model")
        val price = bundle.getLong("price")
        val imgUrls = bundle.getStringArrayList("imgUrls")
        val category = bundle.getString("category")
        val description = bundle.getString("description")
        val posted = bundle.getLong("posted")
        val condition = bundle.getString("condition")
        val year = bundle.getLong("year")
        val color = bundle.getString("color")
        val purpose = bundle.getString("purpose")
        val seller = bundle.getString("seller")
        val transmission = bundle.getString("transmission")
        val views = bundle.getLong("views")
        val fuel = bundle.getString("fuel")
        val engineSize = bundle.getLong("engineSize")
        val phone = bundle.getLong("phone")
        val plate = bundle.getString("plate")
        val mileage = bundle.getLong("mileage")
        val userPosted = bundle.getString("plate")

        sellerPhone = phone

        if (imgUrls!!.size > 1) {
            for (imgUrl in imgUrls) {
                if (imgUrl != "") {
                    imageList.add(SlideModel(imgUrl))
                }
            }
        } else if (imgUrls.size == 1 && imgUrls[0] != "") {
            imageList.add(SlideModel(imgUrls[0]))
        } else {
            imageList.add(SlideModel(R.drawable.category_cars))
        }

        tvProductDetailsID.text = id
        tvTitleProductDetails.text = "$condition-$make $model"
        tvPriceProductDetails.text = "Br ${NumberFormat.getInstance(Locale.US).format(price)}"
        tvDescription.text = description
        tvYearDetails.text = year.toString()
        tvTransDetails.text = transmission
        tvFuelDetails.text = fuel
        tvColorDetails.text = color
        tvCarForSaleOr.text = purpose
        tvCarOwnerOr.text = seller
        tvSeenProductDetails.text = views.toString()
        tvPostedDateProductDetails.text = DateUtils.getRelativeTimeSpanString(posted)

        ivProductDetail.setImageList(imageList)

        if (mileage == 0L) {
            tvKmTitleDetails.isVisible = false
            tvKmDetails.isVisible = false
        }

        if (plate == "") {
            tvPlateNoTitleDetails.isVisible = false
            tvPlateNoDetails.isVisible = false
//            tvPlateNoDetails.setTextColor(getColor(R.color.white))
//            tvPlateNoTitleDetails.setTextColor(getColor(R.color.white))
        }

        db.collection("products").document("$id")
            .update("views", FieldValue.increment(1))
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = category
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        toolbar.setNavigationOnClickListener {
            finish()
        }

        ivProductDetail.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                if (imgUrls[position] != "") {
                    val intent = Intent(this@CarDetails, FillScreenImageView::class.java)
                    intent.putExtra("imgUrl", imgUrls[position])
                    startActivity(intent)
                }
            }
        })

        btnCallSeller.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                requestCallPermissions()
            } else {
                callSeller()
            }
        }

        btnMessageSeller.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                requestSmsPermissions()
            } else {
                smsSeller()
            }
        }

    }

    private fun smsSeller() {
        val smsIntent = Intent(Intent.ACTION_VIEW)
        smsIntent.data = Uri.parse("smsto:")
        smsIntent.type = "vnd.android-dir/mms-sms"
        smsIntent.putExtra("address", "0$sellerPhone")
        startActivity(smsIntent)
    }

    private fun callSeller() {
        val callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.parse("tel:0$sellerPhone")
        startActivity(callIntent)
    }

    private fun requestSmsPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS), 101)
    }

    private fun requestCallPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE), 121)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 121 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            callSeller()
        } else if (requestCode == 101 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            smsSeller()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_details, menu)
        return true
    }
}