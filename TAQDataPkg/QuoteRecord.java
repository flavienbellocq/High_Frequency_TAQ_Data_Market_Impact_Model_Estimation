package TAQDataPkg;

public class QuoteRecord implements I_Record
{
	
	private int _milliSecondFromDay;
	private int _bidSize;
	private float _bidPrice;
	private int _askSize;
	private float _askPrice;
	
	
	public QuoteRecord(int milliSecondFromDay, int bidSize, float bidPrice, int askSize, float askPrice)
	{
		this._milliSecondFromDay = milliSecondFromDay;
		this._bidSize = bidSize;
		this._bidPrice = bidPrice;
		this._askSize = askSize;
		this._askPrice = askPrice;
	}


	public int get_milliSecondFromDay() 
	{
		return this._milliSecondFromDay;
	}


	public int get_bidSize() 
	{
		return this._bidSize;
	}


	public float get_bidPrice() 
	{
		return this._bidPrice;
	}


	public int get_askSize() 
	{
		return this._askSize;
	}


	public float get_askPrice() 
	{
		return this._askPrice;
	}
	
	
}
