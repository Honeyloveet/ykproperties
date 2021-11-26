package net.ykproperties.ykproperties

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.bumptech.glide.Glide
import com.cottacush.android.currencyedittext.CurrencyInputWatcher
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.loadBitmap
import kotlinx.coroutines.launch
import net.ykproperties.ykproperties.model.ProductsModelParcelable
import net.ykproperties.ykproperties.util.ConnectionLiveData
import net.ykproperties.ykproperties.util.RequestPermissions
import java.io.File
import java.io.IOException
import java.text.NumberFormat
import java.util.*

class UserProductEdit : AppCompatActivity() {

    //region declarations
    private companion object {
        private const val TAG = "ProductEdit"
    }

    private lateinit var requestPermissions: RequestPermissions

    private lateinit var networkConnectionStatus: ConnectionLiveData

    private lateinit var auth: FirebaseAuth

    private val db = Firebase.firestore

    private val storageRef = Firebase.storage.reference

    private lateinit var progressDialog: AlertDialog

    private lateinit var toolbar: Toolbar

    private lateinit var editPostLayout: ConstraintLayout

    private lateinit var btnUpdatePost: Button

    private lateinit var autoComTvCategories: AutoCompleteTextView
    private lateinit var autoComTvMake: AutoCompleteTextView
    private lateinit var autoComTvCondition: AutoCompleteTextView
    private lateinit var autoComTvTransmission: AutoCompleteTextView
    private lateinit var autoComTvFuel: AutoCompleteTextView
    private lateinit var autoComTvCommonOwnerOr: AutoCompleteTextView
    private lateinit var autoComTvCommonSaleOr: AutoCompleteTextView
    private lateinit var autoComTvHouseType: AutoCompleteTextView
    private lateinit var autoComTvHouseSize: AutoCompleteTextView
    private lateinit var autoComTvHouseLocation: AutoCompleteTextView
    private lateinit var autoComTvCommonPhone: AutoCompleteTextView
    private lateinit var autoComTvLandLocation: AutoCompleteTextView
    private lateinit var autoComTvLandSize: AutoCompleteTextView
    private lateinit var autoComTvOtherTitle: AutoCompleteTextView
    private lateinit var autoComTvModel: AutoCompleteTextView
    private lateinit var autoComTvYear: AutoCompleteTextView
    private lateinit var autoComTvColor: AutoCompleteTextView
    private lateinit var autoComTvMileage: AutoCompleteTextView
    private lateinit var autoComTvPlate: AutoCompleteTextView
    private lateinit var autoComTvEngineSize: AutoCompleteTextView

    private lateinit var textInputLayoutCommonDesc: TextInputLayout
    private lateinit var etCommonPrice: TextInputEditText
    private lateinit var etCommonDesc: TextInputEditText

    private lateinit var ivPhoto1: ImageView
    private lateinit var ivPhoto2: ImageView

    private lateinit var cvCar: CardView
    private lateinit var cvHouse: CardView
    private lateinit var cvLand: CardView
    private lateinit var cvOther: CardView
    private lateinit var cvCommon: CardView

    private var actualImageFileOne: File? = null
    private var actualImageFileTwo: File? = null
    private var compressedImageOne: File? = null
    private var compressedImageTwo: File? = null

    private lateinit var imageOneBmp: Bitmap
    private lateinit var imageTwoBmp: Bitmap

    private lateinit var uuidImageOneName: String
    private lateinit var uuidImageTwoName: String

    private var pictureOneSelected: Boolean = false
    private var pictureTwoSelected: Boolean = false

    private var imagesUriToUpload = ArrayList<Uri>()
    private var imagesUriString = ArrayList<String>()

    private var imageFileOneToUpload: File? = null
    private var imageFileTwoToUpload: File? = null

    private lateinit var imageOneUriToUpload: Uri
    private lateinit var imageTwoUriToUpload: Uri

    //endregion

