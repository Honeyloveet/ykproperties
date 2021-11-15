package net.ykproperties.ykproperties.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*
import kotlin.collections.ArrayList

data class ProductsModelTwo(
    var uid: String? = null,
    var title: String? = null,
    var price: Long? = null,
    var make: String? = null,
    var model: String? = null,
    var bathRooms: Long? = null,
    var bedRooms: Long? = null,
    var category: String? = null,
    var color: String? = null,
    var condition: String? = null,
    var description: String? = null,
    var engineSize: Long? = null,
    var fuel: String? = null,
    var houseType: String? = null,
    var kitchenType: String? = null,
    var kitchens: Long? = null,
    var location: String? = null,
    var mileage: Long? = null,
    var phone: Long? = null,
    var pictures: ArrayList<String>? = null,
    var plate: String? = null,
    var purpose: String? = null,
    var reported: Boolean? = null,
    var reportedNumber: Long? = null,
    var seller: String? = null,
    var size: Long? = null,
    var transmission: String? = null,
    var userPosted: String? = null,
    var views: Long? = null,
    var year: Long? = null,
    @ServerTimestamp
    var postDate: Date? = null,
    @ServerTimestamp
    var updateDate: Date? = null
)
