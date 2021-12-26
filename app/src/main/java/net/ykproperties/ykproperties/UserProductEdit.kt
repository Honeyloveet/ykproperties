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
import kotlinx.coroutines.tasks.await
import net.ykproperties.ykproperties.model.ProductsModel
import net.ykproperties.ykproperties.model.ProductsModelParcelable
import net.ykproperties.ykproperties.util.ConnectionLiveData
import net.ykproperties.ykproperties.util.RequestPermissions
import net.ykproperties.ykproperties.util.VehicleInfo
import java.io.File
import java.io.IOException
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class UserProductEdit : AppCompatActivity() {

    //region declarations
    private companion object {
        private const val TAG = "PRODUCT_EDIT"
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

    private lateinit var productToEdit: ProductsModelParcelable

    private val makeYears = arrayListOf<String>()

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

        // add make year from 1900 to 2030 to array
        for (i in 1900..2030) {
            makeYears.add(i.toString())
        }

        setupControlViews()

        setupCustomProgressDialog()

        checkPermissions()

//        checkInternetConnectionStatus()

        deleteImagesFile()

        productToEdit = intent.getParcelableExtra("productToEdit")!!

        changeCategoryVisibility(productToEdit.category!!)

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

        toolbar.setNavigationOnClickListener {
            deleteImagesFile()
            finish()
        }

        setupControlDropDownLists()

        if (productToEdit.category == "Cars") {
            if (autoComTvMake.text.isNotBlank()) {
                val make = autoComTvMake.text.toString().replace(Regex("\\s|[(.&,)-]|(</?.*?>)"),"") // Remove the unwanted charactors
                val model = arrayListOf<String>()
                for (vehicle in VehicleInfo.carMakeModel[make]!!) {
                    model.add(vehicle)
                }
                val arrayAdapterAutoComTvModel = ArrayAdapter(this, R.layout.drop_down_category, model)
                autoComTvModel.setAdapter(arrayAdapterAutoComTvModel)
            }
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

        etCommonPrice.addTextChangedListener(CurrencyInputWatcher(etCommonPrice,"Birr ", Locale.getDefault()))

        ivPhoto1.setOnClickListener {
            selectPictureLauncherOne.launch("image/*")
        }

        ivPhoto2.setOnClickListener {
            selectPictureLauncherTwo.launch("image/*")
        }

        btnUpdatePost.setOnClickListener {
            val isInPutCorrect = checkInputFields(productToEdit.category.toString())
            if (isInPutCorrect) {
                btnUpdatePost.isEnabled = false
                Toast.makeText(this, "Every thing is correct", Toast.LENGTH_SHORT).show()

                progressDialog.show()
                progressDialog.findViewById<TextView>(R.id.tvProgressStatus).setTextColor(getColor(R.color.white))
                progressDialog.window?.setBackgroundDrawableResource(R.color.progress_bar_background)

                lifecycleScope.launch(Dispatchers.IO) {
                    updateImagesAndProduct()
                }
            }
        }
    }

    private fun updateCarInfo(productToEdit: ProductsModelParcelable, imagesUrl: ArrayList<String> = arrayListOf("")) {

        val itemToUpdate = ProductsModel(
            productToEdit.uid.toString(),
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
            productToEdit.reported,
            productToEdit.reportedNumber,
            autoComTvCommonOwnerOr.text.toString(),
            0,
            autoComTvTransmission.text.toString(),
            productToEdit.userPosted.toString(),
            productToEdit.views,
            autoComTvYear.text.toString().toLong(),
            productToEdit.sold,
            productToEdit.postDate)
        db.collection("products").document(productToEdit.uid.toString()).set(itemToUpdate, SetOptions.merge())
            .addOnSuccessListener {
                btnUpdatePost.isEnabled = true
                progressDialog.dismiss()
                productUpdateSuccessOrFailedDialog(true)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document", e)
                btnUpdatePost.isEnabled = true
                progressDialog.dismiss()
                productUpdateSuccessOrFailedDialog(false)
            }
    }

    private fun updateHouseInfo(productToEdit: ProductsModelParcelable, imagesUrl: ArrayList<String> = arrayListOf("")) {
        val itemToUpdate = ProductsModel(
            productToEdit.uid.toString(),
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
            productToEdit.reported,
            productToEdit.reportedNumber,
            autoComTvCommonOwnerOr.text.toString(),
            autoComTvHouseSize.text.toString().toLong(),
            "",
            productToEdit.userPosted.toString(),
            productToEdit.views,
            0,
            productToEdit.sold,
            productToEdit.updateDate)
        db.collection("products").document(productToEdit.uid.toString()).set(itemToUpdate, SetOptions.merge())
            .addOnSuccessListener {
                btnUpdatePost.isEnabled = true
                progressDialog.dismiss()
                productUpdateSuccessOrFailedDialog(true)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document", e)
                btnUpdatePost.isEnabled = true
                progressDialog.dismiss()
                productUpdateSuccessOrFailedDialog(false)
            }
    }

    private fun updateLandInfo(productToEdit: ProductsModelParcelable, imagesUrl: ArrayList<String> = arrayListOf("")) {
        val itemToUpdate = ProductsModel(
            productToEdit.uid.toString(),
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
            productToEdit.reported,
            productToEdit.reportedNumber,
            autoComTvCommonOwnerOr.text.toString(),
            autoComTvLandSize.text.toString().toLong(),
            "",
            productToEdit.userPosted.toString(),
            productToEdit.views,
            0,
            productToEdit.sold,
            productToEdit.updateDate)
        db.collection("products").document(productToEdit.uid.toString()).set(itemToUpdate, SetOptions.merge())
            .addOnSuccessListener {
                btnUpdatePost.isEnabled = true
                progressDialog.dismiss()
                productUpdateSuccessOrFailedDialog(true)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document", e)
                btnUpdatePost.isEnabled = true
                progressDialog.dismiss()
                productUpdateSuccessOrFailedDialog(false)
            }
    }

    private fun updateOtherInfo(productToEdit: ProductsModelParcelable, imagesUrl: ArrayList<String> = arrayListOf("")) {
        val itemToUpdate = ProductsModel(
            productToEdit.uid.toString(),
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
            productToEdit.reported,
            productToEdit.reportedNumber,
            autoComTvCommonOwnerOr.text.toString(),
            0,
            "",
            productToEdit.userPosted.toString(),
            productToEdit.views,
            0,
            productToEdit.sold,
            productToEdit.updateDate)
        db.collection("products").document(productToEdit.uid.toString()).set(itemToUpdate, SetOptions.merge())
            .addOnSuccessListener {
                btnUpdatePost.isEnabled = true
                progressDialog.dismiss()
                productUpdateSuccessOrFailedDialog(true)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document", e)
                btnUpdatePost.isEnabled = true
                progressDialog.dismiss()
                productUpdateSuccessOrFailedDialog(false)
            }
    }

    private suspend fun updateProducts(imagesUrl: ArrayList<String> = arrayListOf("")) {
        if (pictureOneSelected && pictureTwoSelected) {
            if (productToEdit.pictures.size > 1) {
                val deleteSuccess = deleteImagesFromServer("All", productToEdit.pictures)
                if (deleteSuccess) {
                    when (productToEdit.category) {
                        "Other" -> {
                            updateOtherInfo(productToEdit, imagesUrl)
                        }
                        "Land" -> {
                            updateLandInfo(productToEdit, imagesUrl)
                        }
                        "House" -> {
                            updateLandInfo(productToEdit, imagesUrl)
                        }
                        else -> {
                            updateLandInfo(productToEdit, imagesUrl)
                        }
                    }
                }
            } else if (productToEdit.pictures.size == 1) {
                val deleteSuccess = deleteImagesFromServer("One", productToEdit.pictures)
                if (deleteSuccess) {
                    when (productToEdit.category) {
                        "Other" -> {
                            updateOtherInfo(productToEdit, imagesUrl)
                        }
                        "Land" -> {
                            updateLandInfo(productToEdit, imagesUrl)
                        }
                        "House" -> {
                            updateLandInfo(productToEdit, imagesUrl)
                        }
                        else -> {
                            updateLandInfo(productToEdit, imagesUrl)
                        }
                    }
                }
            }
        } else if (pictureOneSelected && productToEdit.pictures[0] != "") {
            val deleteSuccess = deleteImagesFromServer("One", productToEdit.pictures)
            if (deleteSuccess) {
                when (productToEdit.category) {
                    "Other" -> {
                        updateOtherInfo(productToEdit, imagesUrl)
                    }
                    "Land" -> {
                        updateLandInfo(productToEdit, imagesUrl)
                    }
                    "House" -> {
                        updateLandInfo(productToEdit, imagesUrl)
                    }
                    else -> {
                        updateLandInfo(productToEdit, imagesUrl)
                    }
                }
            }
        } else if (pictureTwoSelected && productToEdit.pictures.size > 1) {
            val deleteSuccess = deleteImagesFromServer("Two", productToEdit.pictures)
            if (deleteSuccess) {
                when (productToEdit.category) {
                    "Other" -> {
                        updateOtherInfo(productToEdit, imagesUrl)
                    }
                    "Land" -> {
                        updateLandInfo(productToEdit, imagesUrl)
                    }
                    "House" -> {
                        updateLandInfo(productToEdit, imagesUrl)
                    }
                    else -> {
                        updateLandInfo(productToEdit, imagesUrl)
                    }
                }
            }
        } else {
            when (productToEdit.category) {
                "Other" -> {
                    updateOtherInfo(productToEdit, productToEdit.pictures)
                }
                "Land" -> {
                    updateLandInfo(productToEdit, productToEdit.pictures)
                }
                "House" -> {
                    updateHouseInfo(productToEdit, productToEdit.pictures)
                }
                else -> {
                    updateCarInfo(productToEdit, productToEdit.pictures)
                }
            }
        }
    }

    private suspend fun deleteImagesFromServer(imagesToDelete: String, imagesUrl: ArrayList<String>): Boolean {
        var result = false
        when (imagesToDelete) {
            "All" -> {
                var imageDeletedCount = 0
                for (imageUrl in imagesUrl) {
                    if (imageUrl != "") {
                        val imageFileName = getImageFileName(imageUrl)
                        val isDeleteSuccess = deleteSingleImage(imageFileName)
                        if (isDeleteSuccess) {
                            imageDeletedCount++
                            Log.i(TAG, "$imageUrl image deleted successfully!")
                        } else {
                            Log.w(TAG, "Failed to deleted $imageUrl!")
                        }
                        if (imageDeletedCount == imagesUrl.size) {
                            result = true
                            Log.i(TAG, "$imageDeletedCount images deleted successfully!")
                        } else result = false
                    }
                }
            }
            "One" -> {
                if (imagesUrl[0] != "") {
                    val imageFileName = getImageFileName(imagesUrl[0])
                    val isDeleteSuccess = deleteSingleImage(imageFileName)
                    if (isDeleteSuccess) {
                        result = true
                        Log.i(TAG, "${imagesUrl[0]} image deleted successfully!")
                    } else {
                        result = false
                        Log.w(TAG, "Failed to deleted ${imagesUrl[0]}!")
                    }
                } else {
                    return true
                }
            }
            "Two" -> {
                if (imagesUrl.size > 1) {
                    val imageFileName = getImageFileName(imagesUrl[1])
                    val isDeleteSuccess = deleteSingleImage(imageFileName)
                    if (isDeleteSuccess) {
                        result = true
                        Log.i(TAG, "${imagesUrl[1]} image deleted successfully!")
                    } else {
                        result = false
                        Log.w(TAG, "Failed to deleted ${imagesUrl[1]}!")
                    }
                }
            }
        }
        return result
    }

    private suspend fun deleteSingleImage(imageFileName: String): Boolean {
        var result = false
        val photoRef = storageRef.child("images/$imageFileName")
        photoRef.delete()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result = true
                    Log.i(TAG, "$imageFileName deleted successfully!")
                } else {
                    result = false
                    Log.w(TAG, "Failed to deleted $imageFileName!", it.exception)
                }
            }.await()
        return result
    }

    @SuppressLint("SetTextI18n")
    private suspend fun updateImagesAndProduct() {
        if (pictureOneSelected || pictureTwoSelected) {
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
                                    val imageURLs = getImagesUrlsToUpdate(imagesUriString)
                                    lifecycleScope.launch(Dispatchers.IO) {
                                        updateProducts(imageURLs)
                                    }
                                }
                            }
                            .addOnFailureListener {
                                Log.e(TAG,"Failed to upload ${imagesUriToUpload.size - imageUploadCount} out of ${imagesUriToUpload.size}")
                            }
                    }
            }
        } else {
            updateProducts(productToEdit.pictures)
        }
    }

    private fun getImagesUrlsToUpdate(imagesUrls: ArrayList<String>): ArrayList<String> {
        var imageURLs = imagesUrls
        if (imageURLs.isNullOrEmpty()) {
            imageURLs = productToEdit.pictures
        } else if (imageURLs.size == 1) {
            if (productToEdit.pictures.size > 1 && pictureTwoSelected) {
                imageURLs.add(productToEdit.pictures[0])
                return imageURLs
            } else if (productToEdit.pictures.size > 1 && pictureOneSelected) {
                imageURLs.add(productToEdit.pictures[1])
                return imageURLs
            } else if (productToEdit.pictures.size == 1 && productToEdit.pictures[0] != "" && pictureTwoSelected){
                imageURLs.add(productToEdit.pictures[0])
                return imageURLs
            } else if (productToEdit.pictures.size == 1 && productToEdit.pictures[0] != "" && pictureOneSelected){
                imageURLs.add(productToEdit.pictures[0])
            }
        }
        return imageURLs
    }

    // gets product image file name in firestore from url
    private fun getImageFileName(imageURL: String): String {
        var imageFileName = ""
        val startIndex = imageURL.indexOfAny(arrayListOf("images%"),0)
        val endIndex = imageURL.indexOfAny(arrayListOf("jpg"),0)
        for (i in startIndex+9..endIndex+2) {
            imageFileName += imageURL[i]
        }
        return imageFileName
    }

    private fun checkInputFields(category: String): Boolean {
        when(category) {
            "House" -> {
                when {
                    autoComTvHouseType.text.isBlank() -> {
                        Toast.makeText(this, "Please input House type!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    autoComTvHouseSize.text.isBlank() -> {
                        Toast.makeText(this, "Please input House Size in Meter Square!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    autoComTvHouseLocation.text.isBlank() -> {
                        Toast.makeText(this, "Please input Location of the house!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    autoComTvCommonOwnerOr.text.isBlank() -> {
                        Toast.makeText(this, "Please select if you are Owner or Broker of the House!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    autoComTvCommonSaleOr.text.isBlank() -> {
                        Toast.makeText(this, "Please select if the House is for sale/exchange or rent!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    etCommonPrice.text!!.isBlank() -> {
                        Toast.makeText(this, "Please input the price of the House in Birr!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    etCommonDesc.text!!.isBlank() -> {
                        Toast.makeText(this, "Please input Description of the House!", Toast.LENGTH_SHORT).show()
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
            "Cars" -> {
                when {
                    autoComTvMake.text.isBlank() -> {
                        Toast.makeText(this, "Please select the Make/Brand of the Car!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    autoComTvModel.text.isBlank() -> {
                        Toast.makeText(this, "Please input Model of the Car!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    autoComTvYear.text.isBlank() -> {
                        Toast.makeText(this, "Please input year of manufactured of the Car!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    autoComTvColor.text.isBlank() -> {
                        Toast.makeText(this, "Please input Color of the Car!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    autoComTvCondition.text.isBlank() -> {
                        Toast.makeText(this, "Please select the Condition of the Car!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    autoComTvTransmission.text.isBlank() -> {
                        Toast.makeText(this, "Please select the Transmission type of the Car!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    autoComTvMileage.text.isBlank() -> {
                        autoComTvMileage.setText("0")

                    }
                    autoComTvFuel.text.isBlank() -> {
                        Toast.makeText(this, "Please select Fuel type of the Car!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    autoComTvPlate.text.isBlank() -> {
                        autoComTvPlate.setText("0")

                    }
                    autoComTvEngineSize.text.isBlank() -> {
                        autoComTvEngineSize.setText("0")

                    }
                    autoComTvCommonOwnerOr.text.isBlank() -> {
                        Toast.makeText(this, "Please select if you are Owner or Broker of the Car!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    autoComTvCommonSaleOr.text.isBlank() -> {
                        Toast.makeText(this, "Please select if the Car is for sale/exchange or rent!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    etCommonPrice.text!!.isBlank() -> {
                        Toast.makeText(this, "Please input the price of the Car in Birr!", Toast.LENGTH_SHORT).show()
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
            "Land" -> {
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

                when {
                    autoComTvOtherTitle.text.isBlank() -> {
                        Toast.makeText(this, "Please input the Item type!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    autoComTvCommonOwnerOr.text.isBlank() -> {
                        Toast.makeText(this, "Please select if you are Owner or Broker of the Item!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    autoComTvCommonSaleOr.text.isBlank() -> {
                        Toast.makeText(this, "Please select if the Item is for sale/exchange or rent!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    etCommonPrice.text!!.isBlank() -> {
                        Toast.makeText(this, "Please input the price of the Item in Birr!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    etCommonDesc.text!!.isBlank() -> {
                        Toast.makeText(this, "Please input Description of the Item!", Toast.LENGTH_SHORT).show()
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
        }
        return false
    }

    @SuppressLint("SetTextI18n")
    private fun productUpdateSuccessOrFailedDialog(uploadSuccess: Boolean) {
        when {
            uploadSuccess -> {
                val dialog = MaterialDialog(this)
                    .noAutoDismiss()
                    .cancelable(false)
                    .cornerRadius(14f)
                    .customView(R.layout.update_success_or_failed_dialog)
                dialog.findViewById<TextView>(R.id.tvUpdateStatusTitle).text = "Item Update Success."
                dialog.findViewById<TextView>(R.id.tvUpdateStatusTitle).setTextColor(getColor(R.color.orange_main))
                dialog.findViewById<TextView>(R.id.tvUpdateStatusQuestion).text = "Press ok to continue."
                dialog.findViewById<Button>(R.id.btnUpdateStatusYes).visibility = View.GONE
                dialog.findViewById<Button>(R.id.btnUpdateStatusNo).visibility = View.GONE
                dialog.findViewById<Button>(R.id.btnUpdateStatusOk).visibility = View.VISIBLE
                dialog.findViewById<Button>(R.id.btnUpdateStatusOk).setOnClickListener {
                    deleteImagesFile()
                    finish()
                    dialog.dismiss()
                }
                dialog.show()
            }
            else -> {
                val dialog = MaterialDialog(this)
                    .noAutoDismiss()
                    .cancelable(false)
                    .cornerRadius(14f)
                    .customView(R.layout.update_success_or_failed_dialog)
                dialog.findViewById<TextView>(R.id.tvUpdateStatusTitle).text = "Item Update Failed!!!"
                dialog.findViewById<TextView>(R.id.tvUpdateStatusTitle).setTextColor(getColor(R.color.red))
                dialog.findViewById<TextView>(R.id.tvUpdateStatusQuestion).text = "Do you want to try again?"
                dialog.findViewById<Button>(R.id.btnUpdateStatusYes).visibility = View.VISIBLE
                dialog.findViewById<Button>(R.id.btnUpdateStatusNo).visibility = View.VISIBLE
                dialog.findViewById<Button>(R.id.btnUpdateStatusOk).visibility = View.GONE
                dialog.findViewById<Button>(R.id.btnUpdateStatusYes).setOnClickListener {
                    dialog.dismiss()
                }
                dialog.findViewById<Button>(R.id.btnUpdateStatusNo).setOnClickListener {
                    deleteImagesFile()
                    finish()
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
    }

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

//            if (!isConnected) {
//                val snackBar = Snackbar.make(
//                    editPostLayout,
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
        deleteImagesFile()
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