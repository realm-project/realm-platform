package net.realmproject.platform.corc;


import java.util.HashMap;
import java.util.Map;

import net.objectof.corc.Handler;


public class ITopPathRouter extends IPathRouter {

    public ITopPathRouter(Integer discard, Map<String, Handler<Object>> defaults, Map<String, Handler<Object>> overrides) {
        super(discard, merge(defaults, overrides));
    }

    public ITopPathRouter(Map<String, Handler<Object>> defaults, Map<String, Handler<Object>> overrides) {
        super(merge(defaults, overrides));
    }

    private static Map<String, Handler<Object>> merge(Map<String, Handler<Object>> defaults,
            Map<String, Handler<Object>> overrides) {

        Map<String, Handler<Object>> merged = new HashMap<>();
        merged.putAll(defaults);
        merged.putAll(overrides);
        return merged;

    }
}
