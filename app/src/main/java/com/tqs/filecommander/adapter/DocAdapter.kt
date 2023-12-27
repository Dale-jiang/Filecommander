package com.tqs.filecommander.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.tqs.filecommander.R
import com.tqs.filecommander.model.DocumentEntity
import com.tqs.filecommander.utils.FileUtils

class DocAdapter(
    private val context: Context,
    private var data: ArrayList<DocumentEntity>
) : BaseAdapter() {
    private var selecting: Boolean = false
    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder? = null
        val view: View
        if (convertView == null) {
            view = View.inflate(context, R.layout.media_item_doc, null)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }
        holder.title.text = data[position].suffix
        holder.date.text = data[position].number.toString()
        when (data[position].typeFile) {
            FileUtils.TYPEIMAGE -> {
                holder.icon.setImageResource(R.mipmap.ic_image_icon)
            }

            FileUtils.TYPEVIDEO -> {
                holder.icon.setImageResource(R.mipmap.ic_video_icon)
            }

            FileUtils.TYPEAUDIO -> {
                holder.icon.setImageResource(R.mipmap.ic_audio_icon)
            }

            FileUtils.TYPEDOCUMENT -> {
                holder.icon.setImageResource(R.mipmap.ic_dir_icon)
            }

            FileUtils.TYPEDOWNLOAD -> {
                holder.icon.setImageResource(R.mipmap.ic_rubblish_icon)
            }
        }
        holder.select.isChecked = data[position].selected
        if (selecting) {
            holder.select.visibility = View.VISIBLE
        } else {
            holder.select.visibility = View.GONE
        }
        return view
    }

    fun setData(data: ArrayList<DocumentEntity>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun setSelected(select: Boolean) {
        this.selecting = select
        notifyDataSetChanged()
    }

    fun getSelected(): Boolean {
        return this.selecting
    }

    inner class ViewHolder(view: View) {
        var icon: ImageView = view.findViewById<ImageView>(R.id.item_file_icon)
        var title: TextView = view.findViewById<TextView>(R.id.item_file_title)
        var date: TextView = view.findViewById<TextView>(R.id.item_file_date)
        var select: CheckBox = view.findViewById<CheckBox>(R.id.item_file_select)
    }
}