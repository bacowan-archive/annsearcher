package moe.cowan.b.annsearcher.frontend.utils.StringSelectors;

import java.io.Serializable;

import moe.cowan.b.annsearcher.backend.Anime;

/**
 * Created by user on 19/07/2015.
 */
public class AnimeTitleStringSelector extends SearchItemStringSelector {
    @Override
    protected String concreteGetString(Serializable s) {
        return ((Anime) s).getTitle();
    }
}
