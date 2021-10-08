package net.ykproperties.ykproperties

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import net.ykproperties.ykproperties.Model.ProductModelClass
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var auth: FirebaseAuth

    private companion object{
        private const val RC_GOOGLE_SIGN_IN = 2088
        private const val TAG = "SAMUEL"
    }

    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    lateinit var toolbar: Toolbar
    lateinit var rvItems: RecyclerView
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var svSearchProducts: SearchView

    lateinit var btnCatHouse: ImageButton
    lateinit var btnCatLand: ImageButton
    lateinit var btnCatAll: ImageButton
    lateinit var btnCatOther: ImageButton
    lateinit var btnCatCars: ImageButton

    var selectedFilter = 1
    var isLoggedIn: Boolean = false

    val productsList: ArrayList<ProductModelClass> = ArrayList()
    val itemAdapter = ProductAdapter(this, productsList)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        btnCatHouse = findViewById(R.id.btnCatHouse)
        btnCatLand = findViewById(R.id.btnCatLand)
        btnCatAll = findViewById(R.id.btnCatAll)
        btnCatOther = findViewById(R.id.btnCatOther)
        btnCatCars = findViewById(R.id.btnCatCars)

        toolbar = findViewById(R.id.toolBar)
//        toolbar.title = ""
//        toolbar.navigationIcon = null
        setSupportActionBar(toolbar)

//        svSearchProducts = findViewById(R.id.svSearchProducts)

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

        getProductsList("All")

//        productsList

        itemAdapter.setOnItemClickListener(object : ProductAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
//                Toast.makeText(this@MainActivity,"You Clicked on item no. $position",Toast.LENGTH_SHORT).show()

                when (productsList[position].category) {
                    "Cars" -> {
                        val intent = Intent(this@MainActivity, CarDetails::class.java)
                        intent.putExtra("id",productsList[position].id)
                        intent.putExtra("title",productsList[position].title)
                        intent.putExtra("price",productsList[position].price)
                        intent.putExtra("imgUrl",productsList[position].imgUrl)
                        intent.putExtra("category",productsList[position].category)
                        intent.putExtra("description",productsList[position].description)
                        intent.putExtra("posted",productsList[position].posted)
                        intent.putExtra("imgUrl",productsList[position].imgUrl)
                        intent.putExtra("location",productsList[position].location)
                        intent.putExtra("size",productsList[position].size)
                        intent.putExtra("year",productsList[position].year)

                        startActivity(intent)
                    }
                    "House" -> {
                        val intent = Intent(this@MainActivity, HouseDetails::class.java)
                        intent.putExtra("id",productsList[position].id)
                        intent.putExtra("title",productsList[position].title)
                        intent.putExtra("price",productsList[position].price)
                        intent.putExtra("imgUrl",productsList[position].imgUrl)
                        intent.putExtra("category",productsList[position].category)
                        intent.putExtra("description",productsList[position].description)
                        intent.putExtra("posted",productsList[position].posted)
                        intent.putExtra("imgUrl",productsList[position].imgUrl)
                        intent.putExtra("location",productsList[position].location)
                        intent.putExtra("size",productsList[position].size)
                        intent.putExtra("year",productsList[position].year)

                        startActivity(intent)
                    }
                    "Land" -> {
                        val intent = Intent(this@MainActivity, LandDetails::class.java)
                        intent.putExtra("id",productsList[position].id)
                        intent.putExtra("title",productsList[position].title)
                        intent.putExtra("price",productsList[position].price)
                        intent.putExtra("imgUrl",productsList[position].imgUrl)
                        intent.putExtra("category",productsList[position].category)
                        intent.putExtra("description",productsList[position].description)
                        intent.putExtra("posted",productsList[position].posted)
                        intent.putExtra("imgUrl",productsList[position].imgUrl)
                        intent.putExtra("location",productsList[position].location)
                        intent.putExtra("size",productsList[position].size)
                        intent.putExtra("year",productsList[position].year)

                        startActivity(intent)
                    }
                    "Other" -> {
                        val intent = Intent(this@MainActivity, OtherDetails::class.java)
                        intent.putExtra("id",productsList[position].id)
                        intent.putExtra("title",productsList[position].title)
                        intent.putExtra("price",productsList[position].price)
                        intent.putExtra("imgUrl",productsList[position].imgUrl)
                        intent.putExtra("category",productsList[position].category)
                        intent.putExtra("description",productsList[position].description)
                        intent.putExtra("posted",productsList[position].posted)
                        intent.putExtra("imgUrl",productsList[position].imgUrl)
                        intent.putExtra("location",productsList[position].location)
                        intent.putExtra("size",productsList[position].size)
                        intent.putExtra("year",productsList[position].year)

                        startActivity(intent)
                    }
                }

            }

        })

