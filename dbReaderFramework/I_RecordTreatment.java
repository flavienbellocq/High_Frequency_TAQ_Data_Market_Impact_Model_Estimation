package dbReaderFramework;

import java.time.LocalDate;
import java.util.LinkedList;

import TAQDataPkg.I_Record;


public interface I_RecordTreatment 
{

	public void set_dayFromEpoch(int secondFormEpoch);
	
	
	public int get_dayFromEpoch();
	
	
	public void treat(I_Record record) throws Exception;
	
	
	public void stop();
	
	
}
