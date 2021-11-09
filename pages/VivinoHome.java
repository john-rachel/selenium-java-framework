package pages;


import java.util.HashMap;

import org.openqa.selenium.By;


import core.Generic;

/* 
 * This class - page class describing the locators, methods operations
 * */
public class VivinoHome {

	
	 public static String txtSearch = "//input[@name='q']";
	 public static String resultList = "//div[@class='search-results-list']/div";
	 public static String firstResult="(//div[@class='search-results-list']/div[1]//div[contains(@class,'content')]//span)[1]";
	 public static String heading = "//div[@class='grid topSection']//h1/span[2]";
	
	 /* 
	  * This method - launch browser by sending the url from testng xml
	  * */
public void launch(Generic gen, String baseUrl) throws Exception{
		 gen.openURL(baseUrl);
		 gen.waitTime();
}

/* 
 * This method - search keyword - storing wine titles and verifying the keyword
 * clicking on the result
 * 
 * */
public void search(Generic gen, String wine) throws Exception{
	    gen.setValueonObject(txtSearch, "xpath", wine, "Search box");
	    gen.robs(10);
	    gen.waitTime();
	    int Resultsize =  gen.driver.findElements(By.xpath(resultList)).size();
	   
	    System.out.println(Resultsize);
	    
	    HashMap<Integer,String> hm = new HashMap<Integer,String>();
	    for(int c=1;c<Resultsize;c++){
	    	
	    	hm.put(c, gen.getText("(//div[@class='search-results-list']/div["+c+"]//div[contains(@class,'content')]//span)[1]", "xpath","wine link"));
	    	    	
	    }
	    System.out.println("The list of wines which has <keyword>"+wine+"::\n");
	    
	    for(int x:hm.keySet()){
	    	if(hm.get(x).contains(wine)){
	    		System.out.println(hm.get(x));
	    	}
	    }
	    gen.waitTime();
	    gen.clickonObject(firstResult, "xpath", "first link");
	    
	    String title=gen.getText(heading, "xpath", "title");
	    
	    //Verifying the heading contains the keyword
	    
	   boolean val= title.contains(wine);
	   
	   System.out.println("The keyword is present in the title ::"+val);
	   
}
		 
		 
}
