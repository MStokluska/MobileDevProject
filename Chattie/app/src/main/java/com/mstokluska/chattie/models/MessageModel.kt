package com.mstokluska.chattie.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

// parcelized for displaying last message on chat list screen ? optional.
@Parcelize
data class MessageModel(var id: String = "",
                        var mcreator: String = "",
                        var mchat: String = "",
                        var content: String = ""
): Parcelable

