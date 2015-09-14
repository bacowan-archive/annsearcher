package moe.cowan.b.annsearcher.backend.database;

import android.content.Context;

import java.util.Collection;
import java.util.Map;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Ids.Id;
import moe.cowan.b.annsearcher.backend.PeopleOfTitle;
import moe.cowan.b.annsearcher.backend.Person;

/**
 * Created by user on 27/08/2015.
 */
public interface InternalDatabaseProxy {
    void close();
    void addAnime(Anime anime);
    Anime getAnime(Id id);
    Map<String, Collection<String>> crossReferenceActor(Person person);
    Collection<String> getAnimeSynonyms(Id id);
    PeopleOfTitle getPeopleOfTitle(Id id);
}
