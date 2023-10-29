package com.qa.apiTests.GET;

import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class ApiResponseDisposeMethod {

    Playwright playwright;
    APIRequest request;
    APIRequestContext apiRequestContext;

    @BeforeTest
    public void setUp() {
        playwright = Playwright.create();
        request = playwright.request();
        apiRequestContext = request.newContext();

    }

    @Test
    public void disposeResponseTest() throws IOException {
//        GET USER API REQUEST 1
        APIResponse apiResponse = apiRequestContext.get("https://gorest.co.in/public/v2/users");
        int statusCode = apiResponse.status();
        System.out.println("API Responce status code:" + statusCode);
        Assert.assertEquals(statusCode, 200);
        String statusText = apiResponse.statusText();
        System.out.println("API Response status code text: " + statusText);
        Assert.assertEquals(statusText, "OK");
//        api response ok method gives you boolean value response code bet 200-299
        Assert.assertEquals(apiResponse.ok(), true);

//        you can print api response in text also
        System.out.println("------Api Response body in Text before api response dispose --------");
        String apiResponseText = apiResponse.text();
        System.out.println(apiResponseText);
//        We have apiResponse.dispose() method its only dispose response body but its not dispose status code,status text,req url etc
        System.out.println("--------------after dispose method its dispose response body");
       try {
            apiResponse.dispose();//this method dispose response body

        String apiResponseText1 = apiResponse.text();
        System.out.println(apiResponseText1);}
        catch (PlaywrightException e){
            System.out.println("Response body is disposed ");
        }
        int statusCode1 = apiResponse.status();
        System.out.println("API Responce status code After Disposed:" + statusCode1);
        Assert.assertEquals(statusCode, 200);
        String statusText1 = apiResponse.statusText();
        System.out.println("API Response status code text After Disposed: " + statusText1);
        Assert.assertEquals(statusText, "OK");
        System.out.println("Api response URL: "+apiResponse.url() );

//          Api Request 2
        System.out.println("-----------API REQUEST2 RESPONSE---------");
        APIResponse apiResponse1 = apiRequestContext.get("https://reqres.in/api/users/2");
        int statusCode2 = apiResponse1.status();
        System.out.println("API Responce status code Request 2:" + statusCode2);
        Assert.assertEquals(statusCode2, 200);
        String statusText2 = apiResponse1.statusText();
        System.out.println("API Response status code text request 2: " + statusText2);
        Assert.assertEquals(statusText2, "OK");
//        api response ok method gives you boolean value response code bet 200-299
        Assert.assertEquals(apiResponse1.ok(), true);

//        you can print api response in text also
        System.out.println("------Api Response body in Text before api response dispose Request 2 --------");
        String apiResponseText1 = apiResponse1.text();
        System.out.println(apiResponseText1);

//        if we used requestContext.dispose method its dispose all request and response body
        System.out.println("-------------------After using request context dispose method---------------");
        apiRequestContext.dispose();
        System.out.println("------Api Response body in Text after api response dispose Request 2 --------");
       try{ String apiResponseText3 = apiResponse1.text();
        System.out.println(apiResponseText3);
        String apiResponseText2 = apiResponse.text();
        System.out.println(apiResponseText2);}
       catch (PlaywrightException e){
           System.out.println("------All request(req1 and req2) body is disposed with status code ,text,url also-------");
       }

    }
    @AfterTest
    public void tearDown(){
        playwright.close();
    }


}
