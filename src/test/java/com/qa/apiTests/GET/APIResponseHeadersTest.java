package com.qa.apiTests.GET;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.HttpHeader;
import com.sun.net.httpserver.Headers;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class APIResponseHeadersTest {
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
    public void getApiResponseHeadersTest(){

        APIResponse apiResponse= apiRequestContext.get("https://gorest.co.in/public/v2/users");
        System.out.println("Api Response status "+  apiResponse.status());
        Assert.assertEquals(apiResponse.status(),200);

//      Using Map
        System.out.println("==============Using Maps========================");
        Map<String, String> headersmap = apiResponse.headers();
        System.out.println("Total Response header is : " +headersmap.size());
        headersmap.forEach((k,v) -> System.out.println(k +" : "+ v));
        System.out.println("Content type header is : "+headersmap.get("content-type"));
        Assert.assertEquals(headersmap.get("server"),"cloudflare");
        Assert.assertEquals(headersmap.get("x-pagination-pages"),"297");
        Assert.assertEquals(headersmap.get("content-type"),"application/json; charset=utf-8");
//        using ArrayList
        System.out.println("==============Using ArrayList========================");
        List<HttpHeader> httpHeaders = apiResponse.headersArray();
        for (HttpHeader e :httpHeaders){
            System.out.println(e.name +" : "+ e.value);
        }


    }








    @AfterTest
    public void tearDown(){
        playwright.close();
    }
}
