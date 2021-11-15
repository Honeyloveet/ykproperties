package net.ykproperties.ykproperties.viewholder

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.ykproperties.ykproperties.R
import net.ykproperties.ykproperties.model.ProductsModel
import java.text.NumberFormat
import java.util.*

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var tvTitleGridV: TextView = itemView.findViewById(R.id.tvTitleGridV)
    private var tvPriceGridV : TextView = itemView.findViewById(R.id.tvPriceGridV)
    private var ivItemsGridV: ImageView = itemView.findViewById(R.id.ivItemsGridV)
    private var tvProductIdGridV: TextView = itemView.findViewById(R.id.tvProductIdGridV)

    @SuppressLint("SetTextI18n")
    fun bind(product: ProductsModel) {
        if (product.category == "Cars") {
            tvProductIdGridV.text = product.uid
            tvTitleGridV.text = "${product.make} ${product.model}"
            tvPriceGridV.text = "Br ${NumberFormat.getInstance(Locale.US).format(product.price)} ${product.purpose}"
//            Glide.with(context).load(product.pictures[0]).into(holder.ivItemsGridV)
        } else if (product.category == "House") {
            tvProductIdGridV.text = product.uid
            tvTitleGridV.text = "Location: ${product.location}"
            tvPriceGridV.text = "Br ${NumberFormat.getInstance(Locale.US).format(product.price)} ${product.purpose}"
//            Glide.with(context).load(product.pictures[0]).into(holder.ivItemsGridV)
        } else if (product.category == "Other") {
            tvProductIdGridV.text = product.uid
            tvTitleGridV.text = product.title
            tvPriceGridV.text = "Br ${NumberFormat.getInstance(Locale.US).format(product.price)} ${product.purpose}"
//            Glide.with(context).load(product.pictures[0]).into(holder.ivItemsGridV)
        } else if (product.category == "Land") {
            tvProductIdGridV.text = product.uid
            tvTitleGridV.text = "Location: ${product.location}"
            tvPriceGridV.text = "Br ${NumberFormat.getInstance(Locale.US).format(product.price)} ${product.purpose}"
            if (product.pictures.isNotEmpty()) {
                if (product.pictures[0] != "") {
//                    Glide.with(context).load(product.pictures[0]).into(holder.ivItemsGridV)
                } else {
                    ivItemsGridV.setImageResource(R.drawable.ic_add_photo)
                }
            }
        }
    }

}