package net.realmproject.platform.api.components;

public class GamepadInput {

	public class Axes {
		public float axis0, axis1, axis2, axis3;
	}
	
	public class Buttons {
		public boolean button0, button1, button2, button3;
	}
	
	public Axes axes;
	public Buttons buttons;
	
}
