package scripts;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.NoSuchElementException;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import core.BaseClass;
import core.Generic;
import pages.VivinoHome;


/* 
* This class - script class contains data provider, test methods
* */
public class SearchWine extends BaseClass{
	Generic gen = new Generic();			
	public static String sreference;
	
	@DataProvider(name="testData")
	public Object[][] getData2() throws Exception{
		String data[][] = gen.getTableArray(new java.io.File(".").getCanonicalPath() + "\\data\\table.xls","Sheet1","Data");	
		
		return data;
	}
		
	@DataProvider(name="testDataOrder")
	public Object[][] getData3() throws Exception{
		String data[][] = gen.getTableArray(new java.io.File(".").getCanonicalPath() + "\\data\\table.xls","Sheet1","Data");	
		
		return data;
	}
	/*create Fund creation 
	 *  */
	
	@Test(dataProvider="testData",priority=1)
	public void searchWine(String wineName) throws Exception{		
		
		
		gen.setUp(browserType,executionType , "", driverPath);
	//	tcName = "verifyRQAdvSearchBO_TC1" + System.currentTimeMillis();
		
		gen.setTestcaseName("searchWine");
		gen.InitializedetailedReport();
		
		VivinoHome  home= new VivinoHome();
		home.launch(gen, baseUrl);			
		home.search(gen, wineName);
		
				
		//clogin.logout(gen);
		
						
		gen.EndHtmlDetailReporter();
		
		gen.closecurrentPage();
	}
	
}
