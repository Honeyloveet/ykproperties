package net.ykproperties.ykproperties

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.loadBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.ykproperties.ykproperties.model.ProductsModel
import net.ykproperties.ykproperties.util.ConnectionLiveData
import net.ykproperties.ykproperties.util.PriceInputWatcher
import net.ykproperties.ykproperties.util.RequestPermissions
import net.ykproperties.ykproperties.util.VehicleInfo
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.Exception
import kotlin.collections.ArrayList

//private const val PICK_PHOTO_CODE = 6410

class AddPost : AppCompatActivity() {

    private companion object {
        private const val TAG = "AddPostActivity"
    }

    private lateinit var requestPermissions: RequestPermissions

    //    private val easyPermissionManager = EasyPermissionManager(this)
    private lateinit var networkConnectionStatus: ConnectionLiveData

//    private var photoUriOne: Uri? = null

    private lateinit var auth: FirebaseAuth

    private val db = Firebase.firestore

    private lateinit var progressDialog: AlertDialog

    // Create a storage reference from our app
//    private lateinit var storage: FirebaseStorage
//    private lateinit var storageRef: StorageReference
    private val storageRef = Firebase.storage.reference

    private lateinit var addPostLayout: ConstraintLayout

    private lateinit var toolbar: Toolbar
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

    private lateinit var textInputLayoutModel: TextInputLayout

    private lateinit var cvCar: CardView
    private lateinit var cvHouse: CardView
    private lateinit var cvLand: CardView
    private lateinit var cvOther: CardView
    private lateinit var cvCommon: CardView

    private lateinit var tvAddPhoto: TextView

    private lateinit var etCommonPrice: TextInputEditText
    private lateinit var etCommonDesc: TextInputEditText

//    lateinit var photosSelectedList: List<InternalStoragePhoto>
//    lateinit var photosSelected: MutableList<InternalStoragePhoto>
//    lateinit var selectedImageOne: InternalStoragePhoto
//    lateinit var selectedImageTwo: InternalStoragePhoto

    private lateinit var imageOneBmp: Bitmap
    private lateinit var imageTwoBmp: Bitmap

    private var actualImageFileOne: File? = null
    private var actualImageFileTwo: File? = null
    private var compressedImageOne: File? = null
    private var compressedImageTwo: File? = null

    private var imageFileOneToUpload: File? = null
    private var imageFileTwoToUpload: File? = null

    private lateinit var imageOneUriToUpload: Uri
    private lateinit var imageTwoUriToUpload: Uri

    private var imagesUriToUpload = ArrayList<Uri>()
    private var imagesUriString = ArrayList<String>()

    private lateinit var uuidImageOneName: String
    private lateinit var uuidImageTwoName: String

    private lateinit var btnAddPost: Button

    private var pictureOneSelected: Boolean = false
    private var pictureTwoSelected: Boolean = false

    private val makeYears = arrayListOf<String>()

    private val ivPhoto1: ImageView by lazy {
        findViewById(R.id.ivPhoto1)
    }
    private val ivPhoto2: ImageView by lazy {
        findViewById(R.id.ivPhoto2)
    }

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
//                    imagesUriString.remove(imageOneUriToUpload.toString())
                }

                imageFileOneToUpload = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"$uuidImageOneName.jpg")
                Toast.makeText(this,"${imageFileOneToUpload!!.toUri()}",Toast.LENGTH_SHORT).show()
                imageOneUriToUpload = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"$uuidImageOneName.jpg").toUri()
                ivPhoto1.setImageBitmap(imageOneBmp)

                imagesUriToUpload.add(imageOneUriToUpload)
//                imagesUriString.add(imageOneUriToUpload.toString())
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
//                    imagesUriString.remove(imageTwoUriToUpload.toString())
                }

                imageFileTwoToUpload = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$uuidImageTwoName.jpg")
                imageTwoUriToUpload = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$uuidImageTwoName.jpg").toUri()
                ivPhoto2.setImageBitmap(imageTwoBmp)

                imagesUriToUpload.add(imageTwoUriToUpload)
//                imagesUriString.add(imageTwoUriToUpload.toString())
                pictureTwoSelected = true
            }
        }
    }
    @Suppress("unused")
    private var tempImageUri: Uri? = null

    @Suppress("unused")
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            ivPhoto1.setImageURI(tempImageUri)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_add_post)

        addPostLayout = findViewById(R.id.addPostLayout)

        networkConnectionStatus = ConnectionLiveData(applicationContext)

        requestPermissions = RequestPermissions(this, this)

        auth = Firebase.auth

        // add make year from 1900 to 2030 to array
        for (i in 1900..2030) {
            makeYears.add(i.toString())
        }

        setupControlViews()

        toolbar = findViewById(R.id.toolBarDetails)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Add New Post"
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        toolbar.setNavigationOnClickListener {
            deleteImagesFile()
            finish()
        }

        setupCustomProgressDialog()

        checkPermissions()

