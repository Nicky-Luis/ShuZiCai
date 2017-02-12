package com.jiangtao.shuzicai.common.event_message;

import java.util.List;

/**
 * Created by Nicky on 2017/1/23.
 *
 * @def 选取相册照片
 */

public class SelectGalleryPhotoMsg {
    private List<String> photoList;

    public SelectGalleryPhotoMsg(List<String> photoList) {
        this.photoList = photoList;
    }

    public List<String> getPhotoList() {
        return photoList;
    }
}
