package moe.cowan.b.annsearcher.presenter;

import android.app.Application;

import java.util.Collection;
import java.util.List;

import moe.cowan.b.annsearcher.backend.Ids.Id;
import moe.cowan.b.annsearcher.backend.Person;
import moe.cowan.b.annsearcher.backend.database.DatabaseProxy;
import moe.cowan.b.annsearcher.frontend.utils.peopleFilters.JapanesePeopleFilter;
import moe.cowan.b.annsearcher.frontend.utils.peopleFilters.PeopleFilter;


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

    public Collection<Person> getAllCastOfAnime(Id animeId) {
        Collection<Person> allPeopleOfTitle = proxy.getPeopleOfTitle(animeId).getCast();
        peopleFilter.filter(allPeopleOfTitle);
        return allPeopleOfTitle;
    }
}