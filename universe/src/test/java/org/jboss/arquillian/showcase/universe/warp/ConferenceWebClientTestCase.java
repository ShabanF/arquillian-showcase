/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.showcase.universe.warp;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.showcase.universe.Deployments;
import org.jboss.arquillian.showcase.universe.Models;
import org.jboss.arquillian.showcase.universe.model.Conference;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
@WarpTest
@RunWith(Arquillian.class)
public class ConferenceWebClientTestCase {
    
    @Deployment
    public static WebArchive deploy() {
        return Deployments.Client.web()
                .addClass(VerifyConference.class);
    }
    
    @ArquillianResource
    public URL baseURL;
    
    @Drone
    public WebDriver driver;

    @Test @RunAsClient
    public void shouldBeAbleToInsertUser() throws Exception {
        final Conference conference = Models.createRandomConference();
        
        driver.navigate().to(baseURL + "view/conference/create.jsf");
        driver.findElement(By.id("create:conferenceName")).sendKeys(conference.getName());
        driver.findElement(By.id("create:conferenceLocation")).sendKeys(conference.getLocation());
        driver.findElement(By.id("create:conferenceDescription")).sendKeys(conference.getDescription());
        driver.findElement(By.id("create:conferenceId")).sendKeys(conference.getId());
        driver.findElement(By.id("create:conferenceRedirect")).sendKeys("x");

        System.out.println(driver.getPageSource());
        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                driver.findElement(By.id("create:save")).click();
            }
        }).inspect(new VerifyConference(conference));

    }
}