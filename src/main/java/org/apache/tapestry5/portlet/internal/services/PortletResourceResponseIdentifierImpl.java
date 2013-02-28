package org.apache.tapestry5.portlet.internal.services;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.internal.InternalComponentResources;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.portlet.DeclaredResourceResponseSender;
import org.apache.tapestry5.portlet.services.PortletResourceResponseIdentifier;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ComponentSource;
import org.slf4j.Logger;


public class PortletResourceResponseIdentifierImpl implements PortletResourceResponseIdentifier {

	private final List<DeclaredResourceResponseSender> ResourceSenders;
	
	private final ComponentSource componentSource;
	
	private final Logger log;
	
	private final Map<String, Boolean> cache = CollectionFactory.newConcurrentMap();
	
	public  PortletResourceResponseIdentifierImpl(Collection <DeclaredResourceResponseSender> drrs, ComponentSource componentSource,Logger log)
    {
        this.ResourceSenders = CollectionFactory.newList(drrs);
        this.componentSource = componentSource;
        this.log = log;
        
    }

	@Override
	public boolean isResourceResponseHandler(String containingPageName,
											 String nestedComponentId, String eventType) 
	{
		String cptName;
		if(nestedComponentId!=null)
		{
			cptName = containingPageName + ":" + nestedComponentId;
		}
		else
		{
			//for page or event or component like tapestry-jQuery.bind  that send json or zoneupdate       
			cptName = containingPageName;
		}	
	    
		String key = cptName + "." + eventType;
		Boolean result = cache.get(key);
        if (result != null)
        {
        	log.debug("according to cache "+ key + " is link to " + result);
            return result.booleanValue();
        }
		
		Component component = this.componentSource.getComponent(cptName);
	    Class cpt = component.getClass();
	    
	    String cptClassName = cpt.getName();
	     
		for ( Iterator<DeclaredResourceResponseSender> iter = ResourceSenders.iterator(); iter.hasNext(); ) 
		{
			 
			 DeclaredResourceResponseSender d = iter.next();
			 if(d.isMixin())
			 {
				 ComponentResources componentResources = component.getComponentResources();
			     InternalComponentResources internalRes = (InternalComponentResources) componentResources;
			     try{
			    	 
			    	 Object mixin = internalRes.getMixinByClassName(d.getClassName());
			    	 if(mixin!=null)
			    	 {
			    		 log.debug(cptName+" is bind to a mixin of type "+d.getClassName());
			    		 List<String> events = d.getEventList();
						 if(events.size()==0)
						 {
							 log.debug(cptName+" is declared as resource response sender");
							 cache.put(key, new Boolean(true));
							 return true;
						 }
						 else
						 {
							for ( Iterator<String> evt = events.iterator(); evt.hasNext(); ) 
							{
								String e = evt.next();
								if(eventType.equalsIgnoreCase(e)) {
									log.debug(cptName+" is declared as resource response sender for event "+ e );
									cache.put(key, new Boolean(true));
									return true;
								}
							}
									
						 }
			        }
			        
			          
			     }
			     catch(Exception e)
			     { 
			    	 log.debug(d.getClassName()+" is not used for "+cptName);
			     }
			 }
			 else if(cptClassName.equalsIgnoreCase(d.getClassName()))
			 {
				 List<String> events = d.getEventList();
				 if(events.size()==0)
				 {
					 log.debug(cptName+" is declared as resource response sender");
					 cache.put(key, new Boolean(true));
					 return true;
				 }
				 else
				 {
					for ( Iterator<String> evt = events.iterator(); evt.hasNext(); ) 
					{
						String e = evt.next();
						if(eventType.equalsIgnoreCase(e)) {
							 log.debug(cptName+" is declared as resource response sender for event "+ e );
							 cache.put(key, new Boolean(true));
							 return true; 
						}
					}
							
				 }
					 
			 }

		}
		cache.put(key, new Boolean(false));
		return false;
	}

}
