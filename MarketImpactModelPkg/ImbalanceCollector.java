package MarketImpactModelPkg;

import java.util.LinkedList;

import TAQDataPkg.QuoteRecord;
import TAQDataPkg.TradeRecord;

public class ImbalanceCollector 
{
	
	private float _lastMidQuote;
	private int _queueTrades;
	private int _oldSide;
	private long _imbalance;
	
	
	public ImbalanceCollector()
	{
		this._queueTrades = 0;
		this._imbalance = 0L;
		
	}
	
	
	public long get_imbalance() 
	{
		return this._imbalance;
	}
	

	public void updateNewTrade(TradeRecord rec)
	{
		this._queueTrades += rec.get_size();
	}
	
	
	public void updateNewQuote(QuoteRecord rec)
	{
		float mid = (rec.get_askPrice() + rec.get_bidPrice()) * 0.5F;
		if ( mid - this._lastMidQuote >= 0.01F )
		{
			this._imbalance += (long) this._queueTrades;
			this._queueTrades = 0;
			this._oldSide = 1;
		}
		else if ( this._lastMidQuote - mid >= 0.01F )
		{
			this._imbalance -= (long) this._queueTrades;
			this._queueTrades = 0;
			this._oldSide = -1;
		}
		else
		{
			this._imbalance = this._imbalance + (long)(this._oldSide * this._queueTrades) ;
		}
		this._lastMidQuote = mid;
	}
	

}
