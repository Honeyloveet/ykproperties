package net.ykproperties.ykproperties

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import android.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import net.ykproperties.ykproperties.model.ProductsModel
import net.ykproperties.ykproperties.model.ProductsModelParcelable
import net.ykproperties.ykproperties.util.ConnectionLiveData
import net.ykproperties.ykproperties.util.NetworkVariables
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private companion object {
        private const val RC_GOOGLE_SIGN_IN = 2088
        private const val TAG = "SAMUEL"
        private const val ITEM_COUNT_LIMIT = 8L
    }

    private lateinit var networkConnectionStatus: ConnectionLiveData

    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var auth: FirebaseAuth

    private lateinit var linearLayoutProgress: LinearLayout

    private var mIsDestroyed = true

    private val db = Firebase.firestore

    private var totalItems = 0
    private var lastVisibleItem = 0

    private var currentUserName: String = ""
    private var currentUserUid: String = ""

    private lateinit var adapter: ProductAdapter

    private var isLoading = false
    private var isMaxData = false

    private var lastNode: String? = ""
    private var lastKey: String? = ""
    private var selectedCategory: String = "All"

    private var oldProductsListSize = 0
    private var newProductsSize = 0

    // Create a storage reference from our app
    private lateinit var storage: FirebaseStorage

//    var storageE = Firebase.storage

//    lateinit var preferences: SharedPreferences
//    lateinit var editor: SharedPreferences.Editor

    private lateinit var toolbar: Toolbar
    private lateinit var rvItems: RecyclerView
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var svSearchProducts: SearchView

    private lateinit var btnCatHouse: ImageButton
    private lateinit var btnCatLand: ImageButton
    private lateinit var btnCatAll: ImageButton
    private lateinit var btnCatOther: ImageButton
    private lateinit var btnCatCars: ImageButton

    private lateinit var navTvUserName: TextView

    private var selectedFilter = 1
    private var isLoggedIn: Boolean = false

    private lateinit var products: MutableList<ProductsModel>

    private var prodRef = db.collection("products")
//    private var prodQuery = prodRef.orderBy("postDate", Query.Direction.DESCENDING)

    //    private val products: ArrayList<ProductsModel> = ArrayList()
//    private val itemAdapter = ProductAdapter(this, this.products)

    private var lastResult: Timestamp? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)

        networkConnectionStatus = ConnectionLiveData(applicationContext)
//        checkInternetConnectionStatus()

        auth = Firebase.auth

        storage = Firebase.storage

//        var storageRef = storage.reference
        linearLayoutProgress = findViewById(R.id.linearLayoutProgress)

        btnCatHouse = findViewById(R.id.btnCatHouse)
        btnCatLand = findViewById(R.id.btnCatLand)
        btnCatAll = findViewById(R.id.btnCatAll)
        btnCatOther = findViewById(R.id.btnCatOther)
        btnCatCars = findViewById(R.id.btnCatCars)

        toolbar = findViewById(R.id.toolBar)
//        toolbar.title = ""
//        toolbar.navigationIcon = null
        setSupportActionBar(toolbar)

        svSearchProducts = findViewById(R.id.svSearchProducts)

        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rvItems = findViewById(R.id.rvItems)

        navView.setNavigationItemSelectedListener(this)

        // Instance of users list using the data model class.
//        changeLoginLogoutMenu()

        createGoogleRequest()

//        if (NetworkVariables.isNetworkConnected) {
//            getLastKey()
//        } else {
//            showManualConnectionSnackBar("getLastKey")
//        }

        getLastKey()

        val layoutManager = GridLayoutManager(this, 2)

        rvItems.layoutManager = layoutManager
        products = mutableListOf()
        adapter = ProductAdapter(this, products)
        rvItems.adapter = adapter

//        if (NetworkVariables.isNetworkConnected){
//            //Do something when network is connected
//            Log.d("C-Manager", "Internet Connected From NetworkVariables.")
//            getEveryProduct()
//        }
//        else {
//            showManualConnectionSnackBar("getEveryProduct")
//            //Do something else when network is not connected
//            Log.d("C-Manager", "Internet Disconnected! From NetworkVariables.")
//        }

        getEveryProduct()
//        getAllProducts()
//        getProductsData("All")
//        getProductsList("All")

//        productsList

