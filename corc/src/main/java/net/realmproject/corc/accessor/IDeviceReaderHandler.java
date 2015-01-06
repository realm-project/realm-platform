package net.realmproject.corc.accessor;


import java.io.IOException;

import javax.servlet.ServletException;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.impl.corc.IHandler;
import net.realmproject.dcm.accessor.DeviceReader;
import net.realmproject.dcm.accessor.DeviceRecorder;
import net.realmproject.dcm.accessor.impl.IDeviceReader;
import net.realmproject.dcm.event.bus.DeviceEventBus;
import net.realmproject.util.RealmSerialize;


public class IDeviceReaderHandler extends IHandler<HttpRequest> {

    DeviceReader reader;

    public IDeviceReaderHandler(String id, DeviceEventBus bus) {
        super();
        reader = new IDeviceReader(id, bus);
    }

    public IDeviceReaderHandler(String id, DeviceEventBus bus, DeviceRecorder recorder) {
        super();
        reader = new IDeviceReader(id, bus, recorder);
    }

    @Override
    protected void onExecute(Action action, HttpRequest http) throws IOException, ServletException {
        http.getWriter().write(RealmSerialize.serialize(reader.getState()));
    }

    @Override
    public Class<? extends HttpRequest> getArgumentClass() {
        return HttpRequest.class;
    }

}
