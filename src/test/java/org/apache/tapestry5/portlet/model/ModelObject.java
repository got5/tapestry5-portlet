package org.apache.tapestry5.portlet.model;

import java.util.List;

public class ModelObject {

	private String nom;

	private List<ListObject> items;

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public List<ListObject> getItems() {
		return items;
	}

	public void setItems(List<ListObject> items) {
		this.items = items;
	}

}
