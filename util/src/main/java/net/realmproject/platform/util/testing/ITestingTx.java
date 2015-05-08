package net.realmproject.platform.util.testing;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.impl.IPackage;
import net.objectof.model.impl.ITransaction;
import net.objectof.model.query.Query;
import net.objectof.model.query.QueryIterable;
import net.objectof.model.query.QueryResolver;
import net.objectof.model.query.Relation;


public class ITestingTx extends ITransaction {

	public ITestingTx(IPackage aPackage, Object aActor) {
		super(aPackage, aActor);
		getMap().putAll(getPackage().load());
	}

	@Override
	protected void onPost() {
		getPackage().store(getMap());
	}
	
	@Override
	public ITestingPackage getPackage() {
		ITestingPackage repo = (ITestingPackage) super.getPackage();
		return repo;
	}
	


  @Override
  public <T> Iterable<T> enumerate(String kind) throws UnsupportedOperationException {
	  return doEnum(kind);
  }
  
  @Override
  public <T> Iterable<T> query(final String kind, Query query) throws IllegalArgumentException, UnsupportedOperationException {
  	
	Set<String> labels = query.resolve(theLimit, getPackage(), kind, new QueryResolver() {
		
		@Override
		public Set<String> resolve(int aLimit, String key, Relation relation, Object value) {
			throw new UnsupportedOperationException();
		}
		
	});

  	return new QueryIterable<T>(this, kind, labels);
  	
  }
  
	@SuppressWarnings("unchecked")
	private <T> List<T> doEnum(String kind) {
		Kind<?> enumKind = getPackage().forName(kind);
		List<T> result = new ArrayList<T>();
		
		for (Object o : getMap().values()) {
			Resource<?> res = (Resource<?>) o;
			if (!res.id().kind().equals(enumKind)) continue;
			result.add((T) res);
		}
		
		return result;
	}
    
	
}