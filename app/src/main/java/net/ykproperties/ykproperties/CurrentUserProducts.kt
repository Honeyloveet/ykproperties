package net.ykproperties.ykproperties

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import net.ykproperties.ykproperties.model.ProductsModel
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import net.ykproperties.ykproperties.model.ProductsModelParcelable
import net.ykproperties.ykproperties.util.ConnectionLiveData
import net.ykproperties.ykproperties.util.SwipeToDeleteCallback


class CurrentUserProducts : AppCompatActivity() {

    private companion object {
        private const val TAG = "SAMUEL"
        private const val ITEM_COUNT_LIMIT = 8L
    }

    private lateinit var networkConnectionStatus: ConnectionLiveData

    private lateinit var toolbar: Toolbar

    private lateinit var rvCurrentUserProducts: RecyclerView

//    private lateinit var progressDialog: AlertDialog

    private lateinit var currentUserProductsLayout: ConstraintLayout

    private lateinit var currentUserProductsProgressBar: ProgressBar

//    private lateinit var btnCurrentUserProductsRefresh: Button

    private val db = Firebase.firestore
    private var prodRef = db.collection("products")

    private val storageRef = Firebase.storage.reference

    private var lastResult: Timestamp? = null

    private var totalItem = 0
    private var lastVisibleItem = 0

    private var lastNode: String? = ""
    private var lastKey: String? = ""

    private var oldProductsListSize = 0
    private var newProductsSize = 0

    private var isLoading = false
    private var isMaxData = false

    private var currentUserName: String? = null
    private var currentUserUid: String? = null

    private lateinit var adapter: CurrentUserProductsAdapter
    private lateinit var productsArrayList: MutableList<ProductsModel>

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_current_user_products)

        networkConnectionStatus = ConnectionLiveData(applicationContext)
//        checkInternetConnectionStatus()

        currentUserProductsLayout = findViewById(R.id.currentUserProductsLayout)

        toolbar = findViewById(R.id.toolBarCurrentUserProducts)
        rvCurrentUserProducts = findViewById(R.id.rvCurrentUserProducts)

        currentUserProductsProgressBar = findViewById(R.id.currentUserProductsProgressBar)
//        btnCurrentUserProductsRefresh = findViewById(R.id.btnCurrentUserProductsRefresh)

//        setupCustomProgressDialog()

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Your Posts"
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        val bundle = intent.extras
        currentUserName = bundle!!.getString("currentUserName")
        currentUserUid = bundle.getString("currentUserUid")

        getLastKey()

        val layoutManager = LinearLayoutManager(this)
        rvCurrentUserProducts.layoutManager = layoutManager
        productsArrayList = mutableListOf()
        adapter = CurrentUserProductsAdapter(this, productsArrayList)
        rvCurrentUserProducts.adapter = adapter

