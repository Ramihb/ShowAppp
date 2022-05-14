package com.example.showapp.Fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.showapp.Api.ApiUser
import com.example.showapp.Activity.MainActivity
import com.example.showapp.Model.User
import com.example.showapp.R
import com.example.showapp.Utils.Language
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import android.widget.ArrayAdapter
import com.example.showapp.Activity.QrCodeScannerActivity
import com.example.showapp.Activity.ShippingAdressActivity
import com.example.showapp.Activity.SignatureActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    lateinit var mSharedPref: SharedPreferences
    lateinit var image: ImageView
    private var selectedImageUri: Uri? = null
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var myLanguage: Language

    val languageList = arrayOf("en", "ar", "fr")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val QrCodeScanner = view.findViewById<TextView>(R.id.QrCodeScanner)
        QrCodeScanner.setOnClickListener {
            navigateToQrCodeScanner()
        }
        //testing multi languages
        myLanguage = Language(requireContext())
        val languageBtn = view.findViewById<TextView>(R.id.Language)

        languageBtn.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(
                requireContext(), R.style.BottomSheetDialogTheme
            )
            val bottomSheetView = LayoutInflater.from(requireContext()).inflate(
                R.layout.layout_language_bottom_sheet,
                view.findViewById<LinearLayout>(R.id.bottomSheettt)
            )
            val spinner = bottomSheetView.findViewById<Spinner>(R.id.spinner)
            spinner.adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, languageList)

            val lang = myLanguage.getLoginCount()
            val index = languageList.indexOf(lang)
            if (index >= 0) {
                spinner.setSelection(index)
            }
            bottomSheetView.findViewById<View>(R.id.choseLanguage).setOnClickListener {
                myLanguage.setLoginCount(languageList[spinner.selectedItemPosition])
                bottomSheetDialog.dismiss()
                navigateToLogin()
            }
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
        }


        //end testing multi languages
        mSharedPref = this.requireActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        val profileImage = view.findViewById<ImageView>(R.id.profileImage)
        val refuser = mSharedPref.getString("UserID", null)
        Log.i("keys", mSharedPref.all.toString())
        val userPicture = mSharedPref.getString("UserPicture", null)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(view.context, gso);
        Log.d("profilePic est : ", profileImage.toString())
        Glide.with(this).load(Uri.parse(userPicture)).into(profileImage)

        val name = view.findViewById<TextView>(R.id.userName)
        name.text =
            mSharedPref.getString("firstName", null) + " " + mSharedPref.getString("lastName", null)
        val email = view.findViewById<TextView>(R.id.userEmail)
        email.text = mSharedPref.getString("email", null)

        val EditProfileBtn = view.findViewById<Button>(R.id.editProfileBtn)

        EditProfileBtn.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(requireContext(), EditProfileBtn)
            popupMenu.menuInflater.inflate(R.menu.edit_profile, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.EditMail -> {
                        val bottomSheetDialog = context?.let { it1 ->
                            BottomSheetDialog(
                                it1, R.style.BottomSheetDialogTheme
                            )
                        }
                        val bottomSheetView = LayoutInflater.from(context).inflate(
                            R.layout.layout_edit_email_bottom_sheet,
                            view.findViewById<LinearLayout>(R.id.EditMailBottomSheet)
                        )
                        bottomSheetView.findViewById<View>(R.id.EditEmail).setOnClickListener {
                            val emailNew = bottomSheetView.findViewById<View>(R.id.NewEmail)
                            val data: LinkedHashMap<String, RequestBody> = LinkedHashMap()
                            data["email"] =
                                RequestBody.create(MultipartBody.FORM, emailNew.toString())
                            val apiuser = ApiUser.create()
                            apiuser.editMail(refuser, data).enqueue(object : Callback<User> {
                                override fun onResponse(
                                    call: Call<User>,
                                    response: Response<User>
                                ) {
                                    if (response.isSuccessful) {
                                        println(response.body().toString())
                                        Toast.makeText(
                                            requireContext(),
                                            "email updated",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        println(response.body().toString())
                                    }
                                }

                                override fun onFailure(call: Call<User>, t: Throwable) {
                                    TODO("Not yet implemented")
                                }
                            })
                            bottomSheetDialog!!.dismiss()
                        }

                        bottomSheetDialog!!.setContentView(bottomSheetView)
                        bottomSheetDialog.show()
                    }
                    R.id.EditNumber -> {
                        val bottomSheetDialog = context?.let { it1 ->
                            BottomSheetDialog(
                                it1, R.style.BottomSheetDialogTheme
                            )
                        }
                        val bottomSheetView = LayoutInflater.from(context).inflate(
                            R.layout.layout_edit_number_bottom_sheet,
                            view.findViewById<LinearLayout>(R.id.EditNumberBottomSheet)
                        )
                        bottomSheetView.findViewById<View>(R.id.EditNumber).setOnClickListener {
                            val numberNew = bottomSheetView.findViewById<TextView>(R.id.NewNumber)
                            val data: LinkedHashMap<String, RequestBody> = LinkedHashMap()
                            data["phoneNumber"] =
                                RequestBody.create(MultipartBody.FORM, numberNew.text.toString())
                            Log.i("new number:", numberNew.text.toString())
                            val apiuser = ApiUser.create()
                            apiuser.editMail(refuser, data).enqueue(object : Callback<User> {
                                override fun onResponse(
                                    call: Call<User>,
                                    response: Response<User>
                                ) {
                                    if (response.isSuccessful) {
                                        println(response.body().toString())
                                        Toast.makeText(
                                            requireContext(),
                                            "phone number updated",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        println(response.body().toString())
                                    }
                                }

                                override fun onFailure(call: Call<User>, t: Throwable) {
                                    Log.i("error: ", "error")
                                }
                            })
                            bottomSheetDialog!!.dismiss()
                        }

                        bottomSheetDialog!!.setContentView(bottomSheetView)
                        bottomSheetDialog.show()
                    }
                    R.id.EditPicture -> {
                        val bottomSheetDialog = context?.let { it1 ->
                            BottomSheetDialog(
                                it1, R.style.BottomSheetDialogTheme
                            )
                        }
                        val bottomSheetView = LayoutInflater.from(context).inflate(
                            R.layout.layout_edit_profile_picture,
                            view.findViewById<LinearLayout>(R.id.EditPictureBottomSheet)
                        )
                        image = bottomSheetView.findViewById(R.id.UserImagee)
                        val addImage = bottomSheetView.findViewById<Button>(R.id.AddImagee)
                        addImage.setOnClickListener {
                            Intent(Intent.ACTION_PICK).also {
                                it.type = "image/*"
                                val mimeTypes = arrayOf("image/jpeg", "image/png")
                                it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                                startActivityForResult(it, 123)
                            }
                        }



                        bottomSheetView.findViewById<View>(R.id.EditPicture).setOnClickListener {
                            val stream =
                                activity?.contentResolver?.openInputStream(selectedImageUri!!)
                            val request =
                                stream?.let {
                                    RequestBody.create(
                                        "image/*".toMediaTypeOrNull(),
                                        it.readBytes()
                                    )
                                } // read all bytes using kotlin extension
                            val newPicture = request?.let {
                                MultipartBody.Part.createFormData(
                                    "profilePicture",
                                    "image.jpeg",
                                    it
                                )
                            }
                            val apiuser = ApiUser.create()
                            if (newPicture != null) {
                                apiuser.editPicture(refuser, newPicture)
                                    .enqueue(object : Callback<User> {
                                        override fun onResponse(
                                            call: Call<User>,
                                            response: Response<User>
                                        ) {
                                            if (response.isSuccessful) {
                                                mSharedPref.edit().apply {
                                                    putString(
                                                        "UserPicture",
                                                        response.body()?.user?.profilePicture.toString()
                                                    )
                                                }.apply()
                                                println(response.body().toString())
                                                Toast.makeText(
                                                    requireContext(),
                                                    "profile picture updated",
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                            } else {
                                                println(response.body().toString())
                                            }
                                        }

                                        override fun onFailure(call: Call<User>, t: Throwable) {
                                            Log.i("error: ", "error")
                                        }
                                    })
                            }
                            bottomSheetDialog!!.dismiss()

                        }

                        bottomSheetDialog!!.setContentView(bottomSheetView)
                        bottomSheetDialog.show()
                    }
                }
                true
            })
            popupMenu.show()
        }

        //shipping address
        val shippingAdressBtn = view.findViewById<TextView>(R.id.ShippingAddress)
        shippingAdressBtn.setOnClickListener {
            navigateToShippingAddress()
        }

        val AddSignature = view.findViewById<TextView>(R.id.AddSignature)
        AddSignature.setOnClickListener {
            navigateToAddSignature()
        }


        val logOutBtn = view.findViewById<Button>(R.id.LogOutUser)
        logOutBtn.setOnClickListener {
            showAlertDialog()
//            mSharedPref.edit().clear().apply()
//            mGoogleSignInClient.signOut()
//            requireActivity().finish()
//            navigateToLogin()

        }

        val FinishedOrders = view.findViewById<TextView>(R.id.FinishedOrders)
        FinishedOrders.setOnClickListener {
            navigateToFinishedOrders()
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 123) {
            selectedImageUri = data?.data
            image.setImageURI(selectedImageUri)
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(activity, MainActivity::class.java)
        requireActivity().startActivity(intent)
    }
    private fun navigateToShippingAddress() {
        val intent = Intent(activity, ShippingAdressActivity::class.java)
        requireActivity().startActivity(intent)
    }
    private fun navigateToFinishedOrders() {
        val intent = Intent(activity, com.example.showapp.Activity.FinishedOrders::class.java)
        requireActivity().startActivity(intent)
    }

    private fun navigateToAddSignature() {
        val intent = Intent(activity, SignatureActivity::class.java)
        requireActivity().startActivity(intent)
    }
    private fun navigateToQrCodeScanner() {
        val intent = Intent(activity, QrCodeScannerActivity::class.java)
        requireActivity().startActivity(intent)
    }

    private fun showAlertDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Logout")
            .setMessage("are you sure you want to logout?")
            .setPositiveButton("Ok") {dialog, which ->
                mSharedPref.edit().clear().apply()
                mGoogleSignInClient.signOut().addOnCompleteListener {
                    println("sign out google : " +it.result)
                }
                requireActivity().finish()
                navigateToLogin()
            }
            .setNegativeButton("Cancel") {dialog, which ->

            }
            .show()
    }

}