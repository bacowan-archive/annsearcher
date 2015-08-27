package moe.cowan.b.annsearcher.backend.XmlParsers;

import java.util.ArrayList;
import java.util.Collection;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Ids.Id;
import moe.cowan.b.annsearcher.backend.Ids.StringIdKey;
import moe.cowan.b.annsearcher.backend.WatchingStatus;

/**
 * Created by user on 01/08/2015.
 */
public class MalExampleAnimeBuilder {

    public static Anime buildAnimeFromListExample() {
        Anime anime = new Anime();
        anime.setTitle("Alien 9");
        anime.setId(buildAnimeId("1177"));
        anime.addSynonym("Alien Nine");
        anime.setStatus(WatchingStatus.intToStatus(1));
        return anime;
    }

    public static Anime buildAnimeFromAnimeExample() {
        Anime anime = new Anime();
        anime.setTitle("Bleach");
        anime.setId(buildAnimeId("269"));
        Collection<String> synonyms = new ArrayList<>();
        synonyms.add("A synonym");
        anime.setSynonyms(synonyms);
        return anime;
    }

    private static Id buildAnimeId(String stringId) {
        Id id = new Id();
        id.addId(StringIdKey.MAL, stringId);
        return id;
    }

}
