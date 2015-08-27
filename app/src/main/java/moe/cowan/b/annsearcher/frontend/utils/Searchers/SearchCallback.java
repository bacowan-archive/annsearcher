package moe.cowan.b.annsearcher.frontend.utils.Searchers;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by user on 02/08/2015.
 */
public interface SearchCallback {

    Collection<? extends Serializable> search(String query);

}
