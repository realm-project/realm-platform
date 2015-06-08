package net.objectof.repo.impl.sql;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import net.objectof.model.RepositoryException;
import net.objectof.model.Stereotype;
import net.objectof.model.impl.IMetamodel;
import net.objectof.repo.impl.IRepo;
import net.objectof.repo.impl.IRepoType;


public abstract class ISqlRepo extends IRepo {

    private final ISqlDb theDb;

    public ISqlRepo(ISqlDb aDb, String aName) {
        super(aName);
        theDb = aDb;
        load();
    }

    protected IRepoType<?> defineType(ISqlRepo aRepo, String aPath, Stereotype aStereotype, int aId) {
        return new IRepoType<>(aRepo, aPath, aStereotype, aId);
    }

    protected ISqlDb getDb() {
        return theDb;
    }

    protected void initialize() {
        ResourceBundle bundle = theDb.getBundle("repoStatements");
        long id = theDb.getText().get(getUniqueName());
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            conn = theDb.getConnection();
            stmt = ISql.prepare(conn, bundle, "load");
            stmt.setLong(1, id);
            result = stmt.executeQuery();
            result.next();
            String packageName = result.getString("schema_impl");
            String metamodel = result.getString("schema_metadata");
            initialize(packageName, createMetamodel(metamodel), theDb.getText(), theDb.getBlobs());
        }
        catch (SQLException e) {
            throw new RepositoryException(getUniqueName(), e);
        }
        finally {
            ISql.close(result);
            ISql.close(stmt);
            ISql.close(conn);
        }
    }

    protected void load() {
        initialize();
        loadTypes();
    }

    protected void loadTypes() {
        ResourceBundle bundle = theDb.getBundle("repoStatements");
        long repoId = internString(getUniqueName());
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            conn = theDb.getConnection();
            stmt = ISql.prepare(conn, bundle, "loadTypes");
            stmt.setLong(1, repoId);
            result = stmt.executeQuery();
            // keep track of the IDs of loaded types and the target IDs of
            // references
            Map<IRepoType<?>, Integer> typeReferences = new HashMap<>();
            Map<Integer, IRepoType<?>> loadedTypes = new HashMap<>();
            while (result.next()) {
                int id = result.getInt("id");
                String path = result.getString("path");
                String stereotypeName = result.getString("stereotype");
                Stereotype stereotype = Stereotype.valueOf(stereotypeName);
                IRepoType<?> type = defineType(this, path, stereotype, id);
                getMap().put(path, type);
                loadedTypes.put(id, type);
                Integer targetId = result.getInt("target");
                if (!result.wasNull()) {
                    typeReferences.put(type, targetId);
                }
            }
            // now that all types have been loaded, wire up the reference
            // targets
            for (IRepoType<?> sourceType : typeReferences.keySet()) {
                int targetId = typeReferences.get(sourceType);
                IRepoType<?> targetType = loadedTypes.get(targetId);
                sourceType.getParts().add(targetType);
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

    private IMetamodel createMetamodel(String aClassname) {
        try {
            return (IMetamodel) Class.forName(aClassname).newInstance();
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RepositoryException(getUniqueName(), e);
        }
    }
}
