package moe.cowan.b.annsearcher.backend;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 04/07/2015.
 */
public class Anime implements Parcelable {

    public String getTitle() {
        return "";
    }
    public Id getId() {
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
