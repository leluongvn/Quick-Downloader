package com.allvideodownloader.quickdownloader2020.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;


public class WhatsappStatusModel implements Parcelable {
    private String name;
    private Uri uri;
    private String path;
    private String filename;
    public WhatsappStatusModel(String name, Uri uri, String path, String filename ) {
        this.name = name;
        this.uri = uri;
        this.path = path;
        this.filename = filename;
    }

    protected WhatsappStatusModel(Parcel in) {
        name = in.readString();
        uri = in.readParcelable(Uri.class.getClassLoader());
        path = in.readString();
        filename = in.readString();
    }

    public static final Creator<WhatsappStatusModel> CREATOR = new Creator<WhatsappStatusModel>() {
        @Override
        public WhatsappStatusModel createFromParcel(Parcel in) {
            return new WhatsappStatusModel(in);
        }

        @Override
        public WhatsappStatusModel[] newArray(int size) {
            return new WhatsappStatusModel[size];
        }
    };

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeParcelable(uri, i);
        parcel.writeString(path);
        parcel.writeString(filename);
    }
}
