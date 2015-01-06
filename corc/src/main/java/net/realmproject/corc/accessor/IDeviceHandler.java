package net.realmproject.corc.accessor;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.impl.corc.IHandler;
import net.realmproject.dcm.accessor.DeviceAccessor;
import net.realmproject.dcm.accessor.DeviceReader;
import net.realmproject.dcm.accessor.DeviceWriter;
import net.realmproject.dcm.command.Command;
import net.realmproject.util.RealmCorc;
import net.realmproject.util.RealmSerialize;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class IDeviceHandler extends IHandler<HttpRequest> implements ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    @Override
    public Class<? extends HttpRequest> getArgumentClass() {
        return HttpRequest.class;
    }


    @Override
    protected void onExecute(Action action, HttpRequest http) throws IOException, ServletException {

        String method = http.getHttpRequest().getMethod();
        List<String> path = RealmCorc.getPath(http.getHttpRequest());
        String deviceId = path.get(path.size() - 1);

        switch (method) {
            case "GET":
                DeviceReader reader = getDeviceReader(deviceId);
                http.getWriter().write(RealmSerialize.serialize(reader.getState()));
                break;

            case "POST":
                DeviceWriter writer = getDeviceWriter(deviceId);
                if (writer == null) {
                    http.getHttpResponse().sendError(403);
                    break;
                }
                String json = RealmCorc.getJson(http.getReader());
                Command command = RealmSerialize.deserialize(json, Command.class);
                writer.write(command);
                break;

        }
    }



    private List<DeviceAccessor> getAccessors() {
        return new ArrayList<>(context.getBeansOfType(DeviceAccessor.class).values());
    }

    private List<DeviceWriter> getWriters() {
        List<DeviceWriter> writers = new ArrayList<>();
        for (DeviceAccessor accessor : getAccessors()) {
            if (accessor instanceof DeviceWriter) writers.add((DeviceWriter) accessor);
        }
        return writers;
    }

    private List<DeviceReader> getReaders() {
        List<DeviceReader> readers = new ArrayList<>();
        for (DeviceAccessor accessor : getAccessors()) {
            if (accessor instanceof DeviceReader) readers.add((DeviceReader) accessor);
        }
        return readers;
    }

    private DeviceWriter getDeviceWriter(String name) {
        for (DeviceWriter writer : getWriters()) {
            if (name.equals(writer.getDeviceId())) return writer;
        }
        return null;
    }

    private DeviceReader getDeviceReader(String name) {
        for (DeviceReader reader : getReaders()) {
            if (name.equals(reader.getDeviceId())) return reader;
        }
        return null;
    }


}
