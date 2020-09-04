package com.common.base.utils.album;

/**
 * Created by HSK° on 2018/9/10.
 * --function:
 */

import android.content.Context;

import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

/**
 * Created by HSK° on 2018/3/8.
 * --function: 拍照选择相册 ,视频 工具类
 */

public class AlbumUtils {
    /**
     * 添加图片(从相册选择)
     */
    public static void addPhoto(Context context, ArrayList<AlbumFile> albumFileList
            , Action<ArrayList<AlbumFile>> action) {
        Album.image(context)
                .multipleChoice() //多选
                .camera(true)   //是否显示相片
                .columnCount(3) //相册中每行几张图片
                .selectCount(9)//最多可选择几张照片
                .checkedList(albumFileList)
                .onResult(action)
                .start();
    }

    /**
     * 点击图片后进入轮播页面
     */
    public static void photoClick(Context context, ArrayList<AlbumFile> albumFileList
            , int pos) {
        Album.galleryAlbum(context)
                .checkable(false)
                .checkedList(albumFileList)
                .currentPosition(pos)
//                .onResult(action)
                .start();
    }

    /**
     * 拍摄照片
     */
    public static void takePhoto(Context context, Action<String> actionSuccess) {

        Album.camera(context)
                .image()
//                .filePath()
                .onResult(actionSuccess)
                .start();

    }



}