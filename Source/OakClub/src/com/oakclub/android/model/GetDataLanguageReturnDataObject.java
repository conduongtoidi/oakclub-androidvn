package com.oakclub.android.model;

import java.util.ArrayList;

public class GetDataLanguageReturnDataObject {
	ArrayList<WorkCategoryObject> work_cate;
	ArrayList<RelationshipStatusObject> relationship_status;
	ArrayList<EthnicityObject> ethnicity;
	ArrayList<LanguageObject> language;
	public ArrayList<WorkCategoryObject> getWork_cate() {
		return work_cate;
	}
	public void setWork_cate(ArrayList<WorkCategoryObject> work_cate) {
		this.work_cate = work_cate;
	}
	public ArrayList<RelationshipStatusObject> getRelationship_status() {
		return relationship_status;
	}
	public void setRelationship_status(
			ArrayList<RelationshipStatusObject> relationship_status) {
		this.relationship_status = relationship_status;
	}
	public ArrayList<EthnicityObject> getEthnicity() {
		return ethnicity;
	}
	public void setEthnicity(ArrayList<EthnicityObject> ethnicity) {
		this.ethnicity = ethnicity;
	}
	public ArrayList<LanguageObject> getLanguage() {
		return language;
	}
	public void setLanguage(ArrayList<LanguageObject> language) {
		this.language = language;
	}
	
}
