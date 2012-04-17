package org.apache.tapestry5.portlet;

import java.util.List;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

/**
 *  DeclaredResourceResponseSender store informations to define which component should 
 *  generate ajax response and then should create resource link
 * 
 * @author ffacon
 */
public class DeclaredResourceResponseSender {
	
	private String ClassName;
	
	private List<String> eventList;
	
	private boolean mixin;
	
	public DeclaredResourceResponseSender(String className) {
		super();
		ClassName = className;
		this.eventList = CollectionFactory.newList();
		this.mixin = false;
		
	}
	
	public DeclaredResourceResponseSender(String className,boolean isMixin) {
		super();
		ClassName = className;
		this.eventList = CollectionFactory.newList();
		this.mixin = isMixin;
		
	}

	public void addEvent(String eventName)
	{
		eventList.add(eventName);
	}
	
	public String getClassName() {
		return ClassName;
	}

	public List<String> getEventList() {
		return eventList;
	}
	
	public boolean isMixin(){
		return  mixin;
	}
	

}
