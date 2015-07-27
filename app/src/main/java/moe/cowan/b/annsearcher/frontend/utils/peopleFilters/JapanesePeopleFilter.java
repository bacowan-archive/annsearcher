package moe.cowan.b.annsearcher.frontend.utils.peopleFilters;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import moe.cowan.b.annsearcher.backend.Language;
import moe.cowan.b.annsearcher.backend.Person;

/**
 * Created by user on 18/07/2015.
 */
public class JapanesePeopleFilter extends PeopleFilter {
    @Override
    protected void filterImplementation(Collection<Person> persons) {
        Iterator<Person> it = persons.iterator();
        while( it.hasNext() ) {
            Person p = it.next();
            if (p.getLanguage() != Language.JAPANESE)
                it.remove();
        }
    }
}