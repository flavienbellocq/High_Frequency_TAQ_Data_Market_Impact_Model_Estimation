package StatisticalToolsPkg;

import java.util.Iterator;
import java.util.LinkedList;

public class OnlineSimpleLinearRegression 
{
	
	private double _slope;
	private double _intercept;
	private boolean _topLog;
	private LinkedList<Double> _y;
	private LinkedList<Double> _x;
	
	
	public OnlineSimpleLinearRegression(boolean topLog)
	{
		this._topLog = topLog;
		this._x = new LinkedList<Double>();
		this._y = new LinkedList<Double>();
	}
	
	
	public double get_slope() 
	{
		return this._slope;
	}


	public double get_intercept() 
	{
		return this._intercept;
	}
	

	public boolean is_topLog() 
	{
		return this._topLog;
	}


	public void addDataPoint(double y, double x)
	{
		this._x.addLast(x);
		this._y.addLast(y);
	}

	public void regress()
	{
		Iterator<Double> itx = this._x.iterator();
		Iterator<Double> ity = this._y.iterator();
		double ssrX = 0.0;
		double ssrXY = 0.0;
		double mX = 0.0;
		double mY = 0.0;
		double xi = 0.0;
		double yi = 0.0;
		while ( itx.hasNext() && ity.hasNext() )
		{
			if ( this._topLog )
			{
				xi = Math.log(itx.next());
				yi = Math.log(ity.next());
			}
			else
			{
				xi = itx.next();
				yi = ity.next();
			}
			ssrX += xi * xi;
			ssrXY += xi * yi;
			mX += xi;
			mY += yi;
		}
		this._slope = (ssrXY - (mX * mY)) / (ssrX - (mX * mX));
		if ( this._topLog )
		{
			this._intercept = Math.exp(mY - (this._slope * mX));
		}
		else
		{
			this._intercept = mY - (this._slope * mX);
		}
	}
	
	
}
