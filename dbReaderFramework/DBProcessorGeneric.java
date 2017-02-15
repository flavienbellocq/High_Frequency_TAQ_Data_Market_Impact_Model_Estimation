package dbReaderFramework;

import java.util.Iterator;
import java.util.LinkedList;


public class DBProcessorGeneric implements I_DBProcessor
{	
	
	public DBProcessorGeneric()
	{
	}
	
	
	public boolean processReaders(long sequenceNumber, int numReadersWithNewData, LinkedList<I_DBReader> readers) 
	{
		return true;
	}
	
	
	public void stop() throws Exception 
	{
	}
	
	
}
