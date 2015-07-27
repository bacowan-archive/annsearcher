package moe.cowan.b.annsearcher.backend.database;

import moe.cowan.b.annsearcher.exceptions.TitleNotFoundException;
import moe.cowan.b.annsearcher.backend.Ids.Id;
import moe.cowan.b.annsearcher.backend.PeopleOfTitle;

/**
 * Created by user on 18/07/2015.
 */
public interface DatabaseInfoProxy {
    PeopleOfTitle getPeopleOfTitle(Id animeId) throws TitleNotFoundException;
}
