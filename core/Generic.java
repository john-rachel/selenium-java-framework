package core;


import java.awt.Robot;
  	import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Calendar;	
import java.util.Random;
import java.net.URL;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.testng.Assert;
import org.testng.Reporter;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/* 
 * This class contains common methods - launching browser, click object, create summary report, individual report
 * read test data. read text from web page, end the html report. storing files
 * */

public class Generic
{
	public  WebDriver driver;
	private static String ScreenShotName = null;
	public static String filePth;
	public static HashMap mp;
		
	public static String OrderID, sfname;
	
	public String NewTestcaseName,resultFolderName,gTestCaseName,gverify,gstoreValue;
	BufferedWriter output1;
	public static BufferedWriter output;
	Date gdetailStarttime,gdetailEndtime,gStarttime,gEndtime;	
	public double newDetailStartNo;
	public String htmlreport,detailhtmlreport,ScreenshotName,startDetailReport,gProductdir,executionFrom;
	int gdetailstartno,gstartno;
	int gPassedcount,gFailedcount=0;
	public int gStarttestcase=1;
	public String gTestCaseDescription,gExpected=null,oldWindow;
	String gTestcaseStatus;
	public boolean htmlReportNeeded=true;
	String gApplicationName,gRegion,gUserName,gfinaltime;
    public HashMap<String,String> hm = new HashMap<String,String>();
 
    public static String ISIN=null;
    
	public String[][] getTableArray(String xlFilePath, String sheetName, String tableName) throws Exception
	{
		
		String[][] tabArray=null;
		System.out.println(" in the excel function .....");
		Workbook workbook = Workbook.getWorkbook(new File(xlFilePath));
		//   System.out.println("xl file found....");
		Sheet sheet = workbook.getSheet(sheetName); 
		int startRow,startCol, endRow, endCol,ci,cj;
		Cell tableStart=sheet.findCell(tableName);
		startRow=tableStart.getRow();

		startCol=tableStart.getColumn();
		//   System.out.println("startRow...."+ startRow);
		//    System.out.println("startCol...."+startCol);
		Cell tableEnd= sheet.findCell(tableName, startCol+1,startRow+1, 100, 64000,  false);                
		endRow=tableEnd.getRow();
		endCol=tableEnd.getColumn();
		//    System.out.println("startRow="+startRow+", endRow="+endRow+", " +
		//    "startCol="+startCol+", endCol="+endCol);
		tabArray=new String[endRow-startRow-1][endCol-startCol-1];
		ci=0;
		for (int i=startRow+1;i<endRow;i++,ci++)
		{
			cj=0;
			for (int j=startCol+1;j<endCol;j++,cj++){
				tabArray[ci][cj]=sheet.getCell(j,i).getContents();
			}
		}
		return(tabArray);
	}

  public int Rdm() {
	  Random random = new Random();
	  int rand = 0;
	  while (true){
	      rand = random.nextInt(1000);
	      if(rand !=0) break;
	  }
   return rand;
  }
	public void setTestcaseName(String tcName) throws Exception{
		gTestCaseName=tcName;
	}
	public void closesession() throws Exception
	{
		Thread.sleep(1000);
		driver.quit();

	}
	public void closecurrentPage()throws Exception
	{
		Thread.sleep(1000);
		driver.close();
	}

	public void robs(int key) throws Exception{
		Robot rob = new Robot();
		rob.keyPress(key);
		rob.keyRelease(key);
	}

	

	public void setUp (String browsertype,String executionType,String gridURL,String driverPath) throws Exception
	{   
		if (executionType.equalsIgnoreCase("localhost"))
		{  executionFrom=executionType;
		if (browsertype.toLowerCase().equals("chrome"))
		{
			System.setProperty("webdriver.chrome.driver", driverPath+"\\chromedriver.exe");

			driver = new ChromeDriver();
		}
		else if (browsertype.toLowerCase().equals("ie"))
		{

			System.setProperty("webdriver.ie.driver", driverPath+"\\IEDriverServer.exe");
			DesiredCapabilities cap = DesiredCapabilities.internetExplorer();
			cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			cap.setCapability(InternetExplorerDriver.IE_SWITCHES, "-private");
			cap.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
			
			driver = new InternetExplorerDriver(cap);
		}
		else
		{
			driver = new FirefoxDriver();

		}
		}
		else
		{
			executionFrom=executionType;
			if (browsertype.toLowerCase().equals("chrome"))
			{
				DesiredCapabilities browser=DesiredCapabilities.chrome();
				driver = new RemoteWebDriver(new URL(gridURL), browser);
			}
			else if (browsertype.toLowerCase().equals("ie"))

			{
				DesiredCapabilities browser=DesiredCapabilities.internetExplorer();
				driver = new RemoteWebDriver(new URL(gridURL), browser);
			}
			else
			{
				DesiredCapabilities browser=DesiredCapabilities.firefox();
				driver = new RemoteWebDriver(new URL(gridURL), browser);
			} 
		}
	}
	
