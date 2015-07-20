package moe.cowan.b.annsearcher.frontend.utils.peopleFilters;

import java.util.List;

import moe.cowan.b.annsearcher.backend.Person;

/**
 * Created by user on 21/06/2015.
 */
public abstract class PeopleFilter {

    private PeopleFilter _next;

    public void filter(List<Person> persons) {
        filterImplementation(persons);
        if (_next != null)
            _next.filter(persons);
    }

    public void setNextFilter( PeopleFilter next ) {
        _next = next;
    }

    protected abstract void filterImplementation(List<Person> persons);

}