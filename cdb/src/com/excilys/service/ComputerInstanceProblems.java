package com.excilys.service;

public enum ComputerInstanceProblems {
	INVALID_NAME("Nom invalide"),
	INVALID_DISCONTINUATION_DATE("Date d'arrêt de production invalide car précédant celle de début de production"), 
	NULL_INTRO_WITH_NOT_NULL_DISCONTINUATION("Pas de date de début de production mais une date d'arrêt de production");
	
	private String explanation;
	private ComputerInstanceProblems(String str) {
		this.explanation = str;
	}
	public String getExplanation() {
		return this.explanation;
	}
}