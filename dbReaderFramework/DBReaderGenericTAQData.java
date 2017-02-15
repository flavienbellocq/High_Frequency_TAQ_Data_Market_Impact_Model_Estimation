package dbReaderFramework;


import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.zip.GZIPInputStream;

import TAQDataPkg.I_Record;


abstract class DBReaderGenericTAQData implements I_DBReader
{
	
	protected boolean _isFinished;
	protected long _lastSequenceNumberRead; 
	protected int _nRecsRead;
	protected int _recCount;
	protected int _memRecCount;
	protected int _milliSecondFromDay;
	protected int _secondFormEpoch;
	protected DataInputStream _dis;
	protected I_RecordTreatment _treatment;

	
	public DBReaderGenericTAQData(String FilePathName, I_RecordTreatment treatment) throws IOException
	{
		this._treatment = treatment;
		this._dis = new DataInputStream( new GZIPInputStream( new FileInputStream( FilePathName ) ) );
		this._secondFormEpoch = this._dis.readInt();
		this._treatment.set_dayFromEpoch(this._secondFormEpoch);
		int unused = this._dis.readInt();
		this._recCount = 0;
		this._memRecCount = -1;
		this._nRecsRead = 0;
		this._isFinished = false;
		this._lastSequenceNumberRead = 0;
	}
	
	
	/**
	 * Number of record red in the last readChunk call
	 * 
	 * @return the number of records red
	 */
	public int get_nRecsRead() 
	{ 
		return this._nRecsRead; 
	}
	
	
	/**
	 * Read a chunk of data until the end of the data in the file or 
	 * if the sequence number of the record is higher than the target sequence number
	 * 
	 * @return the number of records red
	 */
	@Override
	public int readChunk( long targetSequenceNum )
	{
		if( this._isFinished )
		{
			return 0;
		}
		
		int i = this._recCount;
		
		while( true ) 
		{
			if( this._isFinished ) 
			{
				break;
			}
			if( getSequenceNumber() > targetSequenceNum )
			{
				break;
			}
			if( this._isFinished )
			{
				break;
			}
			
			try
			{
				this._treatment.treat(addRecord());
				
			}
			catch ( Exception e)
			{
				
			}
			this._recCount++;
		}
		
		// save last sequence number
		this._lastSequenceNumberRead = targetSequenceNum;
		
		// Save number of records red and return it
		this._nRecsRead = _recCount - i;
		
		return this._nRecsRead;
	}
	
	
	/**
	 * Return the sequence number of the record that this reader is currently
	 * pointing to and will step over when it is asked to read all of the 
	 * records it has for this sequence number.
	 * 
	 * return Current sequence number of this reader
	 */
	@Override
	public long getSequenceNumber() 
	{
		if ( this._memRecCount != this._recCount )
		{
			try
			{
				this._milliSecondFromDay = this._dis.readInt();
				this._memRecCount = this._recCount;
			}
			catch ( Exception e)
			{
				this._isFinished = true;
			}	
		}
		return this._milliSecondFromDay;
	}
	
	
	/**
	 * Stop reading and close all files. In this case, there's nothing to do
	 * because everything has already been read into memory and the file has
	 * been closed.
	 */
	@Override
	public void stop() 
	{
		try
		{
			this._dis.close();
		}
		catch (Exception e)
		{
			
		}
	}

	
	/**
	 * Return if the reading of the file is over
	 * 
	 * @return boolean with true is it is finished
	 */
	@Override
	public boolean isFinished()
	{
		return this._isFinished;
	}
	
	
	/**
	 * Return the last sequence number for which records were successfully
	 * read by this reader.
	 */
	@Override
	public long getLastSequenceNumberRead()
	{
		return this._lastSequenceNumberRead;
	}
	
	
	abstract I_Record addRecord() throws IOException;

	
}
