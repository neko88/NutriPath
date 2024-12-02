package com.group35.nutripath.homemenu.dataobject

import android.os.Parcel
import android.os.Parcelable

data class BottomDataObject (
    var title: String = "",
    var description: String = "",
    var picUrl: ArrayList<String> = ArrayList(),
    var size: ArrayList<String> = ArrayList(),
    var price: Double = 0.0,
    var rating: Double = 0.0,
    var numberInCart: Int = 0
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.createStringArrayList() as ArrayList<String>,
        parcel.createStringArrayList() as ArrayList<String>,
        parcel.readDouble(),
        parcel.readDouble()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(title)
            parcel.writeString(description)
            parcel.writeStringList(picUrl)
            parcel.writeStringList(size)
            parcel.writeDouble(price)
            parcel.writeDouble(rating)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<BottomDataObject> {
            override fun createFromParcel(parcel: Parcel): BottomDataObject {
                return BottomDataObject(parcel)
            }

            override fun newArray(size: Int): Array<BottomDataObject?> {
                return arrayOfNulls(size)
            }
        }
}
