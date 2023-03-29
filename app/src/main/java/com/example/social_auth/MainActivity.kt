package com.example.social_auth

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.example.social_auth.Model.LinkdinFirstnameModel
import com.example.social_auth.Model.LinkedInEmailModel
import com.example.social_auth.linkedin.LinkedInConstants
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth

import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.json.JSONTokener
import java.io.OutputStreamWriter
import java.net.URL
import java.util.concurrent.TimeUnit
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 1
    private var callbackManager: CallbackManager? = null
    private var googleApiClient: GoogleApiClient? = null

    lateinit var linkedinAuthURLFull: String
    lateinit var linkedIndialog: Dialog
    lateinit var linkedinCode: String
    var id = ""
    var firstName = ""
    var lastName = ""
    var email = ""
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FacebookSdk.sdkInitialize(applicationContext)
//        printHashKey()

        val link_google = findViewById<Button>(R.id.link_google)
        val linkedin = findViewById<Button>(R.id.linkedin)
//        val loginButton = findViewById<LoginButton>(R.id.login_button)
        val img_messs = findViewById<ImageView>(R.id.img_messs)
        val facebookdata = findViewById<TextView>(R.id.facebookdata)
        val myImageView = findViewById<ImageView>(R.id.my_image_view)
        val camera = findViewById<ImageView>(R.id.camera)
        val customFacebookButton = findViewById<Button>(R.id.custom_facebook_button)


        myImageView.setOnClickListener {
            Toast.makeText(this, "Image clicked!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,Map_Activity::class.java))
        }
        img_messs.setOnClickListener{
            startActivity(Intent(this@MainActivity,Chat_Activity::class.java))
        }
        camera.setOnClickListener {
            startActivity(Intent(this@MainActivity,Camera_Activity::class.java))
        }
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        googleApiClient = GoogleApiClient.Builder(this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build()
        callbackManager = CallbackManager.Factory.create()


        link_google.setOnClickListener({
            val intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient!!)
            startActivityForResult(intent, RC_SIGN_IN)
        })


        val state = "linkedin" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
        linkedinAuthURLFull =
            LinkedInConstants.AUTHURL + "?response_type=code&client_id=" + LinkedInConstants.CLIENT_ID + "&scope=" + LinkedInConstants.SCOPE + "&state=" + state + "&redirect_uri=" + LinkedInConstants.REDIRECT_URI

        linkedin.setOnClickListener({
            linkedIndialog = Dialog(this)
            val webView = WebView(this)
            webView.isVerticalScrollBarEnabled = false
            webView.isHorizontalScrollBarEnabled = false
            webView.webViewClient = LinkedInWebViewClient()
            webView.settings.javaScriptEnabled = true
            webView.loadUrl(linkedinAuthURLFull)
            linkedIndialog.setContentView(webView)
            linkedIndialog.show()
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(linkedIndialog.getWindow()?.getAttributes())
            lp.width = WindowManager.LayoutParams.FILL_PARENT
            lp.height = WindowManager.LayoutParams.FILL_PARENT
            linkedIndialog.getWindow()?.setAttributes(lp)
         //   setupLinkedinWebviewDialog(linkedinAuthURLFull)
        })

        customFacebookButton.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
        }
        customFacebookButton.setOnClickListener({
            callbackManager = CallbackManager.Factory.create()
           LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onCancel() {
                    startActivity(Intent(this@MainActivity,MainActivity::class.java))
                    }

                    override fun onError(error: FacebookException) {
                        TODO("Not yet implemented")
                    }

                    override fun onSuccess(loginResult: LoginResult) {
//                val facebook = loginResult.accessToken.userId
//                println("facebook"+facebook)

                        val request = GraphRequest.newMeRequest(
                            AccessToken.getCurrentAccessToken(),
                            object : GraphRequest.GraphJSONObjectCallback {
                                override fun onCompleted(jsonObject: JSONObject?, response: GraphResponse?) {

                                    val intent= Intent(this@MainActivity,Facebook_data_Activity::class.java)
                                    intent.putExtra("name",jsonObject?.getString("name"))
                                    intent.putExtra("id",jsonObject?.getString("id"))
                                    intent.putExtra("picture",jsonObject?.getJSONObject("picture")?.getJSONObject("data")?.getString("url"))
                                    startActivity(intent)
//                                    val name = jsonObject?.getString("name")
//                                    val id = jsonObject?.getString("id")
//                                    val pictureUrl = jsonObject?.getJSONObject("picture")?.getJSONObject("data")?.getString("url")


                                    // Do something with the user's details
                                    // For example, display them in a TextView
//                                    facebookdata.text = "Name: $name\nID: $id"
//                                    Glide.with(this@MainActivity).load(pictureUrl).into(facebookprofile)

                                }
                            }
                        )

                        val parameters = Bundle().apply {
                            putString("fields", "name,id,picture.type(large)") // You can request any additional fields here
                        }
                        request.parameters = parameters
                        request.executeAsync()
                    }

                })
        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)


        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)
            println("result"+result)
            if (result!!.isSuccess) {

                val acct = result.signInAccount
                val intent:Intent = Intent(this,MainActivity2::class.java)
                intent.putExtra("email",acct!!.email)
                intent.putExtra("name",acct.displayName)
                startActivity(intent)
            } else {
//                response_message("false");
            }
        }
    }

    @Suppress("OverridingDeprecatedMember")
    inner class LinkedInWebViewClient : WebViewClient() {

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (request?.url.toString().startsWith(LinkedInConstants.REDIRECT_URI)) {
                handleUrl(request?.url.toString())

                if (request?.url.toString().contains("?code=")) {
                    linkedIndialog.dismiss()
                }
                return true
            }
            return false
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith(LinkedInConstants.REDIRECT_URI)) {
                handleUrl(url)

                // Close the dialog after getting the authorization code
                if (url.contains("?code=")) {
                    linkedIndialog.dismiss()
                }
                return true
            }
            return false
        }
    }

    private fun handleUrl(url: String) {
        val uri = Uri.parse(url)

        if (url.contains("code")) {
            linkedinCode = uri.getQueryParameter("code") ?: ""
            linkedInRequestForAccessToken()
        } else if (url.contains("error")) {
            val error = uri.getQueryParameter("error") ?: ""
            Log.e("Error: ", error)
        }
    }

    fun linkedInRequestForAccessToken() {
        GlobalScope.launch(Dispatchers.Default) {
            val grantType = "authorization_code"
            val postParams =
                "grant_type=" + grantType + "&code=" + linkedinCode + "&redirect_uri=" + LinkedInConstants.REDIRECT_URI + "&client_id=" + LinkedInConstants.CLIENT_ID + "&client_secret=" + LinkedInConstants.CLIENT_SECRET
            val url = URL(LinkedInConstants.TOKENURL)
            val httpsURLConnection =
                withContext(Dispatchers.IO) { url.openConnection() as HttpsURLConnection }
            httpsURLConnection.requestMethod = "POST"
            httpsURLConnection.setRequestProperty(
                "Content-Type",
                "application/x-www-form-urlencoded"
            )
            httpsURLConnection.doInput = true
            httpsURLConnection.doOutput = true
            val outputStreamWriter = OutputStreamWriter(httpsURLConnection.outputStream)
            withContext(Dispatchers.IO) {
                outputStreamWriter.write(postParams)
                outputStreamWriter.flush()
            }
            val response = httpsURLConnection.inputStream.bufferedReader()
                .use { it.readText() }  // defaults to UTF-8
            val jsonObject = JSONTokener(response).nextValue() as JSONObject

            val accessToken = jsonObject.getString("access_token") //The access token
            Log.d("accessToken is: ", accessToken)

            val expiresIn = jsonObject.getInt("expires_in") //When the access token expires
            Log.d("expires in: ", expiresIn.toString())


            withContext(Dispatchers.Main) {
                // Get user's id, first name, last name, profile pic url
                fetchlinkedInUserProfile(accessToken)
            }
        }
    }

    fun fetchlinkedInUserProfile(token: String) {
        GlobalScope.launch(Dispatchers.Default) {
            val tokenURLFull =
                "https://api.linkedin.com/v2/me?projection=(id,firstName,lastName,profilePicture(displayImage~:playableStreams))&oauth2_access_token=$token"
            val url = URL(tokenURLFull)
            val httpsURLConnection =
                withContext(Dispatchers.IO) { url.openConnection() as HttpsURLConnection }
            httpsURLConnection.requestMethod = "GET"
            httpsURLConnection.doInput = true
            httpsURLConnection.doOutput = false
            val response = httpsURLConnection.inputStream.bufferedReader()
                .use { it.readText() }  // defaults to UTF-8


            val responseJson = response

            val gson = Gson()
            try {
                val responseClass = gson.fromJson(responseJson, LinkdinFirstnameModel::class.java)
                if (responseClass != null) {
                    firstName = responseClass.firstName?.localized?.enUS.toString()
                    lastName = responseClass.lastName?.localized?.enUS.toString()
                    println("Name+++ : $firstName")
                    println("Name+++ : $lastName")
                } else {
                    println("Response is null")
                }
            } catch (exception: Exception) {
                exception.message
            }
            val jsonObject = JSONTokener(response).nextValue() as JSONObject


            // Get user's email address
            fetchLinkedInEmailAddress(token)

        }
    }

    fun fetchLinkedInEmailAddress(token: String) {
        val tokenURLFull =
            "https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))&oauth2_access_token=$token"

        GlobalScope.launch(Dispatchers.Default) {
            val url = URL(tokenURLFull)
            val httpsURLConnection =
                withContext(Dispatchers.IO) { url.openConnection() as HttpsURLConnection }
            httpsURLConnection.requestMethod = "GET"
            httpsURLConnection.doInput = true
            httpsURLConnection.doOutput = false

            val response = httpsURLConnection.inputStream.bufferedReader()
                .use { it.readText() }  // defaults to UTF-8
            val jsonObject = JSONTokener(response).nextValue() as JSONObject

            println("responseresponse"+response)
            val responseJson = response

            val gson = Gson()
            try {
                val responseClass = gson.fromJson(responseJson, LinkedInEmailModel::class.java)
                if (responseClass != null) {
                    email = responseClass.elements[0].handleEmail?.emailAddress.toString()
                    println("Email+++ : $email")
                    val emailnew = email

                } else {
                    println("Response is null")
                }
            } catch (exception: Exception) {
                exception.message
            }
            if (email != null) {
                val intent1 = Intent(this@MainActivity,Linkedin_Result::class.java)
                intent1.putExtra("name",firstName)
                intent1.putExtra("last_name",lastName)
                intent1.putExtra("email",email)
                startActivity(intent1)
            }


        }
    }

}


