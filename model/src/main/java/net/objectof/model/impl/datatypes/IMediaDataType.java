package net.objectof.model.impl.datatypes;


import java.io.IOException;
import java.util.Base64;

import javax.json.JsonString;
import javax.json.JsonValue;

import net.objectof.model.Locator;
import net.objectof.model.Stereotype;
import net.objectof.model.impl.IDatatype;
import net.objectof.model.impl.IMetamodel;
import net.objectof.model.impl.ITransaction;


public class IMediaDataType extends IDatatype<byte[]> {

    public IMediaDataType(IMetamodel aMetamodel) {
        super(aMetamodel);
    }

    @Override
    public byte[] fromJson(ITransaction aTx, JsonValue aObject) {
        if (aObject == null || JsonValue.NULL.equals(aObject)) { return null; }
        String base64 = ((JsonString) aObject).getString();
        return Base64.getUrlDecoder().decode(base64);
    }

    @Override
    public Stereotype getStereotype() {
        return Stereotype.MEDIA;
    }

    @Override
    public boolean isAggregate() {
        return false;
    }

    @Override
    public void toJson(byte[] aObject, Locator aLocator, Appendable aWriter) throws IOException {
        if (aObject == null) {
            aWriter.append("null");
        } else {
            aWriter.append(Base64.getUrlEncoder().encodeToString(aObject));
        }
    }

    @Override
    public Class<?> peer() {
        return byte[].class;
    }
}
