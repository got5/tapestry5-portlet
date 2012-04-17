package org.apache.tapestry5.portlet.pages;

public class Exception
{

    void onActivate()
    {
        Integer.parseInt("NaN");
    }

}
