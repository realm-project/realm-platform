package net.objectof.repo.impl.rip;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import net.objectof.Selector;
import net.objectof.model.RepositoryException;
import net.objectof.model.Stereotype;
import net.objectof.model.Transaction;
import net.objectof.model.impl.ITransaction;
import net.objectof.model.query.Relation;
import net.objectof.repo.impl.IRepoType;
import net.objectof.repo.impl.sql.ISql;
import net.objectof.repo.impl.sql.ISqlDb;
import net.objectof.repo.impl.sql.ISqlRepo;


public class IRip extends ISqlRepo {

    private int txIdCounter;

    public IRip(ISqlDb aDb, String aName) {
        super(aDb, aName);
        txIdCounter = firstTx();
    }

    @Override
    public Transaction connect(Object aActor) {
        /*
         * TODO we need to actually create the transaction in the database as
         * multiple concurrent repositories creating transactions will fail.
         * What we really need is a global database object.
         */
        Object actor = authorize(aActor);
        ITransaction t = new IRipTx(this, actor);
        return t;
    }


    protected <T> String kindFromClass(Class<T> cls) {
        Selector s = cls.getAnnotation(Selector.class);
        if (s == null) throw new IllegalArgumentException("Specified class does not specify a Selector");
        return s.value();
    }



    protected Object authorize(Object aActor) {
        return aActor;
    }

    @Override
    protected IRepoType<?> defineType(ISqlRepo aRepo, String aPath, Stereotype aStereotype, int aId) {
        IRipType<?> type = new IRipType<>(this, aPath, aStereotype, aId);
        return type;
    }

    protected Connection getConnection() {
        return getDb().getConnection();
    }

    protected final long lastInstanceOf(IRipType<?> aType) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            conn = getConnection();
            stmt = ISql.prepare(conn, getDb().getBundle("ripStatements"), "lastNumber");
            stmt.setLong(1, aType.getMinId());
            stmt.setLong(2, aType.getMaxId());
            result = stmt.executeQuery();
            while (result.next()) {
                long last = result.getLong(1);
                return aType.decodeNum(last);
            }
            return 0L;
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


    final synchronized int nextTx() {
        return firstTx() + 1;
    }

    private final synchronized int firstTx() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            conn = getConnection();
            stmt = ISql.prepare(conn, getDb().getBundle("ripStatements"), "lastTx");
            result = stmt.executeQuery();
            while (result.next()) {
                return result.getInt(1) + 1;
            }
            return 1;
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

    public ResourceBundle getBundle(String aKey) {
        return getDb().getBundle(aKey);
    }



    Set<String> doQuery(int aLimit, String kind, String key, Object oValue, Relation relation) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        Set<String> labels = new HashSet<>();
        String fullKindName = kind + "." + key;
        IRipType<?> theKind = forName(fullKindName);
        if (theKind == null) { throw new IllegalArgumentException("Unknown kind: " + fullKindName); }

        Stereotype stereotype = theKind.getStereotype();
        String procName = procedureName(relation, stereotype);
        long value = theKind.valueToBits(oValue);

        try {
            conn = getConnection();
            stmt = ISql.prepare(conn, getDb().getBundle("ripStatements"), procName);
            stmt.setString(1, kind);
            stmt.setString(2, kind + "." + key);
            stmt.setLong(3, value);
            stmt.setLong(4, getDb().getText().get(getUniqueName()));
            stmt.setInt(5, aLimit);

            result = stmt.executeQuery();
            while (result.next()) {
                long label = result.getLong(1);
                labels.add("" + label);
            }
            return labels;
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


    Set<String> doEnumeration(String kind, int limit) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        Set<String> labels = new HashSet<>();

        try {

            conn = getConnection();
            stmt = ISql.prepare(conn, getDb().getBundle("ripStatements"), "enumerate");
            stmt.setString(1, kind);
            stmt.setLong(2, getDb().getText().get(getUniqueName()));
            stmt.setInt(3, limit);

            result = stmt.executeQuery();
            while (result.next()) {
                long label = result.getLong(1);
                labels.add("" + label);
            }
            return labels;
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


    protected Stereotype getStereotype(String kind, String key) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;

        try {
            conn = getConnection();
            stmt = ISql.prepare(conn, getDb().getBundle("ripStatements"), "queryStereotype");
            stmt.setString(1, kind + "." + key);
            result = stmt.executeQuery();
            if (result.next()) {
                return getStereotype(result.getString(1));
            } else {
                return null;
            }
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

    private String procedureName(Relation relation, Stereotype stereotype) {

        switch (relation) {

            case EQUAL:
                return "queryEquals";
            case UNEQUAL:
                return "queryNotEquals";
            case CONTAINS:
                switch (stereotype) {
                    case INDEXED:
                        return "queryContainsIndexed";
                    case MAPPED:
                        return "queryContainsMapped";
                    default:
                        throw new UnsupportedOperationException();
                }


            default:
                throw new UnsupportedOperationException();
        }

    }

}
