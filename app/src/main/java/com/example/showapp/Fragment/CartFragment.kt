package com.example.showapp.Fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.example.showapp.Activity.ShippingAdressActivity
import com.example.showapp.Activity.SignatureActivity
import com.example.showapp.Api.ApiUser
import com.example.showapp.Adapter.FactureViewAdapter
import com.example.showapp.Model.BillModelPDF
import com.example.showapp.Model.Facture
import com.example.showapp.Model.GetFactures
import com.example.showapp.R
import com.example.showapp.Utils.Pdf
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_user_sign_up.*
import kotlinx.android.synthetic.main.fragment_cart.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CartFragment : Fragment() {
    lateinit var mSharedPrefUser: SharedPreferences
    lateinit var refuser: String
    lateinit var animationView: LottieAnimationView
    lateinit var facc: List<Facture>
    var sum: Int = 0
    lateinit var name: String
    lateinit var quantity: String
    lateinit var price: String
    lateinit var Total: String
    lateinit var Position: String
    lateinit var cityName: String
    lateinit var PositionAndCityName: String
    lateinit var Signature: String
    var currentState: Int = 1
    var incomplete: Drawable? = null
    var curent: Drawable? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        mSharedPrefUser =
            this.requireActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        refuser = mSharedPrefUser.getString("UserID", null).toString()
        Position = mSharedPrefUser.getString("position", "nulll").toString()
        cityName = mSharedPrefUser.getString("cityName", null).toString()
        Signature = mSharedPrefUser.getString("pathhh", "nulll").toString()
        PositionAndCityName = cityName + " :" + Position
        Log.i("asbaaa", mSharedPrefUser.getString("pathhh","nulll").toString())


        var totalPriceBagFrag = view.findViewById<TextView>(R.id.totalPriceBagFrag)

        animationView = view.findViewById(R.id.animationViewCartPage)
        val emptyBagMsgLayout: LinearLayout = view.findViewById(R.id.emptyBagMsgLayout)
        animationView.playAnimation()
        animationView.loop(true)
        emptyBagMsgLayout.visibility = View.VISIBLE
        view.recycler_viewCart.layoutManager = LinearLayoutManager(activity)

        view.recycler_viewCart.setHasFixedSize(true)
        getFacData { fac ->
            fac.let {
                if (fac.size == 0) {
                    Log.i("coucouc", "cc")
                    animationView.playAnimation()
                    animationView.loop(true)
                    emptyBagMsgLayout.visibility = View.VISIBLE
                } else {
                    emptyBagMsgLayout.visibility = View.GONE
                    animationView.pauseAnimation()
                    facc = fac
                    view.recycler_viewCart.adapter = context?.let { FactureViewAdapter(fac, it) }
                }
            }
            fac.forEach {
                sum += it.price!!.toInt()
            }
            totalPriceBagFrag.text = sum.toString() + " DT"

        }
        /*view.swipeToRefreshCart.setOnRefreshListener {
            view.recycler_viewCart.layoutManager = LinearLayoutManager(activity)
            getFacData { fac ->
                view.recycler_viewCart.adapter = context?.let { FactureViewAdapter(fac, it) }
            }
            view.swipeToRefreshCart.isRefreshing = false
        }*/
        if(Position!="nulll") {
            currentState = currentState + 1
        }
//        else {
//            incomplete = getResources().getDrawable(R.drawable.ic_baseline_map_24)
//            curent = getResources().getDrawable(R.drawable.ic_signature)
//        }
        if(Signature!="nulll"){
            currentState = currentState + 1
        }
//        else {
//            incomplete = getResources().getDrawable(R.drawable.ic_signature)
//            curent = getResources().getDrawable(R.drawable.ic_baseline_map_24)
//        }
        val statusViewScroller = view.findViewById<params.com.stepview.StatusViewScroller>(R.id.StatusViewScroller)
        statusViewScroller.statusView.run {
            currentCount = currentState
            //circleFillColor = Color.RED
//            if(incomplete!=null && curent!=null){
//                incompleteDrawable = incomplete
//                currentDrawable = curent
//            }
//            if(currentCount==3){
//                incompleteDrawable = null
//                currentDrawable = null
//            }
        }

        val checkout = view.findViewById<Button>(R.id.checkOut_BagPage)
        checkout.setOnClickListener {
            if(Signature=="nulll"){
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Alert")
                    .setMessage("You need to add your signature")
                    .setPositiveButton("Ok") {dialog, which ->
                        navigateToAddSignature()
                    }
                    .show()
            }
            if(Position=="nulll"){
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Alert")
                    .setMessage("You need to add your shippment address")
                    .setPositiveButton("Ok") {dialog, which ->
                        navigateToShippingAddress()
                    }
                    .show()
            }
            if(Position != "nulll" && Signature != "nulll") {
                requestStoragePermission()
            }
        }

        return view
    }


    private fun reportPDF() {
        //Dummy data

        facc.forEach {
            name = it.name.toString()
            Log.i("asbaaaaaaaaaaaaaaaaaaa", name)
            quantity = it.qte.toString()
            price = it.price.toString() + " TND"
            Total = sum.toString() + " TND"
        }

        val modelBill = BillModelPDF(
            name,
            quantity,
            price,
            Total,
            PositionAndCityName,

        )
        val pdf = Pdf(modelBill, facc, requireContext())

        pdf.savePDF()

        Toast.makeText(requireContext(), "Bill added successfully", Toast.LENGTH_SHORT).show()

    }

    private fun requestStoragePermission() {
        Dexter.withActivity(requireActivity())
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) { // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        reportPDF()
                    }
                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) { // show alert dialog navigating to Settings

                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).withErrorListener {
                Toast.makeText(requireContext(), "Error occurred! ", Toast.LENGTH_SHORT)
                    .show()
            }
            .onSameThread()
            .check()
    }

    private fun getFacData(callback: (List<Facture>) -> Unit) {
        val apiInterface = ApiUser.create()
        apiInterface.getFac(refuser).enqueue(object : Callback<GetFactures> {
            override fun onResponse(call: Call<GetFactures>, response: Response<GetFactures>) {
                if (response.isSuccessful) {
                    return callback(response.body()!!.factures)
                    //}
                } else {
                    Log.i("nooooo", response.body().toString())
                }
            }

            override fun onFailure(call: Call<GetFactures>, t: Throwable) {
                t.printStackTrace()
                println("OnFailure")
            }

        })
    }


    private fun navigateToShippingAddress() {
        val intent = Intent(activity, ShippingAdressActivity::class.java)
        requireActivity().startActivity(intent)
    }
    private fun navigateToAddSignature() {
        val intent = Intent(activity, SignatureActivity::class.java)
        requireActivity().startActivity(intent)
    }

}