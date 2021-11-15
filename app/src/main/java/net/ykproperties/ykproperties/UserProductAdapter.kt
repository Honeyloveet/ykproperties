package net.ykproperties.ykproperties

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import net.ykproperties.ykproperties.model.ProductsModel
import java.text.NumberFormat
import java.util.*

class UserProductAdapter (val context: Context, private var productList: MutableList<ProductsModel>) : RecyclerView.Adapter<UserProductAdapter.ViewHolder>() {

    private companion object {
        private const val TAG = "UpdateSoldStatus"
    }

    private val db = Firebase.firestore
//    private val storageRef = Firebase.storage.reference

    private lateinit var mListener : OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
        fun onEditClick(position: Int)
        fun onDeleteClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    val lastItemUid: String
        get() = productList[productList.size - 1].uid

    fun addAll(newProducts: List<ProductsModel>){
        val init = productList.size
        productList.addAll(newProducts)
        notifyItemRangeChanged(init,newProducts.size)
    }

    fun clearProductList() {
        if (!productList.isNullOrEmpty()){
            val size = productList.size
            productList.clear()
            notifyItemRangeRemoved(0,size)
        }
    }

    fun getItemToDelete(position: Int): String {
        return productList[position].uid
    }

//    override fun getItemId(position: Int): Long {
//        return productList[position].uid.toLong()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.current_user_products_view,
                parent,
                false
            ), mListener
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.setIsRecyclable(false)
        holder.checkBoxCurrentUserProductsSold.setOnCheckedChangeListener(null)
        val item = productList[position]
        when (item.category) {
            "Cars" -> {
                holder.tvCurrentUserProductsTitle.text = "${item.make} ${item.model}"
                holder.tvCurrentUserProductsPrice.text = "Br ${NumberFormat.getInstance(Locale.US).format(item.price)} ${item.purpose}"
                holder.checkBoxCurrentUserProductsSold.isChecked = item.sold
                if (item.sold) {
                    holder.tvCurrentUserProductStatus.visibility = View.VISIBLE
                } else {
                    holder.tvCurrentUserProductStatus.visibility = View.GONE
                }
                Glide.with(context).load(item.pictures[0]).into(holder.ivCurrentUserProducts)
            }
            "House" -> {
                holder.tvCurrentUserProductsTitle.text = "Location: ${item.location}"
                holder.tvCurrentUserProductsPrice.text = "Br ${NumberFormat.getInstance(Locale.US).format(item.price)} ${item.purpose}"
                holder.checkBoxCurrentUserProductsSold.isChecked = item.sold
                if (item.sold) {
                    holder.tvCurrentUserProductStatus.visibility = View.VISIBLE
                } else {
                    holder.tvCurrentUserProductStatus.visibility = View.GONE
                }
                Glide.with(context).load(item.pictures[0]).into(holder.ivCurrentUserProducts)
            }
            "Other" -> {
                holder.tvCurrentUserProductsTitle.text = item.title
                holder.tvCurrentUserProductsPrice.text = "Br ${NumberFormat.getInstance(Locale.US).format(item.price)} ${item.purpose}"
                holder.checkBoxCurrentUserProductsSold.isChecked = item.sold
                if (item.sold) {
                    holder.tvCurrentUserProductStatus.visibility = View.VISIBLE
                } else {
                    holder.tvCurrentUserProductStatus.visibility = View.GONE
                }
                Glide.with(context).load(item.pictures[0]).into(holder.ivCurrentUserProducts)
            }
            "Land" -> {
                holder.tvCurrentUserProductsTitle.text = "Location: ${item.location}"
                holder.tvCurrentUserProductsPrice.text = "Br ${NumberFormat.getInstance(Locale.US).format(item.price)} ${item.purpose}"
                holder.checkBoxCurrentUserProductsSold.isChecked = item.sold
                if (item.sold) {
                    holder.tvCurrentUserProductStatus.visibility = View.VISIBLE
                } else {
                    holder.tvCurrentUserProductStatus.visibility = View.GONE
                }
                if (item.pictures.isNotEmpty()) {
                    if (item.pictures[0] != "") {
                        Glide.with(context).load(item.pictures[0]).into(holder.ivCurrentUserProducts)
                    } else {
                        holder.ivCurrentUserProducts.setImageResource(R.drawable.ic_add_photo)
                    }
                }
            }
        }

        holder.checkBoxCurrentUserProductsSold.setOnCheckedChangeListener { _, isChecked ->
            if (item.sold != isChecked) {
                db.collection("products").document(item.uid)
                    .update("sold", isChecked)
                    .addOnSuccessListener {
                        Log.d(TAG, "${item.uid} successfully updated!")
                        item.sold = isChecked
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error updating ${item.uid}", e)
                    }
                Log.i("SAMUEL", "Item $position Sold = ")
            }
        }
    }

    override fun getItemId(position: Int) = position.toLong()
    override fun getItemViewType(position: Int) = position

    override fun getItemCount(): Int {
        return productList.size
    }

    class ViewHolder(view: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to
        val tvCurrentUserProductsTitle: TextView = view.findViewById(R.id.tvCurrentUserProductsTitle)
        val tvCurrentUserProductsPrice : TextView = view.findViewById(R.id.tvCurrentUserProductsPrice)
        val tvCurrentUserProductStatus : TextView = view.findViewById(R.id.tvCurrentUserProductStatus)
        val ivCurrentUserProducts: ImageView = view.findViewById(R.id.ivCurrentUserProducts)
        val checkBoxCurrentUserProductsSold: CheckBox = view.findViewById(R.id.checkBoxCurrentUserProductsSold)
        val btnCurrentUserProductsDelete: ImageButton = view.findViewById(R.id.btnCurrentUserProductsDelete)
        val btnCurrentUserProductsEdit: ImageButton = view.findViewById(R.id.btnCurrentUserProductsEdit)

        init {
            view.setOnClickListener {
                listener.onItemClick(absoluteAdapterPosition)
            }
            btnCurrentUserProductsEdit.setOnClickListener {
                listener.onEditClick(absoluteAdapterPosition)
            }
            btnCurrentUserProductsDelete.setOnClickListener {
                listener.onDeleteClick(absoluteAdapterPosition)
            }
        }

    }

}