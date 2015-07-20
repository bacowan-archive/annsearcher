package moe.cowan.b.annsearcher.frontend.utils.StringSelectors;

import java.io.Serializable;

import moe.cowan.b.annsearcher.exceptions.WrongStringSelectorException;

/**
 * Created by user on 19/07/2015.
 */
public abstract class SearchItemStringSelector {

    private String EXCEPTION_MESSAGE = "This string selector cannot be used with the given object.";

    public String getString(Serializable s) {
        try {
            return concreteGetString(s);
        } catch (ClassCastException e) {
            throw new WrongStringSelectorException(EXCEPTION_MESSAGE + e);
        }
    }

    protected abstract String concreteGetString(Serializable s);
}
