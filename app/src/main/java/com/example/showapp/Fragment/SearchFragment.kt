package com.example.showapp.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ExpandableListView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.showapp.*
import com.example.showapp.Activity.UserArticleDetails
import com.example.showapp.Adapter.SearchViewAdapter
import com.example.showapp.Adapter.UserArticleListAdapter
import com.example.showapp.Adapter.filterAdapter
import com.example.showapp.Api.ApiUser
import com.example.showapp.Model.Article
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_search.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment : Fragment(), UserArticleListAdapter.OnItemClickListener,
    filterAdapter.OnItemClickListener {

    val header: MutableList<String> = ArrayList()
    val body: MutableList<MutableList<String>> = ArrayList()
    var tableauFilterArticle: MutableList<Article> = ArrayList()
    var articlesDispo: MutableList<Article> = ArrayList()
    lateinit var test: filterAdapter.OnItemClickListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        test= this
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val image = view.findViewById<ImageView>(R.id.SearchImage)
        val keyword = view.findViewById<TextInputEditText>(R.id.keyword)
        val expandableListView = view.findViewById<ExpandableListView>(R.id.expandableListView)
        val recycler_viewArticleListt = view.findViewById<RecyclerView>(R.id.recycler_viewArticleListt)
        getNewsData { newss: List<Article> ->
            articlesDispo = newss as MutableList<Article>


            //view.recycler_viewArticleListt.adapter = UserArticleListAdapter(newss, this,view.context)
        }
        keyword.setOnFocusChangeListener(OnFocusChangeListener { view, focused ->
            val keyboard =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (focused) keyboard.showSoftInput(keyword, 0) else keyboard.hideSoftInputFromWindow(
                keyword.getWindowToken(),
                0
            )
        })
        keyword.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.isNotEmpty()) {
                    view.recycler_viewArticleListt.layoutManager = GridLayoutManager(activity,2)
                    tableauFilterArticle.clear()
                    articlesDispo.forEach {
                        if (it.name!!.contains(s)) {
                            tableauFilterArticle.add(it)
                        }
                       // Log.i("articles : ", tableauFilterArticle.toString())

                    }
                    recycler_viewArticleListt.visibility = View.VISIBLE
                    expandableListView.visibility = View.INVISIBLE
                    view.recycler_viewArticleListt.adapter = filterAdapter(tableauFilterArticle,test ,view.context)
                }
                if(s.isEmpty()){
                    //view.recycler_viewArticleListt.adapter = UserArticleListAdapter(newss, this,view.context)
                    view.expandableListView.setAdapter(
                        SearchViewAdapter(
                            requireContext(),
                            view.findViewById(R.id.expandableListView),
                            header,
                            body
                        )
                    )
                    recycler_viewArticleListt.visibility = View.INVISIBLE
                    expandableListView.visibility = View.VISIBLE
                }
            }
        })
        val mode: MutableList<String> = ArrayList()
        mode.add("sweaters")
        mode.add("pants")
        mode.add("outlet")
        mode.add("jacket")
        mode.add("shoes")
        mode.add("Dress")


        val highTech: MutableList<String> = ArrayList()
        highTech.add("Pc")
        highTech.add("Tv")
        highTech.add("Computers' accessories")

        val beauty: MutableList<String> = ArrayList()
        beauty.add("make-up")


        val baby: MutableList<String> = ArrayList()
        baby.add("Baby")


        val Jewellery: MutableList<String> = ArrayList()
        Jewellery.add("earrings")
        Jewellery.add("rings")
        Jewellery.add("bracelets")


        val artDeco: MutableList<String> = ArrayList()
        artDeco.add("art deco")
        artDeco.add("Art Supplies")


        header.add("mode")
        header.add("high tech")
        header.add("beauty")
        header.add("baby")
        header.add("Jewellery")
        header.add("art deco")

        body.add(mode)
        body.add(highTech)
        body.add(beauty)
        body.add(baby)
        body.add(Jewellery)
        body.add(artDeco)

        view.expandableListView.setAdapter(
            SearchViewAdapter(
                requireContext(),
                view.findViewById(R.id.expandableListView),
                header,
                body
            )
        )



        return view
    }

    private fun getNewsData(callback: (List<Article>) -> Unit) {
        val apiInterface = ApiUser.create()

        apiInterface.getAllArticles().enqueue(object: Callback<Article> {
            override fun onResponse(call: Call<Article>, response: Response<Article>) {
                if(response.isSuccessful){
                    return callback(response.body()!!.articles!!)
                    Log.i("yessss", response.body().toString())
                    //}
                } else {
                    Log.i("nooooo", response.body().toString())
                }
            }

            override fun onFailure(call: Call<Article>, t: Throwable) {
                t.printStackTrace()
                println("OnFailure")
            }

        })
    }

    override fun onItemClick(position: Int,articless: List<Article>) {
        val intent = Intent(view?.context, UserArticleDetails::class.java)
        intent.putExtra("articleName",articless[position].name)
        intent.putExtra("articlePrice",articless[position].price)
        intent.putExtra("articlePicture",articless[position].articlePicture)
        intent.putExtra("id",articless[position]._id)
        intent.putExtra("details",articless[position].Details)
        intent.putExtra("type",articless[position].type)
        startActivity(intent)
    }

}