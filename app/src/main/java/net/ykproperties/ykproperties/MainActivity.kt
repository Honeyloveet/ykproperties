package net.ykproperties.ykproperties

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import net.ykproperties.ykproperties.Model.ProductModelClass
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    lateinit var toolbar: Toolbar
    lateinit var rvItems: RecyclerView
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var svSearchProducts: SearchView

    lateinit var btnCatHouse: ImageButton
    lateinit var btnCatLand: ImageButton
    lateinit var btnCatAll: ImageButton
    lateinit var btnCatOther: ImageButton
    lateinit var btnCatCars: ImageButton

    var selectedFilter = 1

    val productsList: ArrayList<ProductModelClass> = ArrayList()
    val itemAdapter = ProductAdapter(this, productsList)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rvItems = findViewById(R.id.rvItems)

        // Instance of users list using the data model class.

        getProductsList("All")

//        productsList

        itemAdapter.setOnItemClickListener(object : ProductAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
//                Toast.makeText(this@MainActivity,"You Clicked on item no. $position",Toast.LENGTH_SHORT).show()

                if (productsList[position].category == "Cars"){
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
                } else if (productsList[position].category == "House"){
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
                } else if (productsList[position].category == "Land"){
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
                } else if (productsList[position].category == "Other"){
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

            if (categoryType == "Cars"){
                selectedFilter = 2
            } else if (categoryType == "House"){
                selectedFilter = 3
            } else if (categoryType == "Land"){
                selectedFilter = 4
            } else if (categoryType == "Other"){
                selectedFilter = 5
            } else {
                selectedFilter = 1
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

    private fun showFilterDialog(){
        val dialog = MaterialDialog(this)
            .noAutoDismiss()
            .customView(R.layout.layout_filter)

        if (selectedFilter == 1){
            dialog.findViewById<RadioGroup>(R.id.filter_group).check(R.id.filter_all)
        } else if (selectedFilter == 2) {
            dialog.findViewById<RadioGroup>(R.id.filter_group).check(R.id.filter_car)
        } else if (selectedFilter == 3) {
            dialog.findViewById<RadioGroup>(R.id.filter_group).check(R.id.filter_house)
        } else if (selectedFilter == 4) {
            dialog.findViewById<RadioGroup>(R.id.filter_group).check(R.id.filter_land)
        } else if (selectedFilter == 5) {
            dialog.findViewById<RadioGroup>(R.id.filter_group).check(R.id.filter_other)
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
            if (selectedCategory.text == "Cars"){
                selectedFilter = 2
                getProductsList("Cars")
            } else if (selectedCategory.text == "House"){
                selectedFilter = 3
                getProductsList("House")
            } else if (selectedCategory.text == "Land"){
                selectedFilter = 4
                getProductsList("Land")
            } else if (selectedCategory.text == "Other"){
                selectedFilter = 5
                getProductsList("Other")
            } else if (selectedCategory.text == "All"){
                selectedFilter = 1
                getProductsList("All")
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

    override fun onSupportNavigateUp(): Boolean {

        return super.onSupportNavigateUp()
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
}