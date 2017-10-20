package com.khoantt91.hiqanimationtest.model

/**
 * Created by ThienKhoa on 10/20/17.
 */

class Image() {

    var id: String = ""
    var url: String = ""
    var width = 0
    var height = 0
    var spanSize = 0

    constructor(url: String) : this() {
        this.url = url
    }

    override fun toString(): String {
        return "Image(id='$id', url='$url', width=$width, height=$height)"
    }

}