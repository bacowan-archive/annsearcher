package moe.cowan.b.annsearcher.backend.database;

import android.content.Context;

import java.util.Collection;

import moe.cowan.b.annsearcher.backend.Anime;

/**
 * Created by user on 18/07/2015.
 */
public interface DatabaseListProxy {
    Collection<Anime> getAllSeenAnime();
    void setUsername(String username);
    String getUsername();
    void setContext(Context context);
    void resync();
}
