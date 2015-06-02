package net.realmproject.platform.corc.accessor;


import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletException;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.impl.corc.IHandler;
import net.realmproject.dcm.accessor.DeviceLatencyMonitor;
import net.realmproject.dcm.accessor.impl.IDeviceLatencyMonitor;
import net.realmproject.dcm.event.bus.DeviceEventBus;
import net.realmproject.platform.util.RealmError;
import net.realmproject.platform.util.RealmSerialize;


public class IDeviceLatencyMonitorHandler extends IHandler<HttpRequest> {

    DeviceLatencyMonitor monitor;

    public IDeviceLatencyMonitorHandler(String id, DeviceEventBus bus) {
        super();
        monitor = new IDeviceLatencyMonitor(id, bus);
    }

    @Override
    public Class<? extends HttpRequest> getArgumentClass() {
        return HttpRequest.class;
    }

    @Override
    protected void onExecute(Action action, HttpRequest http) throws IOException, ServletException {
        if (!"GET".equals(http.getHttpRequest().getMethod())) {
            RealmError.send(http, 405, "HTTP method not allowed");
            return;
        }

        Map<String, Long> map = Collections.singletonMap("latency", monitor.getLatency());
        http.getWriter().write(RealmSerialize.serialize(map));
    }
}
