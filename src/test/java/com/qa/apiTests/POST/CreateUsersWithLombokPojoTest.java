package com.qa.apiTests.POST;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import com.qa.api.Data.Users;
import com.qa.api.Data.UsersByLombok;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class CreateUsersWithLombokPojoTest {
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
    public static String getRandomEmail(){
        String randomEmail= "TestAutomation"+System.currentTimeMillis()+"@gmail.com";
        return randomEmail;
    }

    @Test
    public void createUserPostCallUsingPOJO_LambokTest() throws IOException {
        /* if we use lombok library need to create object by using build */
        UsersByLombok users = UsersByLombok.builder()
                .name("testAbg")
                .email(getRandomEmail())
                .gender("female")
                .status("inactive").build();


        APIResponse postApiResponse = apiRequestContext.post("https://gorest.co.in//public/v2/users", RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer d0418e269f6257319312a72819c83cc742bf932e2dd5df88aa4db7629e685495")
                .setData(users)
        );
        System.out.println("Post api response status code is  :"+postApiResponse.status());
        Assert.assertEquals(postApiResponse.statusText(),"Created");
        String postResponseText=postApiResponse.text();
        System.out.println(postResponseText);
//        convert response text/json to pojo by deserialization

        ObjectMapper objectMapper = new ObjectMapper();
        UsersByLombok actualUser=objectMapper.readValue(postResponseText,UsersByLombok.class);

        System.out.println(actualUser.getEmail());
        System.out.println("-----------Actual User from Response is------------------------- : "+ actualUser );
        String userId=  actualUser.getId();
        System.out.println("User Id is : "+userId);
        Assert.assertEquals(actualUser.getName(),users.getName());
        Assert.assertEquals(actualUser.getEmail(),users.getEmail());
        Assert.assertEquals(actualUser.getGender(),users.getGender());
        Assert.assertEquals(actualUser.getStatus(),users.getStatus());
        Assert.assertNotNull(actualUser.getId());

//      Get call-get the same user by Id
        APIResponse getApiResponse = apiRequestContext.get("https://gorest.co.in/public/v2/users/"+userId,
                RequestOptions.create()
                        .setHeader("Authorization","Bearer d0418e269f6257319312a72819c83cc742bf932e2dd5df88aa4db7629e685495"));

        Assert.assertEquals(getApiResponse.status(),200);
        Assert.assertEquals(getApiResponse.statusText(),"OK");
        System.out.println("-----------------Response text from Get method is -------:"+getApiResponse.text());
        Assert.assertTrue(getApiResponse.text().contains(userId));
        Assert.assertTrue(getApiResponse.text().contains(actualUser.getName()));


    }

}
