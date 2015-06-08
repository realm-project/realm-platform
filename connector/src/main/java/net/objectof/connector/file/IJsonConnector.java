package net.objectof.connector.file;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import net.objectof.aggr.Composite;
import net.objectof.connector.AbstractConnector;
import net.objectof.connector.Connector;
import net.objectof.connector.ConnectorException;
import net.objectof.connector.Parameter.Hint;
import net.objectof.model.Kind;
import net.objectof.model.Package;
import net.objectof.model.Resource;
import net.objectof.model.Transaction;
import net.objectof.model.impl.IBaseMetamodel;
import net.objectof.model.impl.IId;
import net.objectof.model.impl.IMetamodel;
import net.objectof.model.impl.IPackage;
import net.objectof.model.impl.ITransaction;
import net.objectof.model.impl.facets.ISourcePackage;
import net.objectof.model.query.Query;
import net.objectof.model.query.QueryIterable;
import net.objectof.model.query.QueryResolver;
import net.objectof.model.query.Relation;

import org.w3c.dom.Document;


public class IJsonConnector extends AbstractConnector implements Connector {

    public static final String KEY_DATAFILE = "JSON File";
    public static final String KEY_SCHEMAFILE = "Schema File";

    public IJsonConnector() {
        addParameter(KEY_DATAFILE, Hint.FILE);
        addParameter(KEY_SCHEMAFILE, Hint.FILE);
    }

    @Override
    public Package getPackage() throws ConnectorException {
        return new IJsonPackage(value(KEY_DATAFILE), IBaseMetamodel.INSTANCE, new File(value(KEY_SCHEMAFILE)));
    }

    @Override
    public Package createPackage(Document schema, Initialize initialize) throws ConnectorException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Package createPackage(InputStream schema, Initialize initialize) throws ConnectorException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPackageName() {
        return new File(value(KEY_DATAFILE)).getName();
    }

    @Override
    public boolean isContainerEmpty() throws ConnectorException {
        return !new File(value(KEY_DATAFILE)).exists();
    }

    @Override
    public void initializeContainer() throws ConnectorException {
        try {
            new File(value(KEY_DATAFILE)).createNewFile();
        }
        catch (IOException e) {
            throw new ConnectorException(e);
        }
    }

    @Override
    public List<String> getPackageNames() throws ConnectorException {
        return Collections.emptyList();
    }

    @Override
    public String getType() {
        return "JSON File";
    }

    @Override
    public boolean hasPackage(String name) throws ConnectorException {
        return false;
    }
}

class IJsonPackage extends ISourcePackage {

    private String filename;

    public IJsonPackage(String filename, IMetamodel aMetamodel, File aFile) {
        super(aMetamodel, aFile);
        this.filename = filename;
    }

    @Override
    public ITransaction connect(Object aActor) {
        return populate(new IJsonTransaction(this, aActor));
    }

    public IJsonTransaction populate(IJsonTransaction tx) {
        try (Scanner scanner = new Scanner(new File(filename)).useDelimiter("\n")) {
            while (scanner.hasNext()) {
                String str = scanner.next();
                StringReader reader = new StringReader(str);
                tx.receive("application/json", reader);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return tx;
    }

    public void store(Transaction tx) {
        // dump the in-memory model to a json file
        try (FileWriter writer = new FileWriter(filename)) {
            for (Kind<?> kind : getParts()) {
                if (kind.getPartOf() != null) {
                    continue;
                }
                Iterable<Resource<?>> iter = tx.enumerate(kind.getComponentName());
                for (Resource<?> res : iter) {
                    ensurePersistentID(res, tx);
                    res.send("application/json", writer);
                    writer.write("\n");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ensurePersistentID(Resource<?> res, Transaction tx) {
        Object label = res.id().label();
        int id = Integer.parseInt(label.toString());
        if (id >= 1) { return; }
        // find current max label
        int max = 0;
        Iterable<Resource<?>> iter = tx.enumerate(res.id().kind().getComponentName());
        for (Resource<?> otherres : iter) {
            int otherid = Integer.parseInt(otherres.id().label().toString());
            max = Math.max(max, otherid);
        }
        id = max + 1;
        ((IId<?>) res.id()).relabel(id);
    }
}

class IJsonTransaction extends ITransaction {

    public IJsonTransaction(IPackage aPackage, Object aActor) {
        super(aPackage, aActor);
    }

    @Override
    protected void onPost() {
        getPackage().store(this);
    }

    @Override
    public IJsonPackage getPackage() {
        return (IJsonPackage) super.getPackage();
    }

    @Override
    public <T> Iterable<T> enumerate(String kind) throws UnsupportedOperationException {
        Kind<?> enumKind = getPackage().forName(kind);
        List<T> result = new ArrayList<T>();
        for (Object o : getMap().values()) {
            Resource<?> res = (Resource<?>) o;
            if (!res.id().kind().equals(enumKind)) continue;
            if (!isEmpty(res)) {
                result.add((T) res);
            }
        }
        return result;
    }

    @Override
    public <T> T retrieve(String aType, Object aLabel) {
        T retr = super.retrieve(aType, aLabel);
        if (retr == null && aLabel != null) {
            retr = create(aType);
        }
        return retr;
    }

    @Override
    public <T> Iterable<T> query(final String kind, Query query) throws IllegalArgumentException,
            UnsupportedOperationException {
        Set<String> labels = query.resolve(theLimit, getPackage(), kind, new QueryResolver() {

            @Override
            public Set<String> resolve(int aLimit, String key, Relation relation, Object value) {
                throw new UnsupportedOperationException();
            }
        });
        return new QueryIterable<T>(this, kind, labels);
    }

    @Override
    public void load(Object aValue) {
        // Do nothing, since everything should already be loaded into the
        // transaction
    }

    private boolean isEmpty(Resource<?> res) {
        if (!(res instanceof Composite)) { return false; }
        Composite comp = (Composite) res;
        for (String key : comp.keySet()) {
            if (comp.get(key) != null) { return false; }
        }
        return true;
    }
}
