package net.ykproperties.ykproperties

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar

class FilterActivity : AppCompatActivity() {

    private companion object {
        private const val TAG = "SAMUEL"
    }

    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_filter)

        toolbar = findViewById(R.id.toolBarFilter)

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Filter"
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        toolbar.setNavigationOnClickListener {
            finish()
        }

    }
}