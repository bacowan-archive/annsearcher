package moe.cowan.b.annsearcher.backend.database;

import java.util.Collection;

import moe.cowan.b.annsearcher.backend.Anime;

/**
 * Created by user on 02/08/2015.
 */
public interface DatabaseSearchProxy {

    Collection<Anime> searchAnime(String searchString);
    void setUsername(String username);
    void setPassword(String password);

}
