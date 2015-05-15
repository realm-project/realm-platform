package net.realmproject.platform.corc.accessor;


import net.realmproject.dcm.accessor.impl.IDeviceAccessor;
import net.realmproject.dcm.event.DeviceEvent;
import net.realmproject.dcm.event.DeviceEventType;
import net.realmproject.dcm.event.bus.DeviceEventBus;
import net.realmproject.dcm.features.Statefulness.State;
import net.realmproject.dcm.features.command.Command;


public class DeviceCommander<T extends State> extends IDeviceAccessor<T> {

    DeviceRecorder recorder;

    public DeviceCommander(String id, DeviceEventBus bus) {
        this(id, bus, new DummyDeviceRecorder());
    }

    public DeviceCommander(String id, DeviceEventBus bus, DeviceRecorder recorder) {
        super(id, bus);
        this.recorder = recorder;
    }

    public String sendCommand(Command command) {
        String label = null;
        if (command.record) {
            label = recorder.recordCommand(command);
        }
        send(getId(), bus, DeviceEventType.MESSAGE, command);
        return label;
    }

    public void handleEvent(DeviceEvent event) {
        super.handleEvent(event);
        recorder.recordState((State) event.getValue());
    }

}
