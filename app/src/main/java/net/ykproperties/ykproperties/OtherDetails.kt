package net.ykproperties.ykproperties

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.Menu
import android.view.View
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

class OtherDetails : AppCompatActivity() {

    private companion object {
        private const val TAG = "SAMUEL"
    }

    lateinit var toolbar: Toolbar

    private lateinit var progressDialog: AlertDialog

    private lateinit var btnCallSeller: Button
    private lateinit var btnMessageSeller: Button

    private lateinit var ivProductDetail: ImageSlider
    private lateinit var tvProductDetailsID: TextView
    private lateinit var tvTitleProductDetails: TextView
    private lateinit var tvPriceProductDetails: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvSeenProductDetails: TextView
    private lateinit var tvPostedDateProductDetails: TextView
    private lateinit var tvItemForSaleOr: TextView
    private lateinit var tvItemOwnerOr: TextView
    private lateinit var tvOtherDetailStatus: TextView

    private lateinit var auth: FirebaseAuth

    private val db = Firebase.firestore

    private var sellerPhone: Long = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_other_details)

        val imageList = ArrayList<SlideModel>()

        toolbar = findViewById(R.id.toolBarDetails)

        setupCustomProgressDialog()

        btnCallSeller = findViewById(R.id.btnCallSeller)
        btnMessageSeller = findViewById(R.id.btnMessageSeller)

        ivProductDetail = findViewById(R.id.ivProductDetail)
        tvProductDetailsID = findViewById(R.id.tvProductDetailsID)
        tvTitleProductDetails = findViewById(R.id.tvTitleProductDetails)
        tvPriceProductDetails = findViewById(R.id.tvPriceProductDetails)
        tvDescription = findViewById(R.id.tvDescription)
        tvSeenProductDetails = findViewById(R.id.tvSeenProductDetails)
        tvPostedDateProductDetails = findViewById(R.id.tvPostedDateProductDetails)
        tvItemForSaleOr = findViewById(R.id.tvItemForSaleOr)
        tvItemOwnerOr = findViewById(R.id.tvItemOwnerOr)
        tvOtherDetailStatus = findViewById(R.id.tvOtherDetailStatus)

        progressDialog.show()
        progressDialog.findViewById<TextView>(R.id.tvProgressStatus).setTextColor(getColor(R.color.white))
        progressDialog.findViewById<TextView>(R.id.tvProgressStatus).text = "Loading..."
        progressDialog.window?.setBackgroundDrawableResource(R.color.progress_bar_background)

        val bundle : Bundle? = intent.extras
        val id = bundle!!.getString("id")
        val title = bundle.getString("title")
        val price = bundle.getLong("price")
        val imgUrls = bundle.getStringArrayList("imgUrls")
        val category = bundle.getString("category")
        val description = bundle.getString("description")
        val purpose = bundle.getString("purpose")
        val posted = bundle.getLong("posted")
        val views = bundle.getLong("views")
        val phone = bundle.getLong("phone")
        val seller = bundle.getString("seller")
        val userPosted = bundle.getString("userPosted")
        val sold = bundle.getBoolean("sold")

        db.collection("products")
            .document("$id")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                tvSeenProductDetails.text = documentSnapshot.get("views").toString()
                progressDialog.dismiss()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error Getting document", e)
                progressDialog.dismiss()
            }

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
        tvTitleProductDetails.text = title
        tvPriceProductDetails.text = "Br ${NumberFormat.getInstance(Locale.US).format(price)}"
        tvDescription.text = description
        tvItemForSaleOr.text = purpose
        tvItemOwnerOr.text = seller
        tvSeenProductDetails.text = views.toString()
        tvPostedDateProductDetails.text = DateUtils.getRelativeTimeSpanString(posted)

        ivProductDetail.setImageList(imageList)

        if (sold) {
            tvOtherDetailStatus.visibility = View.VISIBLE
            tvOtherDetailStatus.bringToFront()
        } else {
            tvOtherDetailStatus.visibility = View.GONE
        }

        db.collection("products").document("$id")
            .update("views", FieldValue.increment(1))
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

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
            finish()
        }

        ivProductDetail.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                if (imgUrls[position] != "") {
                    val intent = Intent(this@OtherDetails, FillScreenImageView::class.java)
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

    private fun setupCustomProgressDialog() {
        val alertView = View.inflate(this, R.layout.custom_progress_bar,null)

        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setView(alertView)
        alertBuilder.setCancelable(false)
        progressDialog = alertBuilder.create()
        progressDialog.setCanceledOnTouchOutside(false)
    }

    private fun smsSeller() {
//        val smsIntent = Intent(Intent.ACTION_VIEW)
//        smsIntent.data = Uri.parse("smsto:")
//        smsIntent.type = "vnd.android-dir/mms-sms"
//        smsIntent.putExtra("address", "0$sellerPhone")
//        startActivity(smsIntent)

        val smsIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse("smsto:")
            putExtra("address", "0$sellerPhone")
            type = "vnd.android-dir/mms-sms"
        }

        try {
            startActivity(smsIntent)
        } catch (e: ActivityNotFoundException) {
            Log.d(TAG, "Sms Activity Not Found: $e")
        }

    }

    private fun callSeller() {
//        val callIntent = Intent(Intent.ACTION_DIAL)
//        callIntent.data = Uri.parse("tel:0$sellerPhone")
//        startActivity(callIntent)

        val callIntent = Intent().apply {
            action = Intent.ACTION_DIAL
            data = Uri.parse("tel:0$sellerPhone")
        }
        try {
            startActivity(callIntent)
        } catch (e: ActivityNotFoundException) {
            Log.d(TAG, "Call Activity Not Found: $e")
        }
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