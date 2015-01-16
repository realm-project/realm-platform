package net.realmproject.platform.corc.accessor;


import java.io.IOException;

import javax.servlet.ServletException;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.impl.corc.IHandler;
import net.realmproject.dcm.accessor.DeviceRecorder;
import net.realmproject.dcm.accessor.impl.IDeviceReaderWriter;
import net.realmproject.dcm.command.Command;
import net.realmproject.dcm.event.bus.DeviceEventBus;
import net.realmproject.platform.util.RealmCorc;
import net.realmproject.platform.util.RealmSerialize;


public class IDeviceReaderWriterHandler extends IHandler<HttpRequest> {

    IDeviceReaderWriter accessor;

    public IDeviceReaderWriterHandler(String id, DeviceEventBus bus) {
        super();
        accessor = new IDeviceReaderWriter(id, bus);
    }

    public IDeviceReaderWriterHandler(String id, DeviceEventBus bus, DeviceRecorder recorder) {
        super();
        accessor = new IDeviceReaderWriter(id, bus, recorder);
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
                String label = accessor.write(command);
                http.getWriter().write("{\"label\": \"" + label + "\"}");
                return;

            default:
                http.getHttpResponse().sendError(405); // Method Not Allowed
                return;
        }
    }

    @Override
    public Class<? extends HttpRequest> getArgumentClass() {
        return HttpRequest.class;
    }

}
