package TAQDataPkg;


public class TradeRecord implements I_Record
{
	
	private int _milliSecondFromDay;
	private int _size;
	private float _price;
	
	
	public TradeRecord(int milliSecondFromDay, int size, float price)
	{
		this._milliSecondFromDay = milliSecondFromDay;
		this._size = size;
		this._price = price;
	}


	public int get_milliSecondFromDay() 
	{
		return this._milliSecondFromDay;
	}


	public int get_size() 
	{
		return this._size;
	}


	public float get_price() 
	{
		return this._price;
	}
	
	
}