//        svSearchProducts.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(query: String?): Boolean {
//
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//
//            }
//
//        })
        adapter.setOnItemClickListener(object : ProductAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                openProductDetailActivity(position)
            }

        })


        btnCatHouse.setOnClickListener {
            if (!isLoading) {
                disableCategoryButtons()
                products.clear()
                adapter.notifyDataSetChanged()
                selectedCategory = "House"
                isMaxData = false
                lastNode = ""
                lastResult = null
                isLoading = true
                getLastKey("House")
                getHouseProducts()
            } else {
                enableCategoryButtons()
            }
        }
        btnCatLand.setOnClickListener {
            if (!isLoading) {
                disableCategoryButtons()
                products.clear()
                adapter.notifyDataSetChanged()
                selectedCategory = "Land"
                isMaxData = false
                lastNode = ""
                lastResult = null
                isLoading = true
                getLastKey("Land")
                getLandProducts()
            } else {
                enableCategoryButtons()
            }
        }
        btnCatAll.setOnClickListener {
            if (!isLoading) {
                disableCategoryButtons()
                products.clear()
                adapter.notifyDataSetChanged()
                selectedCategory = "All"
                isMaxData = false
                lastNode = ""
                lastResult = null
                isLoading = true
                getLastKey()
                getEveryProduct()
            } else {
                enableCategoryButtons()
            }
        }
        btnCatOther.setOnClickListener {
            if (!isLoading) {
                disableCategoryButtons()
                products.clear()
                adapter.notifyDataSetChanged()
                selectedCategory = "Other"
                isMaxData = false
                lastNode = ""
                lastResult = null
                isLoading = true
                getLastKey("Other")
                getOtherProducts()
            } else {
                enableCategoryButtons()
            }
        }
        btnCatCars.setOnClickListener {
            if (!isLoading) {
                disableCategoryButtons()
                products.clear()
                adapter.notifyDataSetChanged()
                selectedCategory = "Cars"
                isMaxData = false
                lastNode = ""
                lastResult = null
                isLoading = true
                getLastKey("Cars")
                getCarProducts()
            } else {
                enableCategoryButtons()
            }
        }

        rvItems.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                totalItems = layoutManager.itemCount
