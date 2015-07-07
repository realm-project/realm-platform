package net.realmproject.platform.corc.accessor;


import java.io.IOException;

import javax.servlet.ServletException;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.impl.corc.IHandler;
import net.realmproject.dcm.event.bus.DeviceEventBus;
import net.realmproject.dcm.features.command.Command;
import net.realmproject.dcm.features.statefulness.State;
import net.realmproject.platform.util.RealmCorc;
import net.realmproject.platform.util.RealmResponse;
import net.realmproject.platform.util.RealmSerialize;


public class IDeviceCommanderHandler extends IHandler<HttpRequest> {

    DeviceCommander<State> accessor;

    public IDeviceCommanderHandler(String id, String deviceId, DeviceEventBus bus) {
        super();
        accessor = new DeviceCommander<>(id, deviceId, bus);
    }

    @Override
    protected void onExecute(Action action, HttpRequest http) throws IOException, ServletException {
        switch (http.getHttpRequest().getMethod()) {

            case "GET":
                String state = RealmSerialize.serialize(accessor.getState());
                http.getWriter().write(state);
                return;

            case "POST":
                String json = RealmCorc.getJson(http.getReader());
                Command command = RealmSerialize.deserialize(json, Command.class);
                String label = accessor.sendCommand(command);
                http.getWriter().write("{\"id\": \"" + label + "\"}");
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
