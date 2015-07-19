package moe.cowan.b.annsearcher.backend;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 18/07/2015.
 */
public class PeopleOfTitle implements Parcelable {

    /**
     * The staff of the title
     */
    private List<Person> _staff = new ArrayList<>();
    /**
     * The cast of the title
     */
    private List<Person> _cast = new ArrayList<>();

    public void setStaff(List<Person> staff) {
        _staff = staff;
    }

    public List<Person> getStaff() {
        return _staff;
    }

    public void setCast(List<Person> cast) {
        _cast = cast;
    }

    public List<Person> getCast() {
        return _cast;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(_staff);
        dest.writeList(_cast);
    }

    private void getFromParcel(Parcel in) {
        _staff = in.readArrayList(null);
        _cast = in.readArrayList(null);
    }

    public static final Parcelable.Creator<PeopleOfTitle> CREATOR
            = new Parcelable.Creator<PeopleOfTitle>() {
        public PeopleOfTitle createFromParcel(Parcel in) {
            PeopleOfTitle people = new PeopleOfTitle();
            people.getFromParcel(in);
            return people;
        }

        public PeopleOfTitle[] newArray(int size) {
            return new PeopleOfTitle[size];
        }
    };

}