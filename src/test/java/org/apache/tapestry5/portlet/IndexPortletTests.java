// Copyright 2009, 2010, 2011 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry5.portlet;

import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.Test;

/**
 * Tests related to the Index Portlet.
 */
public class IndexPortletTests extends SeleniumTestCase
{
	String indexLocation = "/tapestry5-portlet/portal/Index";

    @Test
    public void PublishEvent()
    {
    	open(indexLocation);
        clickAndWait("link=Publish event");
        assertTextPresent("sampleEvent");
    }
    
    @Test
    public void To_Block_Caller()
    {
    	open(indexLocation);
    	click("link=To Block Caller");
    	waitForPageToLoad();
    	clickAndWait("link=1");
    	assertTextPresent("Page activation context: 1");
    	clickAndWait("link=2");
    	assertTextPresent("Page activation context: 2");
    	clickAndWait("link=3");
    	assertTextPresent("Page activation context: 3");
    }

   
    
    @Test
    public void form_validation_test()
    {
    	String SubmitLastName = "//form[contains(@id,'lastName')]/input[@type='submit']";
    	String InputLastName = "//input[starts-with(@id,'lastName')]";
    	
    	open(indexLocation);
    	
        type(InputLastName, "");

        click(SubmitLastName);

        assertText("//div[contains(@id,'errorpopup')][starts-with(@id,'lastName')]/span", "You must provide a value for Last Name.");

        type(InputLastName, "fac");
        click(SubmitLastName);
        waitForPageToLoad();
        assertTextPresent(" fac is stored in portlet session scope Application with @Persist!");
    }
    
    
    
}
