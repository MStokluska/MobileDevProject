package com.mstokluska.chattie.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatModel( var id: String = "",
                      var creator: String = "",
                      var receiver: String = ""): Parcelable