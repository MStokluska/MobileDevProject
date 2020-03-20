package com.mstokluska.chattie.models.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(var id: String = "",
                     var userName: String = "",
                     var name: String = "",
                     var password: String = ""
): Parcelable

