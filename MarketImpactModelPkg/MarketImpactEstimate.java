package MarketImpactModelPkg;


import StatisticalToolsPkg.AverageCollector;
import StatisticalToolsPkg.SSRCollector;
import TAQDataPkg.I_Record;
import TAQDataPkg.QuoteRecord;
import TAQDataPkg.TradeRecord;
import dbReaderFramework.I_RecordTreatment;


public class MarketImpactEstimate implements I_RecordTreatment
{

	private int _dayFormEpoch;
	private AverageCollector<Float,Integer> _VWAP;
	private AverageCollector<Float,Integer> _2minReturnMean;
	private SSRCollector <Float,Integer> _2minReturnSSR;
	private ImbalanceCollector _imbalance;
	private long _dailyVolume;
	private float _openPrice;
	private float _closePrice;
	private int _milliSecondFromDayToCutImbalanceEstim;
	private float _priceBeg2minSlice;
	private float _priceEnd2minSlice;
	private int _milliSecondFromDayBegCurrentSLice;
	private double _ratioDayImbalanceEstim;
	
	
	public MarketImpactEstimate(int minFromDayToCutImbalanceEstim, int minFromDayOpeningMarket)
	{
		this._milliSecondFromDayToCutImbalanceEstim = minFromDayToCutImbalanceEstim * 60000;
		this._VWAP = new AverageCollector<Float,Integer>();
		this._2minReturnMean = new AverageCollector<Float,Integer>();
		this._2minReturnSSR = new SSRCollector<Float,Integer>();
		this._imbalance = new ImbalanceCollector();
		this._openPrice = -1.0F;
		this._closePrice = -1.0F;
		this._dailyVolume = 0L;
		this._priceBeg2minSlice = -1.0F;
		this._priceEnd2minSlice = -1.0F;
		this._milliSecondFromDayBegCurrentSLice = minFromDayOpeningMarket * 60000;
		this._ratioDayImbalanceEstim = ((double) (minFromDayToCutImbalanceEstim - minFromDayOpeningMarket)) / 390.0;
	}
	
	
	public void set_dayFromEpoch(int secondFromEpoch)
	{
		this._dayFormEpoch = secondFromEpoch / 86400;
		
		
	}
	
	
	public int get_dayFromEpoch()
	{
		return this._dayFormEpoch;
	}
	
	
	public AverageCollector<Float, Integer> get_VWAP() 
	{
		return this._VWAP;
	}


	public AverageCollector<Float, Integer> get_2minReturnMean() 
	{
		return this._2minReturnMean;
	}


	public SSRCollector<Float, Integer> get_2minReturnSSR() 
	{
		return this._2minReturnSSR;
	}


	public ImbalanceCollector get_imbalance() 
	{
		return this._imbalance;
	}


	public long get_dailyVolume() 
	{
		return this._dailyVolume;
	}


	public float get_openPrice() 
	{
		return this._openPrice;
	}


	public float get_closePrice() 
	{
		return this._closePrice;
	}
	
	
	public double get_ratioDayImbalanceEstim() 
	{
		return this._ratioDayImbalanceEstim;
	}


	public double getTemporaryImpact()
	{
		return ( 0.5 * ( 2 * this._VWAP.get_average() - (double) this._closePrice - (double) this._openPrice ) );
	}
	
	
	public double getPermanentImpact()
	{
		return ( 0.5 * ((double) this._closePrice - (double) this._openPrice) );
	}
	

	public void treat(I_Record rec) throws Exception
	{
		if ( rec instanceof TradeRecord )
		{
			treatTrade((TradeRecord) rec);
		}
		else if ( rec instanceof QuoteRecord )
		{
			treatQuote((QuoteRecord) rec);
		}
	}
	
	
	public void stop()
	{
		
	}
	
	
	public double inputRegressionX(double ADV)
	{
		return ((Long) this._imbalance.get_imbalance()).doubleValue() / ( ADV * this._ratioDayImbalanceEstim); 
	}
	
	public double inputRegressonY(double vol)
	{
		return getTemporaryImpact() / (vol) ;
	}
	
	
	
	private void treatTrade(TradeRecord rec) throws Exception
	{
		if (this._openPrice < 0.0)
		{
			this._openPrice = rec.get_price();
		}
		if ( this._milliSecondFromDayToCutImbalanceEstim <= rec.get_milliSecondFromDay() )
		{
			this._VWAP.update(rec.get_price(),rec.get_size(),"");
		}
		this._dailyVolume += (long) rec.get_size();
		this._closePrice = rec.get_price();
		this._imbalance.updateNewTrade(rec);
	}
	
	
	private void treatQuote(QuoteRecord rec) throws Exception
	{
		if ( rec.get_milliSecondFromDay() < this._milliSecondFromDayBegCurrentSLice + 120000 )
		{
			this._priceEnd2minSlice = (rec.get_bidPrice() + rec.get_askPrice()) * 0.5F;
		}
		else
		{
			int k = 1;
			while (rec.get_milliSecondFromDay() > this._milliSecondFromDayBegCurrentSLice + (k*120000) )
			{
				this._2minReturnMean.update(0.0F, 1, "");
				this._2minReturnSSR.update(0.0F, 1, "");
				k++;
			}
			this._priceBeg2minSlice = this._priceEnd2minSlice;
			this._priceEnd2minSlice = (rec.get_bidPrice() + rec.get_askPrice()) * 0.5F;
		}
		this._imbalance.updateNewQuote(rec);
	}
	
	
}

