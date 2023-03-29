package com.example.social_auth.Model

import com.google.android.gms.auth.api.identity.SignInPassword

class User {
    var name: String? = null
    var email: String? = null
    var password: String? = null
    var uid: String? = null
//    var isactive: Boolean? = null

    constructor() {}

    constructor(name: String?, email: String?, password: String?, uid: String?) {
        this.name = name
        this.email = email
        this.password = password
        this.uid = uid
//        this.isactive = isactive
    }
}
