package com.tqs.filecommander.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tqs.filecommander.R
import com.tqs.filecommander.model.FileEntity
import com.tqs.filecommander.utils.DateUtils
import com.tqs.filecommander.utils.FileUtils
import com.tqs.filecommander.utils.VideoUtils
import java.io.File

class ImageVideoListAdapter(
    private val context: Context,
    private var data: List<FileEntity>,
    private var onItemClick: (position: Int, touchView: String) -> Unit,
    private var onItemLongClick: (position: Int) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TITLE = 0
    private val CONTENT = 1

    val CLICKSTATE: Int = 11
    val LONGSTATE: Int = 12

    // 1 click 2 Long click
    var touchState: Int = CLICKSTATE

    val TOUCHTITLEVIEW = "touchTitleView"
    val TOUCHSELECTVIEW = "touchSelectView"
    val TOUCHRADIOVIEW = "touchRadioView"
    val TOUCHIMAGEVIEW = "touchImageView"
    val TOUCHPLAYVIEW = "touchPlayView"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            TITLE -> {
//                val binding: MediaPreviewBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.media_item_title, null, false)
                TitleViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.media_item_title, parent, false)
                )
            }

            CONTENT -> {
                ContentViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.media_item_photo, parent, false)
                )
            }

            else -> {
                ContentViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.media_item_photo, parent, false)
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TITLE -> {
                if (holder is TitleViewHolder) {
                    holder.titleDate.text = DateUtils.getMonthTimeBySecond(data[position].date)

                    holder.titleDate.setOnClickListener {
                        onItemClick(position, TOUCHTITLEVIEW)
                    }
                }
            }

            CONTENT -> {
                if (holder is ContentViewHolder) {
                    when (data[position].fileType) {
                        FileUtils.TYPEIMAGE -> {
                            holder.contentImage.setImageURI(Uri.fromFile(data[position].path?.let {
                                File(it)
                            }))
                            holder.itemPlay.visibility = View.GONE
                        }

                        FileUtils.TYPEVIDEO -> {
                            holder.contentImage.setImageBitmap(data[position].path?.let {
                                VideoUtils.getBitmap(it)
                            })
                            holder.itemPlay.visibility = View.VISIBLE
                        }
                    }
                    holder.imageSize.text = FileUtils.getTwoDigitsSpace(data[position].size)
                    if (touchState == LONGSTATE) {
                        holder.selectedImage.visibility = View.VISIBLE
                    } else {
                        holder.selectedImage.visibility = View.GONE
                    }
                    holder.selectedImage.isChecked = data[position].selected
                    holder.contentImage.setOnClickListener {
                        onItemClick(position, TOUCHIMAGEVIEW)
                    }
                    holder.contentImage.setOnLongClickListener {
                        onItemLongClick(position)
                        true
                    }

                    holder.selectedImage.setOnClickListener {
                        onItemClick(position, TOUCHRADIOVIEW)
                    }

                    holder.itemPlay.setOnClickListener {
                        onItemClick(position, TOUCHPLAYVIEW)
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].isTitle) {
            TITLE
        } else {
            CONTENT
        }
    }


    inner class TitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var titleDate: TextView = view.findViewById<TextView>(R.id.item_title)

    }

    inner class ContentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var contentImage: ImageView = view.findViewById<ImageView>(R.id.item_image)
        var selectedImage: CheckBox = view.findViewById<CheckBox>(R.id.item_selected)
        var itemPlay: ImageView = view.findViewById<ImageView>(R.id.item_play)
        var imageSize: TextView = view.findViewById<TextView>(R.id.item_size)
    }

    fun setData(list: ArrayList<FileEntity>) {
        this.data = list
    }

}