package net.objectof.repo.impl.rip;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Set;

import net.objectof.aggr.Aggregate;
import net.objectof.model.Id;
import net.objectof.model.RepositoryException;
import net.objectof.model.Resource;
import net.objectof.model.impl.IId;
import net.objectof.model.impl.IResourceTx;
import net.objectof.model.query.Query;
import net.objectof.model.query.QueryIterable;
import net.objectof.model.query.QueryResolver;
import net.objectof.model.query.Relation;
import net.objectof.repo.impl.sql.ISql;


public final class IRipTx extends IResourceTx {

    private static final int    CREATE_TX       = 4;
    private static final int    STATEMENT_COUNT = 5;
    private PreparedStatement[] theTransaction;
    private final IRip          rip;
    private Integer             id;

    public IRipTx(IRip aRepo, Object aActor) {
        super(aRepo, aActor);
        this.rip = aRepo;
    }

    private int getId() {
        if (id == null) {
            id = rip.nextTx();
        }
        return id;
    }

    @Override
    public IRip getPackage() {
        return (IRip) super.getPackage();
    }

    @Override
    public <T> Iterable<T> enumerate(String kind) {
        return new QueryIterable<>(this, kind, getPackage().doEnumeration(kind, theLimit));
    }


    @Override
    public <T> Iterable<T> query(final String kind, Query query) {

        Set<String> labels = query.resolve(theLimit, getPackage(), kind, new QueryResolver() {

            @Override
            public Set<String> resolve(int resolverLimit, String key, Relation relation, Object value) {
                return getPackage().doQuery(resolverLimit, kind, key, value, relation);
            }
        });



        return new QueryIterable<>(this, kind, labels);
    }


