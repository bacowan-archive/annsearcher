package moe.cowan.b.annsearcher.backend;

import java.io.Serializable;

import moe.cowan.b.annsearcher.backend.Ids.Id;

/**
 * Created by user on 04/07/2015.
 */
public class Anime implements Serializable {

    private String title = "";
    private Id id = null;
    private PeopleOfTitle peopleOfTitle = new PeopleOfTitle();

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
    public PeopleOfTitle getPeopleOfTitle() {
        return peopleOfTitle;
    }
    public void setPeopleOfTitle(PeopleOfTitle peopleOfTitle) {
        this.peopleOfTitle = peopleOfTitle;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
