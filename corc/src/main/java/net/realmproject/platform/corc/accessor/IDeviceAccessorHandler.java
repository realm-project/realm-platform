package net.realmproject.platform.corc.accessor;


import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.impl.corc.IHandler;
import net.realmproject.dcm.accessor.DeviceAccessor;
import net.realmproject.dcm.accessor.impl.IDeviceAccessor;
import net.realmproject.dcm.event.bus.DeviceEventBus;
import net.realmproject.platform.util.RealmResponse;
import net.realmproject.platform.util.RealmSerialize;


public class IDeviceAccessorHandler extends IHandler<HttpRequest> {

    DeviceAccessor<Serializable> accessor;

    public IDeviceAccessorHandler(String id, DeviceEventBus bus) {
        super();
        accessor = new IDeviceAccessor<>(id, bus);
    }

    @Override
    protected void onExecute(Action action, HttpRequest http) throws IOException, ServletException {
        switch (http.getHttpRequest().getMethod()) {

            case "GET":
                String state = RealmSerialize.serialize(accessor.getState());
                http.getWriter().write(state);
                return;

            default:
                RealmResponse.send(http, 405, "HTTP method not allowed");
                return;
        }
    }

    @Override
    public Class<? extends HttpRequest> getArgumentClass() {
        return HttpRequest.class;
    }

}
