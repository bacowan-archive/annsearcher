package moe.cowan.b.annsearcher.backend.database;

import android.content.Context;

import java.util.Collection;
import java.util.concurrent.TimeoutException;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.exceptions.TitleNotFoundException;
import moe.cowan.b.annsearcher.backend.Ids.Id;
import moe.cowan.b.annsearcher.backend.PeopleOfTitle;

/**
 * Created by user on 18/07/2015.
 */
public interface DatabaseInfoProxy {
    PeopleOfTitle getPeopleOfTitle(Id animeId) throws TitleNotFoundException;
    void setContext(Context context);
}
