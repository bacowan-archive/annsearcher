package moe.cowan.b.annsearcher.backend;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

import moe.cowan.b.annsearcher.backend.Ids.Id;

/**
 * Created by user on 04/07/2015.
 */
public class Anime implements Serializable {

    private String title = "";
    private Id id = null;
    private PeopleOfTitle peopleOfTitle = new PeopleOfTitle();
    private Collection<String> synonyms = new LinkedList<>();
    private WatchingStatus status;

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

    public Collection<String> getSynonyms() {
        return synonyms;
    }

    public void addSynonym(String synonym) {
        this.synonyms.add(synonym);
    }

    public void setSynonyms(Collection<String> synonyms) {
        this.synonyms = synonyms;
    }

    public WatchingStatus getStatus() {
        return status;
    }

    public void setStatus(WatchingStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
