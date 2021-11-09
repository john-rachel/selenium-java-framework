package core;

import java.io.IOException;
import java.util.HashMap;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;


/* 
 * This class contains before suite, report initializaiton, testng parameters setting
 * like browserType, driverPath, url
 * */
 
public class BaseClass {
	
	public String browserType;
	public String executionType;
	public String driverPath;
	public String baseUrl;
	public Generic gen1 = new Generic();
	public static HashMap<String,String> hm = new HashMap<String,String>();
		
	@BeforeSuite
	public void base() throws Exception {
				
		gen1.InitializeSummaryReport();
		gen1.StartSummary();
		
		
	}
	@Parameters({ "browserType", "executionType","driverPath","baseUrl" })
	@BeforeClass
	public void baseParameters(String btype, String etype, String dpath, String bUrl) throws Exception {
		
		this.browserType=btype;
		this.executionType=etype;
		this.driverPath=dpath;
		this.baseUrl=bUrl;	
	}
		
	@AfterMethod
	public void EndMethod(ITestResult result) throws Exception {
		
		System.out.println(result.getStatus());
		System.out.println(result.getMethod().getMethodName());
		if(result.getStatus()==1){
		hm.put(result.getMethod().getMethodName(), "Pass");	
		}else{
		hm.put(result.getMethod().getMethodName(), "Fail");	
		
		}
	}
	@AfterSuite
	public void EndTestSuite() throws Exception {
		gen1.tcResults(hm);			
		gen1.SummaryDuration();
		
	}
	
	public void killProcess(){
		try{
			Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe");
			
		}catch(IOException e){
			
		}
	}
			
}
