package org.objectof.test.schema.person.impl;


import net.objectof.model.Resource;

import org.objectof.test.schema.person.Person;


@SuppressWarnings("all")
@net.objectof.Selector("IPerson")
public class IPerson extends net.objectof.model.impl.facets.IResource<net.objectof.aggr.Composite> implements Person {

    public IPerson(net.objectof.model.impl.IId<net.objectof.aggr.Composite> aIdent) {
        super(aIdent);
    }

    public IPerson(net.objectof.model.impl.IKind<net.objectof.aggr.Composite> aType) {
        super(aType);
    }

    public Long getEmpNo() {
        return (Long) value().get("empNo");
    }

    protected void setEmpNo(Long a) {
        value().set("empNo", a);
    }

    public String getName() {
        return (String) value().get("name");
    }

    public void setName(String a) {
        value().set("name", a);
    }

    public java.util.Date getDob() {
        return (java.util.Date) value().get("dob");
    }

    public void setDob(java.util.Date a) {
        value().set("dob", a);
    }

    public byte[] getPicture() {
        return (byte[]) value().get("picture");
    }

    public void setPicture(byte[] a) {
        value().set("picture", a);
    }

    public net.objectof.aggr.Listing<org.objectof.test.schema.person.Location> getLocations() {
        return (net.objectof.aggr.Listing<org.objectof.test.schema.person.Location>) value().get("locations");
    }

    public void setLocations(net.objectof.aggr.Listing<org.objectof.test.schema.person.Location> a) {
        value().set("locations", a);
    }

    public Resource<?> asResource() {
        return this;
    }
}