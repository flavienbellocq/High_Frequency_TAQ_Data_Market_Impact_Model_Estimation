package TAQDataPkg;

import java.io.File;
import java.io.FilenameFilter;


public class FolderMgr 
{
	private String _pathName;
	private String[] _listElt;
	private int _nbElt;
	private int _indexNextElt;
	
	
	public FolderMgr(String pathName, FilenameFilter filter)
	{
		this._pathName = pathName;
		this._listElt = (new File(pathName)).list(filter);
		this._nbElt = this._listElt.length;
		this._indexNextElt = 0;
	}
	
	
	public String get_pathName() 
	{
		return this._pathName;
	}

	
	public String[] get_listElt() 
	{
		return this._listElt;
	}
	
	
	public boolean hasNext()
	{
		if ( this._indexNextElt >= this._nbElt )
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	
	public String next() throws Exception
	{
		if ( this._indexNextElt >= this._nbElt )
		{
			throw new Exception("each elements of the folder have already been iterated you need to reboot it to get through again");
		}
		else
		{
			this._indexNextElt ++;
			return this._listElt[this._indexNextElt - 1];
		}
	}
	
	
	public void reboot()
	{
		this._indexNextElt = 0;
	}


}
