
package com.rxkj.wananpovertyaid.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.rxkj.wananpovertyaid.R;

import java.io.File;

/**
 * @user lqm
 * @desc 图片加载管理
 */
public class ImageLoaderManager {
    public static void loadImage(Context context, String imgUrl, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher)
                .dontAnimate()
                .error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .load(imgUrl)
                .apply(options)
                .into(imageView);
    }

    public static void loadCirleImage(Context context, String imgUrl, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher_round)
                .dontAnimate()
                .error(R.mipmap.ic_launcher_round)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .diskCacheStrategy(DiskCacheStrategy.NONE);

        Glide.with(context)
                .load(imgUrl)
                .apply(options)
                .into(imageView);
    }

    /**
     * 缓存图片到本地
     */
    public static File cacheImageFile(Context context, String imgUrl) {
        File cacheFile = null;
        FutureTarget<File> future = Glide.with(context)
                .load(imgUrl)
                .downloadOnly(500, 500);
        try {
            cacheFile = future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cacheFile;
    }

}