    @Override
    public void load(Object o) {
        @SuppressWarnings("unchecked")
        Resource<Aggregate<Object, Object>> aResource = (Resource<Aggregate<Object, Object>>) o;

        Object label = aResource.id().label();
        if (label == null) { return; }
        long num = (Long) label;
        if (num < 1) { return; }
        IRipType<?> kind = (IRipType<?>) aResource.id().kind();
        long aid = kind.encodePtr(kind.idx(), num);
        Aggregate<Object, Object> val = aResource.value();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            conn = getPackage().getConnection();
            stmt = ISql.prepare(conn, getPackage().getBundle("ripStatements"), "retrieve");
            stmt.setLong(1, aid);
            result = stmt.executeQuery();
            while (result.next()) {
                long k = result.getLong("k");
                long v = result.getLong("v");
                Object key = kind.keyFromBits(k);
                Object value = kind.elementType(key).valueFromBits(this, v);
                val.set(key, value);
            }
            getMap().put(aResource.getUniqueName(), aResource);
        }
        catch (SQLException e) {
            throw new RepositoryException(this, e);
        }
        finally {
            ISql.close(result);
            ISql.close(stmt);
            ISql.close(conn);
        }
    }

    @Override
    protected void onElementDelete(Id<?> aInstance, Object aIndex, Object aCurrent) {
        IRipType<?> kind = (IRipType<?>) aInstance.kind();
        long aid = kind.encodePtr(kind.idx(), (Long) aInstance.label());
        long idx = kind.keyToBits(aIndex);
        long current = kind.elementType(aIndex).valueToBits(aCurrent);
        doArchive(aid, 'D', idx, current);
        doDelete(aid, idx);
    }

    @Override
    protected void onElementInsert(Id<?> aInstance, Object aIndex, Object aNew) {
        IRipType<?> kind = (IRipType<?>) aInstance.kind();
        long aid = kind.encodePtr(kind.idx(), (Long) aInstance.label());
        long k = kind.keyToBits(aIndex);
        long v = kind.elementType(aIndex).valueToBits(aNew);
        doInsert(aid, k, v);
    }

    @Override
    protected void onElementUpdate(Id<?> aInstance, Object aKey, Object aCurrent, Object aNew) {
        IRipType<?> kind = (IRipType<?>) aInstance.kind();
        long aid = kind.encodePtr(kind.idx(), (Long) aInstance.label());
        long k = kind.keyToBits(aKey);
        kind = kind.elementType(aKey);
        long vOld = kind.valueToBits(aCurrent);
        long vNew = kind.valueToBits(aNew);
        doArchive(aid, 'U', k, vOld);
        doUpdate(aid, k, vNew);
    }

    @Override
    protected void onEndPost() {
        try {
            executeBatch();
            commit();
        }
        catch (SQLException e) {
            throw new RepositoryException(this, e);
        }
    }

    @Override
    protected void onPost() {
        try {
            super.onPost();
        }
        finally {
            closeTransaction();
        }
    }

    @Override
    protected boolean onPostItem(Resource<Aggregate<Object, Object>> aInstance) {
        return super.onPostItem(aInstance) ? noteInsert(aInstance.id()) : false;
    }

    @Override
    protected void onPrepare(Resource<Aggregate<Object, Object>> aResource) {
        Object label = aResource.id().label();
        if (label instanceof Long && (Long) label < 1L) {
            Object id = ((IRipType<?>) aResource.id().kind()).nextPersistentLabel();
            ((IId<?>) aResource.id()).relabel(id);
        }
    }

    @Override
    protected void onStartPost() {
        super.onStartPost();
        prepare();
        createTx();
    }

    @Override
    protected Object toLabel(Object aId) {
        return Long.parseLong(aId.toString());
    }

    private final void closeTransaction() {
        PreparedStatement[] tx = theTransaction;
        if (tx != null) {
            try {
                theTransaction[CREATE_TX].getConnection().rollback();
                ISql.close(theTransaction[CREATE_TX].getConnection());
            }
            catch (SQLException e) {
                throw new RepositoryException("commit-fail", e);
            }
            for (PreparedStatement stmt : tx) {
                ISql.close(stmt);
            }
        }
    }

    private final void commit() {
        /*
         * Any statement from the transaction will do for obtaining the
         * connection (they should all be from the same connection and they
         * should be committed together.
         */
        try {
            theTransaction[CREATE_TX].getConnection().commit();
        }
        catch (SQLException e) {
            throw new RepositoryException("commit-fail", e);
        }
    }

    private final void createTx() {
        PreparedStatement statement = theTransaction[CREATE_TX];
        try {
            statement.setInt(1, getId());
            statement.setLong(2, System.currentTimeMillis());
            statement.setLong(3, getPackage().internString(getActor().toString()));
            statement.execute();
        }
        catch (SQLException e) {
            throw new RepositoryException(this, e);
        }
    }

    private final void doArchive(long aAid, char aOp, long aKey, long aValue) {
        PreparedStatement statement = theTransaction[ARCHIVE];
        try {
            statement.setLong(1, getId());
            statement.setLong(2, aAid);
            statement.setString(3, Character.toString(aOp));
            statement.setLong(4, aKey);
            statement.setLong(5, aValue);
            statement.addBatch();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final void doDelete(long aAid, long aKey) {
        PreparedStatement statement = theTransaction[DELETE];
        try {
            statement.setLong(1, aAid);
            statement.setLong(2, aKey);
            statement.addBatch();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final void doInsert(long aAid, long aKey, long aValue) {
        PreparedStatement insert = theTransaction[INSERT];
        try {
            insert.setLong(1, aAid);
            insert.setLong(2, aKey);
            insert.setLong(3, aValue);
            insert.addBatch();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final void doUpdate(long aAid, long aKey, long aNewValue) {
        PreparedStatement statement = theTransaction[UPDATE];
        try {
            statement.setLong(1, aNewValue);
            statement.setLong(2, aAid);
            statement.setLong(3, aKey);
            statement.addBatch();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final void executeBatch() throws SQLException {
        PreparedStatement[] statements = theTransaction;
        statements[ARCHIVE].executeBatch();
        statements[INSERT].executeBatch();
        statements[UPDATE].executeBatch();
        statements[DELETE].executeBatch();
    }

    private final boolean noteInsert(Id<?> aId) {
        IRipType<?> kind = (IRipType<?>) aId.kind();
        long aid = kind.encodePtr(kind.idx(), (Long) aId.label());
        doArchive(aid, 'I', -1L, 0L);
        return true;
    }

    private final void prepare() {
        ResourceBundle bundle = getPackage().getBundle("ripStatements");
        Connection conn = null;
        try {
            conn = getPackage().getConnection();
            conn.setReadOnly(false);
            PreparedStatement[] statements = new PreparedStatement[STATEMENT_COUNT];
            statements[CREATE_TX] = ISql.prepare(conn, bundle, "createTx");
            statements[ARCHIVE] = ISql.prepare(conn, bundle, "archiveElement");
            statements[INSERT] = ISql.prepare(conn, bundle, "insertElement");
            statements[UPDATE] = ISql.prepare(conn, bundle, "updateElement");
            statements[DELETE] = ISql.prepare(conn, bundle, "deleteElement");
            theTransaction = statements;
        }
        catch (SQLException e) {
            ISql.close(conn);
            throw new RepositoryException(getPackage().getUniqueName(), e);
        }
    }




}
