package dbReaderFramework;

import java.io.IOException;

import TAQDataPkg.I_Record;
import TAQDataPkg.TradeRecord;


public class DBReaderTradeData extends DBReaderGenericTAQData
{

	
	public DBReaderTradeData(String FilePathName, I_RecordTreatment treatment) throws IOException
	{
		super(FilePathName,treatment);
	}
	
	
	protected I_Record addRecord() throws IOException
	{
		TradeRecord rec = new TradeRecord(this._milliSecondFromDay,
				this._dis.readInt(), 
				this._dis.readFloat());
		return (I_Record) rec;
	}

	
}

