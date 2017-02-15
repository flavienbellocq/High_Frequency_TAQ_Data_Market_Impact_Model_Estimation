package StatisticalToolsPkg;

import java.util.LinkedList;

public class RollingMeanCollector<ValType,AuxType> implements I_UnivariateStatCollector<ValType,AuxType>
{
	
	private double _rollingMean;
	private double _rollingSum;
	private int _nbDataCollected;
	private int _rollingSize;
	private LinkedList<Double> _roll;


	public RollingMeanCollector(int rollingSize)
	{
		this._rollingMean = 0.0;
		this._rollingSum = 0.0;
		this._nbDataCollected = 0;
		this._rollingSize = rollingSize;
		this._roll = new LinkedList<Double>();
	}
	
	
	public int get_nbDataCollected()
	{
		return this._nbDataCollected;
	}
	

	public double get_rollingMean() 
	{
		return this._rollingMean;
	}


	public void update(ValType newData, AuxType newAuxData, String idNewData) throws Exception
	{
		this._nbDataCollected ++;
		if ( this._roll.size() < this._rollingSize )
		{
			this._roll.addLast((double) newData);
			this._rollingSum += (double) newData;
			this._rollingMean = this._rollingSum / ((double) this._nbDataCollected);
		}
		else
		{
			this._rollingSum -= this._roll.getFirst();
			this._roll.removeFirst();
			this._roll.addLast((double) newData);
			this._rollingSum += (double) newData;
			this._rollingMean = this._rollingSum / ((double) this._rollingSize);
		}
	}
	
	
}
