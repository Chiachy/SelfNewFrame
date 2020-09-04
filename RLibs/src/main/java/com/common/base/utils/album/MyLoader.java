package com.common.base.utils.album;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yanzhenjie.album.AlbumFile;

/**
 * Created by HSK° on 2018/9/11.
 * --function: 相册装载器
 */
public class MyLoader implements com.yanzhenjie.album.AlbumLoader {
    @Override
    public void load(ImageView imageView, AlbumFile albumFile) {
        load(imageView, albumFile.getPath());
    }

    @Override
    public void load(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }
}
