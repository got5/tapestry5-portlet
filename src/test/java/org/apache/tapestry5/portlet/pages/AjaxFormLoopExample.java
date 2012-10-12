package org.apache.tapestry5.portlet.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.portlet.model.ListObject;
import org.apache.tapestry5.portlet.model.ModelObject;




public class AjaxFormLoopExample {

	@Persist
	@Property
	private ModelObject modelObject;

	@Property
	private ListObject currentItem;

	@SetupRender
	public void initForm() {
		if (modelObject == null) {
			modelObject = new ModelObject();
			List<ListObject> items = new ArrayList<ListObject>();
			modelObject.setItems(items);
		}
	}

	@OnEvent(value = "addRow", component = "items")
	private Object ajouteCommentaire() {
		ListObject item = new ListObject();
		modelObject.getItems().add(item);
		return item;
	}

	@OnEvent(value = "removeRow", component = "items")
	private void supprimeCommentaire(ListObject item) {
		int index = modelObject.getItems().indexOf(item);
		if(index!=-1)	modelObject.getItems().remove(index);
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "exampleForm")
	public void enregistrerForm() {
		System.out.println(">> Enregistrement du formulaire");
		System.out.println(">> Nom = " + modelObject.getNom());
		for (ListObject item : modelObject.getItems()) {
			if(item!=null) 
				System.out.println(">> Item = " + item.getId()	+ " ; " + item.getTexte());
			
		}
	}

	public ValueEncoder<ListObject> getEncoder() {
		return new ValueEncoder<ListObject>() {

			public String toClient(ListObject value) {
				return String.valueOf(modelObject.getItems().indexOf(value));
			}

			public ListObject toValue(String clientValue) {
				return modelObject.getItems().get(Integer.parseInt(clientValue));
			}
		};
	}
}
