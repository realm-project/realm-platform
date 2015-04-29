package net.objectof.model.impl;


import java.util.HashMap;

import net.objectof.model.Stereotype;
import net.objectof.model.impl.datatypes.IBoolDatatype;
import net.objectof.model.impl.datatypes.IComposedDatatype;
import net.objectof.model.impl.datatypes.IFnDatatype;
import net.objectof.model.impl.datatypes.IIndexedDatatype;
import net.objectof.model.impl.datatypes.IIntDatatype;
import net.objectof.model.impl.datatypes.IMappedDatatype;
import net.objectof.model.impl.datatypes.IMediaDataType;
import net.objectof.model.impl.datatypes.IMomentDatatype;
import net.objectof.model.impl.datatypes.INumDatatype;
import net.objectof.model.impl.datatypes.IRefDatatype;
import net.objectof.model.impl.datatypes.ISetDatatype;
import net.objectof.model.impl.datatypes.ITextDatatype;


public class IBaseMetamodel extends IMetamodel {

    public static final IMetamodel INSTANCE = new IBaseMetamodel();

    public IBaseMetamodel() {
        super("object.net:1401/mm/default");
    }

    @Override
    protected HashMap<Stereotype, IDatatype<?>> initDatatypes() {
        HashMap<Stereotype, IDatatype<?>> map = new HashMap<>();
        map.put(Stereotype.COMPOSED, new IComposedDatatype(this));
        map.put(Stereotype.INDEXED, new IIndexedDatatype(this));
        map.put(Stereotype.MAPPED, new IMappedDatatype(this));
        map.put(Stereotype.REF, new IRefDatatype(this));
        map.put(Stereotype.TEXT, new ITextDatatype(this));
        map.put(Stereotype.INT, new IIntDatatype(this));
        map.put(Stereotype.NUM, new INumDatatype(this));
        map.put(Stereotype.MOMENT, new IMomentDatatype(this));
        map.put(Stereotype.BOOL, new IBoolDatatype(this));
        map.put(Stereotype.MEDIA, new IMediaDataType(this));
        map.put(Stereotype.SET, new ISetDatatype(this));
        map.put(Stereotype.FN, new IFnDatatype(this));
        return map;
    }
}
