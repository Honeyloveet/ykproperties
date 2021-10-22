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
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.NumberFormat
import java.util.*

class HouseDetails : AppCompatActivity() {

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
    private lateinit var tvSizeDetails: TextView
    private lateinit var tvHouseForSaleOr: TextView
    private lateinit var tvPostedDateProductDetails: TextView
    private lateinit var tvSeenProductDetails: TextView
    private lateinit var tvHouseTypeDetails: TextView
    private lateinit var tvHouseOwnerOr: TextView

    private lateinit var auth: FirebaseAuth

    private val db = Firebase.firestore

    private var sellerPhone: Long = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_house_details)

        val imageList = ArrayList<SlideModel>()

        toolbar = findViewById(R.id.toolBarDetails)

        btnCallSeller = findViewById(R.id.btnCallSeller)
        btnMessageSeller = findViewById(R.id.btnMessageSeller)

        ivProductDetail = findViewById(R.id.ivProductDetail)
        tvProductDetailsID = findViewById(R.id.tvProductDetailsID)
        tvTitleProductDetails = findViewById(R.id.tvTitleProductDetails)
        tvPriceProductDetails = findViewById(R.id.tvPriceProductDetails)
        tvDescription = findViewById(R.id.tvDescription)
        tvSizeDetails = findViewById(R.id.tvSizeDetails)
        tvHouseForSaleOr = findViewById(R.id.tvHouseForSaleOr)
        tvPostedDateProductDetails = findViewById(R.id.tvPostedDateProductDetails)
        tvSeenProductDetails = findViewById(R.id.tvSeenProductDetails)
        tvHouseTypeDetails = findViewById(R.id.tvHouseTypeDetails)
        tvHouseOwnerOr = findViewById(R.id.tvHouseOwnerOr)

        val bundle : Bundle? = intent.extras
        val id = bundle!!.getString("id")
        val location = bundle.getString("location")
        val price = bundle.getLong("price")
        val imgUrls = bundle.getStringArrayList("imgUrls")
        val category = bundle.getString("category")
        val description = bundle.getString("description")
        val houseType = bundle.getString("houseType")
        val size = bundle.getLong("size")
        val purpose = bundle.getString("purpose")
        val posted = bundle.getLong("posted")
        val views = bundle.getLong("views")
        val seller = bundle.getString("seller")
        val userPosted = bundle.getString("userPosted")
        val phone = bundle.getLong("phone")

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
            imageList.add(SlideModel(R.drawable.category_house))
        }

        tvProductDetailsID.text = id
        tvTitleProductDetails.text = "Location: $location"
        tvPriceProductDetails.text = "Br ${NumberFormat.getInstance(Locale.US).format(price)}"
        tvDescription.text = description
        tvSizeDetails.text = "$size Square Meter"
        tvHouseForSaleOr.text = purpose
        tvHouseTypeDetails.text = houseType
        tvHouseOwnerOr.text = seller
        tvPostedDateProductDetails.text = DateUtils.getRelativeTimeSpanString(posted)
        tvSeenProductDetails.text = views.toString()

        ivProductDetail.setImageList(imageList)

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
                    val intent = Intent(this@HouseDetails, FillScreenImageView::class.java)
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