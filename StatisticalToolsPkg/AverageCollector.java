package StatisticalToolsPkg;


/**
 * Weighted average online computation with Valtype is Double, FLoat, Long or Integer
 *
 */
public class AverageCollector<ValType,AuxType> implements I_UnivariateStatCollector<ValType,AuxType>
{
	
	private double _average;
	private int _nbDataCollected;
	private double _sumWeight;
	
	
	public AverageCollector()
	{
		this._average = 0.0;
		this._nbDataCollected = 0;
		this._sumWeight = 0.0;
	}


	public double get_average() 
	{
		return this._average;
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
		this._average = (((this._average * this._sumWeight) + ( (Double) newData * newWeightDbl)) ) / (this._sumWeight + newWeightDbl);
		this._sumWeight += newWeightDbl;
		this._nbDataCollected += 1;
	}
	
		
}