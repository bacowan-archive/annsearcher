package moe.cowan.b.annsearcher.backend.database;

import android.provider.ContactsContract;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Ids.StringIdGetter;
import moe.cowan.b.annsearcher.backend.Ids.StringIdKey;
import moe.cowan.b.annsearcher.backend.Person;

/**
 * Created by user on 25/07/2015.
 */
public class AnimeCrossreferencer {

    private StringIdGetter idGetter = new StringIdGetter(StringIdKey.INTERNAL);

    public Map<Anime, Collection<String>> getOtherCharactersActedBy(Collection<Anime> otherAnime, Person person) {
        Map<Anime, Collection<String>> result = new HashMap<>();
        for (Anime anime : otherAnime) {
            Collection<Person> fullCast = anime.getPeopleOfTitle().getCast();
            Collection<String> matchedCharacters = new LinkedList<>();
            for (Person singleCast : fullCast) {
                if (idGetter.getStringId(singleCast.getId()).equals(idGetter.getStringId(person.getId())))
                    matchedCharacters.add(singleCast.getRole());
            }
            if (matchedCharacters.size() > 0)
                result.put(anime, matchedCharacters);
        }
        return result;
    }

}
