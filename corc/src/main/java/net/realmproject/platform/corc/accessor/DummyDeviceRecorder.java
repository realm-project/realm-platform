package net.realmproject.platform.corc.accessor;


import net.realmproject.dcm.features.Statefulness.State;
import net.realmproject.dcm.features.command.Command;


public class DummyDeviceRecorder implements DeviceRecorder {

    @Override
    public String recordCommand(Command command) {
        return null;
    }

    @Override
    public String recordState(State state) {
        return null;
    }

}