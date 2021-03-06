package net.realmproject.platform.corc.accessor;


import net.realmproject.dcm.event.bus.DeviceEventBus;
import net.realmproject.dcm.features.accessor.IDeviceAccessor;
import net.realmproject.dcm.features.command.Command;
import net.realmproject.dcm.features.stateful.State;


public class DeviceCommander<T extends State> extends IDeviceAccessor<T> {

    public DeviceCommander(String id, String deviceId, DeviceEventBus bus) {
        super(id, deviceId, bus);
    }

    public String sendCommand(Command command) {
        String label = null;
        label = command.getId();
        sendMessage(command);
        return label;
    }

}
