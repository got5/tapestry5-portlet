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

    @Test
    public void PublishEvent()
    {
    	open("/tapestry5-portlet/portal/Index");
        clickAndWait("link=Publish event");
        assertTextPresent("sampleEvent");
    }
    
    @Test
    public void To_Block_Caller()
    {
    	open("/tapestry5-portlet/portal/Index");
    	click("link=To Block Caller");
    	waitForPageToLoad();
    	clickAndWait("link=1");
    	assertTextPresent("Page activation context: 1");
    	clickAndWait("link=2");
    	assertTextPresent("Page activation context: 2");
    	clickAndWait("link=3");
    	assertTextPresent("Page activation context: 3");
    }

    protected final void sleep(long millis)
    {
        try
        {
            Thread.sleep(millis);
        } catch (InterruptedException ex)
        {
            // Ignore.
        }
    }
    
}
