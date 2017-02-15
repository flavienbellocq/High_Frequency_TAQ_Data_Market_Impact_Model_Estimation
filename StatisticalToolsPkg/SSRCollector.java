package StatisticalToolsPkg;


/**
 * Weighted SSR online computation with Valtype is Double, FLoat, Long or Integer
 *
 */
public class SSRCollector<ValType,AuxType> implements I_UnivariateStatCollector<ValType,AuxType> 
{
	
	private double _SSR;
	private int _nbDataCollected;
	private double _sumWeight;
	
	
	public SSRCollector()
	{
		this._SSR = 0.0;
		this._nbDataCollected = 0;
		this._sumWeight = 0.0;
	}


	public double get_SSR() 
	{
		return this._SSR;
	}


	public int get_nbDataCollected() 
	{
		return this._nbDataCollected;
	}


	public double get_sumWeight() 
	{
		return this._sumWeight;
	}
	
	
	/**
	 * Generic update running weighted average the auxData is here the weight)
	 * 
	 * @param newData
	 * @param newWeight
	 * @throws Exception
	 */
	public void update(ValType newData, AuxType newWeight, String idNewData) throws Exception
	{
		double newWeightDbl;
		if ( newWeight instanceof Long )
		{
			newWeightDbl = ((Long) newWeight).doubleValue();
		}
		else
		{
			newWeightDbl = (Double) newWeight;
		}
		this._SSR = (((this._SSR * this._sumWeight) + ( ((Double) newData) * ((Double) newData) * newWeightDbl)) ) / (this._sumWeight + newWeightDbl);
		this._sumWeight += newWeightDbl;
		this._nbDataCollected += 1;
	}
	

}
