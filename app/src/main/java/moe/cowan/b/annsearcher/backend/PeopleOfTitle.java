package moe.cowan.b.annsearcher.backend;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by user on 18/07/2015.
 */
public class PeopleOfTitle implements Serializable {

    private Collection<Person> staff = new ArrayList<>();
    private Collection<Person> cast = new ArrayList<>();

    public PeopleOfTitle() {

    }

    public PeopleOfTitle(Collection<Person> staff, Collection<Person> cast) {
        this.staff = staff;
        this.cast = cast;
    }

    public void setStaff(Collection<Person> staff) {
        this.staff = staff;
    }
    public void addStaff(Person staff) {
        this.staff.add(staff);
    }
    public Collection<Person> getStaff() {
        return staff;
    }
    public void setCast(Collection<Person> cast) {
        this.cast = cast;
    }
    public void addCast(Person cast) {
        this.cast.add(cast);
    }
    public Collection<Person> getCast() {
        return cast;
    }


}