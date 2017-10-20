package com.khoantt91.hiqanimationtest.view.adapter

import android.view.View

/**
 * Created by ThienKhoa on 4/25/17.
 */

interface OnAdapterListener<in ModelObject> {
    fun onSelectedItemListener(model: ModelObject, index: Int, view: View? = null)
}
