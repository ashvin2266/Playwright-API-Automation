package com.qa.apiTests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Map;

public class GET_API_CALL {
    Playwright playwright;
    APIRequest request;
    APIRequestContext apiRequestContext;
    @BeforeTest
    public void setUp(){
        playwright = Playwright.create();
        request = playwright.request();
        apiRequestContext = request.newContext();

    }

    @Test
    public void getUsersApiTest() throws IOException {
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
        System.out.println("------Api Response in Text method--------");
        String apiResponsetext = apiResponse.text();
        System.out.println(apiResponsetext);

        byte[] body = apiResponse.body();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponseNode = objectMapper.readTree(body);
        String prettyJsonResponse = jsonResponseNode.toPrettyString();
        System.out.println("Pretty json string response :" + prettyJsonResponse);

//       verify api request url
        String apiRequesturl = apiResponse.url();
        System.out.println("Print api request URL: " + apiRequesturl);
        Assert.assertEquals(apiRequesturl, "https://gorest.co.in/public/v2/users");


        /*
        How to verify the response body with diff assertion
         */
        Map<String, String> responseHeaders = apiResponse.headers();
        System.out.println("Response Headers:" + responseHeaders);
//        verify the response headers
        Assert.assertEquals(responseHeaders.get("content-type"), "application/json; charset=utf-8");
        Assert.assertEquals(responseHeaders.get("x-download-options"), "noopen");


    }
    @Test
    public void getSpecificUserApiCallTest() throws IOException {
        APIResponse apiResponse=apiRequestContext.get("https://gorest.co.in/public/v2/users",
                RequestOptions.create().
                        setQueryParam("gender","male").
                        setQueryParam("status","active")



                );
        int statusCode=apiResponse.status();
        System.out.println("API Responce status code:" +statusCode);
        Assert.assertEquals(statusCode,200);
        String statusText=apiResponse.statusText();
        System.out.println("API Response status code text: "+statusText);
        Assert.assertEquals(statusText,"OK");
//        api response ok method gives you boolean value response code bet 200-299
        Assert.assertEquals(apiResponse.ok(),true);
        System.out.println("------Api Response in Text method--------");
        String apiResponsetext = apiResponse.text();
        System.out.println(apiResponsetext);
        byte[] body = apiResponse.body();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponseNode = objectMapper.readTree(body);
        String prettyJsonResponse = jsonResponseNode.toPrettyString();
        System.out.println("Pretty json string response :" + prettyJsonResponse);



    }
    @AfterTest
    public void tearDown(){
        playwright.close();
    }

}
