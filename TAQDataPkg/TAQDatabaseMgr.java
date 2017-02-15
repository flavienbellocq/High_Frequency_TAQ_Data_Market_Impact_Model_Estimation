package TAQDataPkg;

import java.io.File;
import java.io.FilenameFilter;

public class TAQDatabaseMgr 
{
	
	private String _pathSep;
	private String _pathDataBase;
	private FolderMgr _tradeFolder;
	private FolderMgr _quoteFolder;
	private FilenameFilter _subFolderFilter;
	private FilenameFilter _dataFilter;
	private String _currentDay;
	private FolderMgr _currentTradeDay;
	private FolderMgr _currentQuoteDay;
	
	
	public TAQDatabaseMgr(String pathDataBase) throws Exception
	{
		if ( pathDataBase.contains("\\") )
		{
			this._pathSep = "\\";
			if ( pathDataBase.endsWith("\\") )
			{
				this._pathDataBase = pathDataBase.substring(0, pathDataBase.length() - 1);
			}
			else
			{
				this._pathDataBase = pathDataBase;
			}
		}
		else
		{
			this._pathSep = "/";
			if ( pathDataBase.endsWith("/") )
			{
				this._pathDataBase = pathDataBase.substring(0, pathDataBase.length() - 1);
			}
			else
			{
				this._pathDataBase = pathDataBase;
			}
		}
		this._subFolderFilter = new FilenameFilter() 
		{
	        public boolean accept(File directory, String fileName) 
	        {
	            return !fileName.contains(".");
	        }
	    };
	    this._dataFilter = new FilenameFilter() 
		{
	        public boolean accept(File directory, String fileName) 
	        {
	        	if (fileName.endsWith(".binRT") || fileName.endsWith(".binRQ") )
	        	{
	        		return true;
	        	}
	        	else
	        	{
	        		return false;
	        	}
	        }
	    };
	    this._tradeFolder = new FolderMgr(this._pathDataBase + this._pathSep + "trades",this._subFolderFilter);
	    this._quoteFolder = new FolderMgr(this._pathDataBase + this._pathSep + "quotes",this._subFolderFilter);
	    if (this._tradeFolder.hasNext() && this._quoteFolder.hasNext() )
	    {
	    	this._currentDay = this._tradeFolder.next();
	    	this._tradeFolder.reboot();
	    }
	    else
	    {
	    	throw new Exception( "inconsistent database between trade and quote" );
	    }
	    
	}
	
	
	public String get_pathDataBase() 
	{
		return this._pathDataBase;
	}


	public FolderMgr get_tradeFolder() 
	{
		return this._tradeFolder;
	}


	public FolderMgr get_quoteFolder() 
	{
		return this._quoteFolder;
	}


	public String get_currentDay() 
	{
		return this._currentDay;
	}


	public FolderMgr get_currentTradeDay() 
	{
		return this._currentTradeDay;
	}


	public FolderMgr get_currentQuoteDay() 
	{
		return this._currentQuoteDay;
	}


	public boolean nextDay() throws Exception
	{
		String dateTrade = null;
		String dateQuote = null;
		if (this._tradeFolder.hasNext() && this._quoteFolder.hasNext() )
		{
			dateTrade = this._tradeFolder.next();
			dateQuote = this._quoteFolder.next();
			if ( dateTrade.equals(dateQuote) )
			{
				this._currentTradeDay = new FolderMgr(this._tradeFolder.get_pathName() + this._pathSep + dateTrade, this._dataFilter);
				this._currentQuoteDay = new FolderMgr(this._quoteFolder.get_pathName() + this._pathSep + dateQuote, this._dataFilter);
				return true;
			}
			else
			{
				throw new Exception( "inconsistent database between trade and quote" );
			}
		}
		else if ( !this._tradeFolder.hasNext() && !this._quoteFolder.hasNext() )
		{
			return false;
		}
		else
		{
			throw new Exception( "inconsistent database between trade and quote" );
		}
	}
	
	
	public String[] nextStock() throws Exception
	{
		String[] res = new String[3];
		String stockTrade = null;
		String stockQuote = null;
		String stockId = null;
		if ( this._currentTradeDay.hasNext() && this._currentQuoteDay.hasNext() )
		{
			stockTrade = this._currentTradeDay.next();
			stockQuote = this._currentQuoteDay.next();
			if ( stockTrade.substring(0, stockTrade.length() - 12).equals(stockQuote.substring(0, stockQuote.length() - 12)))
			{
				res[0] = this._currentTradeDay.get_pathName() + this._pathSep + stockTrade;
				res[1] = this._currentQuoteDay.get_pathName() + this._pathSep + stockQuote;
				res[2] = stockTrade.substring(0, stockTrade.length() - 12);
			}
			else
			{
				throw new Exception( "inconsistent database between trade and quote" );
			}
		}
		else if ( !this._currentTradeDay.hasNext() && !this._currentQuoteDay.hasNext() )
		{
			if ( nextDay() )
			{
				return nextStock();
			}
			else
			{
				res[0] = null;
				res[1] = null;
				res[2] = null;
			}
		}
		else
		{
			throw new Exception( "inconsistent database between trade and quote" );
		}
		return res;
	}
	
	
	public void rebootDay()
	{
		this._currentTradeDay.reboot();
		this._currentQuoteDay.reboot();
	}
	
	
	public void reboot()
	{
		this._tradeFolder.reboot();
		this._quoteFolder.reboot();
		this._currentTradeDay = null;
		this._currentQuoteDay = null;
	}
	

}
