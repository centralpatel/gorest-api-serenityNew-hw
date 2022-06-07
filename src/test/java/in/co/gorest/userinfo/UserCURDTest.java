package in.co.gorest.userinfo;

import in.co.gorest.constants.EndPoints;
import in.co.gorest.model.UserPojo;
import in.co.gorest.testbase.TestBase;
import in.co.gorest.utils.TestUtils;
import io.restassured.http.ContentType;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasValue;


//@RunWith(SerenityRunner.class)
public class UserCURDTest extends TestBase {
    static String name = "Bella Shah" + TestUtils.getRandomValue();
    static String email = TestUtils.getRandomValue() + "shahbella@gmail.com";
    static String gender = "female";
    static String status = "active";
    static int userId;

    @Title("This will create a new user")
    @Test
    public void test001() {
        UserPojo userPojo = new UserPojo();
        userPojo.setName(name);
        userPojo.setEmail(email);
        userPojo.setGender(gender);
        userPojo.setStatus(status);

        HashMap<String, Object> header1 = new HashMap<>();
        header1.put("Content-Type", "application/json");
        header1.put("Accept", "application/json");
        header1.put("Authorization", "Bearer c426452f777927f6e49219f45652a5fd08178e3f873af217a5b982a6fdd15dac");
        SerenityRest.given().log().all()
                .headers(header1)
                .body(userPojo)
                .when()
                .post(EndPoints.CREATE_USER)
                .then().statusCode(201).log().all();
    }

    @Title("Verify if the user was added to the application for name=Rudra Embranthiri")
    @Test
    public void test002() {
        name = "Rudra Embranthiri";
        String p1 = "findAll{it.name=='";
        String p2 = "'}.get(0)";
        HashMap<String, Object> userMap = SerenityRest.given().log().all()
                .when()
                .get(EndPoints.GET_ALL_USERS)
                .then()
                .statusCode(200)
                .extract()
                .path(p1 + name + p2);
        Assert.assertThat(userMap, hasValue(name));
        userId = (int) userMap.get("id");
        System.out.println(userId);
    }

    @Title("Update the user information and verify the updated information for ID=6124")
    @Test
    public void test003() {
        name = "Sila";
        gender="female";
        email="Sila@gmail.com";
        status="active";
        userId =  6124;
        UserPojo userPojo = new UserPojo();
        userPojo.setName(name);
        userPojo.setEmail(email);
        userPojo.setGender(gender);
        userPojo.setStatus(status);

        HashMap<String, Object> header1 = new HashMap<>();
        header1.put("Accept", "application/json");
        header1.put("Authorization", "Bearer c426452f777927f6e49219f45652a5fd08178e3f873af217a5b982a6fdd15dac");

        SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .headers(header1)
                .pathParam("userID", userId)
                .body(userPojo)
                .when()
                .put(EndPoints.UPDATE_USER_BY_ID)
                .then().statusCode(200).log().body().body("name", equalTo(name), "email", equalTo(email));



    }

    @Title("Delete the user and verify if the user is deleted! for ID=6124")
    @Test
    public void test004() {
        userId =  6124;

        HashMap<String, Object> header1 = new HashMap<>();
        header1.put("Accept", "application/json");
        header1.put("Authorization", "Bearer c426452f777927f6e49219f45652a5fd08178e3f873af217a5b982a6fdd15dac");

        SerenityRest.given().log().all()
                .headers(header1)
                .pathParam("userID", userId)
                .when()
                .delete(EndPoints.DELETE_USER_BY_ID)
                .then().statusCode(204)
                .log().status();

        SerenityRest.given().log().all()
                .pathParam("userID", userId)
                .when()
                .get(EndPoints.GET_SINGLE_USER_BY_ID)
                .then()
                .statusCode(404).log().status();
    }

}