//        getCurrentUserProducts()
        getProducts()

        adapter.setOnItemClickListener(object : CurrentUserProductsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
//                Toast.makeText(this@CurrentUserProducts,"Adapter Clicked at position: $position",Toast.LENGTH_SHORT).show()
            }

            override fun onEditClick(position: Int) {
                openProductEditActivity(position)
            }

            override fun onDeleteClick(position: Int) {
                lifecycleScope.launch(Dispatchers.IO) {
                    delete(productsArrayList[position], position)
                }
                Toast.makeText(this@CurrentUserProducts,"Delete Clicked at position: $position",Toast.LENGTH_SHORT).show()
            }
        })

        val swipeToDeleteCallback = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition

                if (ItemTouchHelper.LEFT == direction || ItemTouchHelper.RIGHT == direction) {
//                    adapter.delete(productsArrayList[position], position)
                    lifecycleScope.launch(Dispatchers.IO) {
                        delete(productsArrayList[position], position)
                    }
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(rvCurrentUserProducts)

/*        btnCurrentUserProductsRefresh.setOnClickListener {
            if (!isLoading) {
                btnCurrentUserProductsRefresh.isEnabled = false
                productsArrayList.clear()
                adapter.notifyDataSetChanged()
                isMaxData = false
                lastNode = ""
                lastResult = null
                isLoading = true
                getLastKey()
                getProducts()
            } else {
                btnCurrentUserProductsRefresh.isEnabled = true
            }
//            getCurrentUserProducts()
        }*/

        rvCurrentUserProducts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItem = layoutManager.itemCount
//                Log.i(TAG, "total adapter item = $totalItem")
                lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()

//                Log.i(TAG, "total adapter item = $totalItem item count = ${lastVisibleItem + ITEM_COUNT_LIMIT}")
                if (totalItem < lastVisibleItem + 3) {
                    if (!isLoading) {
                        isLoading = true
//                        getCurrentUserProducts()
                        getProducts()
                    }
                }
            }
        })

        toolbar.setNavigationOnClickListener {
            finish()
        }

    }

    private fun openProductEditActivity(position: Int) {
        val productToEdit = ProductsModelParcelable().apply {
            uid = productsArrayList[position].uid
            title = productsArrayList[position].title
            price = productsArrayList[position].price
            make = productsArrayList[position].make
            model = productsArrayList[position].model
            bathRooms = productsArrayList[position].bathRooms
            bedRooms = productsArrayList[position].bedRooms
            category = productsArrayList[position].category
            color = productsArrayList[position].color
            condition = productsArrayList[position].condition
            description = productsArrayList[position].description
            engineSize = productsArrayList[position].engineSize
            fuel = productsArrayList[position].fuel
            houseType = productsArrayList[position].houseType
            kitchenType = productsArrayList[position].kitchenType
            kitchens = productsArrayList[position].kitchens
            location = productsArrayList[position].location
            mileage = productsArrayList[position].mileage
            phone = productsArrayList[position].phone
            pictures = productsArrayList[position].pictures
            plate = productsArrayList[position].plate
            purpose = productsArrayList[position].purpose
            reported = productsArrayList[position].reported
            reportedNumber = productsArrayList[position].reportedNumber
            seller = productsArrayList[position].seller
            size = productsArrayList[position].size
            transmission = productsArrayList[position].transmission
            userPosted = productsArrayList[position].userPosted
            views = productsArrayList[position].views
            year = productsArrayList[position].year
            sold = productsArrayList[position].sold
            postDate = productsArrayList[position].postDate
            updateDate = productsArrayList[position].updateDate
        }

        val intent = Intent(this@CurrentUserProducts, UserProductEdit::class.java)
        intent.putExtra("productToEdit", productToEdit)

        startActivity(intent)
    }

    // Delete the product and its image if image is available for the product else delete product only from firestore
    private suspend fun delete(itemToDelete: ProductsModel, position: Int) {
        if (itemToDelete.pictures.size == 1 && itemToDelete.pictures[0] == "") {
            val jobDeleteOne = lifecycleScope.launch(Dispatchers.IO) {
                val isDeleteSuccess = deleteItem(itemToDelete, position)
                if (isDeleteSuccess) {
                    Log.d(TAG, "item ${itemToDelete.uid} is successfully Deleted!")
                } else {
                    Log.w(TAG, "Error Deleting item ${itemToDelete.uid}")
                }
            }
            jobDeleteOne.join()
        } else {
            var imageDeletedCount = 0
            for (image in itemToDelete.pictures) {
                val imageFileName = getImageFileName(image)
                val photoRef = storageRef.child("images/$imageFileName")
                photoRef.delete()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            imageDeletedCount++
                            if (imageDeletedCount == itemToDelete.pictures.size) {
                                Log.d(TAG, "$imageDeletedCount images successfully Deleted!")
                                lifecycleScope.launch(Dispatchers.IO) {
                                    val isDeleteSuccess = deleteItem(itemToDelete, position)
                                    if (isDeleteSuccess) {
                                        Log.d(TAG, "item ${itemToDelete.uid} with images is successfully Deleted!")
                                    } else {
                                        Log.w(TAG, "Error Deleting item ${itemToDelete.uid} with images")
                                    }
                                }
                            }
                        } else {
                            Log.w(TAG, "Error Deleting image of ${itemToDelete.uid} with images", it.exception)
                        }
                    }
            }
        }
    }

    // delete product only from firestore
    private suspend fun deleteItem(itemToDelete: ProductsModel, position: Int): Boolean {
        var result = false
        db.collection("products").document(itemToDelete.uid)
            .delete()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    productsArrayList.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    Log.d(TAG, "item ${itemToDelete.uid} is successfully Deleted!")
                    result = true
                } else if (!it.isSuccessful) {
                    Log.w(TAG, "Error Deleting item ${itemToDelete.uid}", it.exception)
                    result = false
                }
            }.await()
        return result
    }

    private fun getImageFileName(imageURL: String): String { // gets product image file name in firestore from url
        var imageFileName = ""
        val startIndex = imageURL.indexOfAny(arrayListOf("images%"),0)
        val endIndex = imageURL.indexOfAny(arrayListOf("jpg"),0)
        for (i in startIndex+9..endIndex+2) {
            imageFileName += imageURL[i]
        }
        return imageFileName
    }

    private fun checkInternetConnectionStatus() {
        networkConnectionStatus.observe(this, { isConnected ->

//            if (!isConnected) {
//                val snackBar = Snackbar.make(
//                    currentUserProductsLayout,
//                    "No Internet Connection!",
//                    Snackbar.LENGTH_INDEFINITE
//                )
//                snackBar.setAction("Retry") {
//                    snackBar.dismiss()
//                    checkInternetConnectionStatus()
//                }.show()
//            }

        })
    }

