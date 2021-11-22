package net.ykproperties.ykproperties

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import net.ykproperties.ykproperties.model.ProductsModelParcelable

class UserProductEdit : AppCompatActivity() {

    private companion object {
        private const val TAG = "ProductEdit"
    }

    private lateinit var toolbar: Toolbar

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_user_product_edit)

        setupControlViews()
        setupControlDropDownLists()

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

        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setProductImagesToViews(productToEdit: ProductsModelParcelable) {
        if (productToEdit.pictures.size > 1) {
            if (productToEdit.pictures[0] != "") {
                Glide.with(this).load(productToEdit.pictures[0]).into(ivPhoto1)
            }
            if (productToEdit.pictures[1] != "") {
                Glide.with(this).load(productToEdit.pictures[1]).into(ivPhoto2)
            }
        } else if (productToEdit.pictures.size == 1 && productToEdit.pictures[0] != "") {
            Glide.with(this).load(productToEdit.pictures[0]).into(ivPhoto1)
        }
    }

    private fun setProductValuesToViews(productToEdit: ProductsModelParcelable) {
        when(productToEdit.category) {
            "Cars" -> {
                setCarValues(productToEdit)
            }
            "House" -> {
                setHouseValues(productToEdit)
            }
            "Land" -> {
                setLandValues(productToEdit)
            }
            "Other" -> {
                setOtherValues(productToEdit)
            }
        }
        setProductImagesToViews(productToEdit)
    }

    private fun setCarValues(productToEdit: ProductsModelParcelable) {
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
        autoComTvCommonOwnerOr.setText(productToEdit.seller)
        autoComTvCommonSaleOr.setText(productToEdit.purpose)
        etCommonPrice.setText(productToEdit.price.toString())
        etCommonDesc.setText(productToEdit.description)
        autoComTvCommonPhone.setText(productToEdit.phone.toString())
    }

    private fun setHouseValues(productToEdit: ProductsModelParcelable) {
        autoComTvHouseType.setText(productToEdit.houseType)
        autoComTvHouseSize.setText(productToEdit.size.toString())
        autoComTvHouseLocation.setText(productToEdit.location)

        // Common for all category
        autoComTvCommonOwnerOr.setText(productToEdit.seller)
        autoComTvCommonSaleOr.setText(productToEdit.purpose)
        etCommonPrice.setText(productToEdit.price.toString())
        etCommonDesc.setText(productToEdit.description)
        autoComTvCommonPhone.setText(productToEdit.phone.toString())
    }

    private fun setLandValues(productToEdit: ProductsModelParcelable) {
        autoComTvLandLocation.setText(productToEdit.location)
        autoComTvLandSize.setText(productToEdit.size.toString())

        // Common for all category
        autoComTvCommonOwnerOr.setText(productToEdit.seller)
        autoComTvCommonSaleOr.setText(productToEdit.purpose)
        etCommonPrice.setText(productToEdit.price.toString())
        etCommonDesc.setText(productToEdit.description)
        autoComTvCommonPhone.setText(productToEdit.phone.toString())
    }

    private fun setOtherValues(productToEdit: ProductsModelParcelable) {
        autoComTvOtherTitle.setText(productToEdit.title)

        // Common for all category
        autoComTvCommonOwnerOr.setText(productToEdit.seller)
        autoComTvCommonSaleOr.setText(productToEdit.purpose)
        etCommonPrice.setText(productToEdit.price.toString())
        etCommonDesc.setText(productToEdit.description)
        autoComTvCommonPhone.setText(productToEdit.phone.toString())
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

}