package MainPkg;

import java.util.HashMap;
import java.util.LinkedList;

import MarketImpactModelPkg.MarketImpactEstimate;
import StatisticalToolsPkg.OnlineSimpleLinearRegression;
import StatisticalToolsPkg.RollingMeanCollector;
import TAQDataPkg.TAQDatabaseMgr;
import dbReaderFramework.DBManager;
import dbReaderFramework.DBProcessorGeneric;
import dbReaderFramework.DBReaderQuoteData;
import dbReaderFramework.DBReaderTradeData;
import dbReaderFramework.I_DBProcessor;
import dbReaderFramework.I_DBReader;
import dbReaderFramework.MergeClock;

public class ClassMain 
{

	public static void main(String[] args)
	{
		try
		{
			
			String pathDataBase = "";
			int openingHourFromMidnightInMin = 100;
			int endImbalanceMeasureFromMidnightInMin = 600;;
		
			
			// database initialization 
			TAQDatabaseMgr dataBase = new TAQDatabaseMgr(pathDataBase);
			
			LinkedList<I_DBReader> readers = new LinkedList<I_DBReader>();
			LinkedList<I_DBProcessor> processors = new LinkedList<I_DBProcessor>();
			
			
			// rolling collector maps initialization
			HashMap<String,RollingMeanCollector<Double,Integer>> mapRollVolCompSSR = new HashMap<String,RollingMeanCollector<Double,Integer>>();
			HashMap<String,RollingMeanCollector<Double,Integer>> mapRollVolCompMean = new HashMap<String,RollingMeanCollector<Double,Integer>>();
			HashMap<String,RollingMeanCollector<Double,Integer>> mapRollADV = new HashMap<String,RollingMeanCollector<Double,Integer>>();
			
			// linear regression initialization 
			OnlineSimpleLinearRegression reg = new OnlineSimpleLinearRegression(true);
			
			// process data
			String[] pathFiles = dataBase.nextStock();
			
			while ( pathFiles[0] != null && pathFiles[1] != null )
			{
				MarketImpactEstimate mi = new MarketImpactEstimate(endImbalanceMeasureFromMidnightInMin,openingHourFromMidnightInMin);
				
				// initializing new rolling collector if we find a new stock
				if ( ! mapRollVolCompSSR.containsKey(pathFiles[2]) )
				{
					mapRollVolCompSSR.put(pathFiles[2], new RollingMeanCollector<Double,Integer>(10));
				}
				if ( ! mapRollVolCompMean.containsKey(pathFiles[2]) )
				{
					mapRollVolCompMean.put(pathFiles[2], new RollingMeanCollector<Double,Integer>(10));
				}
				if ( ! mapRollADV.containsKey(pathFiles[2]) )
				{
					mapRollADV.put(pathFiles[2], new RollingMeanCollector<Double,Integer>(10));
				}
				
				// building readers and processor
				DBReaderTradeData readerTrade = new DBReaderTradeData(pathFiles[0],mi );
				DBReaderQuoteData readerQuote = new DBReaderQuoteData(pathFiles[1],mi );
				DBProcessorGeneric proc = new DBProcessorGeneric();
				readers.add( readerTrade );
				readers.add( readerQuote );
				processors.add( proc );
				
				// launching process
				DBManager dbm = new DBManager( readers, processors, new MergeClock( readers, processors ) );
				dbm.launch();
				
				// output result
				double mean = mapRollVolCompMean.get(pathFiles[2]).get_rollingMean();
				double var = mapRollVolCompSSR.get(pathFiles[2]).get_rollingMean() - (mean * mean);
				reg.addDataPoint(mi.inputRegressonY(Math.sqrt(var * 195)), mi.inputRegressionX(mapRollADV.get(pathFiles[2]).get_rollingMean() ));
				
				// print message OK
				System.out.println( "DAY: " + Integer.toString(mi.get_dayFromEpoch()) + " STOCK: " + pathFiles[0] + " STATUS: OK" );
				
				readers.clear();
				processors.clear();
			}
			
			// perform linear regression
			
			
		
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
		}
	}
	

}