//    private fun setupCustomProgressDialog() {
//        val alertView = View.inflate(this@CurrentUserProducts, R.layout.custom_progress_bar,null)
//
//        val alertBuilder = AlertDialog.Builder(this@CurrentUserProducts)
//        alertBuilder.setView(alertView)
//        alertBuilder.setCancelable(false)
//        progressDialog = alertBuilder.create()
//        progressDialog.setCanceledOnTouchOutside(false)
//    }

    @SuppressLint("SetTextI18n")
    private fun getProducts() {
        if (!isMaxData) {
            currentUserProductsProgressBar.visibility = View.VISIBLE
//            progressDialog.show()
//            progressDialog.findViewById<TextView>(R.id.tvProgressStatus).setTextColor(getColor(R.color.white))
//            progressDialog.findViewById<TextView>(R.id.tvProgressStatus).text = "Loading..."
//            progressDialog.window?.setBackgroundDrawableResource(R.color.progress_bar_background)

//            val query: Query = if (lastResult == null) {
//                prodRef.orderBy("postDate", Query.Direction.DESCENDING)
//                    .whereEqualTo("userPosted",currentUserUid)
//                    .limit(ITEM_COUNT_LIMIT)

            val query: Query = if (lastResult == null) {
                prodRef.orderBy("postDate", Query.Direction.DESCENDING)
                    .limit(ITEM_COUNT_LIMIT)
            } else {
                prodRef.orderBy("postDate", Query.Direction.DESCENDING)
                    .startAfter(lastResult)
                    .limit(ITEM_COUNT_LIMIT)
            }

            query.get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    oldProductsListSize = productsArrayList.size
                    for (documentSnapshot in queryDocumentSnapshots) {
                        productsArrayList.add(documentSnapshot.toObject(ProductsModel::class.java))
                        lastNode = documentSnapshot.id
//                        Log.i(TAG, "Last Key = $last_key Last Node = $lastNode")
                    }

                    newProductsSize = productsArrayList.size

                    lastNode = productsArrayList[productsArrayList.size - 1].uid

                    if (queryDocumentSnapshots.size() > 0) {
                        lastResult = queryDocumentSnapshots.documents[queryDocumentSnapshots.size() - 1].get("postDate") as Timestamp?
//                        Log.i(TAG, "Last Result = $x")
                    }

                    if (lastNode == lastKey) {
                        lastNode = "end"
                        lastResult = null
                        isMaxData = true
                        isLoading = false
                    }
                    adapter.notifyItemRangeChanged(oldProductsListSize, newProductsSize)
//                    Toast.makeText(this, "Last Node = $lastNode", Toast.LENGTH_SHORT).show()
                    isLoading = false
//                    btnCurrentUserProductsRefresh.isEnabled = true
//                    progressDialog.dismiss()
                    currentUserProductsProgressBar.visibility = View.GONE
                }
                .addOnFailureListener { e ->
//                    progressDialog.dismiss()
                    currentUserProductsProgressBar.visibility = View.GONE
                    isLoading = false
//                    btnCurrentUserProductsRefresh.isEnabled = true
                    Log.i(TAG, "$e")
                }

        } else {
            isLoading = false
//            btnCurrentUserProductsRefresh.isEnabled = true
        }
    }

