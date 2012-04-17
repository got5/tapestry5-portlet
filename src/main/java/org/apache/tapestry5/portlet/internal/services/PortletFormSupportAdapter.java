package org.apache.tapestry5.portlet.internal.services;

import org.apache.tapestry5.ComponentAction;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.services.FormSupport;

public class PortletFormSupportAdapter implements FormSupport
{
    private final FormSupport delegate;

    private final String namespace;
    
    public PortletFormSupportAdapter(FormSupport delegate, String namespace)
    {
        this.delegate = delegate;
        this.namespace = namespace;
    }

    public String allocateControlName(String id)
    {
        return delegate.allocateControlName(id) + namespace;
    }

    public <T> void store(T component, ComponentAction<T> action)
    {
        delegate.store(component, action);
    }

    public <T> void storeAndExecute(T component, ComponentAction<T> action)
    {
        delegate.storeAndExecute(component, action);
    }

    public void defer(Runnable command)
    {
        delegate.defer(command);
    }

    public void setEncodingType(String encodingType)
    {
        delegate.setEncodingType(encodingType);
    }

    public void addValidation(Field field, String validationName, String message, Object constraint)
    {
        delegate.addValidation(field, validationName, message, constraint);
    }

    public String getClientId()
    {
        return delegate.getClientId();
    }

    public boolean isClientValidationEnabled()
    {
        return delegate.isClientValidationEnabled();
    }

    public String getFormComponentId()
    {
        return delegate.getFormComponentId();
    }

    public String getFormValidationId()
    {
        return delegate.getFormValidationId();
    }

}
