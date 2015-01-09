package datatypes;

import java.util.Arrays;

public class CreateSessionDateRange {
	public String start;
	public String end;
	public String[] days;
	@Override
	public String toString() {
		return "CreateSessionDateRange [start=" + start + ", end=" + end
				+ ", days=" + Arrays.toString(days) + "]";
	}
	
	
}
