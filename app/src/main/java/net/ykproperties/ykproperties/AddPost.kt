package net.ykproperties.ykproperties

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.math.roundToInt

private const val PICK_PHOTO_CODE = 6410
private const val TAG = "samuel"

class AddPost : AppCompatActivity() {

//    private val easyPermissionManager = EasyPermissionManager(this)

    private var photoUriOne: Uri? = null

    lateinit var toolbar: Toolbar
    lateinit var autoComTvCategories: AutoCompleteTextView
    lateinit var autoComTvMake: AutoCompleteTextView
    lateinit var autoComTvCondition: AutoCompleteTextView
    lateinit var autoComTvTransmission: AutoCompleteTextView
    lateinit var autoComTvFuel: AutoCompleteTextView
    lateinit var autoComTvCommonOwnerOr: AutoCompleteTextView
    lateinit var autoComTvCommonSaleOr: AutoCompleteTextView
    lateinit var cvCar: CardView
    lateinit var cvHouse: CardView
    lateinit var cvLand: CardView
    lateinit var cvOther: CardView
    lateinit var cvCommon: CardView

    lateinit var photosSelectedList: List<InternalStoragePhoto>
    lateinit var photosSelected: MutableList<InternalStoragePhoto>
    lateinit var selectedImageOne: InternalStoragePhoto
    lateinit var selectedImageTwo: InternalStoragePhoto

    lateinit var imageOneBmp: Bitmap
    lateinit var imageTwoBmp: Bitmap
    lateinit var imageFileOne: File
    lateinit var imageFileTwo: File
    lateinit var imageFileOneToUpload: File
    lateinit var imageFileTwoToUpload: File
    lateinit var imageOneBmpToUpload: Bitmap
    lateinit var imageTwoBmpToUpload: Bitmap
    lateinit var imageOneUriToUpload: Uri
    lateinit var imageTwoUriToUpload: Uri

    lateinit var uuidImageOneName: String
    lateinit var uuidImageTwoName: String

    private val ivPhoto1: ImageView by lazy {
        findViewById(R.id.ivPhoto1)
    }
    private val ivPhoto2: ImageView by lazy {
        findViewById(R.id.ivPhoto2)
    }

