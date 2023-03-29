package com.example.social_auth.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class LinkedInEmailModel(
    @SerializedName("elements") var elements: ArrayList<Elements> = arrayListOf()
) : Serializable

data class Elements(
    @SerializedName("handle~") var handleEmail: Handle? = Handle(),
    @SerializedName("handle") var handle: String? = null
)

data class Handle(
    @SerializedName("emailAddress") var emailAddress: String? = null
)