//                Log.i(TAG, "total adapter item = $total_item")
                lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()

                if (selectedCategory == "All") {

                    Log.i(TAG, "total adapter item = $totalItems item count = ${lastVisibleItem + ITEM_COUNT_LIMIT}")
                    if (totalItems <= lastVisibleItem + 3) {
                        if (!isLoading) {
                            isLoading = true
                            getEveryProduct()
                        }
                    }
                } else if (selectedCategory == "Cars") {

                    Log.i(TAG, "total adapter item = $totalItems item count = ${lastVisibleItem + ITEM_COUNT_LIMIT}")

                    if (totalItems <= lastVisibleItem + 3) {
                        if (!isLoading) {
                            isLoading = true
                            getCarProducts()
                        }
                    }
                } else if (selectedCategory == "House") {

                    Log.i(TAG, "total adapter item = $totalItems item count = ${lastVisibleItem + ITEM_COUNT_LIMIT}")

                    if (totalItems <= lastVisibleItem + 3) {
                        if (!isLoading) {
                            isLoading = true
                            getHouseProducts()
                        }
                    }
                } else if (selectedCategory == "Land") {

                    Log.i(TAG, "total adapter item = $totalItems item count = ${lastVisibleItem + ITEM_COUNT_LIMIT}")

                    if (totalItems <= lastVisibleItem + 3) {
                        if (!isLoading) {
                            isLoading = true
                            getLandProducts()
                        }
                    }
                } else if (selectedCategory == "Other") {

                    Log.i(TAG, "total adapter item = $totalItems item count = ${lastVisibleItem + ITEM_COUNT_LIMIT}")

                    if (totalItems <= lastVisibleItem + 3) {
                        if (!isLoading) {
                            isLoading = true
                            getOtherProducts()
                        }
                    }
                }
            }
        })
    }

    private fun openProductDetailActivity(position: Int) {
        val productToView = ProductsModelParcelable().apply {
            uid = products[position].uid
            title = products[position].title
            price = products[position].price
            make = products[position].make
            model = products[position].model
            bathRooms = products[position].bathRooms
            bedRooms = products[position].bedRooms
            category = products[position].category
            color = products[position].color
            condition = products[position].condition
            description = products[position].description
            engineSize = products[position].engineSize
            fuel = products[position].fuel
            houseType = products[position].houseType
            kitchenType = products[position].kitchenType
            kitchens = products[position].kitchens
            location = products[position].location
            mileage = products[position].mileage
            phone = products[position].phone
            pictures = products[position].pictures
            plate = products[position].plate
            purpose = products[position].purpose
            reported = products[position].reported
            reportedNumber = products[position].reportedNumber
            seller = products[position].seller
            size = products[position].size
            transmission = products[position].transmission
            userPosted = products[position].userPosted
            views = products[position].views
            year = products[position].year
            sold = products[position].sold
            postDate = products[position].postDate
            updateDate = products[position].updateDate
        }

        when (products[position].category) {
            "Cars" -> {
                val intent = Intent(this@MainActivity, CarDetails::class.java)
                intent.putExtra("productToView", productToView)
                startActivity(intent)
            }
            "House" -> {
                val intent = Intent(this@MainActivity, HouseDetails::class.java)
                intent.putExtra("productToView", productToView)
                startActivity(intent)
            }
            "Land" -> {
                val intent = Intent(this@MainActivity, LandDetails::class.java)
                intent.putExtra("productToView", productToView)
                startActivity(intent)
            }
            "Other" -> {
                val intent = Intent(this@MainActivity, OtherDetails::class.java)
                intent.putExtra("productToView", productToView)
                startActivity(intent)
            }
        }
    }

    private fun disableCategoryButtons() {
        btnCatHouse.isEnabled = false
        btnCatLand.isEnabled = false
        btnCatOther.isEnabled = false
        btnCatCars.isEnabled = false
        btnCatAll.isEnabled = false
    }

    private fun enableCategoryButtons() {
        btnCatHouse.isEnabled = true
        btnCatLand.isEnabled = true
        btnCatOther.isEnabled = true
        btnCatCars.isEnabled = true
        btnCatAll.isEnabled = true
    }

    private fun getLastKey(categoryType: String = "All") {

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
                    Log.i(TAG, "Last Key = $lastKey")
//                    Toast.makeText(this, "Last Key = $last_key", Toast.LENGTH_SHORT).show()

                }
            }
            .addOnFailureListener { e ->
                Log.i(TAG, "$e")
            }
    }

    private fun checkInternetConnectionStatus() {
        networkConnectionStatus.observe(this, { isConnected ->
//            if (!isConnected) {
//                showLiveDataConnectionSnackBar()
//                Log.d("C-Manager", "No Internet Connection!")
//            } else {
//                Log.d("C-Manager", "Has Internet Connection!")
//            }
        })

    }

    private fun checkManualConnectionStatus(function: String) {
        if (NetworkVariables.isNetworkConnected){
            when (function) {
                "getLastKey" -> {
                    getLastKey()
                }
                "getEveryProduct" -> {
                    getEveryProduct()
                }
            }
        } else {
            showManualConnectionSnackBar(function)
        }
    }

    private fun showLiveDataConnectionSnackBar() {
        val snackBar = Snackbar.make(
            drawerLayout,
            "No Internet Connection!",
            Snackbar.LENGTH_INDEFINITE
        )
        snackBar.setAction("Retry") {
            snackBar.dismiss()
            checkInternetConnectionStatus()
        }.show()
    }

    private fun showManualConnectionSnackBar(function: String) {
        val snackBar = Snackbar.make(
            drawerLayout,
            "No Internet Connection!",
            Snackbar.LENGTH_INDEFINITE
        )
        snackBar.setAction("Retry") {
            snackBar.dismiss()
            checkManualConnectionStatus(function)
        }.show()
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
//        checkInternetConnectionStatus()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

//    @Suppress("UnresolvedReference")
    private fun createGoogleRequest() {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    @Suppress("DEPRECATION")
    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        // Navigate to MainActivity
        if (user == null) {
            Log.w(TAG, "User is null, not going to navigate")
            isLoggedIn = false
            currentUserName = "Guest"
            changeLoginLogoutMenu()
            return
        }

        val currentUser = auth.currentUser
        val docRef = db.collection("users").document(currentUser!!.uid)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("SAMUEL", "DocumentSnapshot data: ${document.data}")
                    if (document.data == null) {
                        val userToAdd = hashMapOf(
                            "name" to currentUser.displayName,
                            "email" to currentUser.email,
                            "phone" to 0,
                            "facebook" to "",
                            "registered" to FieldValue.serverTimestamp(),
                            "role" to "guest",
                            "status" to "active",
                            "favorites" to arrayListOf(""),
                        )
                        db.collection("users").document(currentUser.uid)
                            .set(userToAdd, SetOptions.merge())
                            .addOnSuccessListener {
                                Log.d(
                                    TAG,
                                    "DocumentSnapshot successfully written!"
                                )
                            }
                            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
                    }
                    isLoggedIn = true
                    currentUserName = currentUser.displayName.toString()
                    currentUserUid = currentUser.uid
//                    Toast.makeText(this, "$currentUserUid  $currentUserName",Toast.LENGTH_SHORT).show()
                    changeLoginLogoutMenu()
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }

    /*private fun loadPosts() {
        val query: Query

        if (lastResult == null) {
            query = prodRef.orderBy("postDate", Query.Direction.DESCENDING)
                .limit(5)
        } else {
            query = prodRef.orderBy("postDate", Query.Direction.DESCENDING)
                .startAfter(lastResult)
                .limit(5)
        }
        query.get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                val productsList = queryDocumentSnapshots.toObjects(ProductsModel::class.java)
//                for (product in productsList) {
//                    products.add(product)
//                }
                products.addAll(productsList)
                if (queryDocumentSnapshots.size() > 0) {
                    lastResult = queryDocumentSnapshots.documents[queryDocumentSnapshots.size() - 1].get("postDate") as Timestamp?
                }
                Log.i(TAG, "$queryDocumentSnapshots $lastResult")

//                rvItems.adapter = itemAdapter
//                itemAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.i(TAG, "$e")
            }
    }*/

    private fun getOtherProducts() {
        if (!isMaxData) {
            linearLayoutProgress.visibility = View.VISIBLE
            val query: Query = if (lastResult == null) {
                prodRef.orderBy("postDate", Query.Direction.DESCENDING)
                    .whereEqualTo("category","Other")
                    .limit(ITEM_COUNT_LIMIT)
            } else {
                prodRef.orderBy("postDate", Query.Direction.DESCENDING)
                    .whereEqualTo("category","Other")
                    .startAfter(lastResult)
                    .limit(ITEM_COUNT_LIMIT)
            }
            query.get()
                .addOnSuccessListener { documentsSnapshot ->

                    oldProductsListSize = products.size

                    for (documentSnapshot in documentsSnapshot) {
                        products.add(documentSnapshot.toObject(ProductsModel::class.java))
                    }

                    newProductsSize = products.size

                    lastNode = products[products.size - 1].uid

                    if (documentsSnapshot.size() > 0) {
                        lastResult = documentsSnapshot.documents[documentsSnapshot.size() - 1].get("postDate") as Timestamp?
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
                    enableCategoryButtons()
                    linearLayoutProgress.visibility = View.GONE
                }
                .addOnFailureListener { e ->
                    isLoading = false
                    enableCategoryButtons()
                    linearLayoutProgress.visibility = View.GONE
                    Log.i(TAG, "$e")
                }

        } else {
            isLoading = false
            enableCategoryButtons()
        }
    }

    private fun getLandProducts() {
        if (!isMaxData) {
            linearLayoutProgress.visibility = View.VISIBLE
            val query: Query = if (lastResult == null) {
                prodRef.orderBy("postDate", Query.Direction.DESCENDING)
                    .whereEqualTo("category","Land")
                    .limit(ITEM_COUNT_LIMIT)
            } else {
                prodRef.orderBy("postDate", Query.Direction.DESCENDING)
                    .whereEqualTo("category","Land")
                    .startAfter(lastResult)
                    .limit(ITEM_COUNT_LIMIT)
            }
            query.get()
                .addOnSuccessListener { documentsSnapshot ->

                    oldProductsListSize = products.size

                    for (documentSnapshot in documentsSnapshot) {
                        products.add(documentSnapshot.toObject(ProductsModel::class.java))
                    }

                    newProductsSize = products.size

                    lastNode = products[products.size - 1].uid

                    if (documentsSnapshot.size() > 0) {
                        lastResult = documentsSnapshot.documents[documentsSnapshot.size() - 1].get("postDate") as Timestamp?
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
                    enableCategoryButtons()
                    linearLayoutProgress.visibility = View.GONE
                }
                .addOnFailureListener { e ->
                    isLoading = false
                    enableCategoryButtons()
                    linearLayoutProgress.visibility = View.GONE
                    Log.i(TAG, "$e")
                }

        } else {
            isLoading = false
            enableCategoryButtons()
        }
    }

    private fun getHouseProducts() {
        if (!isMaxData) {
            linearLayoutProgress.visibility = View.VISIBLE
            val query: Query = if (lastResult == null) {
                prodRef.orderBy("postDate", Query.Direction.DESCENDING)
                    .whereEqualTo("category","House")
                    .limit(ITEM_COUNT_LIMIT)
            } else {
                prodRef.orderBy("postDate", Query.Direction.DESCENDING)
                    .whereEqualTo("category","House")
                    .startAfter(lastResult)
                    .limit(ITEM_COUNT_LIMIT)
            }
            query.get()
                .addOnSuccessListener { documentsSnapshot ->

                    oldProductsListSize = products.size

                    for (documentSnapshot in documentsSnapshot) {
                        products.add(documentSnapshot.toObject(ProductsModel::class.java))
                    }

                    newProductsSize = products.size

                    lastNode = products[products.size - 1].uid

                    if (documentsSnapshot.size() > 0) {
                        lastResult = documentsSnapshot.documents[documentsSnapshot.size() - 1].get("postDate") as Timestamp?
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
                    enableCategoryButtons()
                    linearLayoutProgress.visibility = View.GONE
                }
                .addOnFailureListener { e ->
                    isLoading = false
                    enableCategoryButtons()
                    linearLayoutProgress.visibility = View.GONE
                    Log.i(TAG, "$e")
                }

        } else {
            isLoading = false
            enableCategoryButtons()
        }

    }

    private fun getCarProducts() {
        if (!isMaxData) {
            linearLayoutProgress.visibility = View.VISIBLE
            val query: Query = if (lastResult == null) {
                prodRef.orderBy("postDate", Query.Direction.DESCENDING)
                    .whereEqualTo("category","Cars")
                    .limit(ITEM_COUNT_LIMIT)
            } else {
                prodRef.orderBy("postDate", Query.Direction.DESCENDING)
                    .whereEqualTo("category","Cars")
                    .startAfter(lastResult)
                    .limit(ITEM_COUNT_LIMIT)
            }
            query.get()
                .addOnSuccessListener { documentsSnapshot ->

                    oldProductsListSize = products.size

                    for (documentSnapshot in documentsSnapshot) {
                        products.add(documentSnapshot.toObject(ProductsModel::class.java))
                    }

                    newProductsSize = products.size

                    lastNode = products[products.size - 1].uid

                    if (documentsSnapshot.size() > 0) {
                        lastResult = documentsSnapshot.documents[documentsSnapshot.size() - 1].get("postDate") as Timestamp?
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
                    linearLayoutProgress.visibility = View.GONE
                    enableCategoryButtons()
                }
                .addOnFailureListener { e ->
                    isLoading = false
                    linearLayoutProgress.visibility = View.GONE
                    enableCategoryButtons()
                    Log.i(TAG, "$e")
                }

        } else {
            isLoading = false
            enableCategoryButtons()
        }

    }

    private fun getEveryProduct() {
        if (!isMaxData) {
            linearLayoutProgress.visibility = View.VISIBLE
            val query: Query = if (lastResult == null) {
                prodRef.orderBy("postDate", Query.Direction.DESCENDING)
                    .limit(ITEM_COUNT_LIMIT)
            } else {
                prodRef.orderBy("postDate", Query.Direction.DESCENDING)
                    .startAfter(lastResult)
                    .limit(ITEM_COUNT_LIMIT)
            }
            query.get()
                .addOnSuccessListener { documentsSnapshot ->

                    oldProductsListSize = products.size

                    for (documentSnapshot in documentsSnapshot) {
                        products.add(documentSnapshot.toObject(ProductsModel::class.java))
                    }

                    newProductsSize = products.size

                    lastNode = products[products.size - 1].uid

                    if (documentsSnapshot.size() > 0) {
                        lastResult = documentsSnapshot.documents[documentsSnapshot.size() - 1].get("postDate") as Timestamp?
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
                    linearLayoutProgress.visibility = View.GONE
                    enableCategoryButtons()
                }
                .addOnFailureListener { e ->
                    isLoading = false
                    linearLayoutProgress.visibility = View.GONE
                    enableCategoryButtons()
                    Log.i(TAG, "$e")
                }

        } else {
            isLoading = false
            enableCategoryButtons()
        }

    }

    /*private fun getAllProducts() {

        // Init Paging Configuration
        val config = PagingConfig(2, 1, false)

        val options = FirestorePagingOptions.Builder<ProductsModel>()
            .setLifecycleOwner(this)
            .setQuery(prodQuery, config, ProductsModel::class.java)
            .build()

//        pAdapterOne = ProductsAdapterOne(options)

        newAdapter = object : FirestorePagingAdapter<ProductsModel, ProductViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
                val view = layoutInflater.inflate(R.layout.grid_item_view, parent, false)
                return ProductViewHolder(view)
            }

            override fun onBindViewHolder(
                holder: ProductViewHolder,
                position: Int,
                model: ProductsModel
            ) {
                holder.bind(model)
            }

        }

        *//*lifecycleScope.launch {
            pAdapterOne.loadStateFlow.collectLatest { state ->
                when(state.refresh) {
                    is LoadState.Error -> {
                        Log.i(TAG, "Refresh Loading State Error")
                    }
                    is LoadState.NotLoading -> {
                        Log.i(TAG, "Refresh Not Loading State")
                    }
                    LoadState.Loading -> {
                        Log.i(TAG, "Refresh Loading State")
                    }
                }
                when (state.append) {
                    is LoadState.Error -> {
                        Log.i(TAG, "Append State Error")
                    }
                    is LoadState.NotLoading -> {
                        if (state.append.endOfPaginationReached) {
                            Log.i(TAG, "Append Loading Finished")
                        }
                        if (state.refresh is LoadState.NotLoading) {
                            Log.i(TAG, "Append Previous Loading Finished")
                        }
                    }
                    LoadState.Loading -> {
                        Log.i(TAG, "Append Loading State")
                    }
                }
            }
        }*//*
//        rvItems.adapter = newAdapter
    }*/

    /*private fun getProductsData(categoryType: String) {

//        selectedFilter = 1

        val query: Query

        if (lastResult == null) {
            query = prodRef.orderBy("postDate", Query.Direction.DESCENDING)
                .limit(50)
        } else {
            query = prodRef.orderBy("postDate", Query.Direction.DESCENDING)
                .startAfter(lastResult)
                .limit(50)
        }
        query.get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                val productsList = queryDocumentSnapshots.toObjects(ProductsModel::class.java)
//                for (product in productsList) {
//                    products.add(product)
//                }
                products.addAll(productsList)
                if (queryDocumentSnapshots.size() > 0) {
                    lastResult = queryDocumentSnapshots.documents[queryDocumentSnapshots.size() - 1].get("postDate") as Timestamp?
                }
                Log.i(TAG, "$queryDocumentSnapshots $lastResult")

//                rvItems.adapter = itemAdapter
//                itemAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.i(TAG, "$e")
            }


//        itemAdapter.notifyDataSetChanged()
//        itemAdapter.notifyDataSetChanged()

//        if (categoryType != "All") {
//            prodRef.orderBy("postDate", Query.Direction.DESCENDING)
//                .limit(7)
//                .get()
//                .addOnSuccessListener {queryDocumentSnapshots ->
//                    for (documentSnapshot in queryDocumentSnapshots) {
//                        val productsList = documentSnapshot.toObject(ProductsModel::class.java)
//                        products.add(productsList)
//                    }
//                }
//        }
*//*        val productReference = db
            .collection("products")
//            .whereEqualTo("category",categoryType )
            .limit(20)
            .orderBy("postDate", Query.Direction.DESCENDING)
        productReference.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                Log.e(TAG, "Exception when querying products", exception)
                return@addSnapshotListener
            }
            val productList = snapshot.toObjects(ProductsModel::class.java)
            this.products.clear()
            for (product in productList) {
                if (categoryType == "Cars" && product.category == "Cars") {
                    this.products.add(product)
                } else if (categoryType == "House" && product.category == "House") {
                    this.products.add(product)
                } else if (categoryType == "Land" && product.category == "Land") {
                    this.products.add(product)
                } else if (categoryType == "Other" && product.category == "Other") {
                    this.products.add(product)
                } else if (categoryType == "All") {
                    this.products.add(product)
                }
            }
            when (categoryType) {
                "Cars" -> {
                    selectedFilter = 2
                }
                "House" -> {
                    selectedFilter = 3
                }
                "Land" -> {
                    selectedFilter = 4
                }
                "Other" -> {
                    selectedFilter = 5
                }
                else -> {
                    selectedFilter = 1
                }
            }
//            this.products.addAll(productList)
            // Set the LayoutManager that this RecyclerView will use.
            rvItems.layoutManager = GridLayoutManager(this, 2)
            rvItems.adapter = itemAdapter
//            itemAdapter.notifyDataSetChanged()
//            for (product in productList) {
//                Log.i(TAG, "Product $product")
//            }
        }*//*
    }*/

/*    private fun getProductsList(categoryType: String){
        productsList.clear()
        try {
            // As we have JSON object, so we are getting the object
            //Here we are calling a Method which is returning the JSON object
            val obj = JSONObject(getJSONFromAssets()!!)
            // fetch JSONArray named users by using getJSONArray
            val productsArray = obj.getJSONArray("products")
            // Get the users data using for loop i.e. id, name, email and so on

            for (i in 0 until productsArray.length()) {
                // Create a JSONObject for fetching single User's Data
                val product = productsArray.getJSONObject(i)
                // Fetch id store it in variable
                val id = product.getInt("id")
                val title = product.getString("title")
                val year = product.getString("year")
                val price = product.getString("price")
                val imgUrl = product.getString("picture")
                val age = product.getString("age")
                val category = product.getString("category")
                val name = product.getString("name")
                val email = product.getString("email")
                val description = product.getString("description")
                val postedDate = product.getString("posted")
                val location = product.getString("location")
                val size = product.getString("size")

                // Now add all the variables to the data model class and the data model class to the array list.
                val productDetails =
                    ProductModelClass(id, title, year, price, imgUrl, age, category, name, email, description, postedDate, location, size)

                // add the details in the list
                if (categoryType == "Cars" && category == "Cars"){
                    productsList.add(productDetails)
                } else if (categoryType == "House" && category == "House"){
                    productsList.add(productDetails)
                } else if (categoryType == "Land" && category == "Land"){
                    productsList.add(productDetails)
                } else if (categoryType == "Other" && category == "Other"){
                    productsList.add(productDetails)
                } else if (categoryType == "All"){
                    productsList.add(productDetails)
                }
            }

            when (categoryType) {
                "Cars" -> {
                    selectedFilter = 2
                }
                "House" -> {
                    selectedFilter = 3
                }
                "Land" -> {
                    selectedFilter = 4
                }
                "Other" -> {
                    selectedFilter = 5
                }
                else -> {
                    selectedFilter = 1
                }
            }

        } catch (e: JSONException) {
            //exception
            e.printStackTrace()
        }

        // Set the LayoutManager that this RecyclerView will use.
        rvItems.layoutManager = GridLayoutManager(this, 2)
        // Adapter class is initialized and list is passed in the param.
//        val itemAdapter = ProductAdapter(this, productsList)
        // adapter instance is set to the recyclerview to inflate the items.
        rvItems.adapter = itemAdapter
        itemAdapter.notifyDataSetChanged()
    }*/

    private fun showLogoutDialog() {
        val dialog = MaterialDialog(this)
            .noAutoDismiss()
            .cancelOnTouchOutside(false)
            .cornerRadius(14f)
            .customView(R.layout.logout_layout)
        dialog.findViewById<Button>(R.id.btnYes).setOnClickListener {
            Firebase.auth.signOut()
            isLoggedIn = false
            changeLoginLogoutMenu()
            navTvUserName.text = "Guest"

            dialog.dismiss()
        }

        dialog.findViewById<Button>(R.id.btnNo).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showLoginDialog() {
        val dialog = MaterialDialog(this)
            .noAutoDismiss()
            .cornerRadius(14f)
            .customView(R.layout.login_layout)

        dialog.findViewById<Button>(R.id.btnGoogleSignIn).setOnClickListener {
//            isLoggedIn = true
//            changeLoginLogoutMenu()
            googleSignIn()
            changeLoginLogoutMenu()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showFilterDialog() {
        val dialog = MaterialDialog(this)
            .noAutoDismiss()
            .cornerRadius(14f)
            .customView(R.layout.layout_filter)

        when (selectedFilter) {
            1 -> {
                dialog.findViewById<RadioGroup>(R.id.filter_group).check(R.id.filter_all)
            }
            2 -> {
                dialog.findViewById<RadioGroup>(R.id.filter_group).check(R.id.filter_car)
            }
            3 -> {
                dialog.findViewById<RadioGroup>(R.id.filter_group).check(R.id.filter_house)
            }
            4 -> {
                dialog.findViewById<RadioGroup>(R.id.filter_group).check(R.id.filter_land)
            }
            5 -> {
                dialog.findViewById<RadioGroup>(R.id.filter_group).check(R.id.filter_other)
            }
        }

//        // set initial preferences
//        val filter = preferences.getString(
//            getString(R.string.key_filter),
//            getString(R.string.filter_date)
//            )
//        if (filter.equals(getString(R.string.filter_date))){
//            dialog.findViewById<RadioGroup>(R.id.filter_group).check(R.id.filter_date)
//        } else {
//            dialog.findViewById<RadioGroup>(R.id.filter_group).check(R.id.filter_author)
//        }
//
//        val order = preferences.getString(
//            getString(R.string.key_order),
//            getString(R.string.order_asc)
//        )
//        if (order.equals(getString(R.string.order_asc))){
//            dialog.findViewById<RadioGroup>(R.id.order_group).check(R.id.order_asc)
//        } else {
//            dialog.findViewById<RadioGroup>(R.id.order_group).check(R.id.order_desc)
//        }

        // get new preferences
        dialog.findViewById<TextView>(R.id.positive_button).setOnClickListener {
            val selectedCategory = dialog.getCustomView().findViewById<RadioButton>(
                dialog.findViewById<RadioGroup>(R.id.filter_group).checkedRadioButtonId
            )
/*            when (selectedCategory.text) {
                "Cars" -> {
                    selectedFilter = 2
//                    getProductsData("Cars")
                }
                "House" -> {
                    selectedFilter = 3
//                    getProductsData("House")
                }
                "Land" -> {
                    selectedFilter = 4
//                    getProductsData("Land")
                }
                "Other" -> {
                    selectedFilter = 5
//                    getProductsData("Other")
                }
                "All" -> {
                    selectedFilter = 1
//                    getProductsData("All")
                }
            }*/

            dialog.dismiss()
        }

        dialog.findViewById<TextView>(R.id.negative_button).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        if (svSearchProducts.query.isEmpty()) {
            svSearchProducts.clearFocus()
        }
    }

    override fun onBackPressed() {
//        Toast.makeText(this,"Hello", Toast.LENGTH_SHORT).show()
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            if (svSearchProducts.query.isEmpty()) {
                svSearchProducts.clearFocus()
            }
        } else {
            super.onBackPressed()
            if (svSearchProducts.query.isEmpty()) {
                svSearchProducts.clearFocus()
            }
        }
    }

    private fun changeLoginLogoutMenu() {

        if (isLoggedIn) {
            navView.menu.clear()
            navView.inflateMenu(R.menu.menu_with_logout)
            navTvUserName = findViewById(R.id.tvNavUserName)
            navTvUserName.text = currentUserName
//            navView.menu.removeItem()
        } else {
            navView.menu.clear()
            navView.inflateMenu(R.menu.menu_with_login)
//            navTvUserName = findViewById(R.id.tvNavUserName)
//            navTvUserName.text = "Guest"
        }

//        navView.menu.findItem(R.id.miLogin).isVisible = !isLoggedIn

//        navView.menu.removeItem(R.id.miLogout)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            Toast.makeText(this, "Clicked on ${item.title}", Toast.LENGTH_SHORT).show()
            return true
        }
        if (item.itemId == R.id.miFilter) {
//            showFilterDialog()
        }
        if (item.itemId == R.id.miGoHome) {
            if (!isLoading) {
                disableCategoryButtons()
                products.clear()
                adapter.notifyDataSetChanged()
                selectedCategory = "All"
                isMaxData = false
                lastNode = ""
                lastResult = null
                isLoading = true
                getLastKey()
                getEveryProduct()
            } else {
                enableCategoryButtons()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_top, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miLogout -> {
                showLogoutDialog()
                drawerLayout.closeDrawer(GravityCompat.START)
//                isLoggedIn = false
                return true
            }
            R.id.miLogin -> {
                //            drawerLayout.closeDrawer(GravityCompat.START)
                showLoginDialog()
                //            isLoggedIn = true
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }
            R.id.miAddPost -> {
                return if (!isLoggedIn) {
                    showLoginDialog()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                } else {
                    val intent = Intent(this@MainActivity, AddPost::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
            }
            R.id.miYourPost -> {
                if (isLoggedIn) {
                    val intent = Intent(this@MainActivity, CurrentUserProducts::class.java)
                    intent.putExtra("currentUserName", this@MainActivity.currentUserName)
                    intent.putExtra("currentUserUid", this@MainActivity.currentUserUid)
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    return true
                }
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
