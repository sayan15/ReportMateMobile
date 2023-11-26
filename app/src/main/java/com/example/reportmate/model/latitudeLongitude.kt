package com.example.reportmate.model

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng

data class latitudeLongitude(val latlng: LatLng?, val title:String):Parcelable

 {
     constructor(parcel: Parcel) : this(
         parcel.readParcelable(LatLng::class.java.classLoader),
         parcel.readString().toString()
     )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(latlng, flags)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<latitudeLongitude> {
        override fun createFromParcel(parcel: Parcel): latitudeLongitude {
            return latitudeLongitude(parcel)
        }

        override fun newArray(size: Int): Array<latitudeLongitude?> {
            return arrayOfNulls(size)
        }
    }
}
