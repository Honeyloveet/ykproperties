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
import net.ykproperties.ykproperties.Model.ProductsModel
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ProductAdapter (val context: Context, private val items: ArrayList<ProductsModel>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private lateinit var mListener : OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

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
            ),mListener
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
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]

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
//        holder.tvProductIdGridV.text = item.uid
//        holder.tvTitleGridV.text = "Location ${item.location}"
//        holder.tvPriceGridV.text = "${item.price} ${item.purpose}"
//        Glide.with(context).load(item.pictures[0]).into(holder.ivItemsGridV)
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
    class ViewHolder(view: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to
        val tvTitleGridV:TextView = view.findViewById(R.id.tvTitleGridV)
        val tvPriceGridV :TextView = view.findViewById(R.id.tvPriceGridV)
        val ivItemsGridV: ImageView = view.findViewById(R.id.ivItemsGridV)
        val tvProductIdGridV: TextView = view.findViewById(R.id.tvProductIdGridV)

        init {
            view.setOnClickListener {
                listener.onItemClick(absoluteAdapterPosition)
            }
        }

    }

    private fun formatPrice() {

    }
}