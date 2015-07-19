package moe.cowan.b.annsearcher.backend;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 04/07/2015.
 */
public class Person implements Parcelable {

    public Id getId() {
        return null;
    }
    public String getRole() {
        return null;
    }
    public Language getLanguage() { return null; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator<Person> CREATOR
            = new Parcelable.Creator<Person>() {
        public Person createFromParcel(Parcel in) {
            return null;
        }

        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}
