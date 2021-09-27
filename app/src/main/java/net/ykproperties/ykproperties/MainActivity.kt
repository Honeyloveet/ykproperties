package net.ykproperties.ykproperties

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.ykproperties.ykproperties.Model.ProductModelClass
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var rvItems: RecyclerView
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolBar)
//        toolbar.title = ""
//        toolbar.navigationIcon = null
        setSupportActionBar(toolbar)



        drawerLayout = findViewById(R.id.drawerLayout)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rvItems = findViewById(R.id.rvItems)


        // Instance of users list using the data model class.
        val productsList: ArrayList<ProductModelClass> = ArrayList()

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
                productsList.add(productDetails)
            }
        } catch (e: JSONException) {
            //exception
            e.printStackTrace()
        }

//        productsList

        // Set the LayoutManager that this RecyclerView will use.
        rvItems.layoutManager = GridLayoutManager(this, 2)
        // Adapter class is initialized and list is passed in the param.
        val itemAdapter = ProductAdapter(this, productsList)
        // adapter instance is set to the recyclerview to inflate the items.
        rvItems.adapter = itemAdapter

    }

    override fun onBackPressed() {
        Toast.makeText(this,"Hello", Toast.LENGTH_SHORT).show()
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
            return true
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