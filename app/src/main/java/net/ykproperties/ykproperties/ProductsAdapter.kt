package net.ykproperties.ykproperties

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import net.ykproperties.ykproperties.model.ProductsModel
import java.text.NumberFormat
import java.util.*

class ProductsAdapter(options: FirestoreRecyclerOptions<ProductsModel>, val context: Context) : FirestoreRecyclerAdapter<ProductsModel, ProductsAdapter.ProductsViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.grid_item_view,
            parent,
            false)
        return ProductsViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int, item: ProductsModel) {
        if (item.category == "Cars") {
            holder.tvProductIdGridV.text = item.uid
            holder.tvTitleGridV.text = "${item.make} ${item.model}"
            holder.tvPriceGridV.text = "Br ${NumberFormat.getInstance(Locale.US).format(item.price)} ${item.purpose}"
            Glide.with(context).load(item.pictures[0]).into(holder.ivItemsGridV)
        } else if (item.category == "House") {
            holder.tvProductIdGridV.text = item.uid
            holder.tvTitleGridV.text = "Location: ${item.location}"
            holder.tvPriceGridV.text = "Br ${NumberFormat.getInstance(Locale.US).format(item.price)} ${item.purpose}"
            Glide.with(context).load(item.pictures[0]).into(holder.ivItemsGridV)
        } else if (item.category == "Other") {
            holder.tvProductIdGridV.text = item.uid
            holder.tvTitleGridV.text = item.title
            holder.tvPriceGridV.text = "Br ${NumberFormat.getInstance(Locale.US).format(item.price)} ${item.purpose}"
            Glide.with(context).load(item.pictures[0]).into(holder.ivItemsGridV)
        } else if (item.category == "Land") {
            holder.tvProductIdGridV.text = item.uid
            holder.tvTitleGridV.text = "Location: ${item.location}"
            holder.tvPriceGridV.text = "Br ${NumberFormat.getInstance(Locale.US).format(item.price)} ${item.purpose}"
            if (item.pictures.isNotEmpty()) {
                if (item.pictures[0] != "") {
                    Glide.with(context).load(item.pictures[0]).into(holder.ivItemsGridV)
                } else {
                    holder.ivItemsGridV.setImageResource(R.drawable.ic_add_photo)
                }
            }
        }
    }

    class ProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitleGridV: TextView = itemView.findViewById(R.id.tvTitleGridV)
        val tvPriceGridV : TextView = itemView.findViewById(R.id.tvPriceGridV)
        val ivItemsGridV: ImageView = itemView.findViewById(R.id.ivItemsGridV)
        val tvProductIdGridV: TextView = itemView.findViewById(R.id.tvProductIdGridV)

    }

}