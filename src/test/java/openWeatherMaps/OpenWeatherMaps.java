package openWeatherMaps;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OpenWeatherMaps {

	static WebDriver driver = null;
	static WebDriverWait wait = null;
	String strOpenWeatherMapsURL = "https://openweathermap.org/";
	
	/**
	 * Configure the initial setup for WebDriver
	 */
	@BeforeClass
	public static void setup (){
		
		//Configure ChromeDriver to to Path for Mac 
		if (System.getProperty("os.name").toLowerCase().contains("mac")){
			System.setProperty("webdriver.chrome.driver", "chromedriver");	
		}
		//Set ChromeDriver to to Path for Unix / Linux
		else if (System.getProperty("os.name").toLowerCase().contains("nix") || System.getProperty("os.name").toLowerCase().contains("nux") || System.getProperty("os.name").toLowerCase().contains("aix")){
			System.setProperty("webdriver.chrome.driver", "chromedriver_linux_2_38");
		}
		else if (System.getProperty("os.name").toLowerCase().contains("win") ){
			System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		}
		else {
			System.err.println("Please configure Chrome Driver in Setup");
			System.exit(0);
		}
		
	}
	
	
	/**
	 * Launch Open Weather Maps before each test
	 */
	@Before
	public void navigateTo_OpenWeatherMaps (){
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 30);
		driver.get(strOpenWeatherMapsURL);
	}
	
	@Test
	public void verifySiteHomePage (){
		
		//Verify Site Logo - //*[@src="/themes/openweathermap/assets/vendor/owm/img/logo_OpenWeatherMap_orange.svg"]
		boolean rc = isElementVisible(By.xpath("//*[@src=\"/themes/openweathermap/assets/vendor/owm/img/logo_OpenWeatherMap_orange.svg\"]"), "Site Logo");
		Assert.assertTrue(rc);
		
		//Verify Menu Item - //*[@class="nav__item"]//*[text()='Weather']
		rc = isElementVisible(By.xpath("//*[@class=\"nav__item\"]//*[text()='Weather']"), "Weather Menu Item");
		Assert.assertTrue(rc);
		
		//Verify Maps Menu Item - //*[@class="nav__item dropdown"]//*[contains (text(), 'Maps')]
		rc = isElementVisible(By.xpath("//*[@class=\"nav__item dropdown\"]//*[contains (text(), 'Maps')]"), "Maps Menu Item");
		Assert.assertTrue(rc);
		
		//Verify API - //*[@class="nav__item"]//*[text()='API']
		rc = isElementVisible(By.xpath("//*[@class=\"nav__item\"]//*[text()='API']"), "API Menu Item");
		Assert.assertTrue(rc);
		
		//Verify - //*[@id="metric"]
		rc = isElementVisible(By.xpath("//*[@id=\"metric\"]"), "Deg Cel");
		Assert.assertTrue(rc);
		
		//Verify //*[@id="imperial"]
		rc = isElementVisible(By.xpath("//*[@id=\"imperial\"]"), "Deg F");
		Assert.assertTrue(rc);
		
	}
	
	/**
	 * Will verify whether element is visible on screen
	 * @param byLocator - by Locator - could be XPath / CSS / ID 
	 * @param strElementName - String representation of what the element is reffered to 
	 * @return
	 */
	public boolean isElementVisible (By byLocator, String strElementName){
		try 
		{
			wait.until(ExpectedConditions.visibilityOfElementLocated(byLocator));
			log(strElementName+" is visible", "Passed");
			return true;			
		}
		catch (Exception e) 
		{
			log(strElementName+" is not visible", "Failed");
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * Test whether 
	 * - after entering an invalida city name - ABCD 
	 * - the results does not show up in the list view
	 */
	
	@Test
	public void verifyOpenWeatherMap_valid_City_Name (){
		
		try 
		{

			//Search for city Name - Click id = nav-search
			String strCityName = "Mumbai";
			boolean rc = searchCityName(strCityName);
			Assert.assertTrue(rc);
		
			//Verify - Result Data Table exists with the name of the city searched with - //*[@id="forecast_list_ul"]//*[contains (text(), 'Bangalore, IN')]
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='forecast_list_ul']//*[contains (text(), '"+strCityName+"')]")));
			
			//Verify Flag for that city appears - //*[contains (@src, "http://openweathermap.org/images/flags")] 
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains (@src, 'http://openweathermap.org/images/flags')]")));
			log("Verified test after entering valid city name", "Passed");
			
		}
		catch (Exception e) 
		{
			log("Unable to verify test after entering valid city name", "Failed");
			e.printStackTrace();
		}
	}

	@Test
	public void verifyOpenWeatherMap_InValid_City_Name (){
		
		try {

			//Search for city Name - Click id = nav-search
			String strCityName = "ABC";
			boolean rc = searchCityName(strCityName);
			Assert.assertTrue(rc);
		
			//Verify - Result Data Table exists with the name of the city searched with - //*[@id="forecast_list_ul"]//*[contains (text(), 'Bangalore, IN')]
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains (text(), 'Not found')]")));
			log("Verified test for invalid city name", "Passed");

		} catch (Exception e) {
			e.printStackTrace();
			log("Unable to verify test for invalid city name", "Failed");
		}		
	}

	
	/**
	 * Will search for the city name and verify result page shows after searching
	 * Precondition - Should have home page loaded
	 * @param strCityName
	 * @return
	 */
	public boolean searchCityName (String strCityName){
		try {

			//Search for city Name - Click id = nav-search
			driver.findElement(By.id("nav-search")).click();;
			
			//Type City Name - id="q"
			wait.until(ExpectedConditions.invisibilityOfElementLocated((By.id("nav-search"))));
			driver.findElement(By.id("q")).sendKeys(strCityName + Keys.ENTER);
			
			//Verify - Result Data Table exists - //*[@id="forecast_list_ul"]
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='forecast_list_ul']")));
			log("Successfully searched City", "Passed");
			return true;
		} catch (Exception e) {
			log("Unable to search for City", "Failed");
			e.printStackTrace();
			return false;
		}
	}
	
	@After
	public void teardown (){
		driver.close();
		driver.quit();
	}
	
	public static void log(String strMessage, String strStatus){
		System.out.println(strStatus+ " : "+strMessage);
	}
	
}
