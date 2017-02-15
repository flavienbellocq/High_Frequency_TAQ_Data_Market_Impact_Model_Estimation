package StatisticalToolsPkg;


/**
 * Generic univariate statistics collector (on the fly updating)
 * AuxData corresponds to an extra data point that we can gather if we want to compute some statistics conditional to an other variable
 * (for instance: weighted statistics, grouped by statistics,...)
 *
 * @param <ValType>
 */
public interface I_UnivariateStatCollector<ValType,AuxType> 
{
	
	public int get_nbDataCollected();
	
	
	public void update(ValType newData, AuxType newAuxData, String idNewData) throws Exception;
	
	
}
