package dbReaderFramework;

import java.io.IOException;

import TAQDataPkg.I_Record;
import TAQDataPkg.QuoteRecord;

public class DBReaderQuoteData extends DBReaderGenericTAQData
{

	public DBReaderQuoteData(String FilePathName, I_RecordTreatment treatment) throws IOException 
	{
		super(FilePathName, treatment);
	}
	
	
	protected I_Record addRecord() throws IOException 
	{
		QuoteRecord rec = new QuoteRecord(this._milliSecondFromDay,
				this._dis.readInt(), 
				this._dis.readFloat(),
				this._dis.readInt(), 
				this._dis.readFloat());
		return (I_Record) rec;
	}

	
}