/*    @SuppressLint("SetTextI18n")
    private fun getCurrentUserProducts() {
        if (!isMaxData) {

            progressDialog.show()
            progressDialog.findViewById<TextView>(R.id.tvProgressStatus).setTextColor(getColor(R.color.white))
            progressDialog.findViewById<TextView>(R.id.tvProgressStatus).text = "Loading..."
            progressDialog.window?.setBackgroundDrawableResource(R.color.progress_bar_background)

            val query: Query = if (lastResult == null) {
                prodRef.orderBy("postDate", Query.Direction.DESCENDING)
                    .limit(ITEM_COUNT_LIMIT)
            } else {
                prodRef.orderBy("postDate", Query.Direction.DESCENDING)
                    .startAfter(lastResult)
                    .limit(ITEM_COUNT_LIMIT)
            }
            query.get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    val productsList = ArrayList<ProductsModel>()
                    for (documentSnapshot in queryDocumentSnapshots) {
                        productsList.add(documentSnapshot.toObject(ProductsModel::class.java))
                        lastNode = documentSnapshot.id
//                        Log.i(TAG, "Last Key = $last_key Last Node = $lastNode")
                    }
//                        lastNode = productList[productList.size - 1].uid

                    if (queryDocumentSnapshots.size() > 0) {
                        lastResult = queryDocumentSnapshots.documents[queryDocumentSnapshots.size() - 1].get("postDate") as Timestamp?
                        val x = queryDocumentSnapshots.documents[queryDocumentSnapshots.size() - 1].id
//                        Log.i(TAG, "Last Result = $x")
                    }

                    if (lastNode == last_key) {
                        lastNode = "end"
                        lastResult = null
                        isMaxData = true
                        isLoading = false
                    }
                    adapter.addAll(productsList)
//                    Toast.makeText(this, "Last Node = $lastNode", Toast.LENGTH_SHORT).show()
                    isLoading = false
                    progressDialog.dismiss()
                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Log.i(TAG, "$e")
                }
        }
    }*/



    private fun getLastKey(categoryType: String = "All") {
        currentUserProductsProgressBar.visibility = View.VISIBLE

        val query: Query

        when (categoryType) {
            "Cars" -> {
                query = prodRef.orderBy("postDate", Query.Direction.DESCENDING)
                    .whereEqualTo("category","Cars")
                    .limitToLast(1)
            }
            "House" -> {
                query = prodRef.orderBy("postDate", Query.Direction.DESCENDING)
                    .whereEqualTo("category","House")
                    .limitToLast(1)
            }
            "Land" -> {
                query = prodRef.orderBy("postDate", Query.Direction.DESCENDING)
                    .whereEqualTo("category","Land")
                    .limitToLast(1)
            }
            "Other" -> {
                query = prodRef.orderBy("postDate", Query.Direction.DESCENDING)
                    .whereEqualTo("category","Other")
                    .limitToLast(1)
            }
            else -> {
                query = prodRef.orderBy("postDate", Query.Direction.DESCENDING)
                    .limitToLast(1)
            }
        }
        query.get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                if (queryDocumentSnapshots.size() > 0) {
//                    lastResult = queryDocumentSnapshots.documents[queryDocumentSnapshots.size() - 1]
                    lastKey = queryDocumentSnapshots.documents[queryDocumentSnapshots.size() - 1].id
//                    Log.i(TAG, "Last Key = $last_key")
//                    Toast.makeText(this, "Last Key = $last_key", Toast.LENGTH_SHORT).show()

                }
            }
            .addOnFailureListener { e ->
                Log.i(TAG, "$e")
            }
    }


}