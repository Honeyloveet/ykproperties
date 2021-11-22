package net.ykproperties.ykproperties.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*
import kotlin.collections.ArrayList

data class ProductsModelParcelable(
    var uid: String? = "",
    var title: String? = "",
    var price: Long = 0,
    var make: String? = "",
    var model: String? = "",
    var bathRooms: Long = 0,
    var bedRooms: Long = 0,
    var category: String? = "",
    var color: String? = "",
    var condition: String? = "",
    var description: String? = "",
    var engineSize: Long = 0,
    var fuel: String? = "",
    var houseType: String? = "",
    var kitchenType: String? = "",
    var kitchens: Long = 0,
    var location: String? = "",
    var mileage: Long = 0,
    var phone: Long = 0,
    var pictures: ArrayList<String> = ArrayList(),
    var plate: String? = "",
    var purpose: String? = "",
    var reported: Boolean = false,
    var reportedNumber: Long = 0,
    var seller: String? = "",
    var size: Long = 0,
    var transmission: String? = "",
    var userPosted: String? = "",
    var views: Long = 0,
    var year: Long = 0,
    var sold: Boolean = false,
    var postDate: Date? = null,
    var updateDate: Date? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readLong(),
        arrayListOf<String>().apply { parcel.readList(this, String::class.java.classLoader) },
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readByte() != 0.toByte(),
        parcel.readValue(Date::class.java.classLoader) as? Date,
        parcel.readValue(Date::class.java.classLoader) as? Date
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(title)
        parcel.writeLong(price)
        parcel.writeString(make)
        parcel.writeString(model)
        parcel.writeLong(bathRooms)
        parcel.writeLong(bedRooms)
        parcel.writeString(category)
        parcel.writeString(color)
        parcel.writeString(condition)
        parcel.writeString(description)
        parcel.writeLong(engineSize)
        parcel.writeString(fuel)
        parcel.writeString(houseType)
        parcel.writeString(kitchenType)
        parcel.writeLong(kitchens)
        parcel.writeString(location)
        parcel.writeLong(mileage)
        parcel.writeLong(phone)
        parcel.writeList(pictures)
        parcel.writeString(plate)
        parcel.writeString(purpose)
        parcel.writeByte(if (reported) 1 else 0)
        parcel.writeLong(reportedNumber)
        parcel.writeString(seller)
        parcel.writeLong(size)
        parcel.writeString(transmission)
        parcel.writeString(userPosted)
        parcel.writeLong(views)
        parcel.writeLong(year)
        parcel.writeByte(if (sold) 1 else 0)
        parcel.writeValue(postDate)
        parcel.writeValue(updateDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductsModelParcelable> {
        override fun createFromParcel(parcel: Parcel): ProductsModelParcelable {
            return ProductsModelParcelable(parcel)
        }

        override fun newArray(size: Int): Array<ProductsModelParcelable?> {
            return arrayOfNulls(size)
        }
    }
}
