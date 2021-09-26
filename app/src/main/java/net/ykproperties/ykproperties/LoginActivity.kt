package net.ykproperties.ykproperties

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate

class LoginActivity : AppCompatActivity() {

    lateinit var btnGoogleSignIn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_login)

        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn)

        btnGoogleSignIn.setOnClickListener {
            goToMainActivity()
        }
    }

    private fun goToMainActivity() {
        val mainActivity = Intent(this, MainActivity::class.java)
        startActivity(mainActivity)
        finish()
    }
}