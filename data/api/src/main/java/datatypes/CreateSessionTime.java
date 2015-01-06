package datatypes;

public class CreateSessionTime {
	
	public CreateSingleSessionTime single;
	public CreateBulkSessionTime bulk;
	
	@Override
	public String toString() {
		return "CreateSessionTime [start=" + ", single=" + single
				+ ", bulk=" + bulk + "]";
	}
}
