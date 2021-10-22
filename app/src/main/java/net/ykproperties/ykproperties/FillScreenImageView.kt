package net.ykproperties.ykproperties

import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.ortiz.touchview.TouchImageView

class FillScreenImageView : AppCompatActivity() {

    private lateinit var ivFullScreenImage: TouchImageView
    private lateinit var ivCloseImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fill_screen_image_view)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        ivFullScreenImage = findViewById(R.id.ivFullScreenImage)
        ivCloseImage = findViewById(R.id.ivCloseImage)

        val bundle : Bundle? = intent.extras
        val imgUrl = bundle!!.getString("imgUrl")

        Glide.with(this)
            .load(imgUrl)
            .into(object : CustomTarget<Drawable?>(){
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable?>?
                ) {
                    ivFullScreenImage.setImageDrawable(resource)
                }
                override fun onLoadCleared(placeholder: Drawable?) = Unit
            })

        ivCloseImage.setOnClickListener {
            finish()
        }

    }
}