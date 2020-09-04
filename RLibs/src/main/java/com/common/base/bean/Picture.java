package com.common.base.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HSK° on 2018/9/10.
 * --function:附件实体
 */
public class Picture implements Parcelable {
    private String id;
    private String size;
    private String picturename;
    private String state; //是否上传标志 0：未上传    1：已上传
    private String keyid;
    private String type;    //附件类型   0:图片   1：视频
    private String picturePath;
    private long startPos;
    private String islast;   //是否为单个剪切附件的最后一段   0：不是  1：是
    private String ext;  //附件类型例：PNG,amr
    private String isupload;  //是否为需要上传附件  0：需要   1：不需要
    private String info_id;
    private String addr;



    public String getInfo_id() {
        return info_id;
    }
    public void setInfo_id(String info_id) {
        this.info_id = info_id;
    }
    public String getIsupload() {
        return isupload;
    }
    public void setIsupload(String isupload) {
        this.isupload = isupload;
    }
    public String getExt() {
        return ext;
    }
    public void setExt(String ext) {
        this.ext = ext;
    }
    public long getStartPos() {
        return startPos;
    }
    public void setStartPos(long startPos) {
        this.startPos = startPos;
    }
    public String getPicturePath() {
        return picturePath;
    }
    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
    public String getIslast() {
        return islast;
    }
    public void setIslast(String islast) {
        this.islast = islast;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public String getPicturename() {
        return picturename;
    }
    public void setPicturename(String picturename) {
        this.picturename = picturename;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getKeyid() {
        return keyid;
    }
    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getAddr() {
        return addr;
    }
    public void setAddr(String addr) {
        this.addr = addr;
    }
    @Override
    public int describeContents() {
        return 0;
    }


    public static final Creator<Picture> CREATOR = new Creator<Picture>() {
        public Picture createFromParcel(Parcel source) {
            Picture picture = new Picture();
            picture.id = source.readString();
            picture.size = source.readString();
            picture.picturename = source.readString();
            picture.state=source.readString();
            picture.keyid=source.readString();
            picture.type=source.readString();
            picture.picturePath=source.readString();
            picture.startPos = source.readLong();
            picture.islast = source.readString();
            picture.ext = source.readString();
            picture.isupload = source.readString();
            picture.info_id = source.readString();
            picture.addr = source.readString();
            return picture;
        }
        public Picture[] newArray(int size) {
            return new Picture[size];
        }
    };

    @Override
    public void writeToParcel(Parcel arg0, int arg1) {
        arg0.writeString(id);
        arg0.writeString(size);
        arg0.writeString(picturename);
        arg0.writeString(state);
        arg0.writeString(keyid);
        arg0.writeString(type);
        arg0.writeString(picturePath);
        arg0.writeLong(startPos);
        arg0.writeString(islast);
        arg0.writeString(ext);
        arg0.writeString(isupload);
        arg0.writeString(info_id);
        arg0.writeString(addr);
    }


}
