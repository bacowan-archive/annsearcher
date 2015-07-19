package moe.cowan.b.annsearcher.presenter;

import android.app.Application;

import java.util.List;

import moe.cowan.b.annsearcher.backend.Id;
import moe.cowan.b.annsearcher.backend.Person;
import moe.cowan.b.annsearcher.backend.database.DatabaseProxy;
import moe.cowan.b.annsearcher.frontend.peopleFilters.JapanesePeopleFilter;
import moe.cowan.b.annsearcher.frontend.peopleFilters.PeopleFilter;


/**
 * Created by user on 07/03/2015.
 */
public class CharacterSearchPresenter {
    private DatabaseProxy proxy;
    private PeopleFilter peopleFilter;

    public CharacterSearchPresenter(DatabaseProxy proxy, Application app) {
        this.proxy = proxy;
        this.proxy.setContext(app);
        setPersonsFilter();
    }

    private void setPersonsFilter() {
        peopleFilter = new JapanesePeopleFilter();
    }

    public List<Person> getAllCastOfAnime(Id animeId) {
        List<Person> allPeopleOfTitle = proxy.getPeopleOfTitle(animeId).getCast();
        peopleFilter.filter(allPeopleOfTitle);
        return allPeopleOfTitle;
    }
}