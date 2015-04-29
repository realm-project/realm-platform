package net.objectof.repo.impl.sql;


import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import net.objectof.model.RepositoryException;
import net.objectof.repo.impl.IRepoBlobs;


public class ISqlBlobs extends IRepoBlobs {

    private final ResourceBundle theStatements;
    private final ISqlDb theDb;

    public ISqlBlobs(ISqlDb aDb, ResourceBundle aStatements) {
        theDb = aDb;
        theStatements = aStatements;
    }

    @Override
    protected Long define(byte[] aBlob) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            conn.setReadOnly(false);
            stmt = ISql.prepare(conn, theStatements, "addBlob");
            Long id = getNextId();
            stmt.setLong(1, id);
            stmt.setInt(2, aBlob.hashCode());
            stmt.setBytes(3, aBlob);
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
    protected Long find(byte[] aChars) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            conn = getConnection();
            stmt = ISql.prepare(conn, theStatements, "getBlobHashIds");
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
    protected final synchronized byte[] load(Long aId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            conn = getConnection();
            stmt = ISql.prepare(conn, theStatements, "getBlob");
            stmt.setLong(1, aId);
            result = stmt.executeQuery();
            if (!result.next()) { throw new RepositoryException(this, "Blob id '" + aId + "' not found"); }
            return result.getBytes("bytes");
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
    protected Reader openBlob(Long aId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            conn = getConnection();
            stmt = ISql.prepare(conn, theStatements, "getBlob");
            stmt.setLong(1, aId);
            result = stmt.executeQuery();
            if (!result.next()) { throw new RepositoryException(this, "Blob id '" + aId + "' not found"); }
            return new InputStreamReader(new ByteArrayInputStream(result.getBytes("bytes")));
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
            stmt = ISql.prepare(conn, theStatements, "getLastBlobId");
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
