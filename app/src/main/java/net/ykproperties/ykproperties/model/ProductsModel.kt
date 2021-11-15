package net.ykproperties.ykproperties.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*
import kotlin.collections.ArrayList

data class ProductsModel(
    var uid: String = "",
    var title: String = "",
    var price: Long = 0,
    var make: String = "",
    var model: String = "",
    var bathRooms: Long = 0,
    var bedRooms: Long = 0,
    var category: String = "",
    var color: String = "",
    var condition: String = "",
    var description: String = "",
    var engineSize: Long = 0,
    var fuel: String = "",
    var houseType: String = "",
    var kitchenType: String = "",
    var kitchens: Long = 0,
    var location: String = "",
    var mileage: Long = 0,
    var phone: Long = 0,
    var pictures: ArrayList<String> = ArrayList(),
    var plate: String = "",
    var purpose: String = "",
    var reported: Boolean = false,
    var reportedNumber: Long = 0,
    var seller: String = "",
    var size: Long = 0,
    var transmission: String = "",
    var userPosted: String = "",
    var views: Long = 0,
    var year: Long = 0,
    var sold: Boolean = false,
    @ServerTimestamp
    var postDate: Date? = null,
    @ServerTimestamp
    var updateDate: Date? = null
)