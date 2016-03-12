/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.carbon.iot.integration.web.ui.test.login;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.carbon.automation.extensions.selenium.BrowserManager;
import org.wso2.carbon.iot.integration.web.ui.test.common.Constants;
import org.wso2.carbon.iot.integration.web.ui.test.common.IOTIntegrationUIBaseTestCase;
import org.wso2.iot.integration.ui.pages.UIElementMapper;


/**
 * Test cases to test the incorrect login from submissions.
 * Ex:
 * 1. Empty form
 * 2. Incorrect username or password
 * 3. short password
 */
public class LoginFormValidationTest extends IOTIntegrationUIBaseTestCase {

    private WebDriver driver;
    private UIElementMapper uiElementMapper;

    WebElement userNameField;
    WebElement passwordField;
    WebElement loginButton;

    @BeforeClass(alwaysRun = true)
    public void setup() throws Exception {
        super.init();
        driver = BrowserManager.getWebDriver();
        clearForm();
    }

    @Test(description = "Test for empty login form submission")
    public void emptyLoginFormTest() throws Exception {
        userNameField.sendKeys("");
        passwordField.sendKeys("");
        loginButton.click();

        WebElement alertUserName = driver.findElement(By.id(
                uiElementMapper.getElement("iot.user.login.username.error")));
        WebElement alertPassword = driver.findElement(By.id(
                uiElementMapper.getElement("iot.user.login.password.error")));

        if (!alertUserName.isDisplayed()) Assert.assertTrue(false, "Alert for user name is not present.");
        if (!alertPassword.isDisplayed()) Assert.assertTrue(false, "Alert for password is not present.");

            Assert.assertEquals(alertUserName.getText(), "Please enter a username");
            Assert.assertEquals(alertPassword.getText(), "Please provide a password");

    }

    @Test(description = "Test for incorrect username")
    public void incorrectUserNameTest() throws Exception {
        clearForm();
        userNameField.sendKeys("admin1");
        passwordField.sendKeys(automationContext.getSuperTenant().getTenantAdmin().getPassword());
        loginButton.click();

        WebElement alert = driver.findElement(By.xpath(uiElementMapper.getElement("iot.user.login.incorrect.xpath")));
        if (alert.isDisplayed()) {
            Assert.assertEquals(alert.getText(), "Incorrect username or password.!");
        } else {
            Assert.assertTrue(false, "Alert is not present.");
        }

    }

    @Test(description = "Test for incorrect password")
    public void incorrectPasswordTest() throws Exception {
        clearForm();
        userNameField.sendKeys(automationContext.getSuperTenant().getTenantAdmin().getPassword());
        passwordField.sendKeys("admnn");
        loginButton.click();

        WebElement alert = driver.findElement(By.xpath(uiElementMapper.getElement("iot.user.login.incorrect.xpath")));
        if (alert.isDisplayed()) {
            Assert.assertEquals(alert.getText(), "Incorrect username or password.!");
        } else {
            Assert.assertTrue(false, "Alert is not present.");
        }
    }


    @Test(description = "Test for short password")
    public void shortPasswordTest() throws Exception {
        clearForm();
        userNameField.sendKeys(automationContext.getSuperTenant().getTenantAdmin().getUserName());
        passwordField.sendKeys("ad");
        loginButton.click();

        WebElement alert = driver.findElement(By.id(uiElementMapper.getElement("iot.user.login.password.error")));
        if (alert.isDisplayed()) {
            Assert.assertEquals(alert.getText(), "Your password must be at least 3 characters long");
        } else {
            Assert.assertTrue(false, "Alert is not present.");
        }
    }

    public void clearForm() throws Exception {
        driver.get(getWebAppURL() + Constants.IOT_LOGIN_PATH);
        uiElementMapper = UIElementMapper.getInstance();

        userNameField = driver.findElement(By.xpath(
                uiElementMapper.getElement("iot.user.login.input.username.xpath")));
        passwordField = driver.findElement(By.xpath(
                uiElementMapper.getElement("iot.user.login.input.password.xpath")));
        loginButton = driver.findElement(By.xpath(
                uiElementMapper.getElement("iot.user.login.button.xpath")));
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.quit();
    }
}
