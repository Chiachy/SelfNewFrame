package com.common.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.common.base.R;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

/**
 * Created by HSK° on 2018/2/1.
 * --function:相片适配器
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private ArrayList<AlbumFile> albumFiles;
    private LayoutInflater inflater;

    private Context mContext;

    private final static int TYPE_ADD = 1;
    private final static int TYPE_PHOTO = 2;
    private final static int MAX = 9;//最多显示照片数

    private OnPhotoListener onPhotoListener;

    public PhotoAdapter(Context mContext, @NonNull ArrayList<AlbumFile> albumFiles, OnPhotoListener onPhotoListener) {
        this.albumFiles = albumFiles;
        this.mContext = mContext;
        this.onPhotoListener = onPhotoListener;
        inflater = LayoutInflater.from(mContext);
    }

    /**
     * 接口回调
     */
    public interface OnPhotoListener {

        void onPhotoAdd(View view);//添加照片

        void onPhotoDelete(View view, int pos);//删除照片

        void onPhotoClick(View view, int pos);//点击已有相片进行预览
    }


    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType) {
            case TYPE_ADD:
                itemView = inflater.inflate(R.layout.item_photo_add, parent, false);
                break;
            case TYPE_PHOTO:
                itemView = inflater.inflate(R.layout.item_photo, parent, false);
                break;
        }
        return new PhotoViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {

        if (getItemViewType(position) == TYPE_PHOTO) {
            String path = albumFiles.get(position).getPath();
            holder.ivPhoto .setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(mContext)
                    .load(path)
                    .thumbnail(0.1f)
                    .into(holder.ivPhoto);
        }

        if (holder.iv_add != null) {
            holder.iv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onPhotoListener != null) {
                        onPhotoListener.onPhotoAdd(view);
                    }
                }
            });
        }

        if (holder.iv_delete != null) {
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onPhotoListener != null) {
                        onPhotoListener.onPhotoDelete(view, position);
                    }
                }
            });
        }

        if (holder.ivPhoto != null) {
            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onPhotoListener != null) {
                        onPhotoListener.onPhotoClick(view, position);
                    }
                }
            });
        }

    }



    public void addData(AlbumFile albumFile){
        albumFiles.add(albumFile);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        int count = albumFiles.size() + 1;
        if (count > MAX) {
            count = MAX;
        }
        return count;
    }

    /**
     * @return 获取相片文件列表（包含 图片路径，日期，经纬度，大小，类型(jpg,png,gif)....）
     * 按需去取
     */

    public ArrayList<AlbumFile> getAlbumFiles() {
        return albumFiles == null ? new ArrayList<AlbumFile>() : albumFiles;
    }

    /**
     *
     * @return  获取图片路径集合
     */
    public ArrayList<String> getPhotoList() {
        ArrayList<String> urlList = new ArrayList<>();
        if (albumFiles.size() > 0) {
            for (int i = 0; i < albumFiles.size(); i++) {
                urlList.add(albumFiles.get(i).getPath());
            }
        }
        return urlList;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == albumFiles.size() && position != MAX) ? TYPE_ADD : TYPE_PHOTO;
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView ivPhoto;
        private View iv_delete;
        private View iv_add;

        PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            iv_add = itemView.findViewById(R.id.add);

            if (iv_delete != null) iv_delete.setVisibility(View.VISIBLE);
        }
    }

}