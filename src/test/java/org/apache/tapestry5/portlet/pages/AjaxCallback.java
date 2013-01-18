//
// Copyright 2012 Apache Tapestry 5
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// 	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package org.apache.tapestry5.portlet.pages;

import java.util.Date;

import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.ajax.JSONCallback;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Import(library = "testJSON.js")
public class AjaxCallback{
    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    @InjectComponent
    private Zone topZone;

    @InjectComponent
    private Zone bottomZone;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    @InjectComponent
    private EventLink jsonCallbackLink;

    @Inject
    private Messages messages;

    @AfterRender
    void addJavaScript(){
        javaScriptSupport.addInitializerCall("testJSON", jsonCallbackLink.getClientId());
    }

    @OnEvent("serverAlert")
    void showAlert() {
        ajaxResponseRenderer.addCallback(new JavaScriptCallback() {
            public void run(JavaScriptSupport javascriptSupport) {
                javascriptSupport.addScript(
                    String.format("alert('%s');", messages.get("server.hello")));
            }
        });
    }

    @OnEvent("sendJSON")
    void sendJSON() {
        ajaxResponseRenderer.addCallback(new JSONCallback() {
          
			@Override
			public void run(JSONObject reply) {
				 reply.put("message", messages.get("server.message"));
				
			}
        });
    }

    @OnEvent("multipleZoneUpdate")
    void showZones() {
        ajaxResponseRenderer.addRender(topZone.getClientId(), topZone).
            addRender(bottomZone.getClientId(), bottomZone);
    }

    public Date getDate() {
        return new Date();
    }

    public String getTopZoneId()
    {
        return topZone.getClientId();
    }
    
    public String getBottomZoneId()
    {
        return bottomZone.getClientId();
    }
}
