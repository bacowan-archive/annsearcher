package moe.cowan.b.annsearcher.backend;

import java.io.Serializable;

/**
 * Created by user on 04/07/2015.
 */
public class Id implements Serializable {
    private String key = "";
    public Id(String key) {
        this.key = key;
    }
}
