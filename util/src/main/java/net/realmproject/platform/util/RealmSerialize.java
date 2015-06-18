package net.realmproject.platform.util;


import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.AbstractTransformer;


public class RealmSerialize {

    public static Map<String, Serializable> structToMap(Object o) {
        Object o2 = deserialize(serialize(o));
        if (!(o2 instanceof Map)) o = Collections.singletonMap("value", o);
        return (Map<String, Serializable>) deserialize(serialize(o));
    }

    public static <T> T convertMessage(Object publication, Class<T> clazz) throws IOException {
        String asString = serialize(publication);
        return deserialize(asString, clazz);
    }

    public static Object deserialize(String json) {
        return new JSONDeserializer<Object>().deserialize(json);
    }

    public static <T> T deserialize(String json, Class<T> clazz) throws IOException {
        if (json == null || json.length() == 0) { throw new IOException(); }
        return new JSONDeserializer<T>().deserialize(json, clazz);
    }

    public static String serialize(Object o) {
        return new JSONSerializer().transform(new ExcludeTransformer(), void.class).exclude("*.class")
                .prettyPrint(true).deepSerialize(o);
    }

}

class ExcludeTransformer extends AbstractTransformer {

    @Override
    public Boolean isInline() {
        return true;
    }

    @Override
    public void transform(Object object) {
        // Do nothing, null objects are not serialized.
        return;
    }
}
