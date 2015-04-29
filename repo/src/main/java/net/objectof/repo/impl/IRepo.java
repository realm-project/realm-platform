package net.objectof.repo.impl;


import net.objectof.model.Kind;
import net.objectof.model.RepositoryException;
import net.objectof.model.impl.IMetamodel;
import net.objectof.model.impl.IPackage;
import net.objectof.repo.impl.rip.IRipType;


public abstract class IRepo extends IPackage {

    private String thePackageName;
    private IMetamodel theMetamodel;
    private IRepoText theText;
    private IRepoBlobs theBlobs;

    /**
     * Partial constructor for Repositories that initialize state later in the
     * construction cycle. The subclass must call initialize().
     */
    public IRepo(String aName) {
        super(aName);
    }

    public IRepo(String aName, String aPackageName, IMetamodel aMetamodel, IRepoText aTextService,
            IRepoBlobs aBlobService) {
        super(aName);
        thePackageName = aPackageName;
        theMetamodel = aMetamodel;
        theText = aTextService;
        theBlobs = aBlobService;
    }

    public IRepoType<?> findType(int aIdx) {
        for (Kind<?> kind : this) {
            IRepoType<?> type = (IRepoType<?>) kind;
            if (type.idx() == aIdx) { return type; }
        }
        throw new RepositoryException(aIdx, "Invalid Type Number");
    }

    @Override
    public IRipType<?> forName(String aName) {
        return (IRipType<?>) super.forName(aName);
    }

    @Override
    public String getComponentName() {
        return thePackageName;
    }

    @Override
    public IMetamodel getMetamodel() {
        return theMetamodel;
    }

    public String getText(long aId) {
        return theText.get(aId);
    }

    public byte[] getBlob(long aId) {
        return theBlobs.get(aId);
    }

    public long internString(String aString) {
        return theText.get(aString);
    }

    public long internBlob(byte[] aBlob) {
        return theBlobs.get(aBlob);
    }

    protected void initialize(String aPackageName, IMetamodel aMetamodel, IRepoText aText, IRepoBlobs aBlob) {
        theText = aText;
        theBlobs = aBlob;
        thePackageName = aPackageName;
        theMetamodel = aMetamodel;
    }
}
