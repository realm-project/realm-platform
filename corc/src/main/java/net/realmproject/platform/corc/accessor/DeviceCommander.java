package net.realmproject.platform.corc.accessor;


import net.realmproject.dcm.accessor.impl.IDeviceAccessor;
import net.realmproject.dcm.event.DeviceEventType;
import net.realmproject.dcm.event.bus.DeviceEventBus;
import net.realmproject.dcm.features.Statefulness.State;
import net.realmproject.dcm.features.command.Command;


public class DeviceCommander<T extends State> extends IDeviceAccessor<T> {

    public DeviceCommander(String id, DeviceEventBus bus) {
        super(id, bus);
    }

    public String sendCommand(Command command) {
        String label = null;
        if (command.isToRecord()) {
            label = command.getId();
        }
        send(getId(), bus, DeviceEventType.MESSAGE, command);
        return label;
    }

}
