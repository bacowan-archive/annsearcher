package moe.cowan.b.annsearcher.backend;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by user on 04/07/2015.
 */
public class Anime implements Serializable {

    private String title = "";
    private Id id = null;

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
    public void setId(Id id) {
        this.id = id;
    }
    public Id getId() {
        return id;
    }

    @Override
    public String toString() {
        return getTitle();
    }
/*
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator<Anime> CREATOR
            = new Parcelable.Creator<Anime>() {
        public Anime createFromParcel(Parcel in) {
            return new Anime();
        }

        public Anime[] newArray(int size) {
            return new Anime[size];
        }
    };
    */
}
