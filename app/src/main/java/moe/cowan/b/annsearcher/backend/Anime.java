package moe.cowan.b.annsearcher.backend;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import moe.cowan.b.annsearcher.backend.Ids.Id;

/**
 * Created by user on 04/07/2015.
 */
public class Anime implements Serializable {

    private String title = "";
    private Id id = null;
    private PeopleOfTitle peopleOfTitle = new PeopleOfTitle();
    private Collection<String> synonyms = new HashSet<>();
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

    public void addSynonym(Collection<String> synonyms) {
        this.synonyms.addAll(synonyms);
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

    public void setAllValues(Anime otherAnime) {
        setId(otherAnime.getId());
        setPeopleOfTitle(otherAnime.getPeopleOfTitle());
        this.synonyms.addAll(otherAnime.getSynonyms());
        setStatus(otherAnime.getStatus());
        setTitle(otherAnime.getTitle());
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
