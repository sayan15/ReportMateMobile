package com.example.reportmate.model

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng

data class FinalData(val latLng: LatLng?, val title: String, val selectedTime: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(LatLng::class.java.classLoader),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(latLng, flags)
        dest.writeString(title)
        dest.writeString(selectedTime)
    }

    companion object CREATOR : Parcelable.Creator<FinalData> {
        override fun createFromParcel(parcel: Parcel): FinalData {
            return FinalData(parcel)
        }

        override fun newArray(size: Int): Array<FinalData?> {
            return arrayOfNulls(size)
        }
    }
}
