package net.ykproperties.ykproperties

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.ykproperties.ykproperties.Model.ProductModelClass
import com.bumptech.glide.Glide

class ProductAdapter (val context: Context, val items: ArrayList<ProductModelClass>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.grid_item_view,
                parent,
                false
            )
        )
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items.get(position)

        holder.tvProductIdGridV.text = item.title
        holder.tvTitleGridV.text = item.title
        holder.tvPriceGridV.text = "${item.price} - For SALE"
        Glide.with(context).load(item.imgUrl).into(holder.ivItemsGridV)
//        holder.tvGender.text = item.gender
//        holder.tvWeight.text = item.weight.toString()
//        holder.tvHeight.text = item.height.toString()
//        holder.tvMobileNumber.text = item.mobile
//        holder.tvOfficeNumber.text = item.office
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to
        val tvTitleGridV = view.findViewById<TextView>(R.id.tvTitleGridV)
        val tvPriceGridV = view.findViewById<TextView>(R.id.tvPriceGridV)
        val ivItemsGridV = view.findViewById<ImageView>(R.id.ivItemsGridV)
        val tvProductIdGridV = view.findViewById<TextView>(R.id.tvProductIdGridV)
    }
}