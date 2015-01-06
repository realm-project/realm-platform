package net.realmproject.util.spring;

import org.springframework.beans.factory.FactoryBean;

/**
 * This spring factory bean is used to create concrete implementations of 
 * factory beans which return a testing/dummy object during testing, 
 * but a real/live object for production.
 * @author NAS
 *
 * @param <T>
 */

public abstract class DebugBeanFactory<T> implements FactoryBean<T> {

	T t;
	
	public DebugBeanFactory(T t) {
		this.t = t;
	}
	
	@Override
	public T getObject() throws Exception {
		return t;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
