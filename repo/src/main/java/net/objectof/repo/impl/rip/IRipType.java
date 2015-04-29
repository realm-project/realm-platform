package net.objectof.repo.impl.rip;


import java.util.Date;

import net.objectof.aggr.Listing;
import net.objectof.aggr.impl.IListing;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;
import net.objectof.model.impl.IKind;
import net.objectof.model.impl.IMoment;
import net.objectof.model.impl.ITransaction;
import net.objectof.repo.impl.IRepoType;


public class IRipType<T> extends IRepoType<T> {

    private long theLastInstance;

    public IRipType(IRip aRepo, String aPath, Stereotype aStereotype, int aIdx) {
        super(aRepo, aPath, aStereotype, aIdx);
        theLastInstance = aRepo.lastInstanceOf(this);
    }

    public IRipType<?> elementType(Object aKey) {
        switch (getStereotype()) {
            case COMPOSED:
                return (IRipType<?>) findMember((String) aKey);
            case MAPPED:
            case INDEXED:
            case SET:
                return (IRipType<?>) getParts().get(0);
            default:
                throw new RuntimeException("Stereotype is not an aggregate");
        }
    }

    public long getMaxId() {
        return getMinId() + Integer.MAX_VALUE;
    }

    public long getMinId() {
        return (long) idx() << 48;
    }

    @Override
    public IRip getPackage() {
        return (IRip) super.getPackage();
    }

    public Object keyFromBits(long aValue) {
        switch (getStereotype()) {
            case COMPOSED:
                return getPackage().findType((int) aValue).getSelector();
            case INDEXED:
                return (int) aValue;
            case MAPPED:
                return getPackage().getText(aValue);
            case TEXT:
                return getPackage().getText(aValue);
            default:
                throw new RuntimeException("Stereotype is not an aggregate");
        }
    }

    public long keyToBits(Object aIndex) {
        switch (getStereotype()) {
            case COMPOSED:
                IRipType<?> type = (IRipType<?>) findMember((String) aIndex);
                return type.idx();
            case INDEXED:
                return ((Number) aIndex).longValue();
            case MAPPED:
                return getPackage().internString((String) aIndex);
            case TEXT:
                return getPackage().internString((String) aIndex);
            default:
                throw new RuntimeException("Stereotype is not an aggregate");
        }
    }

    public synchronized long nextPersistentLabel() {
        return ++theLastInstance;
    }

    public Object valueFromBits(ITransaction aTx, long aValue) {
        switch (getStereotype()) {
            case REF:
            case COMPOSED:
            case INDEXED:
            case MAPPED:
            case SET:
                int kind = decodeKix(aValue);
                long num = decodeNum(aValue);
                // Clear the encoded type reference to get the Instance number:
                IRipType<Object> type = typeof(kind);
                return type.datatype().newInstance(aTx, type, num);
            case TEXT:
                return getPackage().getText(aValue);
            case BOOL:
                return aValue == 0 ? false : true;
            case FN:
                throw new RuntimeException("Unsupported.");
            case INT:
                return aValue;
            case MEDIA:
                return getPackage().getBlob(aValue);
            case MOMENT:
                return new IMoment(aValue);
            case NUM:
                return Double.longBitsToDouble(aValue);
            default:
                throw new RuntimeException("Unrecognized Stereotype");
        }
    }

    public long valueToBits(Object aValue) {
        switch (getStereotype()) {
            case REF:
            case COMPOSED:
            case INDEXED:
            case MAPPED:
            case SET:
                Resource<?> r = (Resource<?>) aValue;
                IRipType<?> type = (IRipType<?>) r.id().kind();
                int kix = type.idx();
                long num = (Long) r.id().label();
                return encodePtr(kix, num);
            case TEXT:
                return getPackage().internString((String) aValue);
            case BOOL:
                return (Boolean) aValue ? 1L : 0L;
            case INT:
                return (Long) aValue;
            case MOMENT:
                return ((Date) aValue).getTime();
            case NUM:
                return Double.doubleToLongBits((Double) aValue);
            case FN:
                throw new RuntimeException("Unsupported.");
            case MEDIA:
                return getPackage().internBlob((byte[]) aValue);
            default:
                throw new RuntimeException("Unrecognized Stereotype");
        }
    }

    protected int decodeKix(long aValue) {
        return (int) (aValue >> 48);
    }

    protected long decodeNum(long aValue) {
        return aValue & 0x0000FFFFFFFFFFFFL;
    }

    /**
     * This implementation encodes the kind in the 2 MSB allowing 2^48 instances
     * per kind.
     * 
     * @param aKix
     *            The Kind index, extended to long.
     * @param aObjectIdx
     * @return A single long 'pointer' with the kind & number encoded within it.
     */
    protected long encodePtr(long aKix, long aNum) {
        // Just in case - make sure we're not dealing with a transient id.
        if (aNum < 1L) { throw new RuntimeException(); }
        return aKix << 48 | aNum;
    }

    @Override
    protected Listing<IKind<?>> initializeParts() {
        Listing<IKind<?>> children = new IListing<>(IKind.class);
        for (Kind<?> type : getPackage()) {
            if (type.getPartOf() == this) {
                children.add((IKind<?>) type);
            }
        }
        return children;
    }

    private final IRipType<Object> typeof(int kind) {
        @SuppressWarnings("unchecked")
        IRipType<Object> type = (IRipType<Object>) getPackage().findType(kind);
        if (type == null) { throw new RuntimeException("Invalid Type Number: " + kind); }
        if (getStereotype() == Stereotype.REF) {
            // TODO add check.
            // if (type != getParts().get(0))
            // {
            // System.err.println("Type Number '" + kind
            // + "' does not match REF type in model.");
            // }
        } else if (type != this) {
            System.err.println("Type Number '" + kind + "' does not match PART type in model.");
        }
        return type;
    }
}
