package com.example.social_auth.Model

import com.google.gson.annotations.SerializedName

class LinkdinFirstnameModel (

    @SerializedName("firstName"      ) var firstName      : FirstName?      = FirstName(),
    @SerializedName("lastName"       ) var lastName       : LastName?       = LastName(),
    @SerializedName("id"             ) var id             : String?         = null
)


data class FirstName (
    @SerializedName("localized"       ) var localized       : Localized?       = Localized(),

    )
data class Localized (
    @SerializedName("en_US" ) var enUS : String? = null
)
data class LastName (
    @SerializedName("localized"       ) var localized       : Localized?       = Localized(),

    )
