package moe.cowan.b.annsearcher.backend;

import java.io.Serializable;

import moe.cowan.b.annsearcher.backend.Ids.Id;

/**
 * Created by user on 04/07/2015.
 */
public class Person implements Serializable {

    private Id id = null;
    private String role = "";
    private Language language = null;
    private String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return getName();
    }

    /*@Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Person))
            return false;
        return this.getId() == ((Person) obj).getId();
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }*/
}
