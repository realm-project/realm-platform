package net.realmproject.platform.corc.accessor;


import java.io.IOException;

import javax.servlet.ServletException;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.impl.corc.IHandler;
import net.realmproject.dcm.accessor.DeviceWriter;
import net.realmproject.dcm.accessor.impl.IDeviceWriter;
import net.realmproject.dcm.command.Command;
import net.realmproject.dcm.event.bus.DeviceEventBus;
import net.realmproject.platform.util.RealmCorc;
import net.realmproject.platform.util.RealmSerialize;


public class IDeviceWriterHandler extends IHandler<HttpRequest> {

    DeviceWriter writer;

    public IDeviceWriterHandler(String id, DeviceEventBus bus) {
        super();
        writer = new IDeviceWriter(id, bus);
    }

    public IDeviceWriterHandler(String id, DeviceEventBus bus, IDeviceRecorder deviceRecorder) {
        super();
        writer = new IDeviceWriter(id, bus, deviceRecorder);
    }

    @Override
    public Class<? extends HttpRequest> getArgumentClass() {
        return HttpRequest.class;
    }

    @Override
    protected void onExecute(Action action, HttpRequest http) throws IOException, ServletException {
        String json = RealmCorc.getJson(http.getReader());
        Command command = RealmSerialize.deserialize(json, Command.class);
        String label = writer.write(command);
        http.getWriter().write("{\"label\": \"" + label + "\"}");
    }

}
