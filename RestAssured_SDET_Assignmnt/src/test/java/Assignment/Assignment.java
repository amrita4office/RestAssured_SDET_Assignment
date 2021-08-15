package Assignment;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


public class Assignment {
	
	public String accessToken,username,password;
			
	@Test @DataProvider(name="adminLogin")
	public Object[][] loginData() throws BiffException, IOException {
		Object[][] arrayObject = getExcelData("src\\test\\resources\\RestTestData.xls","TC002");
		return arrayObject;
	}


	public String[][] getExcelData(String fileName, String sheetName) throws BiffException, IOException 
	{
		String[][] arrayExcelData = null;
			FileInputStream fs = new FileInputStream(fileName);
			Workbook wb = Workbook.getWorkbook(fs);
			Sheet sh = wb.getSheet(sheetName);

			int totalNoOfCols = sh.getColumns();
			int totalNoOfRows = sh.getRows();
			
			arrayExcelData = new String[totalNoOfRows-1][totalNoOfCols];
			
			for (int i= 1 ; i < totalNoOfRows; i++) 
			{

				for (int j=0; j < totalNoOfCols; j++) 
				{
					arrayExcelData[i-1][j] = sh.getCell(j, i).getContents();
					System.out.println(arrayExcelData[i-1][j]);
				}

			}
		return arrayExcelData;
	}



	
	@Test(enabled=true)
	public void TC_001() {
		
		basicAuthentication();
		

	}

	@Test(enabled=true)
	public void TC_002() {
		basicAuthentication();
		login();
		GetAdminUserDetails();
		Logout();

	}
	@Test(enabled=true)
	public void TC_015() {
		basicAuthentication();
		login();
		GetCategoryFilterBylimitPage();
		Logout();

	}
	@Test(enabled=true)
	public void TC_021() {
		basicAuthentication();
		login();
		ProductFilterByLimit();
		Logout();

	}
	
	public void login()
	{	RestAssured.baseURI="http://rest-api.upskills.in";
	Response response = given()
			.header("content-Type","application/json")
			.header("Authorization","bearer "+accessToken)
			.body("{\r\n" + 
					"  \"username\": \"upskills_admin\",\r\n" + 
					"  \"password\": \"Talent4$$\"\r\n" + 
					"}")

			.when()
			.post("/api/rest_admin/login")
			.then()
			.assertThat().statusCode(200).extract().response();
		
	}
	public void basicAuthentication()
	{
		RestAssured.baseURI="http://rest-api.upskills.in";
				
		Response response = given()
				.header("content-Type","application/json")
				.header("Authorization","Basic dXBza2lsbHNfcmVzdF9hZG1pbl9vYXV0aF9jbGllbnQ6dXBza2lsbHNfcmVzdF9hZG1pbl9vYXV0aF9zZWNyZXQ=")
				.header("Grant type","client_credentials")
				
				.when()
				.post("/api/rest_admin/oauth2/token/client_credentials")
				.then()
				.assertThat().statusCode(200).extract().response();
			    System.out.println(response.asString());
			    String jsonResp = response.asString();
				JsonPath responseBody = new JsonPath(jsonResp);
				accessToken=responseBody.get("data.access_token");
				//System.out.println("The AccessToken is "+accessToken);
		
	}
	
	public void GetAdminUserDetails()
	{ 
		RestAssured.baseURI="http://rest-api.upskills.in";
		Response response = given()
			.header("content-Type","application/json")
			.header("Authorization","bearer "+accessToken)
		
			.when()
			.get("/api/rest_admin/user")
			.then()
			.assertThat().statusCode(200).extract().response();
		System.out.println(response.asString());
	}
	
	public void Login()
	{
		RestAssured.baseURI="http://rest-api.upskills.in";
		Response response = given()
			.header("content-Type","application/json")
			.header("Authorization","bearer "+accessToken)
		
			.when()
			.post("/api/rest_admin/login")
			.then()
			.assertThat().statusCode(200).extract().response();
		System.out.println(response.asString());
	}
	
	public void Logout()
	{
		RestAssured.baseURI="http://rest-api.upskills.in";
		Response response = given()
			.header("content-Type","application/json")
			.header("Authorization","bearer "+accessToken)
		
			.when()
			.post("/api/rest_admin/logout")
			.then()
			.assertThat().statusCode(200).extract().response();
		System.out.println(response.asString());
	}
	
	public void GetCategoryFilterBylimitPage()
	{
		RestAssured.baseURI="http://rest-api.upskills.in";
		Response response = given()
			.header("content-Type","application/json")
			.header("Authorization","bearer "+accessToken)
		
			.when()
			.get("/api/rest_admin/categories/extended/limit/6/page/1")
			
			.then()
			.assertThat().statusCode(200).header("x-pagination-limit",equalTo("6")).extract().response();
		System.out.println(response.asString());
		
	}
	
	public void ProductFilterByLimit()
	{
		RestAssured.baseURI="http://rest-api.upskills.in";
		Response response = given()
			.header("content-Type","application/json")
			.header("Authorization","bearer "+accessToken)
		
			.when()
			.get("/api/rest_admin/products/limit/2/page/1")
			
			.then()
			.assertThat().statusCode(200).header("x-pagination-limit",equalTo("2")).extract().response();
		System.out.println(response.asString());
	}

}
