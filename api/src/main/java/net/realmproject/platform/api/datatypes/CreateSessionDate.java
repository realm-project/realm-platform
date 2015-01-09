package net.realmproject.platform.api.datatypes;

import java.util.Arrays;

public class CreateSessionDate {
	public CreateSessionDateRange range;
	public String[] list;
	@Override
	public String toString() {
		return "CreateSessionDate [range=" + range + ", list="
				+ Arrays.toString(list) + "]";
	}
	

}
