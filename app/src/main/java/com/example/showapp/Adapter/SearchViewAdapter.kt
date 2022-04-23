package com.example.showapp.Adapter
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.showapp.R
import com.example.showapp.Activity.UserArticleList

class SearchViewAdapter(var context: Context,var expandableListView : ExpandableListView,var header : MutableList<String>, var body : MutableList<MutableList<String>>) : BaseExpandableListAdapter(){
    override fun getGroup(groupPosition: Int): String {
        return header[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        if(convertView == null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.layout_group,null)
        }
        val image = convertView?.findViewById<ImageView>(R.id.SearchImage)

        val title = convertView?.findViewById<TextView>(R.id.tv_title)
        title?.text = getGroup(groupPosition)
        if(title?.text == "mode"){
            image?.setImageResource(R.drawable.mode)
        }
        if(title?.text == "high tech"){
            image?.setImageResource(R.drawable.hightech)
        }
        if(title?.text == "beauty"){
            image?.setImageResource(R.drawable.beauty)
        }
        if(title?.text == "baby"){
            image?.setImageResource(R.drawable.baby)
        }
        if(title?.text == "Jewellery"){
            image?.setImageResource(R.drawable.jewerly)
        }
        if(title?.text == "art deco"){
            image?.setImageResource(R.drawable.artdeco)
        }
        title?.setOnClickListener {
            if(expandableListView.isGroupExpanded(groupPosition))
                expandableListView.collapseGroup(groupPosition)
            else
                expandableListView.expandGroup(groupPosition)
            Toast.makeText(context, getGroup(groupPosition),Toast.LENGTH_SHORT).show()
        }
        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return body[groupPosition].size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): String {
        return body[groupPosition][childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        if(convertView == null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.layout_child,null)
        }
        val title = convertView?.findViewById<TextView>(R.id.tv_title)
        title?.text = getChild(groupPosition,childPosition)

            title?.setOnClickListener {
                //Toast.makeText(context, getChild(groupPosition,childPosition),Toast.LENGTH_SHORT).show()
                val intent = Intent(context, UserArticleList::class.java)
                intent.putExtra("type", title?.text)
                context.startActivity(intent)
            }

        return convertView
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return header.size
    }
}