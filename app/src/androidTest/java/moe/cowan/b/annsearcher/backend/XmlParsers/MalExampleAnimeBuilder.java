package moe.cowan.b.annsearcher.backend.XmlParsers;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Ids.Id;
import moe.cowan.b.annsearcher.backend.Ids.StringIdKey;
import moe.cowan.b.annsearcher.backend.WatchingStatus;

/**
 * Created by user on 01/08/2015.
 */
public class MalExampleAnimeBuilder {

    public static Anime buildAnime() {
        Anime anime = new Anime();
        anime.setTitle("Alien 9");
        anime.setId(buildAnimeId());
        anime.addSynonym("Alien Nine");
        anime.setStatus(WatchingStatus.intToStatus(1));
        return anime;
    }

    private static Id buildAnimeId() {
        Id id = new Id();
        id.addId(StringIdKey.MAL, "1177");
        return id;
    }

}
