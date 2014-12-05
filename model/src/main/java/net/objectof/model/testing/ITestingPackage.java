package net.objectof.model.testing;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import net.objectof.model.impl.IMetamodel;
import net.objectof.model.impl.ITransaction;
import net.objectof.model.impl.facets.ISourcePackage;

public class ITestingPackage extends ISourcePackage {

	private Map<String, Object> map = new LinkedHashMap<String, Object>();
	
	public ITestingPackage(IMetamodel aMetamodel, File aFile) {
		super(aMetamodel, aFile);
	}

	@Override
	public ITransaction connect(Object aActor) {
		return new ITestingTx(this, aActor);
	}
	
	public void store(Map<String, Object> entries) {
		map.putAll(entries);
	}
	
	public Map<String, Object> load() {
		return new LinkedHashMap<>(map);
	}

	
}
