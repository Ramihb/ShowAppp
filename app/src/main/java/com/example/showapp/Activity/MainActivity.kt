package com.example.showapp.Activity

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.airbnb.lottie.LottieAnimationView
import com.example.showapp.Api.ApiCompany
import com.example.showapp.Api.ApiUser
import com.example.showapp.Model.Company
import com.example.showapp.Model.User
import com.example.showapp.R
import com.example.showapp.Utils.Language
import com.example.showapp.Utils.LoadingDialog
import com.example.showapp.Utils.MyContextWrapper
import com.example.showapp.Utils.NetworkConnection
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_user_sign_up.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    val RC_SIGN_IN = 123

    lateinit var mSharedPref: SharedPreferences
    lateinit var mSharedPrefUser: SharedPreferences
    lateinit var myPreference: Language
    lateinit var loginMail: EditText
    lateinit var loginPassword: EditText
    var loadingDialog = LoadingDialog()
    lateinit var animationView: LottieAnimationView
    private lateinit var cld: NetworkConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkNetworkConnection()
        val showappImage = findViewById<ImageView>(R.id.Showapp)
        var clicks = 0
        showappImage.setOnClickListener {
            clicks = clicks + 1
            println("testing clicks"+clicks)
            if (clicks == 5) {
                Toast.makeText(this,"admin login enabled",Toast.LENGTH_SHORT).show()
            }
            if(clicks>=6){
                val intent = Intent(this@MainActivity, AdminLoginActivity::class.java)
                startActivity(intent)
                showappImage.setEnabled(false)

            }
        }

        animationView = findViewById(R.id.animationView)
        hideLayout()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        //Begin user Sign up navigation


        val signUpUser = findViewById<TextView>(R.id.SignUpUser)
        signUpUser.setOnClickListener {
            navigateToSignUpUser()
        }
        //End user sign up navigation

        //Begin Company Sign up navigation

        val signUpCompany = findViewById<TextView>(R.id.SignUpComapny)
        signUpCompany.setOnClickListener {
            navigateToSignUpCompany()
        }
        //End company sign up navigation


        var loginBtn: Button = findViewById(R.id.LoginBtn)
        //val loadingDialog = LoadingDialog()
        loadingDialog.LoadingDialog(this)
        loginBtn.setOnClickListener {
            loginMail = findViewById(R.id.EmailInput)
            loginPassword = findViewById(R.id.PasswordInput)
            if (loginMail.text.toString().isEmpty()) {
                loginMail.error = "Email required"
                loginMail.requestFocus()
                return@setOnClickListener
            }
            if (loginPassword.text.toString().isEmpty()) {
                loginPassword.error = "password required"
                loginPassword.requestFocus()
                return@setOnClickListener
            }
            //loadingDialog.startLoadingDialog()
            showLayout()

            companyLogin()

        }


        val ForgetPasswordBtn = findViewById<TextView>(R.id.ForgetPassword)
        ForgetPasswordBtn.setOnClickListener {
            navigateToForgetPassword()
        }

        val googleBtn = findViewById<ImageView>(R.id.google_btn)



        mSharedPref = getSharedPreferences("CompanyPref", Context.MODE_PRIVATE)
        if (mSharedPref.getBoolean("session", false)) {
            navigateToCompanyProfile()
        }
        mSharedPrefUser = getSharedPreferences("UserPref", Context.MODE_PRIVATE)

        if (mSharedPrefUser.getBoolean("session", false)) {
            navigateToUser()
        }
        googleBtn.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }


    }

    private fun checkNetworkConnection() {
        cld = NetworkConnection(application)

        cld.observe(this) { isConnected ->

            if (isConnected) {
                Log.i("connected", "good")

            } else {
                Snackbar.make(MainActivityy, "Check your connection", Snackbar.LENGTH_SHORT)
                    .setIcon(
                        getDrawable(R.drawable.wifi_off)!!,
                        resources.getColor(android.R.color.white, theme)
                    )
                    .show()
            }

        }
    }


    //test
    private fun Snackbar.setIcon(drawable: Drawable, @ColorInt colorTint: Int): Snackbar {
        return this.apply {
            setAction(" ") {}
            val textView =
                view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
            textView.text = ""

            drawable.setTint(colorTint)
            drawable.setTintMode(PorterDuff.Mode.SRC_ATOP)
            textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        }
    }

    //Begin Company Login
    private fun companyLogin() {
        loginMail = findViewById(R.id.EmailInput)
        loginPassword = findViewById(R.id.PasswordInput)
        val companyy = Company()
        companyy.emailCompany = loginMail.text.toString()
        companyy.passwordCompany = loginPassword.text.toString()
        val apicompany = ApiCompany.create().logincompany(companyy)
        apicompany.enqueue(object : Callback<Company> {
            override fun onResponse(call: Call<Company>, response: Response<Company>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "good", Toast.LENGTH_LONG).show()
                    Log.i("login Company", response.body().toString())
                    Log.i("LastNameCompany:", response.body()?.Company?.lastNameCompany.toString())
                    Log.i("login response", response.body().toString())
                    //Store company data in sharedpref
                    //mSharedPref = getSharedPreferences("CompanyPref", Context.MODE_PRIVATE)
                    mSharedPref.edit().apply {
                        putString(
                            "brandPicCompany",
                            response.body()?.Company?.brandPicCompany.toString()
                        )
                        putString("brandID", response.body()?.Company?._id.toString())
                        putBoolean("session", true)
                    }.apply()
                    //loadingDialog.dismissDialog()
                    hideLayout()

                    navigateToCompanyProfile()

                } else {
                    //Toast.makeText(applicationContext, "uncorrect email or password", Toast.LENGTH_LONG).show()
                    Log.i("RETROFIT_API_RESPONSE", response.toString())
                    Log.i("login response", response.body().toString())
                    userLogin()
                }
            }

            override fun onFailure(call: Call<Company>, t: Throwable) {
                hideLayout()
                Toast.makeText(applicationContext, "erreur server", Toast.LENGTH_LONG).show()
            }

        })
    }

    //End Company Login
    //Begin User Login
    private fun userLogin() {
        loginMail = findViewById(R.id.EmailInput)
        loginPassword = findViewById(R.id.PasswordInput)
        val userr = User()
        userr.email = loginMail.text.toString()
        userr.password = loginPassword.text.toString()
        val apiuser = ApiUser.create().loginUser(userr)
        apiuser.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "good", Toast.LENGTH_LONG).show()
                    Log.i("login User:", response.body().toString())
                    //Store company data in sharedpref
                    //mSharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
                    mSharedPrefUser.edit().apply {
                        putString("UserPicture", response.body()?.user?.profilePicture.toString())
                        putString("UserID", response.body()?.user?._id.toString())
                        putString("firstName", response.body()?.user?.firstName.toString())
                        putString("lastName", response.body()?.user?.lastName.toString())
                        putString("email", response.body()?.user?.email.toString())
                        putBoolean("session", true)
                    }.apply()
                    //loadingDialog.dismissDialog()
                    hideLayout()
                    navigateToUser()
                } else {
                    //loadingDialog.dismissDialog()
                    hideLayout()
                    Toast.makeText(
                        applicationContext,
                        "uncorrect email or password",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.i("RETROFIT_API_RESPONSE", response.toString())
                    Log.i("login response", response.body().toString())
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                //loadingDialog.dismissDialog()
                Toast.makeText(applicationContext, "erreur server", Toast.LENGTH_LONG).show()
                hideLayout()
            }

        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            var userr = User()
            userr.email = account.email.toString()
            userr.lastName = account.familyName.toString()
            userr.firstName = account.givenName.toString()
            Log.i("degla", userr.toString() + account.photoUrl)
            loadingDialog.LoadingDialog(this)
            //loadingDialog.startLoadingDialog()
            showLayout()
            val apiuser = ApiUser.create().loginSocial(userr)
            apiuser.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        Log.i("degla2", response.body()?.user?.profilePicture.toString())
                        //Store company data in sharedpref
                        //mSharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
                        mSharedPrefUser.edit().apply {
                            putString(
                                "UserPicture",
                                response.body()?.user?.profilePicture.toString()
                            )
                            putString("UserID", response.body()?.user?._id.toString())
                            putString("firstName", response.body()?.user?.firstName.toString())
                            putString("email", response.body()?.user?.email.toString())
                            putString("phoneNumber", response.body()?.user?.phoneNumber.toString())
                            putString("lastName", response.body()?.user?.lastName.toString())
                            //putBoolean("session", true)
                        }.apply()
                        Log.i("degla3", mSharedPrefUser.getString("UserPicture", "").toString())
                        //loadingDialog.dismissDialog()
                        hideLayout()
                        finish()
                        navigateToUser()

                    } else if (response.code() == 401) {

                        createAccount(
                            account.givenName.toString(),
                            account.familyName.toString(),
                            account.email.toString(),
                            null,
                            null
                        )
                        navigateToUser()
                        Log.i("degla4",response.body().toString())

                    } else {
                        //loadingDialog.dismissDialog()
                        hideLayout()
                        Toast.makeText(
                            applicationContext,
                            "Email ou Mot de passe incorrect",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.i("RETROFIT_API_RESPONSE", response.toString())
                        Log.i("login response", response.body().toString())
                    }
                    Log.d("status code : ", response.body().toString())
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    //loadingDialog.dismissDialog()
                    hideLayout()
                    Toast.makeText(applicationContext, "Erreur server", Toast.LENGTH_LONG).show()
                }

            })
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            Log.i("user google est : ", e.toString())
        }
    }

    fun createAccount(
        firstName: String,
        lastName: String,
        email: String,
        password: String?,
        number: String?
    ) {


        val apiInterface = ApiUser.create()
        val data: LinkedHashMap<String, RequestBody> = LinkedHashMap()

        data["firstName"] = RequestBody.create(MultipartBody.FORM, firstName)
        data["lastName"] = RequestBody.create(MultipartBody.FORM, lastName)
        data["email"] = RequestBody.create(MultipartBody.FORM, email)
        if (password != null) {
            data["password"] = RequestBody.create(MultipartBody.FORM, password)
            data["phoneNumber"] = RequestBody.create(MultipartBody.FORM, number!!)
        }
        Log.d("data to signup with social : ", data.toString())

        apiInterface.userSignUpSocial(data).enqueue(object : Callback<User> {
            override fun onResponse(
                call: Call<User>,
                response: Response<User>
            ) {
                println("waaaaa"+response.code())
                if (response.isSuccessful) {
                    mSharedPrefUser.edit().apply {
                        putString("UserPicture", response.body()?.user?.profilePicture.toString().trim())
                        putString("UserID", response.body()?.user?._id.toString())
                        putString("firstName", response.body()?.user?.firstName.toString())
                        putString("lastName", response.body()?.user?.lastName.toString())
                        putString("email", response.body()?.user?.email.toString())
                        //putBoolean("session", true)
                    }.apply()
                    Log.i("signup good", response.body()?.user.toString())
//                        showAlertDialog()
                } else {
                    Log.i("signup error", response.body().toString())
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                println(t.toString())
            }

        })


    }

    private fun hideLayout() {
        animationView.pauseAnimation()
        animationView.visibility = View.GONE
    }

    private fun showLayout() {

        animationView.playAnimation()
        animationView.loop(true)
        animationView.visibility = View.VISIBLE
    }

    //navigation functions
    private fun navigateToSecondActivity() {
        finish()
        val intent = Intent(this@MainActivity, CompanyProfile::class.java)
        startActivity(intent)
    }

    private fun navigateToSignUpUser() {
        finish()
        val intent = Intent(this@MainActivity, UserSignUp::class.java)
        startActivity(intent)
    }

    private fun navigateToSignUpCompany() {
        finish()
        val intent = Intent(this@MainActivity, CompanySignUp::class.java)
        startActivity(intent)
    }


    //for testing new interfaces

    private fun navigateToCompanyProfile() {
        finish()
        val intent = Intent(this@MainActivity, CompanyProfile::class.java)
        startActivity(intent)
    }

    private fun navigateToUser() {
        finish()
        val intent = Intent(this@MainActivity, UserActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToForgetPassword() {
        finish()
        val intent = Intent(this@MainActivity, UserForgetPassword::class.java)
        startActivity(intent)
    }
    override fun attachBaseContext(newBase: Context?) {
        myPreference = Language(newBase!!)
        val lang = myPreference.getLoginCount()
        super.attachBaseContext(lang?.let { MyContextWrapper.wrap(newBase, it) })
    }

}