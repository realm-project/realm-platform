package net.realmproject.platform.corc.accessor;


import net.realmproject.dcm.features.Statefulness.State;
import net.realmproject.dcm.features.command.Command;


public interface DeviceRecorder {

    String recordCommand(Command values);

    String recordState(State state);
}
