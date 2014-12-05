package net.objectof.model.corc;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.impl.corc.IHandler;


public final class IRateLimiter extends IHandler<HttpRequest>{

	
	Map<String, long[]> requests;
	
	
	//what is the length of time (in 100ms units) we're going to track the requests over?
	private long timeSpan = 15000; //15 seconds
	//what is the length of time before we just drop everything from memory?
	private long expireTimeSpan = timeSpan * 50;
	//how many requests are we going to allow?
	private int requestCount = 15;
	
	
	private Random rand;
	

	/**
	 * 
	 * @param aDefault hander to limit the call-rate of
	 * @param allowedRequests number of requests to allow per duration
	 * @param duration duration in ms over which to remember requests
	 */
	public IRateLimiter(Handler<?> aDefault, int allowedRequests, long duration) {
		super(aDefault);
		requests = new HashMap<>();
		rand = new Random();
		
		timeSpan = duration;
		expireTimeSpan = timeSpan * 50;
		requestCount = allowedRequests;
		
		System.out.println(timeSpan);
		System.out.println(requestCount);
		
	}
	
		

	@Override
	public Class<? extends HttpRequest> getArgumentClass() {
		return HttpRequest.class;
	}
	
	@Override
	protected void onExecute(Action action, HttpRequest request) throws Exception {
		
		String ip = request.getHttpRequest().getRemoteAddr();
		if (request(ip)) {
			chain(action, request);
		} else {
			request.getHttpResponse().sendError(429);
			return;
		}
		
	}
	
	private synchronized boolean request(String ip) {
		clean();
		clean();
		long[] times = forIP(ip);
		if (isSaturated(times)) { return false; }
		record(times);
		return true;
	}
	
	
	
	
	private void clean() {
		if (requests.keySet().size() < 2) { return; }
		Iterator<String> iter = requests.keySet().iterator();
		int keyindex = rand.nextInt(requests.size());
		
		for (int i = 0; i < keyindex-1; i++) {
			iter.next();
		}
		String ip = iter.next();
		
		cleanUser(ip);
		
	}
	
	private void cleanUser(String ip) {
		if (isTimeout(forIP(ip))) {
			requests.remove(ip);
		}
	}
	
	private long time() {
		return new Date().getTime();
	}
	
	private long[] forIP(String ip) {
		if (!requests.containsKey(ip)) {
			long[] times = new long[requestCount];
			requests.put(ip, times);
		}
		return requests.get(ip);
	}

	//if any of the times in the list are earlier than the limit, we're not saturated yet
	private boolean isSaturated(long[] times) {
		
		for (long time : times) {
			System.out.print(time + ", ");
		}
		System.out.println();
		
		
		long limit = time() - timeSpan;
		
		
		for (long time : times) {
			if (time < limit) return false;
		}
		return true;
	}
	
	private boolean isTimeout(long[] times) {
		long limit = time() - expireTimeSpan;
		for (int i = 0; i < requestCount; i++) {
			if (times[i] > limit) return false;
		}
		return true;
	}

	private void record(long[] times) {
		times[lowestIndex(times)] = time();

	}
	
	private int lowestIndex(long[] times) {
		int lowestIndex = 0;
		long lowest = times[0];
		for (int i = 1; i < requestCount; i++) {
			if (times[i] < lowest) {
				lowestIndex = i;
				lowest = times[i];
			}
		}
		return lowestIndex;
	}
	
}
