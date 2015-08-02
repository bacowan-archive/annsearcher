package moe.cowan.b.annsearcher.backend;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 01/08/2015.
 */
public enum WatchingStatus {
    WATCHING(1),
    COMPLETED(2),
    ON_HOLD(3),
    DROPPED(4),
    PLAN_TO_WATCH(6);

    private static Map<Integer, WatchingStatus> intValMap = setUpWatchingStatus();
    private static Map<Integer, WatchingStatus> setUpWatchingStatus() {
        Map<Integer, WatchingStatus> statuses = new HashMap<>();
        for (WatchingStatus status : values())
            statuses.put(status.getStatusInt(), status);
        return statuses;
    }

    private Integer status;

    WatchingStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatusInt() {
        return status;
    }

    public static WatchingStatus intToStatus(int val) {
        return intValMap.get(val);
    }
}