//        checkInternetConnectionStatus()

        deleteImagesFile()

        setupControlDropDownLists()

        autoComTvCategories.setOnItemClickListener { adapterView, _, i, _ ->
            val value = adapterView.getItemAtPosition(i)
//            Toast.makeText(this, value.toString(), Toast.LENGTH_LONG).show()
            changeCategoryVisibility(value.toString())
            autoComTvCategories.setText(value.toString())
            setupControlDropDownLists()
        }

        autoComTvMake.setOnItemClickListener { parent, _, position, _ ->
            val a = parent.adapter
            val make = a.getItem(position).toString().replace(Regex("\\s|[(.&,)-]|(</?.*?>)"),"") // Remove the unwanted charactors
            val model = arrayListOf<String>()
            for (vehicle in VehicleInfo.carMakeModel[make]!!) {
                model.add(vehicle)
            }
            val arrayAdapterAutoComTvModel = ArrayAdapter(this, R.layout.drop_down_category, model)
            autoComTvModel.setAdapter(arrayAdapterAutoComTvModel)
//            Toast.makeText(this, make, Toast.LENGTH_SHORT).show()

        }

        autoComTvModel.setOnClickListener {
            if (autoComTvMake.text.isBlank()) {
                autoComTvMake.requestFocus()
                Toast.makeText(this, "Select Make First", Toast.LENGTH_SHORT).show()
            }
        }

//        textInputLayoutModel.setEndIconOnClickListener {
//            if (autoComTvMake.text.isBlank()) {
//                autoComTvMake.requestFocus()
//                Toast.makeText(this, "Select Make First", Toast.LENGTH_SHORT).show()
//            }
//
//        }

//        textInputLayoutModel.setOnClickListener {
//            if (autoComTvMake.text.isBlank()) {
//                autoComTvMake.requestFocus()
//                Toast.makeText(this, "Select Make First", Toast.LENGTH_SHORT).show()
//            }
//        }