    private val selectPictureLauncherOne = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        if (uri != null) {
            actualImageFileOne = FileUtil.from(this, uri)?.also {
                imageOneBmp = loadBitmap(it)
                uuidImageOneName = UUID.randomUUID().toString()
            }
            val isSavedSuccessfully = savePhotoToInternalStorage(uuidImageOneName, actualImageFileOne, 1)
            if (isSavedSuccessfully) {

                if (pictureOneSelected) {
                    imagesUriToUpload.remove(imageOneUriToUpload)
                }

                imageFileOneToUpload = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"$uuidImageOneName.jpg")
                Toast.makeText(this,"${imageFileOneToUpload!!.toUri()}", Toast.LENGTH_SHORT).show()
                imageOneUriToUpload = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"$uuidImageOneName.jpg").toUri()
                ivPhoto1.setImageBitmap(imageOneBmp)

                imagesUriToUpload.add(imageOneUriToUpload)
                pictureOneSelected = true
            }
        }
    }

    private val selectPictureLauncherTwo = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        if (uri != null) {
            actualImageFileTwo = FileUtil.from(this, uri)?.also {
                imageTwoBmp = loadBitmap(it)
                uuidImageTwoName = UUID.randomUUID().toString()
            }
            val isSavedSuccessfully = savePhotoToInternalStorage(uuidImageTwoName, actualImageFileTwo, 2)
            if (isSavedSuccessfully) {

                if (pictureTwoSelected) {
                    imagesUriToUpload.remove(imageTwoUriToUpload)
                }

                imageFileTwoToUpload = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$uuidImageTwoName.jpg")
                imageTwoUriToUpload = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$uuidImageTwoName.jpg").toUri()
                ivPhoto2.setImageBitmap(imageTwoBmp)

                imagesUriToUpload.add(imageTwoUriToUpload)
                pictureTwoSelected = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_user_product_edit)

        editPostLayout = findViewById(R.id.editPostLayout)

        networkConnectionStatus = ConnectionLiveData(applicationContext)

        requestPermissions = RequestPermissions(this, this)

        auth = Firebase.auth

        setupControlViews()

        setupCustomProgressDialog()

        checkPermissions()

        checkInternetConnectionStatus()

        deleteImagesFile()

        val productToEdit = intent.getParcelableExtra<ProductsModelParcelable>("productToEdit")
        changeCategoryVisibility(productToEdit?.category!!)
        setProductValuesToViews(productToEdit)

        toolbar = findViewById(R.id.toolBarProductEdit)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Edit your product ${productToEdit.category}"
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        setupControlDropDownLists()

        etCommonPrice.addTextChangedListener(CurrencyInputWatcher(etCommonPrice,"Birr ", Locale.getDefault()))

        ivPhoto1.setOnClickListener {
            selectPictureLauncherOne.launch("image/*")
        }

        ivPhoto2.setOnClickListener {
            selectPictureLauncherTwo.launch("image/*")
        }

        toolbar.setNavigationOnClickListener {
            deleteImagesFile()
            finish()
        }
    }

    private fun savePhotoToInternalStorage(filename: String, imageFileSelected: File?, selectedImage: Int): Boolean {
        try {
            imageFileSelected?.let { imageFile ->
                lifecycleScope.launch {
                    // Default compression with custom destination file

                    val targetSize = 200.0
                    val length = imageFile.length()
                    val fileSizeInKB = (length / 1024).toString().toDouble()

                    var quality = 100
                    if (fileSizeInKB > targetSize) {
                        quality = ((targetSize / fileSizeInKB) * 100).toInt()
                    }

                    if (selectedImage == 1) {
                        if (quality == 100) {
                            compressedImageOne = Compressor.compress(this@UserProductEdit, imageFile) {
                                default(width = 620)
                                getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.also {
                                    val file = File("${it.absolutePath}${File.separator}${filename}.jpg")
                                    destination(file)
                                }
                            }
                        } else {
                            compressedImageOne = Compressor.compress(this@UserProductEdit, imageFile) {
                                default(width = 620, format = Bitmap.CompressFormat.WEBP, quality = quality)
                                getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.also {
                                    val file = File("${it.absolutePath}${File.separator}${filename}.jpg")
                                    destination(file)
                                }
                            }
                        }
                    } else if (selectedImage == 2) {
                        if (quality == 100) {
                            compressedImageTwo = Compressor.compress(this@UserProductEdit, imageFile) {
                                default(width = 620)
                                getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.also {
                                    val file = File("${it.absolutePath}${File.separator}${filename}.jpg")
                                    destination(file)
                                }
                            }
                        } else {
                            compressedImageTwo = Compressor.compress(this@UserProductEdit, imageFile) {
                                default(width = 620, format = Bitmap.CompressFormat.WEBP, quality = quality)
                                getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.also {
                                    val file = File("${it.absolutePath}${File.separator}${filename}.jpg")
                                    destination(file)
                                }
                            }
                        }
                    }
                }
            }
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }

    private fun deleteImagesFile() {
        val imageDirs = getExternalFilesDirs(Environment.DIRECTORY_PICTURES)
        if (imageDirs.isNotEmpty()) {
            for (imageDir in imageDirs) {
                if (imageDir.name == "Pictures") {
                    val files = imageDir.listFiles()
                    files?.forEach { file ->
                        try {
                            file.delete()
                            Log.i(TAG, "Deleted ${file.name}")
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.v(TAG, "Failed to delete:" + e.printStackTrace())
                        }
                    }
                }
            }
        }
    }

    private fun checkInternetConnectionStatus() {
        networkConnectionStatus.observe(this, { isConnected ->

            if (!isConnected) {
                val snackBar = Snackbar.make(
                    editPostLayout,
                    "No Internet Connection!",
                    Snackbar.LENGTH_INDEFINITE
                )
                snackBar.setAction("Retry"){
                    snackBar.dismiss()
                    checkInternetConnectionStatus()
                }.show()
            }

        })
    }

    private fun checkPermissions() {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (!requestPermissions.hasWriteExternalStoragePermission()) {
                requestPermissions.requestReadWritePermissions()
            }
        } else {
            if (!requestPermissions.hasReadExternalStoragePermission()) {
                requestPermissions.requestReadWritePermissions()
            }
        }

    }

    private fun showPermissionDeniedDialog() {

        val dialog = MaterialDialog(this)
            .noAutoDismiss()
            .cancelable(false)
            .cancelOnTouchOutside(false)
            .cornerRadius(14f)
            .customView(R.layout.permission_denied_layout)

        dialog.findViewById<Button>(R.id.btnYes).setOnClickListener {
            dialog.dismiss()
            checkPermissions()
        }

        dialog.findViewById<Button>(R.id.btnNo).setOnClickListener {
            dialog.dismiss()
            finish()
        }

        dialog.show()

    }

    private fun setupCustomProgressDialog() {
        val alertView = View.inflate(this@UserProductEdit, R.layout.custom_progress_bar,null)

        val alertBuilder = AlertDialog.Builder(this@UserProductEdit)
        alertBuilder.setView(alertView)
        alertBuilder.setCancelable(false)
        progressDialog = alertBuilder.create()
        progressDialog.setCanceledOnTouchOutside(false)
    }

    private fun setProductImagesToViews(productToEdit: ProductsModelParcelable) {
        if (productToEdit.pictures.size > 1) {
            if (productToEdit.pictures[0] != "") {
                Glide.with(this).load(productToEdit.pictures[0]).placeholder(R.drawable.ic_add_photo).into(ivPhoto1)
            }
            if (productToEdit.pictures[1] != "") {
                Glide.with(this).load(productToEdit.pictures[1]).placeholder(R.drawable.ic_add_photo).into(ivPhoto2)
            }
        } else if (productToEdit.pictures.size == 1 && productToEdit.pictures[0] != "") {
            Glide.with(this).load(productToEdit.pictures[0]).placeholder(R.drawable.ic_add_photo).into(ivPhoto1)
        }
    }

    private fun setProductValuesToViews(productToEdit: ProductsModelParcelable) {
        when(productToEdit.category) {
            "Cars" -> {
                setCarCategoryValues(productToEdit)
            }
            "House" -> {
                setHouseCategoryValues(productToEdit)
            }
            "Land" -> {
                setLandCategoryValues(productToEdit)
            }
            "Other" -> {
                setOtherCategoryValues(productToEdit)
            }
        }
        setProductImagesToViews(productToEdit)
    }

    private fun setCarCategoryValues(productToEdit: ProductsModelParcelable) {
        autoComTvMake.setText(productToEdit.make)
        autoComTvModel.setText(productToEdit.model)
        autoComTvYear.setText(productToEdit.year.toString())
        autoComTvColor.setText(productToEdit.color)
        autoComTvCondition.setText(productToEdit.condition)
        autoComTvTransmission.setText(productToEdit.transmission)
        autoComTvMileage.setText(productToEdit.mileage.toString())
        autoComTvFuel.setText(productToEdit.fuel)
        autoComTvPlate.setText(productToEdit.plate)
        autoComTvEngineSize.setText(productToEdit.engineSize.toString())

        // Common for all category
        setCommonCategoryValues(productToEdit)
    }

    private fun setHouseCategoryValues(productToEdit: ProductsModelParcelable) {
        autoComTvHouseType.setText(productToEdit.houseType)
        autoComTvHouseSize.setText(productToEdit.size.toString())
        autoComTvHouseLocation.setText(productToEdit.location)

        // Common for all category
        setCommonCategoryValues(productToEdit)
    }

    private fun setLandCategoryValues(productToEdit: ProductsModelParcelable) {
        autoComTvLandLocation.setText(productToEdit.location)
        autoComTvLandSize.setText(productToEdit.size.toString())

        // Common for all category
        setCommonCategoryValues(productToEdit)
    }

    private fun setOtherCategoryValues(productToEdit: ProductsModelParcelable) {
        autoComTvOtherTitle.setText(productToEdit.title)

        // Common for all category
        setCommonCategoryValues(productToEdit)
    }

    @SuppressLint("SetTextI18n")
    private fun setCommonCategoryValues(productToEdit: ProductsModelParcelable) {

        autoComTvCommonOwnerOr.setText(productToEdit.seller)
        autoComTvCommonSaleOr.setText(productToEdit.purpose)
        etCommonDesc.setText(productToEdit.description)
        autoComTvCommonPhone.setText(productToEdit.phone.toString())
        etCommonPrice.setText("Birr ${NumberFormat.getNumberInstance(Locale.US).format(productToEdit.price)}")

    }

    private fun setupControlViews() {
        btnUpdatePost = findViewById(R.id.btnUpdatePost)

        autoComTvCategories = findViewById(R.id.autoComTvCategories)
        autoComTvMake = findViewById(R.id.autoComTvMake)
        autoComTvCondition = findViewById(R.id.autoComTvCondition)
        autoComTvTransmission = findViewById(R.id.autoComTvTransmission)
        autoComTvFuel = findViewById(R.id.autoComTvFuel)
        autoComTvCommonOwnerOr = findViewById(R.id.autoComTvCommonOwnerOr)
        autoComTvCommonSaleOr = findViewById(R.id.autoComTvCommonSaleOr)
        autoComTvHouseType = findViewById(R.id.autoComTvHouseType)
        autoComTvHouseSize = findViewById(R.id.autoComTvHouseSize)
        autoComTvHouseLocation = findViewById(R.id.autoComTvHouseLocation)
        autoComTvCommonPhone = findViewById(R.id.autoComTvCommonPhone)
        autoComTvLandLocation = findViewById(R.id.autoComTvLandLocation)
        autoComTvLandSize = findViewById(R.id.autoComTvLandSize)
        autoComTvOtherTitle = findViewById(R.id.autoComTvOtherTitle)
        autoComTvModel = findViewById(R.id.autoComTvModel)
        autoComTvYear = findViewById(R.id.autoComTvYear)
        autoComTvColor = findViewById(R.id.autoComTvColor)
        autoComTvMileage = findViewById(R.id.autoComTvMileage)
        autoComTvPlate = findViewById(R.id.autoComTvPlate)
        autoComTvEngineSize = findViewById(R.id.autoComTvEngineSize)

        textInputLayoutCommonDesc = findViewById(R.id.textInputLayoutCommonDesc)
        etCommonPrice = findViewById(R.id.etCommonPrice)
        etCommonDesc = findViewById(R.id.etCommonDesc)

        cvCar = findViewById(R.id.cvCar)
        cvHouse = findViewById(R.id.cvHouse)
        cvLand = findViewById(R.id.cvLand)
        cvOther = findViewById(R.id.cvOther)
        cvCommon = findViewById(R.id.cvCommon)

        ivPhoto1 = findViewById(R.id.ivPhoto1)
        ivPhoto2 = findViewById(R.id.ivPhoto2)
    }

    private fun setupControlDropDownLists() {
        val ownerOrSeller = resources.getStringArray(R.array.seller)
        val arrayAdapterOwnerOrSeller = ArrayAdapter(this, R.layout.drop_down_category, ownerOrSeller)
        autoComTvCommonOwnerOr.setAdapter(arrayAdapterOwnerOrSeller)

        val fuel = resources.getStringArray(R.array.fuel)
        val arrayAdapterFuel = ArrayAdapter(this, R.layout.drop_down_category, fuel)
        autoComTvFuel.setAdapter(arrayAdapterFuel)

        val transmission = resources.getStringArray(R.array.transmission)
        val arrayAdapterTransmission = ArrayAdapter(this, R.layout.drop_down_category, transmission)
        autoComTvTransmission.setAdapter(arrayAdapterTransmission)

        val condition = resources.getStringArray(R.array.car_condition)
        val arrayAdapterCondition = ArrayAdapter(this, R.layout.drop_down_category, condition)
        autoComTvCondition.setAdapter(arrayAdapterCondition)

        val saleRentExchange = resources.getStringArray(R.array.sale_rent_exchange)
        val arrayAdapterSaleRentExchange = ArrayAdapter(this, R.layout.drop_down_category, saleRentExchange)
        autoComTvCommonSaleOr.setAdapter(arrayAdapterSaleRentExchange)

        val make = resources.getStringArray(R.array.make)
        val arrayAdapterMake = ArrayAdapter(this, R.layout.drop_down_category, make)
        autoComTvMake.setAdapter(arrayAdapterMake)
    }

    private fun changeCategoryVisibility(category: String) {

        when(category) {
            "Cars" -> {
                cvCar.isVisible = true
                cvHouse.isVisible = false
                cvLand.isVisible = false
                cvOther.isVisible = false
                cvCommon.isVisible = true
            }
            "House" -> {
                cvCar.isVisible = false
                cvHouse.isVisible = true
                cvLand.isVisible = false
                cvOther.isVisible = false
                cvCommon.isVisible = true
            }
            "Land" -> {
                cvCar.isVisible = false
                cvHouse.isVisible = false
                cvLand.isVisible = true
                cvOther.isVisible = false
                cvCommon.isVisible = true
            }
            "Other" -> {
                cvCar.isVisible = false
                cvHouse.isVisible = false
                cvLand.isVisible = false
                cvOther.isVisible = true
                cvCommon.isVisible = true
            }
        }

    }

    //region OverRide functions
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (!requestPermissions.hasWriteExternalStoragePermission() || !requestPermissions.hasReadExternalStoragePermission()) {
                showPermissionDeniedDialog()
            }
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            if (!requestPermissions.hasReadExternalStoragePermission()) {
                showPermissionDeniedDialog()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        checkInternetConnectionStatus()
    }

    override fun onBackPressed() {

        if (progressDialog.isShowing){
            return
        } else {
            deleteImagesFile()
            finish()
        }

    }
    //endregion

}