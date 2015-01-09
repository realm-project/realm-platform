package net.realmproject.platform.security.authorization;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.model.Package;
import net.objectof.model.Transaction;
import net.realmproject.model.schema.Person;
import net.realmproject.platform.corc.DatabaseRepository;

public abstract class RecordAuthorizer implements Authorizer {

	protected Package repo;
	
	protected enum Mode {
		CREATE, READ, UPDATE, DELETE, UNKNOWN
	}
	
	protected class RecordData {
		
		public RecordData(Action action, HttpRequest request) throws IOException {
    		
			className = RealmAuthorizers.className(action, request);;
    		recordName = RealmAuthorizers.recordName(action, request);
    		kind = RealmAuthorizers.kind(action, request);
    		label = RealmAuthorizers.label(action, request);
    		if (RealmAuthorizers.isRead(action, request)) mode = Mode.READ;
    		if (RealmAuthorizers.isCreate(action, request)) mode = Mode.CREATE;
    		if (RealmAuthorizers.isUpdate(action, request)) mode = Mode.UPDATE;
    		if (RealmAuthorizers.isDelete(action, request)) mode = Mode.DELETE;
    		if (mode == null) mode = Mode.UNKNOWN;
    		
    		Transaction tx = repo.connect(action);
    		Reader reader = request.getReader();
    		
    		if (mode == Mode.CREATE || mode == Mode.UPDATE) {
    			reader.mark(Integer.MAX_VALUE);
    			tx.receive("application/json", reader);
    			reader.reset();
    		}
    		
    		if (label != null) { object = tx.retrieve(kind, label); }
		}
		
		public String className;
		public String recordName;
		public String kind;
		public String label;
		public Mode mode;
		public Object object;
	}
	
		
	public RecordAuthorizer(DatabaseRepository dbrepo) {
		repo = dbrepo.get();
	}
	
	protected Package repo() {
		return repo;
	}
	
	
	@Override
	public boolean authorize(Action action, HttpRequest request, Person person) {
		
		RecordData data;
		
		try {
			if (!request.getReader().markSupported()) return false;
			data = new RecordData(action, request);
		}
		catch (IOException e) {
			return false;
		}

		
		try {
    		Method m = this.getClass().getMethod(data.className.toLowerCase(), Action.class, HttpRequest.class, Person.class, RecordData.class);
    		Object result = m.invoke(this, action, request, person, data);
    		return (Boolean) result;
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return defaultResponse(action, request, person, data);
		}
	}
	
	protected boolean readonly(RecordData data) {
		return data.mode == Mode.READ;
	}
	
	protected abstract boolean defaultResponse(Action action, HttpRequest request, Person person, RecordData data);
	
}
