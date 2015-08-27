package moe.cowan.b.annsearcher.frontend.utils.Searchers;

import java.io.Serializable;
import java.util.Collection;

import moe.cowan.b.annsearcher.backend.database.DatabaseProxy;

/**
 * Created by user on 02/08/2015.
 */
public class MalAnimeSearch implements SearchCallback {

    private DatabaseProxy proxy;

    public MalAnimeSearch(DatabaseProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public Collection<? extends Serializable> search(String query) {
        return proxy.searchAnime(query);
    }
}
