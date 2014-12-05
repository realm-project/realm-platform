package net.objectof.repo.impl.sql;


import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import net.objectof.model.RepositoryException;
import net.objectof.repo.impl.IRepoText;


public class ISqlText extends IRepoText {

    private final ResourceBundle theStatements;
    // private long theLastId;
    private final ISqlDb         theDb;

    public ISqlText(ISqlDb aDb, ResourceBundle aStatements) {
        theDb = aDb;
        theStatements = aStatements;
        // theLastId = getLastId();
    }

    @Override
    protected Long define(String aChars) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            conn.setReadOnly(false);
            stmt = ISql.prepare(conn, theStatements, "addChars");
            Long id = getNextId();
            stmt.setLong(1, id);
            stmt.setInt(2, aChars.hashCode());
            stmt.setString(3, aChars);
            stmt.executeUpdate();
            conn.commit();
            return id;
        }
        catch (SQLException e) {
            throw new RepositoryException(this, e);
        }
        finally {
            ISql.close(stmt);
            ISql.close(conn);
        }
    }

    @SuppressWarnings("resource")
    @Override
    protected Long find(String aChars) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            conn = getConnection();
            stmt = ISql.prepare(conn, theStatements, "getHashIds");
            stmt.setInt(1, aChars.hashCode());
            result = stmt.executeQuery();
            while (result.next()) {
                Long id = result.getLong("id");
                String chars = result.getString("chars");
                if (chars.equals(aChars)) { return id; }
            }
            return null;
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
    protected final synchronized String load(Long aId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            conn = getConnection();
            stmt = ISql.prepare(conn, theStatements, "getChars");
            stmt.setLong(1, aId);
            result = stmt.executeQuery();
            if (!result.next()) { throw new RepositoryException(this, "Text id '" + aId + "' not found"); }
            return result.getString("chars");
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
    protected Reader openText(Long aId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            conn = getConnection();
            stmt = ISql.prepare(conn, theStatements, "getChars");
            stmt.setLong(1, aId);
            result = stmt.executeQuery();
            if (!result.next()) { throw new RepositoryException(this, "Text id '" + aId + "' not found"); }
            return result.getCharacterStream("chars");
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

    private final Connection getConnection() {
        return theDb.getConnection();
    }

    private final long getLastId() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            conn = getConnection();
            stmt = ISql.prepare(conn, theStatements, "getLastId");
            result = stmt.executeQuery();
            result.next();

            long lastId = result.getLong(1);
            return lastId;
            // return result.getLong(1);
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

    private synchronized long getNextId() {
        // return ++theLastId;
        return getLastId() + 1;
    }
}
