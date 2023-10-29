package com.qa.apiTests.POST;

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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

public class CreateUserWithJsonFileTest {
    Playwright playwright;
    APIRequest request;
    APIRequestContext apiRequestContext;
    @BeforeTest
    public void setUp(){
        playwright = Playwright.create();
        request = playwright.request();
        apiRequestContext = request.newContext();

    }
    @AfterTest
    public void tearDown(){
        playwright.close();
    }
    @Test
            public void createUserWithJsonFileTest() throws IOException {


    //        Creating json body with json file create one folder for json data file and use it here
//        get json file here we convert data file to byte arry because set data takes byte array

        byte[] fileBytes=null;
        File file= new File("src/test/java/Data/user.Json");
        fileBytes= Files.readAllBytes(file.toPath());



       APIResponse postApiResponse = apiRequestContext.post("https://gorest.co.in//public/v2/users", RequestOptions.create()
            .setHeader("Content-Type", "application/json")
            .setHeader("Authorization", "Bearer d0418e269f6257319312a72819c83cc742bf932e2dd5df88aa4db7629e685495")
            .setData(fileBytes)
    );
        System.out.println("Post api response status code is  :"+postApiResponse.status());
        Assert.assertEquals(postApiResponse.statusText(),"Created");
        System.out.println(postApiResponse.text());

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode postJsonResponse= objectMapper.readTree(postApiResponse.body());
    String prettyJsonResponse = postJsonResponse.toPrettyString();
        System.out.println(prettyJsonResponse);
    String userId=  postJsonResponse.get("id").asText();
        System.out.println("User Id is : "+userId);

    //      Get call-get the same user by Id
    APIResponse getApiResponse = apiRequestContext.get("https://gorest.co.in/public/v2/users/"+userId,
            RequestOptions.create()
                    .setHeader("Authorization","Bearer d0418e269f6257319312a72819c83cc742bf932e2dd5df88aa4db7629e685495"));

        Assert.assertEquals(getApiResponse.status(),200);
        Assert.assertEquals(getApiResponse.statusText(),"OK");
        System.out.println(getApiResponse.text());
        Assert.assertTrue(getApiResponse.text().contains(userId));
        Assert.assertTrue(getApiResponse.text().contains("ashvin"));


}


}