	public void waitForPagetoLoadJS(int times) throws Exception
	{
		try
		{
			ExpectedCondition<Boolean> pageLoadCondition = new
					ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
				}
			};
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(pageLoadCondition);
		}catch (Exception e)
		{
			Assert.fail("Wait for page to load failed due to "+e.getMessage());
		}
	}
	
	public void openURL(String baseUrl)
	{
		driver.manage().deleteAllCookies();
		driver.get(baseUrl);
		driver.manage().window().maximize();		
		
	}
	public void waitTime() throws InterruptedException
	{
		Thread.sleep(5000);

	}
	public String getText(String locator,String locatorType,String objectName) throws Exception
	{
		waitforobjecttoload(locator,locatorType,60);
		return findobjType(locatorType,locator).getText();

	}

	public void mAction(String locator,String locatorType) throws Exception
	{
		WebElement driverObj = findobjType(locatorType,locator);
		Actions action= new Actions(driver);
		action.contextClick(driverObj).sendKeys(Keys.ARROW_DOWN).build().perform();
	}
	public void clickObjectUsingJS(String locator,String locatorType,String objectName) throws Exception{
		try{ 
			waitforobjecttoload(locator,locatorType,60);
			WebElement driverObj = findobjType(locatorType,locator);

			JavascriptExecutor js = (JavascriptExecutor) driver;    	
			js.executeScript("arguments[0].click();", driverObj);
			ReportStep("Perform click operation on <b>" +objectName +"</b>"+" ("+locator+") object" ,"Perform click operation on <b>" +objectName +"</b>"+" ("+locator+") object should be successful", "Perform click operation on <b>"+objectName +"</b>"+" ("+locator+") object is successful", "pass");
			Reporter.log("Perform click operation on " +objectName +" ("+locator+") object is successful",true);
		}
		catch(Exception e){
			CaptureScreenshot();
			ReportStep("Perform click operation on <b>" +objectName +"</b>"+" ("+locator+") object" ,"Perform click operation on <b>" +objectName +"</b>"+" ("+locator+") object should be successful", "Perform click operation on <b>"+objectName +"</b>"+" ("+locator+") object failed due to "+e.getMessage()+ "<a href=\"" + ScreenShotName+"\""+">link to screenshot</a>", "fail");
			driver.close();
			gStarttestcase=gStarttestcase+1;
			EndHtmlDetailReporter();
			Assert.fail("Perform click operation on " +objectName +"("+locator+") object failed due to " +e.getMessage());

		}


	}

	public void CaptureScreenshot()throws Exception {

		try{
			if (htmlReportNeeded==true) 
			{
				String path = new java.io.File(".").getCanonicalPath();
				Date date=new Date();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);

				ScreenShotName=gTestCaseName +"-"+calendar.get(Calendar.HOUR_OF_DAY)+calendar.get(Calendar.MINUTE)+calendar.get(Calendar.SECOND)+".png";
				if(executionFrom.equalsIgnoreCase("localhost"))
				{
					File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
					FileUtils.copyFile(scrFile, new File(filePth+"\\"+ScreenShotName));

				}
				else
				{
					WebDriver augmentedDriver = new Augmenter().augment(driver);
					File scrFile = ((TakesScreenshot)augmentedDriver).
							getScreenshotAs(OutputType.FILE);
					FileUtils.copyFile(scrFile, new File(filePth+"\\"+ScreenShotName));
				}

			}
		}catch (Exception e)
		{
			Assert.fail("Capturing Screenshot failed due to " +e.getMessage());
		}


	}


	public void clickonObject(String locator,String locatorType,String objectName) throws Exception
	{
		try
		{   waitforobjecttoload(locator,locatorType,60);
		WebElement driverObj = findobjType(locatorType,locator);
		driverObj.click();
		ReportStep("Perform click operation on <b>" +objectName +"</b>"+" ("+locator+") object" ,"Perform click operation on <b>" +objectName +"</b>"+" ("+locator+") object should be successful", "Perform click operation on <b>"+objectName +"</b>"+" ("+locator+") object is successful", "pass");
		Reporter.log("Perform click operation on " +objectName +" ("+locator+") object is successful",true);

		}
		catch(Exception e)
		{
			CaptureScreenshot();
			ReportStep("Perform click operation on <b>" +objectName +"</b>"+" ("+locator+") object" ,"Perform click operation on <b>" +objectName +"</b>"+" ("+locator+") object should be successful", "Perform click operation on <b>"+objectName +"</b>"+" ("+locator+") object failed due to "+e.getMessage()+ "<a href=\"" + ScreenShotName+"\""+">link to screenshot</a>", "fail");
			driver.close();
			gStarttestcase=gStarttestcase+1;
			EndHtmlDetailReporter();
			Assert.fail("Perform click operation on " +objectName +"("+locator+") object failed due to " +e.getMessage());
		}
	}

	public void ClickRowObject(String locator,String locatorType,String objectName, String curr) throws Exception{
		
		try  {      	
			waitforobjecttoload(locator,locatorType,60);
			List<WebElement> options= driver.findElements(By.xpath(locator));
			for(int position=0;position<options.size();position++)	{      	
				if(options.get(position).getText().equals(curr)){
					options.get(position).click();
					break;
				}
			}         
			ReportStep("Get list of values <b>" +objectName +"</b>"+" ("+locator+") object" ,"Perform click operation on <b>" +objectName +"</b>"+" ("+locator+") object should be successful", "Perform click operation on <b>"+objectName +"</b>"+" ("+locator+") object is successful", "pass");
			Reporter.log("Get list of values on " +objectName +" ("+locator+") object is successful",true);
		}
		catch(Exception e)
		{  
			CaptureScreenshot();
			ReportStep("Perform click operation on <b>" +objectName +"</b>"+" ("+locator+") object" ,"Perform click operation on <b>" +objectName +"</b>"+" ("+locator+") object should be successful", "Perform click operation on <b>"+objectName +"</b>"+" ("+locator+") object failed due to "+e.getMessage(), "fail");
			driver.close();
			gStarttestcase=gStarttestcase+1;
			EndHtmlDetailReporter();
			Assert.fail("Perform click operation on " +objectName +"("+locator+") object failed due to " +e.getMessage());
		}
		
	}
	
	public String[] listObjects(String locator,String locatorType,String objectName) throws Exception{
		String [] arr = null;
		try  {      	
			waitforobjecttoload(locator,locatorType,60);
			List<WebElement> options= driver.findElements(By.xpath(locator));
			for(int position=0;position<options.size();position++)	{      	
				arr[position]=options.get(position).getText();
			}         
			ReportStep("Get list of values <b>" +objectName +"</b>"+" ("+locator+") object" ,"Perform click operation on <b>" +objectName +"</b>"+" ("+locator+") object should be successful", "Perform click operation on <b>"+objectName +"</b>"+" ("+locator+") object is successful", "pass");
			Reporter.log("Get list of values on " +objectName +" ("+locator+") object is successful",true);
		}
		catch(Exception e)
		{  
			CaptureScreenshot();
			ReportStep("Perform click operation on <b>" +objectName +"</b>"+" ("+locator+") object" ,"Perform click operation on <b>" +objectName +"</b>"+" ("+locator+") object should be successful", "Perform click operation on <b>"+objectName +"</b>"+" ("+locator+") object failed due to "+e.getMessage(), "fail");
			driver.close();
			gStarttestcase=gStarttestcase+1;
			EndHtmlDetailReporter();
			Assert.fail("Perform click operation on " +objectName +"("+locator+") object failed due to " +e.getMessage());
		}
		return arr;
	}

   public String getDocID(int position){
	   return driver.findElement(By.xpath("(//tr[@class='x-grid-rowbody-tr ']/preceding-sibling::tr/td[2]/div/parent::td/preceding-sibling::td//div[@class='x-grid-row-checker'])["+position+"]/parent::div/parent::td//following-sibling::td[10]/div")).getText();
	   	   
   }
   
   
	public int clickonObjectList(String locator,String locValue,String locatorType,String value, String objectName) throws Exception
	{
		int chkposition=0;
		try
		{   waitforobjecttoload(locator,locatorType,60);
		List<WebElement> options= driver.findElements(By.xpath(locator));
		for(int position=0;position<options.size();position++)  
		{  
			System.out.println(options.get(position).getText());

			if (options.get(position).getText().equals(value))
			{  
				 chkposition=position+1;
				System.out.println(locValue+ chkposition+"]");

				driver.findElement(By.xpath(locValue+ chkposition+"]")).click();
							
                
				//  we.click();
              
				break;
			}
		}          
		ReportStep("Perform click operation on <b>" +objectName +"</b>"+" ("+locator+") object" ,"Perform click operation on <b>" +objectName +"</b>"+" ("+locator+") object should be successful", "Perform click operation on <b>"+objectName +"</b>"+" ("+locator+") object is successful", "pass");
		Reporter.log("Perform click operation on " +objectName +" ("+locator+") object is successful",true);

		}
		catch(Exception e)
		{
			CaptureScreenshot();
			ReportStep("Perform click operation on <b>" +objectName +"</b>"+" ("+locator+") object" ,"Perform click operation on <b>" +objectName +"</b>"+" ("+locator+") object should be successful", "Perform click operation on <b>"+objectName +"</b>"+" ("+locator+") object failed due to "+e.getMessage(), "fail");
			driver.close();
			gStarttestcase=gStarttestcase+1;
			EndHtmlDetailReporter();
			Assert.fail("Perform click operation on " +objectName +"("+locator+") object failed due to " +e.getMessage());
		}
		return chkposition;
	}


	public void setValueonObjectJS(String locator,String locatorType,String actionValue,String objectName)throws Exception
	{
		try
		{
			waitforobjecttoload(locator,locatorType,60);
			WebElement driverObj = findobjType(locatorType,locator);
			JavascriptExecutor js = (JavascriptExecutor) driver;   
			js.executeScript("arguments[0].value='"+actionValue+"';", driverObj);
						
			ReportStep("Perform set operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue, "Perform set operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue +" should be successful", "Perform set operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue+" is successful", "pass");
			Reporter.log("Perform set operation on " +objectName +" ("+locator+") object with value "+actionValue +" is successful",true);

		}
		catch(Exception e)
		{   CaptureScreenshot();
		ReportStep("Perform set operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue, "Perform set operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue +" should be successful", "Perform set operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue+" failed due to "+e.getMessage()+"<a href=\"" + ScreenShotName+"\""+">link to screenshot</a>" , "fail");
		driver.close();
		gStarttestcase=gStarttestcase+1;
		EndHtmlDetailReporter();
		Assert.fail("Perform set operation failed due to " +e.getMessage());
		}
	}

	public void setValueonObject(String locator,String locatorType,String actionValue,String objectName)throws Exception
	{
		try
		{

			waitforobjecttoload(locator,locatorType,60);
			WebElement driverObj = findobjType(locatorType,locator);
			driverObj.sendKeys(actionValue);
			ReportStep("Perform set operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue, "Perform set operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue +" should be successful", "Perform set operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue+" is successful", "pass");
			Reporter.log("Perform set operation on " +objectName +" ("+locator+") object with value "+actionValue +" is successful",true);

		}
		catch(Exception e)
		{   CaptureScreenshot();
		ReportStep("Perform set operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue, "Perform set operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue +" should be successful", "Perform set operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue+" failed due to "+e.getMessage()+"<a href=\"" + ScreenShotName+"\""+">link to screenshot</a>" , "fail");
		driver.close();
		gStarttestcase=gStarttestcase+1;
		EndHtmlDetailReporter();
		Assert.fail("Perform set operation failed due to " +e.getMessage());
		}
	}

	public void verifyValueOfObject(String locator,String locatorType,String actionValue,String objectName,String propertyName,boolean verifyType)throws Exception
	{
		try
		{
			String storeTextValue= null;
			boolean storeproperty=false;

			if(propertyName.toLowerCase().equals("title"))
			{
				storeTextValue = driver.getTitle();
				objectName="page";
				locator="page";
			}
			else if(propertyName=="")
			{
				storeTextValue = driver.getPageSource();
			}
			else if (propertyName.toLowerCase().equals("url"))
			{
				storeTextValue = driver.getCurrentUrl();
				objectName="page";
				locator="page";
			}
			else if (propertyName.toLowerCase().equals("options"))
			{
				WebElement driverObj = findobjType(locatorType,locator);  
				Select select = new Select(driverObj);  
				List<WebElement> options = select.getOptions();  
				for(WebElement we:options)  
				{  
					if (we.getText().equals(actionValue))
					{
						storeTextValue=we.getText();
						break;
					}
				}  

			}

			else if (propertyName.toLowerCase().equals("enable"))
			{   waitforobjecttoload(locator,locatorType,60);
			WebElement driverObj = findobjType(locatorType,locator);
			storeproperty = driverObj.isEnabled();
			//  storeTextValue=new Boolean(storeproperty).toString();
			}
			else if (propertyName.toLowerCase().equals("exist"))
			{   
				WebElement driverObj = findobjType(locatorType,locator);
				storeproperty = driverObj.isDisplayed();
				//  storeTextValue=new Boolean(storeproperty).toString();

			}
			else if (propertyName.toLowerCase().equals("text"))
			{   waitforobjecttoload(locator,locatorType,60);
			WebElement driverObj = findobjType(locatorType,locator);
			storeTextValue = driverObj.getText();
			if(storeTextValue.equalsIgnoreCase(actionValue)){
				storeproperty=true;
			}
			}


			else if (propertyName.toLowerCase().equals("visible"))
			{
				WebElement driverObj = findobjType(locatorType,locator);
				storeproperty = driverObj.isDisplayed();
				// storeTextValue=new Boolean(storeproperty).toString();
			}
			else if (propertyName.toLowerCase().equals("selected"))
			{
				WebElement driverObj = findobjType(locatorType,locator);
				storeproperty = driverObj.isSelected();
				//  storeTextValue=new Boolean(storeproperty).toString();
			}
			else 
			{
				WebElement driverObj = findobjType(locatorType,locator);
				storeTextValue = driverObj.getAttribute("title");
				storeproperty=storeTextValue.contains(actionValue);
				//  storeTextValue=new Boolean(storeproperty).toString();

			}

			if (storeproperty!=false)
			{
				if(verifyType==true)
				{
					if(storeTextValue.toLowerCase().contains(actionValue.toLowerCase()))
					{ gverify=storeTextValue;
					if(propertyName=="")
					{
						storeTextValue=actionValue;
						propertyName="pagesource";
					}
					ReportStep("Perform verify operation (" + propertyName + ") on <b>"+objectName +"</b>("+locator+") object" , "Perform verify operation on <b>"+objectName +"</b>("+locator+") object should be successful", "Perform verify operation on <b>"+objectName +"</b>("+locator+") object is successful ["+storeTextValue+" contains- "+actionValue+"]", "pass");
					Reporter.log("Perform verify operation (" + propertyName + ") on " +objectName +" ("+locator+") object with value "+actionValue +" is successful.Expected( "+storeTextValue +"="+actionValue+")",true);
					}
					else
					{   
						if(propertyName=="")
						{
							storeTextValue=actionValue;
							propertyName="pagesource";
						}
						CaptureScreenshot();
						ReportStep("Perform verify operation (" + propertyName + ") on <b>"+objectName +"</b>("+locator+") object" , "Perform verify operation on <b>"+objectName +"</b>("+locator+") object should be successful", "Perform verify operation on <b>"+objectName +"</b>("+locator+") object failed ["+storeTextValue+" not contains- "+actionValue+"]", "fail");
						driver.close();
						gStarttestcase=gStarttestcase+1;
						EndHtmlDetailReporter();
						Assert.fail("Perform verify operation (" + propertyName + ") on "+objectName +" failed due to["+storeTextValue+" <> "+actionValue+"]");

					}

				}
				else
				{
					if(storeTextValue.toLowerCase().equals(actionValue.toLowerCase()))
					{gverify=storeTextValue;
					if(propertyName=="")
					{
						storeTextValue=actionValue;
						propertyName="pagesource";
					}
					ReportStep("Perform verify operation on (" + propertyName + ") <b>"+objectName +"</b>("+locator+") object" , "Perform verify operation on <b>"+objectName +"</b>("+locator+") object should be successful", "Perform verify operation on <b>"+objectName +"</b>("+locator+") object is successful ["+storeTextValue+" = "+actionValue+"]", "pass");
					Reporter.log("Perform verify operation on (" + propertyName + ") " +objectName +" ("+locator+") object with value "+actionValue +" is successful.Expected( "+storeTextValue +"="+actionValue+")",true);
					}
					else
					{   CaptureScreenshot();
					if(propertyName=="")
					{
						storeTextValue=actionValue;
						propertyName="pagesource";
					}
					ReportStep("Perform verify operation on (" + propertyName + ") <b>"+objectName +"</b>("+locator+") object" , "Perform verify operation on <b>"+objectName +"</b>("+locator+") object should be successful", "Perform verify operation on <b>"+objectName +"</b>("+locator+") object failed ["+storeTextValue+" <> "+actionValue+"]", "fail");
					driver.close();
					gStarttestcase=gStarttestcase+1;
					EndHtmlDetailReporter();
					Assert.fail("Perform verify operation on (" + propertyName + ") "+objectName +" failed due to["+storeTextValue+" <> "+actionValue+"]");

					}
				}
			}

		}
		catch(Exception e)
		{

			CaptureScreenshot();
			ReportStep("Perform verify operation on (" + propertyName + ") <b>"+objectName +"</b>("+locator+") object" , "Perform verify operation on <b>"+objectName +"</b>("+locator+") object should be successful", "Perform verify operation on <b>"+objectName +"</b>("+locator+") object failed due to "+e.getMessage(), "fail");
			driver.close();
			gStarttestcase=gStarttestcase+1;
			EndHtmlDetailReporter();
			Assert.fail("Perform verify operation on (" + propertyName + ") "+objectName +" failed due to " +e.getMessage());
		}
	}


	public void selectValueonObject(String locator,String locatorType,String actionValue,String objectName)throws Exception
	{
		try
		{
			WebElement driverObj = findobjType(locatorType,locator);
			new Select(driverObj).selectByValue(actionValue);
			ReportStep("Perform select operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue, "Perform select operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue +" should be successful", "Perform select operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue+" is successful", "pass");
			Reporter.log("Perform select operation on " +objectName +" ("+locator+") object with value "+actionValue +" is successful",true);

		}
		catch(Exception e)
		{
			CaptureScreenshot();
			ReportStep("Perform select operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue, "Perform select operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue +" should be successful", "Perform select operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue+" failed due to "+e.getMessage(), "fail");
			driver.close();
			gStarttestcase=gStarttestcase+1;
			EndHtmlDetailReporter();
			Assert.fail("Perform select operation failed due to " +e.getMessage());
		}
	}
	public void selectVisibleValueonObject(String locator,String locatorType,String actionValue,String objectName)throws Exception
	{
		try
		{
			WebElement driverObj = findobjType(locatorType,locator);
			new Select(driverObj).selectByVisibleText(actionValue);
			ReportStep("Perform select operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue, "Perform select operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue +" should be successful", "Perform select operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue+" is successful", "pass");
			Reporter.log("Perform select operation on " +objectName +" ("+locator+") object with value "+actionValue +" is successful",true);

		}
		catch(Exception e)
		{
			CaptureScreenshot();
			ReportStep("Perform select operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue, "Perform select operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue +" should be successful", "Perform select operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue+" failed due to "+e.getMessage(), "fail");
			driver.close();
			gStarttestcase=gStarttestcase+1;
			EndHtmlDetailReporter();
			Assert.fail("Perform select operation failed due to " +e.getMessage());
		}
	}
	public WebElement findobjType(String locatorType,String locator)
	{   
		WebElement w = null;
		if (locatorType.toLowerCase()=="id")
		{
			w=driver.findElement(By.id(locator));

		}
		else if (locatorType.toLowerCase()=="name")
		{
			w=driver.findElement(By.name(locator));

		}
		else if (locatorType.toLowerCase()=="css")
		{
			w=driver.findElement(By.cssSelector(locator));

		}
		else if (locatorType.toLowerCase()=="classname")
		{
			w=driver.findElement(By.className(locator));

		}
		else if (locatorType.toLowerCase()=="tagname")
		{
			w=driver.findElement(By.className(locator));

		}
		else if (locatorType.toLowerCase()=="xpath")
		{
			w=driver.findElement(By.xpath(locator));

		}
		else if (locatorType.toLowerCase()=="link")
		{
			w=driver.findElement(By.linkText(locator));

		}
		else if (locatorType.toLowerCase()=="partiallinktext")
		{
			w=driver.findElement(By.partialLinkText(locator));

		}
		return w;
	}   

	public void GetTotalTime(Date Starttime,Date Endtime)
	{
		long TotalTransaction;
		TotalTransaction= Endtime.getTime()-Starttime.getTime() ;
		gfinaltime=(int)((TotalTransaction / 1000)/3600) +" hrs " + (int)(((TotalTransaction / 1000)/60)%60) +" min " +(int) ((TotalTransaction / 1000) % 60) +" Sec";
	}

	public void InitializeSummaryReport() throws Exception{
		if (htmlReportNeeded==true)
		{
			LocalDateTime ldtObj = LocalDateTime.now();
			String time = ldtObj.toString().replace(':', '.');
			time = time.substring(0, time.length()-6);
			
			
			String path = new java.io.File(".").getCanonicalPath();
			String nwFolder = "Result_" + time;
			
			filePth=path+"\\report\\"+nwFolder;
			new File(filePth).mkdir();
			File f = new File(filePth+"\\Summary.html");
			f.createNewFile();
			output = new BufferedWriter(new FileWriter(f));
		
		}
	}
	public void InitializedetailedReport() throws Exception{
		if (htmlReportNeeded==true)
		{
			
			File f = new File(filePth+"\\"+gTestCaseName+".html");
            System.out.println("detail");
            if(f.exists()){
            	output1 = new BufferedWriter(new FileWriter(f,true));
            	DisplayDetailStep();
            }else{
            	f.createNewFile();
    			output1 = new BufferedWriter(new FileWriter(f));
    			DisplayDetailHeader();
            }
			
			
		}
	}

	
	
	public void EndHtmlDetailReporter() throws Exception{
		if (htmlReportNeeded==true)
		{
			DisplayDetailTestDuration();
			output1.close();
		}
	}
	public void ReportStep(String stepDescription,String Expected,String Actual,String stepStatus) throws Exception{

		if (htmlReportNeeded==true)
		{

			String Screenshotdesc;
			String newFilePath = null;
			output1.write("<tr>");
			output1.write("<td  height=" + "1" + " class=" + "bg_darkblue" + "></td>");
			output1.write("<td  height=" + "1" + " class=" + "bg_darkblue" + "></td>");
			output1.write("<td  height=" + "1" + " class=" + "bg_darkblue" + "></td>");
			output1.write("<td  height=" + "1" + " class=" + "bg_darkblue" + "></td>");
			output1.write("<td  height=" + "1" + " class=" + "bg_darkblue" + "></td>");
			output1.write("<td  height=" + "1" + " class=" + "bg_darkblue" + "></td>");
			output1.write("</tr></tr>");
			output1.write("<td valign=" + "middle" + " align=" + "center" + " height=" + "20" + "><span class=" + "text" + ">" + newDetailStartNo + "</span></td>");
			output1.write("<td valign=" + "middle" + " align=" + "center" + " height=" + "20" + "><span class=" + "text" + ">" + stepDescription + "</span></td>");
			output1.write("<td valign=" + "middle" + " align=" + "center" + " height=" + "20" + "><span class=" + "text" + ">" + Expected + "</span></td>");
			output1.write("<td valign=" + "middle" + " align=" + "center" + " height=" + "20" + "><span class=" + "text" + ">" + Actual + "</span></td>");
			//String path = new java.io.File(".").getCanonicalPath();
			newFilePath="\\report\\htmlFolder";
			gProductdir=newFilePath;
			if (stepStatus=="pass")
			{
				output1.write("<td valign=" + "middle" + " align=" + "center" + " height=" + "20" + "><span class=" + "passed" + ">Passed</span></td>");
				gTestcaseStatus="pass";
				Screenshotdesc="";
				if (ScreenShotName!=null)
				{
					Screenshotdesc= ScreenShotName;
					output1.write("<td valign=" + "middle" + " align=" + "center" + " height=" + "20" + "><a target="+ "_blank href=" + Screenshotdesc +"><IMG SRC="+gProductdir +"\\dat\\logo.png "+"HEIGHT=15></a></td>");
					ScreenShotName=null;
				}
			}
			else
			{
				output1.write("<td valign=" + "middle" + " align=" + "center" + " height=" + "20" + "><span class=" + "failed" + ">Failed</span></td>");
				gTestcaseStatus="fail";
				Screenshotdesc=ScreenShotName;
				if (ScreenShotName!=null)
				{
					output1.write("<td valign=" + "middle" + " align=" + "center" + " height=" + "20" + "><a target="+ "_blank href=" + Screenshotdesc +"><IMG SRC="+gProductdir +"\\dat\\logo.png "+"HEIGHT=15></a></td>");
					ScreenShotName=null;
				}
			}
			output1.write("<tr>");
			DecimalFormat twoDForm = new DecimalFormat("#.##");
			//return Double.valueOf(twoDForm.format(newDetailStartNo));
			newDetailStartNo = Double.valueOf(twoDForm.format(newDetailStartNo)) + .1;
			newDetailStartNo= Double.valueOf(twoDForm.format(newDetailStartNo));
			//startDetailReport = startDetailReport + 1;
		}

	}
	
	public void StartSummary() throws Exception{
		
		String path = new java.io.File(".").getCanonicalPath();
		newDetailStartNo=1;
		gStarttime=new Date();
		int temp=path.lastIndexOf("\\");
		String projectName=path.substring(temp+1);
		output.write("</Head>");
		output.write("<Style>");
		output.write(".hl1");
		output.write("{");
		output.write("    COLOR: #669;");
		output.write("    FONT-FAMILY: Mic Shell Dlg;");
		output.write("   FONT-SIZE: 16pt;");
		output.write("    FONT-WEIGHT: bold");
		output.write("}");
		output.write(".bg_darkblue");
		output.write("{");
		output.write("   BACKGROUND-COLOR: #669");
		output.write("}");
		output.write(".bg_midblue");
		output.write("{");
		output.write("    BACKGROUND-COLOR: #99c");
		output.write("}");
		output.write(".bg_gray_eee");
		output.write("{");
		output.write("   BACKGROUND-COLOR: #eee");
		output.write("}");
		output.write(".text");
		output.write("{");
		output.write("   FONT-FAMILY: Mic Shell Dlg;");
		output.write("   FONT-SIZE: 10pt");
		output.write("}");
		output.write(".tablehl");
		output.write("{");
		output.write("   BACKGROUND-COLOR: #eee;");
		output.write("  COLOR: #669;");
		output.write("   FONT-FAMILY: Mic Shell Dlg;");
		output.write("   FONT-SIZE: 10pt;");
		output.write("  FONT-WEIGHT: bold;");
		output.write("  LINE-HEIGHT: 14pt");
		output.write("}");
		output.write(".Failed");
		output.write("{");
		output.write("    COLOR: #f03;");
		output.write("    FONT-FAMILY: Mic Shell Dlg;");
		output.write("    FONT-SIZE: 10pt;");
		output.write("    FONT-WEIGHT: bold");
		output.write("}");
		output.write(".Passed");
		output.write("{");
		output.write("    COLOR: #096;");
		output.write("    FONT-FAMILY: Mic Shell Dlg;");
		output.write("    FONT-SIZE: 10pt;");
		output.write("    FONT-WEIGHT: bold");
		output.write("}");
		output.write("</Style>");
		output.write("</Head>");
		output.write("<Title>Detail Report</Title>");
		output.write("<hr class=" + "bg_midblue" +">");
		output.write("<div align="+ "center"+ "><span class=" + "hl1" + " Localizable_1=" + "True" + ">" + projectName+ " </span></div>");
		output.write("<hr class=" + "bg_darkblue" + ">");
		output.write("<table border=" + "0" + "cellpadding=" + "3" + " cellspacing=" + "1" + " width=" + "100%" + " bgcolor=" + "#666699" + ">");
		output.write("<tr><td bgcolor=" + "white" + ">");
		output.write("<table border=" + "0" + " cellpadding=" + "3" + " cellspacing=" + "0" + " width=" + "100%" + ">");
		output.write("<tr>");
		output.write("<td valign=" + "middle" + " align=" + "center" + " class=" + "tablehl" + "> <span class=" + "tablehl" + ">Test Case Name</span></td>");
		output.write("<td valign=" + "middle" + " align=" + "center" + " class=" + "tablehl" + "> <span class=" + "tablehl" + ">Result</span></td>");		
		output.write("</tr>");
		output.write("<tr>");
		output.write("<td  height=" + "1" + " class=" + "bg_darkblue" + "></td>");
		output.write("<td  height=" + "1" + " class=" + "bg_darkblue" + "></td>");		
		output.write("</tr>");		
		
		
	}
	public void tcResults(HashMap<String,String> mp) throws Exception{        
		
	for(String ob : mp.keySet()){
	output.write("<tr> <td valign=" + "middle" + " align=" + "center" + " height=" + "20" + "><span class=" + "text" + "></span><a href=\"" + ob + ".html\" >"+ob+"</a></td>");
	output.write("<td valign=" + "middle" + " align=" + "center" + " height=" + "20" + "><span class=" + "text" + ">"+ mp.get(ob) +"</span></td>");	
	output.write("</tr>");
	output.write("<tr>");
	output.write("<td  height=" + "1" + " class=" + "bg_gray_eee" + "></td>");
	output.write("<td  height=" + "1" + " class=" + "bg_gray_eee" + "></td>");		
	output.write("</tr>");
	}
	
	}
	public void tcResult() throws Exception{        
		
		output.write("<tr> <td valign=" + "middle" + " align=" + "center" + " height=" + "20" + "><span class=" + "text" + "></span><a href=\"" + gTestCaseName + ".html\" >"+gTestCaseName+"</a></td>");
		
		}
	
	public void tcResult(String caseName, int result) throws Exception{        
		
	
		output.write("<tr> <td valign=" + "middle" + " align=" + "center" + " height=" + "20" + "><span class=" + "text" + "></span><a href=\"" + caseName + ".html\" >"+caseName+"</a></td>");
		output.write("<td valign=" + "middle" + " align=" + "center" + " height=" + "20" + "><span class=" + "text" + ">"+ result +"</span></td>");	
		output.write("</tr>");
		output.write("<tr>");
		output.write("<td  height=" + "1" + " class=" + "bg_gray_eee" + "></td>");
		output.write("<td  height=" + "1" + " class=" + "bg_gray_eee" + "></td>");		
		output.write("</tr>");
		
		
		}
	
	public void SummaryDuration() throws Exception{
		
		
		output.write("</table>");
		output.write("</table>");
		output.write("</br>");
		output.write("<table border=" + "0" + "cellpadding=" + "3" + " cellspacing=" + "1" + " width=" + "100%" + " bgcolor=" + "#666699" + ">");
		output.write("<tr><td bgcolor=" + "white" + ">");
		output.write("<table border=" + "0" + " cellpadding=" + "3" + " cellspacing=" + "0" + " width=" + "100%" + ">");
		output.write("<tr>");
		output.write("<td valign=" + "middle" + " align=" + "center" + " class=" + "tablehl" + "> <span class=" + "tablehl" + ">Status</span></td>");
		output.write("<td valign=" + "middle" + " align=" + "center" + " class=" + "tablehl" + "> <span class=" + "tablehl" + ">Duration</span></td>");
		output.write("</tr>");
		output.write("<tr>");
		output.write("<td  height=" + "1" + " class=" + "bg_darkblue" + "></td>");
		output.write("<td  height=" + "1" + " class=" + "bg_darkblue" + "></td>");
		output.write("</tr><tr>");
		output.write("<td valign=" + "middle" + " align=" + "center" + " class=" + "text" + "> <span class=" + "text" + ">Run Started</span></td>");
		output.write("<td valign=" + "middle" + " align=" + "center" + " height=" + "20" + "><span class=" + "text" + ">"+ gStarttime +"</span></td>");
		output.write("</tr>");
		output.write("<tr>");
		output.write("<td  height=" + "1" + " class=" + "bg_darkblue" + "></td>");
		output.write("<td  height=" + "1" + " class=" + "bg_darkblue" + "></td>");
		output.write("</tr>");
		output.write("<tr>");
		gEndtime=new Date();
		output.write("<td valign=" + "middle" + " align=" + "center" + " class=" + "text" + "> <span class=" + "text" + ">Run ended</span></td>");
		output.write("<td valign=" + "middle" + " align=" + "center" + " height=" + "20" + "><span class=" + "text" + ">"+ gEndtime +"</span></td>");
		output.write("</tr>");
		output.write("<tr>");
		output.write("<td  height=" + "1" + " class=" + "bg_darkblue" + "></td>");
		output.write("<td  height=" + "1" + " class=" + "bg_darkblue" + "></td>");
		output.write("</tr>");
		output.write("<tr>");
		GetTotalTime (gStarttime,gEndtime);
		output.write("<td valign=" + "middle" + " align=" + "center" + " class=" + "text" + "> <span class=" + "text" + ">Total Duration</span></td>");
		output.write("<td valign=" + "middle" + " align=" + "center" + " height=" + "20" + "><span class=" + "text" + ">"+ gfinaltime +"</span></td>");
		output.write("</tr>");
		output.write("</table>");
		output.write("</td></tr>");
		output.write("</tr>");
		output.close();
	}
	public void DisplayDetailStep() throws Exception{
		
	output1.write("<table border=" + "0" + "cellpadding=" + "3" + " cellspacing=" + "1" + " width=" + "100%" + " bgcolor=" + "#666699" + ">");
	output1.write("<tr><td bgcolor="+"white"+">");
	output1.write("<table border="+"0"+" cellpadding="+"3"+" cellspacing="+"0" +" width="+"100%"+">");
	output1.write("<tr>");
	output1.write("<td valign="+"middle" +" align="+"center" +" class="+"tablehl"+"> <span class="+"tablehl"+">Step No</span></td>");
	output1.write("<td valign="+"middle" +" align="+"center" +" class="+"tablehl"+"> <span class="+"tablehl"+">Step Description</span></td>");
	output1.write("<td valign="+"middle" +" align="+"center" +" class="+"tablehl"+"> <span class="+"tablehl"+">Expected</span></td>");
	output1.write("<td valign="+"middle" +" align="+"center" +" class="+"tablehl"+"> <span class="+"tablehl"+">Actual</span></td>");
	output1.write("<td valign="+"middle" +" align="+"center" +" class="+"tablehl"+"> <span class="+"tablehl"+">Status</span></td>");
	output1.write("<td valign="+"middle" +" align="+"center" +" class="+"tablehl"+"> <span class="+"tablehl"+">Screenshot</span></td>");
	output1.write("</tr>");
	}
	public void DisplayDetailHeader() throws Exception{
		
		String path = new java.io.File(".").getCanonicalPath();
		newDetailStartNo=1.0;
		gdetailStarttime=new Date();
		int temp=path.lastIndexOf("\\");
		String projectName=path.substring(temp+1);
		output1.write("</Head>");
		output1.write("<Style>");
		output1.write(".hl1");
		output1.write("{");
		output1.write("    COLOR: #669;");
		output1.write("    FONT-FAMILY: Mic Shell Dlg;");
		output1.write("   FONT-SIZE: 16pt;");
		output1.write("    FONT-WEIGHT: bold");
		output1.write("}");
		output1.write(".bg_darkblue");
		output1.write("{");
		output1.write("   BACKGROUND-COLOR: #669");
		output1.write("}");
		output1.write(".bg_midblue");
		output1.write("{");
		output1.write("    BACKGROUND-COLOR: #99c");
		output1.write("}");
		output1.write(".bg_gray_eee");
		output1.write("{");
		output1.write("   BACKGROUND-COLOR: #eee");
		output1.write("}");
		output1.write(".text");
		output1.write("{");
		output1.write("   FONT-FAMILY: Mic Shell Dlg;");
		output1.write("   FONT-SIZE: 10pt");
		output1.write("}");
		output1.write(".tablehl");
		output1.write("{");
		output1.write("   BACKGROUND-COLOR: #eee;");
		output1.write("  COLOR: #669;");
		output1.write("   FONT-FAMILY: Mic Shell Dlg;");
		output1.write("   FONT-SIZE: 10pt;");
		output1.write("  FONT-WEIGHT: bold;");
		output1.write("  LINE-HEIGHT: 14pt");
		output1.write("}");
		output1.write(".Failed");
		output1.write("{");
		output1.write("    COLOR: #f03;");
		output1.write("    FONT-FAMILY: Mic Shell Dlg;");
		output1.write("    FONT-SIZE: 10pt;");
		output1.write("    FONT-WEIGHT: bold");
		output1.write("}");
		output1.write(".Passed");
		output1.write("{");
		output1.write("    COLOR: #096;");
		output1.write("    FONT-FAMILY: Mic Shell Dlg;");
		output1.write("    FONT-SIZE: 10pt;");
		output1.write("    FONT-WEIGHT: bold");
		output1.write("}");
		output1.write("</Style>");
		output1.write("</Head>");
		output1.write("<Title>Detail Report</Title>");
		output1.write("<hr class=" + "bg_midblue" +">");
		output1.write("<div align="+ "center"+ "><span class=" + "hl1" + " Localizable_1=" + "True" + ">" + projectName+ " Detail Results </span></div>");
		output1.write("<hr class=" + "bg_darkblue" + ">");
		output1.write("<table border=" + "0" + "cellpadding=" + "3" + " cellspacing=" + "1" + " width=" + "100%" + " bgcolor=" + "#666699" + ">");
		output1.write("<tr><td bgcolor=" + "white" + ">");
		output1.write("<table border=" + "0" + " cellpadding=" + "3" + " cellspacing=" + "0" + " width=" + "100%" + ">");
		output1.write("<tr>");
		output1.write("<td valign=" + "middle" + " align=" + "center" + " class=" + "tablehl" + "> <span class=" + "tablehl" + ">Test Case Name</span></td>");
		output1.write("<td valign=" + "middle" + " align=" + "center" + " class=" + "tablehl" + "> <span class=" + "tablehl" + ">Execution Date</span></td>");		
		output1.write("<td valign=" + "middle" + " align=" + "center" + " class=" + "tablehl" + "> <span class=" + "tablehl" + ">UserName</span></td>");
		output1.write("</tr>");
		output1.write("<tr>");
		output1.write("<td  height=" + "1" + " class=" + "bg_darkblue" + "></td>");
		output1.write("<td  height=" + "1" + " class=" + "bg_darkblue" + "></td>");
		output1.write("<td  height=" + "1" + " class=" + "bg_darkblue" + "></td>");
		output1.write("<td  height=" + "1" + " class=" + "bg_darkblue" + "></td>");
		output1.write("</tr><tr>");
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		output1.write("<td valign=" + "middle" + " align=" + "center" + " height=" + "20" + "><span class=" + "text" + ">" + gTestCaseName + "</span></a></td>");
		output1.write("<td valign=" + "middle" + " align=" + "center" + " height=" + "20" + "><span class=" + "text" + ">"+ dateFormat.format(date) +"</span></td>");
		output1.write("<td valign=" + "middle" + " align=" + "center" + " height=" + "20" + "><span class=" + "text" + ">"+ "" +"</span></td>");
		output1.write("<td valign=" + "middle" + " align=" + "center" + " height=" + "20" + "><span class=" + "text" + ">"+ System.getProperty("user.name") +"</span></td>");
		output1.write("</tr>");
		output1.write("<tr>");
		output1.write("<td  height=" + "1" + " class=" + "bg_gray_eee" + "></td>");
		output1.write("<td  height=" + "1" + " class=" + "bg_gray_eee" + "></td>");
		output1.write("<td  height=" + "1" + " class=" + "bg_gray_eee" + "></td>");
		output1.write("<td  height=" + "1" + " class=" + "bg_gray_eee" + "></td>");
		output1.write("</tr>");
		output1.write("</table> ");
		output1.write("</td></tr>");
		output1.write("</table> ");
		output1.write("<br>");
		output1.write("<table border=" + "0" + "cellpadding=" + "3" + " cellspacing=" + "1" + " width=" + "100%" + " bgcolor=" + "#666699" + ">");
		output1.write("<tr><td bgcolor="+"white"+">");
		output1.write("<table border="+"0"+" cellpadding="+"3"+" cellspacing="+"0" +" width="+"100%"+">");
		output1.write("<tr>");
		output1.write("<td valign="+"middle" +" align="+"center" +" class="+"tablehl"+"> <span class="+"tablehl"+">Step No</span></td>");
		output1.write("<td valign="+"middle" +" align="+"center" +" class="+"tablehl"+"> <span class="+"tablehl"+">Step Description</span></td>");
		output1.write("<td valign="+"middle" +" align="+"center" +" class="+"tablehl"+"> <span class="+"tablehl"+">Expected</span></td>");
		output1.write("<td valign="+"middle" +" align="+"center" +" class="+"tablehl"+"> <span class="+"tablehl"+">Actual</span></td>");
		output1.write("<td valign="+"middle" +" align="+"center" +" class="+"tablehl"+"> <span class="+"tablehl"+">Status</span></td>");
		output1.write("<td valign="+"middle" +" align="+"center" +" class="+"tablehl"+"> <span class="+"tablehl"+">Screenshot</span></td>");
		output1.write("</tr>");
	}

	public void DisplayDetailTestDuration() throws Exception{
		output1.write("</table>");
		output1.write("</table>");
		output1.write("</br>");
		output1.write("<table border=" + "0" + "cellpadding=" + "3" + " cellspacing=" + "1" + " width=" + "100%" + " bgcolor=" + "#666699" + ">");
		output1.write("<tr><td bgcolor=" + "white" + ">");
		output1.write("<table border=" + "0" + " cellpadding=" + "3" + " cellspacing=" + "0" + " width=" + "100%" + ">");
		output1.write("<tr>");
		output1.write("<td valign=" + "middle" + " align=" + "center" + " class=" + "tablehl" + "> <span class=" + "tablehl" + ">Status</span></td>");
		output1.write("<td valign=" + "middle" + " align=" + "center" + " class=" + "tablehl" + "> <span class=" + "tablehl" + ">Duration</span></td>");
		output1.write("</tr>");
		output1.write("<tr>");
		output1.write("<td  height=" + "1" + " class=" + "bg_darkblue" + "></td>");
		output1.write("<td  height=" + "1" + " class=" + "bg_darkblue" + "></td>");
		output1.write("</tr><tr>");
		output1.write("<td valign=" + "middle" + " align=" + "center" + " class=" + "text" + "> <span class=" + "text" + ">Run Started</span></td>");
		output1.write("<td valign=" + "middle" + " align=" + "center" + " height=" + "20" + "><span class=" + "text" + ">"+ gdetailStarttime +"</span></td>");
		output1.write("</tr>");
		output1.write("<tr>");
		output1.write("<td  height=" + "1" + " class=" + "bg_darkblue" + "></td>");
		output1.write("<td  height=" + "1" + " class=" + "bg_darkblue" + "></td>");
		output1.write("</tr>");
		output1.write("<tr>");
		gdetailEndtime=new Date();
		output1.write("<td valign=" + "middle" + " align=" + "center" + " class=" + "text" + "> <span class=" + "text" + ">Run ended</span></td>");
		output1.write("<td valign=" + "middle" + " align=" + "center" + " height=" + "20" + "><span class=" + "text" + ">"+ gdetailEndtime +"</span></td>");
		output1.write("</tr>");
		output1.write("<tr>");
		output1.write("<td  height=" + "1" + " class=" + "bg_darkblue" + "></td>");
		output1.write("<td  height=" + "1" + " class=" + "bg_darkblue" + "></td>");
		output1.write("</tr>");
		output1.write("<tr>");
		GetTotalTime (gdetailStarttime,gdetailEndtime);
		output1.write("<td valign=" + "middle" + " align=" + "center" + " class=" + "text" + "> <span class=" + "text" + ">Total Duration</span></td>");
		output1.write("<td valign=" + "middle" + " align=" + "center" + " height=" + "20" + "><span class=" + "text" + ">"+ gfinaltime +"</span></td>");
		output1.write("</tr>");
		output1.write("</table>");
		output1.write("</td></tr>");
		output1.write("</tr>");
	}
	
	
	public void waitforobjecttoload(String locator,String locatortype,int times) throws Exception
	{    
     

		if(locatortype.toLowerCase().equalsIgnoreCase("id"))
		{
			WebElement myDynamicElement = (new WebDriverWait(driver, times))
					.until(ExpectedConditions.presenceOfElementLocated(By.id(locator)));
		}
		else if(locatortype.toLowerCase().equalsIgnoreCase("name"))
		{
			WebElement myDynamicElement = (new WebDriverWait(driver, times))
					.until(ExpectedConditions.presenceOfElementLocated(By.name(locator)));
		}
		else if(locatortype.toLowerCase().equalsIgnoreCase("css"))
		{
			WebElement myDynamicElement = (new WebDriverWait(driver, times))
					.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(locator)));
		}
		else if(locatortype.toLowerCase().equalsIgnoreCase("xpath"))
		{
			WebElement myDynamicElement = (new WebDriverWait(driver, times))
					.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
		}
		else if(locatortype.toLowerCase().equalsIgnoreCase("link"))
		{
			WebElement myDynamicElement = (new WebDriverWait(driver, times))
					.until(ExpectedConditions.presenceOfElementLocated(By.linkText(locator)));
		}
		else
		{
			Thread.sleep(20000);
		}
      
      
	}

	

	public boolean verifyObjectExist(String locator,String locatortype,int times) throws Exception
	{    
		try{ 


			if(locatortype.toLowerCase().equalsIgnoreCase("id"))
			{
				WebElement myDynamicElement = (new WebDriverWait(driver, times))
						.until(ExpectedConditions.presenceOfElementLocated(By.id(locator)));
			}
			else if(locatortype.toLowerCase().equalsIgnoreCase("name"))
			{
				WebElement myDynamicElement = (new WebDriverWait(driver, times))
						.until(ExpectedConditions.presenceOfElementLocated(By.name(locator)));
			}
			else if(locatortype.toLowerCase().equalsIgnoreCase("css"))
			{
				WebElement myDynamicElement = (new WebDriverWait(driver, times))
						.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(locator)));
			}
			else if(locatortype.toLowerCase().equalsIgnoreCase("xpath"))
			{
				WebElement myDynamicElement = (new WebDriverWait(driver, times))
						.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
			}
			else if(locatortype.toLowerCase().equalsIgnoreCase("link"))
			{
				WebElement myDynamicElement = (new WebDriverWait(driver, times))
						.until(ExpectedConditions.presenceOfElementLocated(By.linkText(locator)));
			}
			else
			{
				Thread.sleep(20000);
			}
			return true;
		}catch(Exception e)
		{
			CaptureScreenshot();
			ReportStep("element" +"</b>"+" ("+locator+") object" ,"did not exist "+" ("+locator+") object should be successful", "+ ("+locator+") object failed due to "+e.getMessage()+"<b> <a href=\"" + ScreenShotName+"\""+">link to screenshot</a>", "fail");
			driver.close();
			gStarttestcase=gStarttestcase+1;
			EndHtmlDetailReporter();
			Assert.fail("Veify  " +"element" +"("+locator+") object failed due to " +e.getMessage());
			return false;

		}

	}
	public void waitForPagetoLoad(int times) throws Exception
	{
		try
		{
			ExpectedCondition<Boolean> pageLoadCondition = new
					ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
				}
			};
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(pageLoadCondition);
		}catch (Exception e)
		{
			Assert.fail("Wait for page to load failed due to "+e.getMessage());
		}
	}

	public void rightClickonObject(String locator,String locatorType,String objectName) throws Exception
	{
		try
		{   waitforobjecttoload(locator,locatorType,60);
		WebElement driverObj = findobjType(locatorType,locator);
		Actions actions = new Actions(driver);    
		Action action=actions.contextClick(driverObj).build(); //pass WebElement as an argument
		action.perform();
		ReportStep("Perform right click operation on <b>" +objectName +"</b>"+" ("+locator+") object" ,"Perform right click operation on <b>" +objectName +"</b>"+" ("+locator+") object should be successful", "Perform right click operation on <b>"+objectName +"</b>"+" ("+locator+") object is successful", "pass");
		Reporter.log("Perform right click operation on " +objectName +" ("+locator+") object is successful",true);

		}
		catch(Exception e)
		{
			CaptureScreenshot();
			ReportStep("Perform right click operation on <b>" +objectName +"</b>"+" ("+locator+") object" ,"Perform right click operation on <b>" +objectName +"</b>"+" ("+locator+") object should be successful", "Perform right click operation on <b>"+objectName +"</b>"+" ("+locator+") object failed due to "+e.getMessage(), "fail");
			driver.close();
			gStarttestcase=gStarttestcase+1;
			EndHtmlDetailReporter();
			Assert.fail("Perform right click operation on " +objectName +"("+locator+") object failed due to " +e.getMessage());
		}
	}

	public void switchtoFrame(int index) throws Exception{
		driver.switchTo().frame(index);     
	}


	public void switchtoWindow(String locator,String windowType) throws Exception
	{
		try
		{ 
			if (windowType.equalsIgnoreCase("frame"))
			{

				driver.switchTo().frame(locator);     
			}
			else if (windowType.equalsIgnoreCase("window"))
			{
				oldWindow=driver.getWindowHandle();
				Thread.sleep(5000);
				for (String popUpHandle : driver.getWindowHandles()) {
					if(!popUpHandle.equals(oldWindow)){

						driver.switchTo().window(popUpHandle);

					}
				}
			}
			else if (windowType.equalsIgnoreCase("alert"))
			{   
				oldWindow=driver.getWindowHandle();
				driver.switchTo().alert();
				locator="";
			}

			ReportStep("Perform switch to "+windowType +" operation on "+locator+" object" ,"Perform switch to "+windowType +" on "+locator+" object should be successful", "Perform switch to "+windowType +" on "+" ("+locator+") object is successful", "pass");
			Reporter.log("Perform switch to "+windowType +" operation on "+locator+" object is successful",true);

		}catch(Exception e)
		{
			CaptureScreenshot();
			ReportStep("Perform switch to "+windowType +" operation on "+locator+" object" ,"Perform switch to "+windowType +" on "+locator+" object should be successful", "Perform switch to "+windowType +" on "+" ("+locator+") object is failed due to "+ e.getMessage(), "fail");
			driver.close();

			gStarttestcase=gStarttestcase+1;
			EndHtmlDetailReporter();
			Assert.fail("Perform switch to "+windowType +" operation on "+locator+" object failed due to " +e.getMessage());
		}
	}
	public void switchback(String windowType) throws Exception
	{
		try
		{ 
			if (windowType.equalsIgnoreCase("frame"))
			{

				driver.switchTo().defaultContent();
			}
			else if (windowType.equalsIgnoreCase("window"))
			{

				driver.switchTo().window(oldWindow);
			}
			else if (windowType.equalsIgnoreCase("alert"))
			{   

				driver.switchTo().window(oldWindow);

			}

			ReportStep("Perform switchback to "+windowType +" operation" ,"Perform switchback to "+windowType +" object should be successful", "Perform switchback to "+windowType +" object is successful", "pass");
			Reporter.log("Perform switchback to "+windowType +" operation is successful",true);

		}catch(Exception e)
		{
			CaptureScreenshot();
			ReportStep("Perform switchback to "+windowType +" operation" ,"Perform switchback to "+windowType +" object should be successful", "Perform switchback to "+windowType +" object is failed due to "+ e.getMessage(), "fail");
			driver.close();


			gStarttestcase=gStarttestcase+1;
			EndHtmlDetailReporter();
			Assert.fail("Perform switchback to "+windowType +" object failed due to " +e.getMessage());
		}
	}
	
	public void selectbyindex(String locator,String locatorType,int actionValue,String objectName)throws Exception
	{
		try
		{
			WebElement driverObj = findobjType(locatorType,locator);
			new Select(driverObj).selectByIndex(actionValue);
			ReportStep("Perform select operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue, "Perform select operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue +" should be successful", "Perform select operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue+" is successful", "pass");
			Reporter.log("Perform select operation on " +objectName +" ("+locator+") object with value "+actionValue +" is successful",true);

		}
		catch(Exception e)
		{
			CaptureScreenshot();
			ReportStep("Perform select operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue, "Perform select operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue +" should be successful", "Perform select operation on <b>"+objectName +"</b>("+locator+") object with value "+actionValue+" failed due to "+e.getMessage(), "fail");
			driver.close();
			gStarttestcase=gStarttestcase+1;
			EndHtmlDetailReporter();
			Assert.fail("Perform select operation failed due to " +e.getMessage());
		}
	}
	
	public void actionClickObject(String locator,String locatorType,String objectName) throws Exception
	{
		try
		{   waitforobjecttoload(locator,locatorType,60);
		WebElement driverObj = findobjType(locatorType,locator);
		Actions actions = new Actions(driver);    
		Action action=actions.doubleClick(driverObj).build(); //pass WebElement as an argument
		
		action.perform();
		Thread.sleep(2000);
		ReportStep("Perform mousemove operation on <b>" +objectName +"</b>"+" ("+locator+") object" ,"Perform mousemove operation on <b>" +objectName +"</b>"+" ("+locator+") object should be successful", "Perform mousemove operation on <b>"+objectName +"</b>"+" ("+locator+") object is successful", "pass");
		Reporter.log("Perform mousemove operation on " +objectName +" ("+locator+") object is successful",true);

		}
		catch(Exception e)
		{
			CaptureScreenshot();
			ReportStep("Perform mousemove operation on <b>" +objectName +"</b>"+" ("+locator+") object" ,"Perform mousemove operation on <b>" +objectName +"</b>"+" ("+locator+") object should be successful", "Perform mousemove operation on <b>"+objectName +"</b>"+" ("+locator+") object failed due to "+e.getMessage(), "fail");
			driver.close();
			gStarttestcase=gStarttestcase+1;
			EndHtmlDetailReporter();
			Assert.fail("Perform mousemove operation on " +objectName +"("+locator+") object failed due to " +e.getMessage());
		}
	}

	
}



