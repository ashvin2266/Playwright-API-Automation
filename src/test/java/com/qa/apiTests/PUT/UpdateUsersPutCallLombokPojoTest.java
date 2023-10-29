package com.qa.apiTests.PUT;

import com.fasterxml.jackson.core.JsonProcessingException;
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

public class UpdateUsersPutCallLombokPojoTest {
    //1.Step 1 to Create new user---Take UserId
    //2.Step 2 Update the new user--- update status active to inactive
    //3.Step 3 GET updated users details-- get details from updated users and verify its updated


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
    public void UpdateUsersPutCallLombokPojoTest() throws JsonProcessingException {
        //    Step 1 Create new User
        System.out.println("***********************************POST Call Response *******************************");
        UsersByLombok users = UsersByLombok.builder()
                .name("AbgTest")
                .email(getRandomEmail())
                .gender("male")
                .status("active").build();
        APIResponse postApiResponse = apiRequestContext.post("https://gorest.co.in//public/v2/users", RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer d0418e269f6257319312a72819c83cc742bf932e2dd5df88aa4db7629e685495")
                .setData(users)
        );
        System.out.println("Post api response status code is  :"+postApiResponse.status());
        Assert.assertEquals(postApiResponse.statusText(),"Created");
        String postResponseText=postApiResponse.text();
        System.out.println("New Users Response text is  : "+postResponseText);
//        convert response text/json to pojo by deserialization
        ObjectMapper objectMapper = new ObjectMapper();
        UsersByLombok actualUser=objectMapper.readValue(postResponseText,UsersByLombok.class);
        System.out.println("New Users Email is  : "+actualUser.getEmail());
        System.out.println("New Created User from Post Response is- : "+ actualUser );
        Assert.assertEquals(actualUser.getName(),users.getName());
        Assert.assertEquals(actualUser.getEmail(),users.getEmail());
        Assert.assertEquals(actualUser.getGender(),users.getGender());
        Assert.assertEquals(actualUser.getStatus(),users.getStatus());
        Assert.assertNotNull(actualUser.getId());
        String userId=  actualUser.getId();
        System.out.println("New Uses UserId is : "+ userId);

        //2.Step 2 Update the new user--- update status active to inactive
        System.out.println("***********************************PUT Call Response *******************************");
//        update users satus ctive to inactive and name Abgtest to AshvinTest
        users.setStatus("inactive");
        users.setName("AshvinTest");
        APIResponse putApiResponse = apiRequestContext.put("https://gorest.co.in//public/v2/users/"+userId, RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer d0418e269f6257319312a72819c83cc742bf932e2dd5df88aa4db7629e685495")
                .setData(users)
        );
        System.out.println("Put api response status code is  :"+putApiResponse.status());
        Assert.assertEquals(putApiResponse.statusText(),"OK");
        String putResponseText=putApiResponse.text();
        System.out.println("New Users Updated Response text is :" +putResponseText);
        //        convert response text/json to pojo by deserialization
//        ObjectMapper objectMapper = new ObjectMapper();
        UsersByLombok updatedUser=objectMapper.readValue(putResponseText,
                UsersByLombok.class);
        System.out.println(updatedUser.getEmail());
        System.out.println("Updated Users from PUT Response is------------------------- : "+ updatedUser );
        String updatedUserId=  updatedUser.getId();
        System.out.println("Updated users User Id is : "+ updatedUserId);
        Assert.assertEquals(updatedUser.getEmail(),users.getEmail());
        Assert.assertEquals(updatedUser.getGender(),users.getGender());
        Assert.assertNotNull(updatedUser.getId());
        System.out.println("************************Updated User Status and Updated name Assertion");
        System.out.println("Updated Users Name is  : "+ updatedUser.getName());
        Assert.assertEquals(updatedUser.getName(),users.getName());
        System.out.println("Updated Users Staus is  : "+ updatedUser.getStatus());
        Assert.assertEquals(updatedUser.getStatus(),users.getStatus());

//      Step 3 Get details od Updated users By GET request
        System.out.println("***********************************GET Call Response *******************************");

        APIResponse getUpdatedUserResponse = apiRequestContext.get("https://gorest.co.in/public/v2/users/"+userId,
                RequestOptions.create()
                        .setHeader("Authorization","Bearer d0418e269f6257319312a72819c83cc742bf932e2dd5df88aa4db7629e685495"));

        Assert.assertEquals(getUpdatedUserResponse.status(),200);
        Assert.assertEquals(getUpdatedUserResponse.statusText(),"OK");
        String getResponseText=getUpdatedUserResponse.text();
        System.out.println("Response text from Get method is -------:"+getUpdatedUserResponse.text());
        UsersByLombok getUpdatedUser=objectMapper.readValue(getResponseText,
                UsersByLombok.class);
        Assert.assertTrue(getUpdatedUserResponse.text().contains(userId));
        Assert.assertTrue(getUpdatedUserResponse.text().contains(users.getName()));
        Assert.assertEquals(getUpdatedUser.getId(),userId);
        Assert.assertEquals(getUpdatedUser.getStatus(),users.getStatus());
        Assert.assertEquals(getUpdatedUser.getName(),users.getName());









    }
}
