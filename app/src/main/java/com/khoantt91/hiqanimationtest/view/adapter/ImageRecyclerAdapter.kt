package com.khoantt91.hiqanimationtest.view.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.khoantt91.hiqanimationtest.R
import com.khoantt91.hiqanimationtest.helper.Logger
import com.khoantt91.hiqanimationtest.model.Image


/**
 * Created by ThienKhoa on 5/10/17.
 */

class ImageRecyclerAdapter(activity: Activity, listener: OnAdapterListener<Image>?) : BaseRecyclerAdapter<Image>(activity, listener) {

    private val TAG = ImageRecyclerAdapter::class.java.simpleName

    override fun createView(context: Context, viewGroup: ViewGroup, viewType: Int): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return inflater.inflate(R.layout.cell_image, viewGroup, false)
    }

    override fun bindView(image: Image?, position: Int, viewHolder: BaseRecyclerAdapter.BaseViewHolder) {
        if (image == null) return Logger.e(TAG, "Image is null")
        Glide.with(activity).load(image.url).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().placeholder(R.color.colorLightGray).into(viewHolder.itemView as ImageView)

        viewHolder.itemView.setOnClickListener {
            listener?.onSelectedItemListener(image, list.indexOf(image), viewHolder.itemView)
        }
    }
}
