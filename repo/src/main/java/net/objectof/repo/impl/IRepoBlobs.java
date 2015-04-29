package net.objectof.repo.impl;


import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;


public abstract class IRepoBlobs {

    private final HashMap<byte[], Long> theIds = new HashMap<byte[], Long>();
    private final HashMap<Long, byte[]> theBlobs = new HashMap<Long, byte[]>();

    public IRepoBlobs() {}

    public final byte[] get(Long aId) {
        byte[] bytes = theBlobs.get(aId);
        if (bytes == null) {
            synchronized (this) {
                bytes = load(aId);
                add(aId, bytes);
            }
        }
        return bytes;
    }

    public final Long get(byte[] aChars) {
        Long id = theIds.get(aChars);
        if (id == null) {
            synchronized (this) {
                id = find(aChars);
                if (id == null) {
                    id = define(aChars);
                }
                add(id, aChars);
            }
        }
        return id;
    }

    public Reader getReader(Long aId) {
        if (theBlobs.containsKey(aId)) { return new InputStreamReader(new ByteArrayInputStream(get(aId))); }
        return openBlob(aId);
    }

    protected abstract Reader openBlob(Long aId);

    protected abstract Long define(byte[] aBlob);

    protected abstract Long find(byte[] aBlob);

    protected abstract byte[] load(Long aId);

    private final void add(Long aId, byte[] aChars) {
        theIds.put(aChars, aId);
        theBlobs.put(aId, aChars);
    }
}
