package in.co.gorest.userinfo;

import in.co.gorest.testbase.TestBase;
import in.co.gorest.utils.TestUtils;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasValue;

@RunWith(SerenityRunner.class)
public class UserCURDTestWithSteps extends TestBase {
    static String name = "Nehal Patel";
    static String email = TestUtils.getRandomValue() + "nehal@gmail.com";
    static String gender = "female";
    static String status = "active";
    static int userId;

    @Steps
    UserSteps userSteps;

    @Title("This will create a new user")
    @Test
    public void test001() {
       userSteps.createUser(name,email,gender,status).statusCode(201).log().all();
    }

    @Title("Verify if the user was added to the application")
    @Test
    public void test002() {
        name = "Gemine Khanna";
        HashMap<String, Object> userMap = userSteps.getUserInfoByFirstName(name);
        Assert.assertThat(userMap, hasValue(name));
        userId = (int) userMap.get("id");
        System.out.println(userId);
    }

    @Title("Update the user information and verify the updated information")
    @Test
    public void test003() {
        name = "Neha";
        gender="female";
        email= TestUtils.getRandomValue() + "neha@gmail.com";
        status="active";
        userId = 12326;
        userSteps.updateUser(userId,name,email,gender,status).statusCode(200).log().body().body("name", equalTo(name), "email", equalTo(email));
    }

    @Title("Delete the user and verify if the user is deleted!")
    @Test
    public void test004() {
        userSteps.deleteUser(userId).statusCode(204).log().status();
        userSteps.getUserById(userId).statusCode(404).log().status();
    }


}