    private val selectPictureLauncherOne = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        if (uri != null) {
            contentResolver.openInputStream(uri).let {
                imageOneBmp = BitmapFactory.decodeStream(it)
                imageFileOne = File(uri.path)
                uuidImageOneName = UUID.randomUUID().toString()
            }
//            openFileInput(imageFileOne.name).use {
//                imageOneBmp = BitmapFactory.decodeStream(it)
//            }
            val isSavedSuccessfully = savePhotoToInternalStorage(uuidImageOneName, imageOneBmp, imageFileOne.absolutePath)
            if (isSavedSuccessfully) {

                val files = filesDir.listFiles()

                files?.filter { it.canRead() && it.isFile && it.name.endsWith(".jpg") }
                if (files.isNotEmpty()) {
                    for (file in files) {
                        if (file.name == "$uuidImageOneName.jpg") {
                            imageOneUriToUpload = file.toUri()
                            imageFileOneToUpload = file
                            val bytes = file.readBytes()
                            imageOneBmpToUpload = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                            selectedImageOne = InternalStoragePhoto(uuidImageOneName, imageOneBmpToUpload, imageOneUriToUpload, imageFileOneToUpload.absolutePath)

                            ivPhoto1.setImageURI(imageOneUriToUpload)
                            Toast.makeText(this,"$imageOneUriToUpload $",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
    private val selectPictureLauncherTwo = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        if (uri != null) {
            contentResolver.openInputStream(uri).let {
                imageTwoBmp = BitmapFactory.decodeStream(it)
                imageFileTwo = File(uri.path)
                uuidImageTwoName = UUID.randomUUID().toString()
            }
//            openFileInput(imageFileOne.name).use {
//                imageOneBmp = BitmapFactory.decodeStream(it)
//            }
            val isSavedSuccessfully = savePhotoToInternalStorage(uuidImageTwoName, imageTwoBmp, imageFileTwo.absolutePath)
            if (isSavedSuccessfully) {

                val files = filesDir.listFiles()

                files?.filter { it.canRead() && it.isFile && it.name.endsWith(".jpg") }
                if (files.isNotEmpty()) {
                    for (file in files) {
                        if (file.name == "$uuidImageTwoName.jpg") {
                            imageTwoUriToUpload = file.toUri()
                            imageFileTwoToUpload = file
                            val bytes = file.readBytes()
                            imageTwoBmpToUpload = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                            selectedImageTwo = InternalStoragePhoto(uuidImageTwoName, imageTwoBmpToUpload, imageTwoUriToUpload, imageFileTwoToUpload.absolutePath)

                            ivPhoto2.setImageURI(imageTwoUriToUpload)
                            Toast.makeText(this,"$imageTwoUriToUpload $",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private var tempImageUri: Uri? = null

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            ivPhoto1.setImageURI(tempImageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        autoComTvCategories = findViewById(R.id.autoComTvCategories)
        autoComTvMake = findViewById(R.id.autoComTvMake)
        autoComTvCondition = findViewById(R.id.autoComTvCondition)
        autoComTvTransmission = findViewById(R.id.autoComTvTransmission)
        autoComTvFuel = findViewById(R.id.autoComTvFuel)
        autoComTvCommonOwnerOr = findViewById(R.id.autoComTvCommonOwnerOr)
        autoComTvCommonSaleOr = findViewById(R.id.autoComTvCommonSaleOr)

        cvCar = findViewById(R.id.cvCar)
        cvHouse = findViewById(R.id.cvHouse)
        cvLand = findViewById(R.id.cvLand)
        cvOther = findViewById(R.id.cvOther)

        cvCommon = findViewById(R.id.cvCommon)

        cvCar.isVisible = false
        cvHouse.isVisible = false
        cvLand.isVisible = false
        cvOther.isVisible = false

        cvCommon.isVisible = false

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
            deletePhotosFromInternalStorage()
            finish();
        }

        deletePhotosFromInternalStorage()

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

        autoComTvCategories.setOnItemClickListener { adapterView, view, i, l ->
            val value = adapterView.getItemAtPosition(i)
//            Toast.makeText(this, value.toString(), Toast.LENGTH_LONG).show()
            changeCategoryVisibility(value.toString())
        }

        ivPhoto1.setOnClickListener {
            selectPictureLauncherOne.launch("image/*")
//            val imagePickerIntent = Intent(Intent.ACTION_GET_CONTENT)
//            imagePickerIntent.type = "image/*"
//            if (imagePickerIntent.resolveActivity(packageManager) != null){
//                startActivityForResult(imagePickerIntent, PICK_PHOTO_CODE)
//            }
        }

        ivPhoto2.setOnClickListener {
            selectPictureLauncherTwo.launch("image/*")
        }
    }

    private fun deletePhotosFromInternalStorage() {
        lifecycleScope.launch {
            val photos = loadPhotosFromInternalStorage()
            if (photos.isNotEmpty()) {
                for (photo in photos) {
                    deleteFile(photo.name)
                    Toast.makeText(this@AddPost,"finished",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun loadPhotosFromInternalStorage(): List<InternalStoragePhoto> {
        return withContext(Dispatchers.IO) {
            val files = filesDir.listFiles()
            files?.filter { it.canRead() && it.isFile && it.name.endsWith(".jpg") }?.map {
                val bytes = it.readBytes()
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                InternalStoragePhoto(it.name, bmp, it.toUri(), it.absolutePath)
            } ?: listOf()
        }
    }

    private fun savePhotoToInternalStorage(filename: String, bmp: Bitmap, filePath: String): Boolean {
        return try {
            openFileOutput("$filename.jpg", MODE_PRIVATE).use { stream ->
                val resizedBmp = bmp.resize(620)

                val targetSize: Double = 0.2
                val file = File(filePath)
                val length = file.length()

                val fileSizeInKB = (length / 1024).toString().toDouble()
                val fileSizeInMB = (fileSizeInKB / 1024).toString().toDouble()

                var quality = 100
                if (fileSizeInMB > targetSize) {
                    quality = ((targetSize / fileSizeInMB) * 100).toInt()
                }

                if (!resizedBmp.compress(Bitmap.CompressFormat.JPEG, quality, stream)) {
                    throw IOException("Couldn't save bitmap.")
                }
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    private fun createImageFile(fileName: String = "temp_image"): File {
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
    }

    private fun changeCategoryVisibility(value: String) {
        when (value) {
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

}

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
}