//        svSearchProducts.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                TODO("Not yet implemented")
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                TODO("Not yet implemented")
//            }
//
//        })

        btnCatHouse.setOnClickListener {
            if (selectedFilter != 3){
                getProductsList("House")
                selectedFilter = 3
            }
        }
        btnCatLand.setOnClickListener {
            if (selectedFilter != 4){
                getProductsList("Land")
                selectedFilter = 4
            }
        }
        btnCatAll.setOnClickListener {
            if (selectedFilter != 1){
                getProductsList("All")
                selectedFilter = 1
            }
        }
        btnCatOther.setOnClickListener {
            if (selectedFilter != 5){
                getProductsList("Other")
                selectedFilter = 5
            }
        }
        btnCatCars.setOnClickListener {
            if (selectedFilter != 2){
                getProductsList("Cars")
                selectedFilter = 2
            }
        }

    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun createGoogleRequest(){
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
    }

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
            changeLoginLogoutMenu()
            return
        } else {
            Log.w(TAG, "User is $user")
            isLoggedIn = true
            changeLoginLogoutMenu()
        }
    }

    private fun getProductsList(categoryType: String){
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
    }

    private fun showLogoutDialog(){
        val dialog = MaterialDialog(this)
            .noAutoDismiss()
            .cornerRadius(14f)
            .customView(R.layout.logout_layout)
        dialog.findViewById<Button>(R.id.btnYes).setOnClickListener {
            Firebase.auth.signOut()
            isLoggedIn = false
            changeLoginLogoutMenu()
            dialog.dismiss()
        }

        dialog.findViewById<Button>(R.id.btnNo).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showLoginDialog(){
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

    private fun showFilterDialog(){
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
            when (selectedCategory.text) {
                "Cars" -> {
                    selectedFilter = 2
                    getProductsList("Cars")
                }
                "House" -> {
                    selectedFilter = 3
                    getProductsList("House")
                }
                "Land" -> {
                    selectedFilter = 4
                    getProductsList("Land")
                }
                "Other" -> {
                    selectedFilter = 5
                    getProductsList("Other")
                }
                "All" -> {
                    selectedFilter = 1
                    getProductsList("All")
                }
            }

            dialog.dismiss()
        }

        dialog.findViewById<TextView>(R.id.negative_button).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onBackPressed() {
//        Toast.makeText(this,"Hello", Toast.LENGTH_SHORT).show()
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun changeLoginLogoutMenu() {

        if (isLoggedIn){
            navView.menu.clear()
            navView.inflateMenu(R.menu.menu_with_logout)
//            navView.menu.removeItem()
        } else {
            navView.menu.clear()
            navView.inflateMenu(R.menu.menu_with_login)
        }

//        navView.menu.findItem(R.id.miLogin).isVisible = !isLoggedIn

//        navView.menu.removeItem(R.id.miLogout)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            Toast.makeText(this, "Clicked on ${item.title}", Toast.LENGTH_SHORT).show()
            return true
        }
        if (item.itemId == R.id.miFilter){
            showFilterDialog()
        }
        if (item.itemId == R.id.miGoHome){
            if (selectedFilter != 1){
                getProductsList("All")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_top, menu)
        return true
    }

    /**
     * Method to load the JSON from the Assets file and return the object
     */
    private fun getJSONFromAssets(): String? {

        var jsonn: String? = null
        val charset: Charset = Charsets.UTF_8
        try {
            val myUsersJSONFile = assets.open("products.json")
            val size = myUsersJSONFile.available()
            val buffer = ByteArray(size)
            myUsersJSONFile.read(buffer)
            myUsersJSONFile.close()
            jsonn = String(buffer, charset)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return jsonn
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
                if (!isLoggedIn) {
                    showLoginDialog()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    return true
                } else {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    val intent = Intent(this@MainActivity, AddPost::class.java)
                    startActivity(intent)
                    return true
                }
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
