package net.objectof.repo.impl.sql;


import java.io.File;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.sql.DataSource;

import net.objectof.Context;
import net.objectof.model.Kind;
import net.objectof.model.Package;
import net.objectof.model.RepositoryException;
import net.objectof.model.Stereotype;
import net.objectof.model.impl.IBaseMetamodel;
import net.objectof.model.impl.IPackage;
import net.objectof.model.impl.facets.ISourcePackage;
import net.objectof.repo.impl.IRepoBlobs;
import net.objectof.repo.impl.IRepoText;
import net.objectof.rt.impl.IFn;


public class ISqlDb extends IFn implements Context<Package> {

    private final String theName;
    private final DataSource theConnector;
    private final IRepoText theText;
    private final IRepoBlobs theBlobs;
    private final Map<String, Package> thePackages;
    private int theLastTypeId;

    public ISqlDb(String aName, DataSource dataSource) {
        theName = aName;
        thePackages = new HashMap<>();
        theConnector = dataSource;
        theText = createText();
        theBlobs = createBlobs();
        theLastTypeId = getLastTypeId();
    }

    public ISqlDb(String aName) {
        theName = aName;
        thePackages = new HashMap<>();
        theConnector = ISql.createPool(theName);
        theText = createText();
        theBlobs = createBlobs();
        theLastTypeId = getLastTypeId();
    }

    public Package create(String aRepoName, Class<?> aRepoClass, File aSchemaFile) {
        IPackage schema = new ISourcePackage(IBaseMetamodel.INSTANCE, aSchemaFile);
        Package repo = createPackage(aRepoName, aRepoClass.getName(), schema);
        return repo;
    }

    public Package createPackage(String aUniqueName, String aImpl, IPackage aPackage) {
        long repoId = createRepo(aUniqueName, aImpl, aPackage);
        createTypes(repoId, aPackage);
        return getPackage(aUniqueName);
    }

    @Override
    public Package forName(String aUniqueName) {
        return getPackage(aUniqueName);
    }

    public ResourceBundle getBundle(String aKey) {
        ResourceBundle thisBundle = ResourceBundle.getBundle(theName);
        String targetBundleName = thisBundle.getString(aKey);
        return ResourceBundle.getBundle(targetBundleName);
    }

    public Connection getConnection() {
        try {
            return theConnector.getConnection();
        }
        catch (SQLException e) {
            throw new RepositoryException(this, e);
        }
    }

    public Package getPackage(String aUniqueName) {
        Package pkg = thePackages.get(aUniqueName);
        if (pkg == null) {
            pkg = loadPackage(aUniqueName);
        }
        return pkg;
    }

    public IRepoText getText() {
        return theText;
    }

    public IRepoBlobs getBlobs() {
        return theBlobs;
    }

    @Override
    public String getUniqueName() {
        return "db.net.objectof:1401/" + theName;
    }

    @Override
    public String toString() {
        return "database/" + theName;
    }

    protected Package createPackageInstance(String aUniqueName) {
        ResourceBundle bundle = getBundle("repoStatements");
        long id = getText().get(aUniqueName);
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            conn = getConnection();
            stmt = ISql.prepare(conn, bundle, "load");
            stmt.setLong(1, id);
            result = stmt.executeQuery();
            result.next();
            long implId = result.getLong("impl_txt");
            String impl = getText().get(implId);
            return definePackage(aUniqueName, impl);
        }
        catch (SQLException e) {
            throw new RepositoryException(aUniqueName, e);
        }
        finally {
            ISql.close(result);
            ISql.close(stmt);
            ISql.close(conn);
        }
    }

    protected long createRepo(String aUniqueName, String aImpl, IPackage aPackage) {
        ResourceBundle bundle = getBundle("repoStatements");
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            conn.setReadOnly(false);
            long id = getText().get(aUniqueName);
            long impl = getText().get(aImpl);
            stmt = ISql.prepare(conn, bundle, "createRepository");
            stmt.setLong(1, id);
            stmt.setLong(2, impl);
            stmt.setString(3, aPackage.getUniqueName());
            stmt.setString(4, "<NOVER>");
            stmt.setString(5, aPackage.getComponentName());
            stmt.setString(6, aPackage.getMetamodel().getClass().getName());
            stmt.executeUpdate();
            conn.commit();
            return id;
        }
        catch (SQLException e) {
            throw new RepositoryException(aUniqueName, e);
        }
        finally {
            ISql.close(stmt);
            ISql.close(conn);
        }
    }

    protected IRepoText createText() {
        return new ISqlText(this, getBundle("textStatements"));
    }

    protected IRepoBlobs createBlobs() {
        return new ISqlBlobs(this, getBundle("blobStatements"));
    }

    protected void createTypes(long aRepo, IPackage aSchema) {
        ResourceBundle bundle = getBundle("repoStatements");
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // in order to properly insert reference targets later, we generate
            // all type ids now.
            // TODO: This only works within the given package/repo
            Map<String, Integer> typeIds = new HashMap<>();
            for (Kind<?> type : aSchema) {
                typeIds.put(type.getComponentName(), ++theLastTypeId);
            }
            conn = getConnection();
            conn.setReadOnly(false);
            stmt = ISql.prepare(conn, bundle, "createType");
            for (Kind<?> type : aSchema) {
                stmt.setInt(1, typeIds.get(type.getComponentName()));
                stmt.setLong(2, aRepo);
                stmt.setString(3, type.getComponentName());
                stmt.setString(4, type.getStereotype().toString().toUpperCase());
                // if this is a refernce, look up the target's ID
                if (type.getStereotype() == Stereotype.REF) {
                    Kind<?> target = type.getParts().get(0);
                    stmt.setInt(5, typeIds.get(target.getComponentName()));
                } else {
                    stmt.setNull(5, Types.SMALLINT);
                }
                stmt.addBatch();
            }
            stmt.executeBatch();
            conn.commit();
        }
        catch (SQLException e) {
            throw new RepositoryException(aSchema.getUniqueName(), e);
        }
        finally {
            ISql.close(stmt);
            ISql.close(conn);
        }
    }

    protected Package definePackage(String aUniqueName, String aImpl) {
        try {
            @SuppressWarnings("unchecked")
            Class<Package> cls = (Class<Package>) Class.forName(aImpl);
            Constructor<Package> constr = cls.getConstructor(getClass(), String.class);
            return constr.newInstance(this, aUniqueName);
        }
        catch (Exception e) {
            throw new RepositoryException(aUniqueName, e);
        }
    }

    protected int getLastTypeId() {
        ResourceBundle bundle = getBundle("repoStatements");
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            conn = getConnection();
            stmt = ISql.prepare(conn, bundle, "getLastTypeId");
            result = stmt.executeQuery();
            result.next();
            int last = result.getInt(1);
            return last;
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

    protected synchronized Package loadPackage(String aUniqueName) {
        // Check again, while synchronized, to manage race conditions.
        Package pkg = thePackages.get(aUniqueName);
        if (pkg == null) {
            pkg = createPackageInstance(aUniqueName);
            thePackages.put(aUniqueName, pkg);
        }
        return pkg;
    }
}