//        etCommonPrice.addTextChangedListener(CurrencyInputWatcher(etCommonPrice,"Birr ", Locale.getDefault()))
        etCommonPrice.addTextChangedListener(PriceInputWatcher(etCommonPrice,"Birr ", Locale.getDefault()))

        ivPhoto1.setOnClickListener {
            selectPictureLauncherOne.launch("image/*")
        }

        ivPhoto2.setOnClickListener {
            selectPictureLauncherTwo.launch("image/*")
        }

        btnAddPost.setOnClickListener {

            val isInPutCorrect = checkInputFields()
            if (isInPutCorrect) {
                btnAddPost.isEnabled = false
                Toast.makeText(this, "Every thing is correct", Toast.LENGTH_SHORT).show()
                if (pictureOneSelected || pictureTwoSelected) {
                    progressDialog.show()
                    progressDialog.findViewById<TextView>(R.id.tvProgressStatus).setTextColor(getColor(R.color.white))
                    progressDialog.window?.setBackgroundDrawableResource(R.color.progress_bar_background)
                    uploadImages()
                } else {
                    progressDialog.show()
                    progressDialog.findViewById<TextView>(R.id.tvProgressStatus).setTextColor(getColor(R.color.white))
                    progressDialog.findViewById<TextView>(R.id.tvProgressStatus).text = "Uploading Data..."
                    progressDialog.window?.setBackgroundDrawableResource(R.color.progress_bar_background)
                    uploadProducts()
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        checkInternetConnectionStatus()
    }

    private fun setupControlViews() {
        btnAddPost = findViewById(R.id.btnAddPost)

        tvAddPhoto = findViewById(R.id.tvAddPhoto)

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
        textInputLayoutModel = findViewById(R.id.textInputLayoutModel)

        etCommonPrice = findViewById(R.id.etCommonPrice)
        etCommonDesc = findViewById(R.id.etCommonDesc)

        cvCar = findViewById(R.id.cvCar)
        cvHouse = findViewById(R.id.cvHouse)
        cvLand = findViewById(R.id.cvLand)
        cvOther = findViewById(R.id.cvOther)

        cvCommon = findViewById(R.id.cvCommon)

        ivPhoto1.isVisible = false
        ivPhoto2.isVisible = false
        tvAddPhoto.isVisible = false

        cvCar.isVisible = false
        cvHouse.isVisible = false
        cvLand.isVisible = false
        cvOther.isVisible = false

        cvCommon.isVisible = false
    }

    private fun setupControlDropDownLists() {

        val arrayAdapterAutoComTvYear = ArrayAdapter(this, R.layout.drop_down_category, makeYears)
        autoComTvYear.setAdapter(arrayAdapterAutoComTvYear)

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

        val categories = resources.getStringArray(R.array.categories)
        val arrayAdapter = ArrayAdapter(this, R.layout.drop_down_category, categories)
        autoComTvCategories.setAdapter(arrayAdapter)
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

    private fun checkInternetConnectionStatus() {
        networkConnectionStatus.observe(this, { isConnected ->

//            if (!isConnected) {
//                val snackBar = Snackbar.make(
//                    addPostLayout,
//                    "No Internet Connection!",
//                    Snackbar.LENGTH_INDEFINITE
//                )
//                snackBar.setAction("Retry"){
//                    snackBar.dismiss()
//                    checkInternetConnectionStatus()
//                }.show()
//            }

        })
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
        val alertView = View.inflate(this@AddPost, R.layout.custom_progress_bar,null)

        val alertBuilder = AlertDialog.Builder(this@AddPost)
        alertBuilder.setView(alertView)
        alertBuilder.setCancelable(false)
        progressDialog = alertBuilder.create()
        progressDialog.setCanceledOnTouchOutside(false)
//        progressDialog.findViewById<TextView>(R.id.tvProgressStatus).setTextColor(getColor(R.color.white))
//        progressDialog.show()
//        progressDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

    }

    @SuppressLint("SetTextI18n")
    private fun productUploadSuccessOrFailedDialog(uploadSuccess: Boolean) {
        if (uploadSuccess) {
            val dialog = MaterialDialog(this)
                .noAutoDismiss()
                .cancelable(false)
                .cornerRadius(14f)
                .customView(R.layout.upload_success_or_failed_dialog)
            dialog.findViewById<TextView>(R.id.tvUploadStatusTitle).text = "Item Post Success."
            dialog.findViewById<TextView>(R.id.tvUploadStatusTitle).setTextColor(getColor(R.color.orange_main))
            dialog.findViewById<TextView>(R.id.tvUploadStatusQuestion).text = "Do you want to post another Item?"
            dialog.findViewById<Button>(R.id.btnUploadStatusYes).setOnClickListener {
                clearImageSelection()
                clearInputFields()
                changeCategoryVisibility()
                dialog.dismiss()
            }
            dialog.findViewById<Button>(R.id.btnUploadStatusNo).setOnClickListener {
                deleteImagesFile()
                finish()
                dialog.dismiss()
            }
            dialog.show()
        } else {
            val dialog = MaterialDialog(this)
                .noAutoDismiss()
                .cancelable(false)
                .cornerRadius(14f)
                .customView(R.layout.upload_success_or_failed_dialog)
            dialog.findViewById<TextView>(R.id.tvUploadStatusTitle).text = "Item Post Failed!!!"
            dialog.findViewById<TextView>(R.id.tvUploadStatusTitle).setTextColor(getColor(R.color.red))
            dialog.findViewById<TextView>(R.id.tvUploadStatusQuestion).text = "Do you want to try again?"
            dialog.findViewById<Button>(R.id.btnUploadStatusYes).setOnClickListener {
                clearImageSelection()
                clearInputFields()
                changeCategoryVisibility()
                dialog.dismiss()
            }
            dialog.findViewById<Button>(R.id.btnUploadStatusNo).setOnClickListener {
                deleteImagesFile()
                finish()
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun uploadImages() {
        var imageUploadCount = 0
        for (image in imagesUriToUpload) {
            val photoRef = storageRef.child("images/${System.currentTimeMillis()}-photo.jpg")
            photoRef.putFile(image)
                .addOnSuccessListener {
                    photoRef.downloadUrl
                        .addOnSuccessListener { url ->
                            imagesUriString.add(url.toString())
                            imageUploadCount++
                            if (imageUploadCount == imagesUriToUpload.size) {
                                progressDialog.findViewById<TextView>(R.id.tvProgressStatus).text = "Uploading Data..."
                                uploadProducts(imagesUriString)
                            }
                        }
                        .addOnFailureListener {
                            Log.e(TAG,"Failed to upload ${imagesUriToUpload.size - imageUploadCount} out of ${imagesUriToUpload.size}")
                        }
                }
        }
    }

    private fun uploadProducts(imagesUrl: ArrayList<String> = arrayListOf("")) {
        val selectedCategory = getSelectedCategory(autoComTvCategories.text.toString())
        val currentUser = auth.currentUser
        val uid = UUID.randomUUID().toString()

        when (selectedCategory) {
            "Other" -> {
                val itemToPost = ProductsModel(
                    uid,
                    autoComTvOtherTitle.text.toString(),
                    etCommonPrice.text.toString().filter { it.isDigit() }.toLong(),
                    "",
                    "",
                    0,
                    0,
                    "Other",
                    "",
                    "",
                    etCommonDesc.text.toString(),
                    0,
                    "",
                    "",
                    "",
                    0,
                    "",
                    0,
                    autoComTvCommonPhone.text.toString().toLong(),
                    imagesUrl,
                    "",
                    autoComTvCommonSaleOr.text.toString(),
                    false,
                    0,
                    autoComTvCommonOwnerOr.text.toString(),
                    0,
                    "",
                    currentUser!!.uid,
                    0,
                    0,
                false)
                db.collection("products").document(uid).set(itemToPost, SetOptions.merge())
                    .addOnSuccessListener {
                        btnAddPost.isEnabled = true
                        clearImageSelection()
                        progressDialog.dismiss()
                        productUploadSuccessOrFailedDialog(true)
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error writing document", e)
                        btnAddPost.isEnabled = true
                        progressDialog.dismiss()
                        productUploadSuccessOrFailedDialog(false)
                    }
            }
            "Land" -> {
                val itemToPost = ProductsModel(
                    uid,
                    "",
                    etCommonPrice.text.toString().filter { it.isDigit() }.toLong(),
                    "",
                    "",
                    0,
                    0,
                    "Land",
                    "",
                    "",
                    etCommonDesc.text.toString(),
                    0,
                    "",
                    "",
                    "",
                    0,
                    autoComTvLandLocation.text.toString(),
                    0,
                    autoComTvCommonPhone.text.toString().toLong(),
                    imagesUrl,
                    "",
                    autoComTvCommonSaleOr.text.toString(),
                    false,
                    0,
                    autoComTvCommonOwnerOr.text.toString(),
                    autoComTvLandSize.text.toString().toLong(),
                    "",
                    currentUser!!.uid,
                    0,
                    0,
                false)
                db.collection("products").document(uid).set(itemToPost, SetOptions.merge())
                    .addOnSuccessListener {
                        btnAddPost.isEnabled = true
                        clearImageSelection()
                        progressDialog.dismiss()
                        productUploadSuccessOrFailedDialog(true)
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error writing document", e)
                        btnAddPost.isEnabled = true
                        progressDialog.dismiss()
                        productUploadSuccessOrFailedDialog(false)
                    }
            }
            "House" -> {
                val itemToPost = ProductsModel(
                    uid,
                    "",
                    etCommonPrice.text.toString().filter { it.isDigit() }.toLong(),
                    "",
                    "",
                    0,
                    0,
                    "House",
                    "",
                    "",
                    etCommonDesc.text.toString(),
                    0,
                    "",
                    autoComTvHouseType.text.toString(),
                    "",
                    0,
                    autoComTvHouseLocation.text.toString(),
                    0,
                    autoComTvCommonPhone.text.toString().toLong(),
                    imagesUrl,
                    "",
                    autoComTvCommonSaleOr.text.toString(),
                    false,
                    0,
                    autoComTvCommonOwnerOr.text.toString(),
                    autoComTvHouseSize.text.toString().toLong(),
                    "",
                    currentUser!!.uid,
                    0,
                    0,
                false)
                db.collection("products").document(uid).set(itemToPost, SetOptions.merge())
                    .addOnSuccessListener {
                        btnAddPost.isEnabled = true
                        clearImageSelection()
                        progressDialog.dismiss()
                        productUploadSuccessOrFailedDialog(true)
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error writing document", e)
                        btnAddPost.isEnabled = true
                        progressDialog.dismiss()
                        productUploadSuccessOrFailedDialog(false)
                    }
            }
            "Cars" -> {
                val itemToPost = ProductsModel(
                    uid,
                    "",
                    etCommonPrice.text.toString().filter { it.isDigit() }.toLong(),
                    autoComTvMake.text.toString(),
                    autoComTvModel.text.toString(),
                    0,
                    0,
                    "Cars",
                    autoComTvColor.text.toString(),
                    autoComTvCondition.text.toString(),
                    etCommonDesc.text.toString(),
                    autoComTvEngineSize.text.toString().toLong(),
                    autoComTvFuel.text.toString(),
                    autoComTvHouseType.text.toString(),
                    "",
                    0,
                    "",
                    autoComTvMileage.text.toString().toLong(),
                    autoComTvCommonPhone.text.toString().toLong(),
                    imagesUrl,
                    autoComTvPlate.text.toString(),
                    autoComTvCommonSaleOr.text.toString(),
                    false,
                    0,
                    autoComTvCommonOwnerOr.text.toString(),
                    0,
                    autoComTvTransmission.text.toString(),
                    currentUser!!.uid,
                    0,
                    autoComTvYear.text.toString().toLong(),
                false)
                db.collection("products").document(uid).set(itemToPost, SetOptions.merge())
                    .addOnSuccessListener {
                        btnAddPost.isEnabled = true
                        clearImageSelection()
                        progressDialog.dismiss()
                        productUploadSuccessOrFailedDialog(true)
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error writing document", e)
                        btnAddPost.isEnabled = true
                        progressDialog.dismiss()
                        productUploadSuccessOrFailedDialog(false)
                    }
            }
        }
    }

    private fun getSelectedCategory(category: String): String {
        when(category) {
            "House" -> {
                return category
            }
            "Cars" -> {
                return category
            }
            "Land" -> {
                return category
            }
            "Other" -> {
                return category
            }
        }
        return ""
    }

    private fun checkInputFields(): Boolean {

        when(getSelectedCategory(autoComTvCategories.text.toString())) {
            "House" -> {
                if (!pictureOneSelected && !pictureTwoSelected) {
                    Toast.makeText(this, "Please Select at list one image!!", Toast.LENGTH_SHORT).show()
                    return false
                } else if (autoComTvHouseType.text.isBlank()) {
                    Toast.makeText(this, "Please input House type!", Toast.LENGTH_SHORT).show()
                    return false
                } else if (autoComTvHouseSize.text.isBlank()) {
                    Toast.makeText(this, "Please input House Size in Meter Square!", Toast.LENGTH_SHORT).show()
                    return false
                } else if (autoComTvHouseLocation.text.isBlank()) {
                    Toast.makeText(this, "Please input Location of the house!", Toast.LENGTH_SHORT).show()
                    return false
                } else if (autoComTvCommonOwnerOr.text.isBlank()) {
                    Toast.makeText(this, "Please select if you are Owner or Broker of the House!", Toast.LENGTH_SHORT).show()
                    return false
                } else if (autoComTvCommonSaleOr.text.isBlank()) {
                    Toast.makeText(this, "Please select if the House is for sale/exchange or rent!", Toast.LENGTH_SHORT).show()
                    return false
                } else if (etCommonPrice.text!!.isBlank()) {
                    Toast.makeText(this, "Please input the price of the House in Birr!", Toast.LENGTH_SHORT).show()
                    return false
                } else if (etCommonDesc.text!!.isBlank()) {
                    Toast.makeText(this, "Please input Description of the House!", Toast.LENGTH_SHORT).show()
                    return false
                } else if (autoComTvCommonPhone.text.isBlank()) {
                    Toast.makeText(this, "Please input Your Phone Number for buyers to contact you!", Toast.LENGTH_SHORT).show()
                    return false
                } else {
                    return true
                }
            }
            "Cars" -> {
                if (!pictureOneSelected && !pictureTwoSelected) {
                    Toast.makeText(this, "Please Select at list one image!!", Toast.LENGTH_SHORT).show()
                    return false
                } else if (autoComTvMake.text.isBlank()) {
                    Toast.makeText(this, "Please select the Make/Brand of the Car!", Toast.LENGTH_SHORT).show()
                    return false
                } else if (autoComTvModel.text.isBlank()) {
                    Toast.makeText(this, "Please input Model of the Car!", Toast.LENGTH_SHORT).show()
                    return false
                } else if (autoComTvYear.text.isBlank()) {
                    Toast.makeText(this, "Please input year of manufactured of the Car!", Toast.LENGTH_SHORT).show()
                    return false
                } else if (autoComTvColor.text.isBlank()) {
                    Toast.makeText(this, "Please input Color of the Car!", Toast.LENGTH_SHORT).show()
                    return false
                } else if (autoComTvCondition.text.isBlank()) {
                    Toast.makeText(this, "Please select the Condition of the Car!", Toast.LENGTH_SHORT).show()
                    return false
                } else if (autoComTvTransmission.text.isBlank()) {
                    Toast.makeText(this, "Please select the Transmission type of the Car!", Toast.LENGTH_SHORT).show()
                    return false
                } else if (autoComTvMileage.text.isBlank()) {
                    autoComTvMileage.setText("0")

                } else if (autoComTvFuel.text.isBlank()) {
                    Toast.makeText(this, "Please select Fuel type of the Car!", Toast.LENGTH_SHORT).show()
                    return false
                } else if (autoComTvPlate.text.isBlank()) {
                    autoComTvPlate.setText("0")

                } else if (autoComTvEngineSize.text.isBlank()) {
                    autoComTvEngineSize.setText("0")

                } else if (autoComTvCommonOwnerOr.text.isBlank()) {
                    Toast.makeText(this, "Please select if you are Owner or Broker of the Car!", Toast.LENGTH_SHORT).show()
                    return false
                } else if (autoComTvCommonSaleOr.text.isBlank()) {
                    Toast.makeText(this, "Please select if the Car is for sale/exchange or rent!", Toast.LENGTH_SHORT).show()
                    return false
                } else if (etCommonPrice.text!!.isBlank()) {
                    Toast.makeText(this, "Please input the price of the Car in Birr!", Toast.LENGTH_SHORT).show()
                    return false
                } else if (autoComTvCommonPhone.text.isBlank()) {
                    Toast.makeText(this, "Please input Your Phone Number for buyers to contact you!", Toast.LENGTH_SHORT).show()
                    return false
                } else {
                    return true
                }
            }
            "Land" -> {
//                if (!pictureOneSelected && !pictureTwoSelected) {
//                    Toast.makeText(this, "Please Select at list one image!!", Toast.LENGTH_SHORT).show()
//                    return false
//                } else
                when {
                    autoComTvLandLocation.text.isBlank() -> {
                        Toast.makeText(this, "Please input the Location of the Land!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    autoComTvLandSize.text.isBlank() -> {
                        Toast.makeText(this, "Please input Land Size in Meter Square!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    autoComTvCommonOwnerOr.text.isBlank() -> {
                        Toast.makeText(this, "Please select if you are Owner or Broker of the Land!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    autoComTvCommonSaleOr.text.isBlank() -> {
                        Toast.makeText(this, "Please select if the Land is for sale/exchange or rent!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    etCommonPrice.text!!.isBlank() -> {
                        Toast.makeText(this, "Please input the price of the Land in Birr!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    etCommonDesc.text!!.isBlank() -> {
                        Toast.makeText(this, "Please input Description of the Land!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    autoComTvCommonPhone.text.isBlank() -> {
                        Toast.makeText(this, "Please input Your Phone Number for buyers to contact you!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    else -> {
                        return true
                    }
                }
            }
            "Other" -> {
                if (!pictureOneSelected && !pictureTwoSelected) {
                    Toast.makeText(this, "Please Select at list one image!!", Toast.LENGTH_SHORT).show()
                    return false
                } else if (autoComTvOtherTitle.text.isBlank()) {
                    Toast.makeText(this, "Please input the Item type!", Toast.LENGTH_SHORT).show()
                    return false
                } else if (autoComTvCommonOwnerOr.text.isBlank()) {
                    Toast.makeText(this, "Please select if you are Owner or Broker of the Item!", Toast.LENGTH_SHORT).show()
                    return false
                } else if (autoComTvCommonSaleOr.text.isBlank()) {
                    Toast.makeText(this, "Please select if the Item is for sale/exchange or rent!", Toast.LENGTH_SHORT).show()
                    return false
                } else if (etCommonPrice.text!!.isBlank()) {
                    Toast.makeText(this, "Please input the price of the Item in Birr!", Toast.LENGTH_SHORT).show()
                    return false
                } else if (etCommonDesc.text!!.isBlank()) {
                    Toast.makeText(this, "Please input Description of the Item!", Toast.LENGTH_SHORT).show()
                    return false
                } else if (autoComTvCommonPhone.text.isBlank()) {
                    Toast.makeText(this, "Please input Your Phone Number for buyers to contact you!", Toast.LENGTH_SHORT).show()
                    return false
                } else {
                    return true
                }
            }
        }
        return false
    }

    override fun onBackPressed() {

        if (progressDialog.isShowing){
            return
        } else {
            deleteImagesFile()
            finish()
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

    /**
     * Code for later viewing
     */
/*    private fun deletePhotosFromInternalStorage(name: String? = null) {
        lifecycleScope.launch {
            val photos = loadPhotosFromInternalStorage()
            if (!name.isNullOrEmpty()) {
                if (photos.isNotEmpty()) {
                    for (photo in photos) {
                        if (photo.name == "$name.jpg") {
                            deleteFile(photo.name)
                            Toast.makeText(this@AddPost,"Deleted ${photo.name}",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                return@launch
            } else if (photos.isNotEmpty()) {
                for (photo in photos) {
                    val isDeleteSuccess = deleteFile(photo.name)
                    try {
                        deleteFile(photo.name)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.d(TAG, "Failed to delete:" + e.printStackTrace())
                    }
                    if (isDeleteSuccess) {
                        Toast.makeText(this@AddPost,"Deleted All Files",Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@AddPost,"cannot delete ${photo.name}",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }*/

    /**
     * Code for later viewing
     */
/*    private suspend fun loadPhotosFromInternalStorage(): List<InternalStoragePhoto> {
        return withContext(Dispatchers.IO) {
            val files = getExternalFilesDirs(Environment.DIRECTORY_PICTURES)
//            val files = storageDir.listFiles()
            if (files.isEmpty()) {
                return@withContext emptyList()
            }
            val fileE = files[0].listFiles()
            fileE?.filter { it.name.endsWith(".jpg") || it.name.endsWith(".png")}?.map {
                val bytes = it.readBytes()
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

//                if (bmp == null){
//                    bmp = BitmapFactory.decodeResource(resources,R.drawable.ic_add_photo)
//                }

                InternalStoragePhoto(it.name, bmp, it.toUri(), it.absolutePath)
            } ?: listOf()
        }
    }*/

    @Suppress("DEPRECATION")
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
                            compressedImageOne = Compressor.compress(this@AddPost, imageFile) {
                                default(width = 620)
                                getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.also {
                                    val file = File("${it.absolutePath}${File.separator}${filename}.jpg")
                                    destination(file)
                                }
                            }
                        } else {
                            compressedImageOne = Compressor.compress(this@AddPost, imageFile) {
                                default(width = 620, format = Bitmap.CompressFormat.WEBP, quality = quality)
                                getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.also {
                                    val file = File("${it.absolutePath}${File.separator}${filename}.jpg")
                                    destination(file)
                                }
                            }
                        }
                    } else if (selectedImage == 2) {
                        if (quality == 100) {
                            compressedImageTwo = Compressor.compress(this@AddPost, imageFile) {
                                default(width = 620)
                                getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.also {
                                    val file = File("${it.absolutePath}${File.separator}${filename}.jpg")
                                    destination(file)
                                }
                            }
                        } else {
                            compressedImageTwo = Compressor.compress(this@AddPost, imageFile) {
                                default(width = 620, format = Bitmap.CompressFormat.WEBP, quality = quality)
                                getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.also {
                                    val file = File("${it.absolutePath}${File.separator}${filename}.jpg")
                                    destination(file)
                                }
                            }
                        }
                    }
//                    setCompressedImage()
                }
            }
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
/*
//        return try {
//            openFileOutput("$filename.jpg", MODE_PRIVATE).use { stream ->
////                val resizedBmp = bmp.resize(620)
//
//                val targetSize: Double = 200.0
//                val file = File(filePath)
//                val length = file.length()
//
//                val fileSizeInKB = (length / 1024).toString().toDouble()
//                val fileSizeInMB = (fileSizeInKB / 1024).toString().toDouble()
//
//                var quality = 100
//                if (fileSizeInKB > targetSize) {
//                    quality = ((targetSize / fileSizeInKB) * 100).toInt()
//                }
//
//                if (!bmp.compress(Bitmap.CompressFormat.JPEG, quality, stream)) {
//                    Toast.makeText(this,"Couldn't save bitmap",Toast.LENGTH_SHORT).show()
//                    throw IOException("Couldn't save bitmap.")
//                }
//            }
//            true
//        } catch (e: IOException) {
//            e.printStackTrace()
//            false
//        } */
    }

/*    private fun createImageFile(fileName: String = "temp_image"): File {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDir)
    }

    private fun uriToFile(context: Context, uri: Uri, fileName: String): File? {
        context.contentResolver.openInputStream(uri)?.let { inputStream ->
            val tempFile: File = createImageFile(fileName)
            val fileOutPutStream = FileOutputStream(tempFile)

            inputStream.copyTo(fileOutPutStream)
            inputStream.close()
            fileOutPutStream.close()

            return tempFile
        }
        return null
    }

    private fun compressImage(filePath: String, targetSize: Double = 0.2) {
        val image: Bitmap = BitmapFactory.decodeFile(filePath)

        try {

            val file = File(filePath)
            val length = file.length()

            val fileSizeInKB = (length / 1024).toString().toDouble()
            val fileSizeInMB = (fileSizeInKB / 1024).toString().toDouble()

            var quality = 100
            if (fileSizeInMB > targetSize) {
                quality = ((targetSize / fileSizeInMB) * 100).toInt()
            }

            val fileOutputStream = FileOutputStream(filePath)
            image.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream)
            fileOutputStream.close()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }*/

    private fun clearInputFields() {
        autoComTvCategories.setText("")
        autoComTvOtherTitle.setText("")
        autoComTvCommonOwnerOr.setText("")
        autoComTvCommonSaleOr.setText("")
        autoComTvCommonPhone.setText("")
        etCommonDesc.setText("")
        etCommonPrice.setText("")
        autoComTvLandLocation.setText("")
        autoComTvLandSize.setText("")
        autoComTvHouseLocation.setText("")
        autoComTvHouseSize.setText("")
        autoComTvHouseType.setText("")
        autoComTvMake.setText("")
        autoComTvModel.setText("")
        autoComTvYear.setText("")
        autoComTvColor.setText("")
        autoComTvCondition.setText("")
        autoComTvTransmission.setText("")
        autoComTvMileage.setText("")
        autoComTvFuel.setText("")
        autoComTvPlate.setText("")
        autoComTvEngineSize.setText("")
        btnAddPost.isEnabled = true
    }

    private fun clearImageSelection() {
        ivPhoto1.setImageResource(R.drawable.ic_add_photo)
        ivPhoto2.setImageResource(R.drawable.ic_add_photo)
        imagesUriString.clear()
        imagesUriToUpload.clear()
        pictureOneSelected = false
        pictureTwoSelected = false
    }

    private fun changeCategoryVisibility(value: String = "") {
        when (value) {
            "Cars" -> {
                textInputLayoutCommonDesc.hint = "Description"
                tvAddPhoto.isVisible = true
                ivPhoto1.isVisible = true
                ivPhoto2.isVisible = true
                cvCar.isVisible = true
                cvHouse.isVisible = false
                cvLand.isVisible = false
                cvOther.isVisible = false
                cvCommon.isVisible = true
                clearImageSelection()
                clearInputFields()
            }
            "House" -> {
                textInputLayoutCommonDesc.hint = "Description*"
                tvAddPhoto.isVisible = true
                ivPhoto1.isVisible = true
                ivPhoto2.isVisible = true
                cvCar.isVisible = false
                cvHouse.isVisible = true
                cvLand.isVisible = false
                cvOther.isVisible = false
                cvCommon.isVisible = true
                clearImageSelection()
                clearInputFields()
            }
            "Land" -> {
                textInputLayoutCommonDesc.hint = "Description*"
                tvAddPhoto.isVisible = true
                ivPhoto1.isVisible = true
                ivPhoto2.isVisible = true
                cvCar.isVisible = false
                cvHouse.isVisible = false
                cvLand.isVisible = true
                cvOther.isVisible = false
                cvCommon.isVisible = true
                clearImageSelection()
                clearInputFields()
            }
            "Other" -> {
                textInputLayoutCommonDesc.hint = "Description*"
                tvAddPhoto.isVisible = true
                ivPhoto1.isVisible = true
                ivPhoto2.isVisible = true
                cvCar.isVisible = false
                cvHouse.isVisible = false
                cvLand.isVisible = false
                cvOther.isVisible = true
                cvCommon.isVisible = true
                clearImageSelection()
                clearInputFields()
            }
            "" -> {
                textInputLayoutCommonDesc.hint = "Description*"
                tvAddPhoto.isVisible = false
                ivPhoto1.isVisible = false
                ivPhoto2.isVisible = false
                cvCar.isVisible = false
                cvHouse.isVisible = false
                cvLand.isVisible = false
                cvOther.isVisible = false
                cvCommon.isVisible = false
                clearImageSelection()
                clearInputFields()
            }
        }
    }

}

/*
// Extension function to resize bitmap using new width value by keeping aspect ratio
fun Bitmap.resize(width:Int):Bitmap{

    val ratio:Float = this.width.toFloat() / this.height.toFloat()
    val height:Int = (width / ratio).roundToInt()

    return Bitmap.createScaledBitmap(
        this,
        width,
        height,
        false
    )
}*/
