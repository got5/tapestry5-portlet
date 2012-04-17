package org.apache.tapestry5.portlet.pages;

import org.apache.tapestry5.annotations.Property;

public class ActivationContext
{
    @Property
    private String message;

    void onActivate(String message)
    {
        this.message = message;
    }

    void onAction(String actionMessage)
    {
        System.out.println(actionMessage);
    }

    String onPassivate()
    {
        return message;
    }
}
