package model;

/**
 * 
 * @author jguyot2
 */
public class DTOComputer {

	private String name;
	private String strId;
	private String strEntrepriseId;
	private String introductionDate;
	private String discontinuationDate;
	
	public DTOComputer(String name, String strId, String strEntrepriseId, String introductionDate,
			String discontinuationDate) {
		super();
		this.name = name;
		this.strId = strId;
		this.strEntrepriseId = strEntrepriseId;
		this.introductionDate = introductionDate;
		this.discontinuationDate = discontinuationDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStrId() {
		return strId;
	}

	public void setStrId(String strId) {
		this.strId = strId;
	}

	public String getStrEntrepriseId() {
		return strEntrepriseId;
	}

	public void setStrEntrepriseId(String strEntrepriseId) {
		this.strEntrepriseId = strEntrepriseId;
	}

	public String getIntroductionDate() {
		return introductionDate;
	}

	public void setIntroductionDate(String introductionDate) {
		this.introductionDate = introductionDate;
	}

	public String getDiscontinuationDate() {
		return discontinuationDate;
	}

	public void setDiscontinuationDate(String discontinuationDate) {
		this.discontinuationDate = discontinuationDate;
	}	
	
	
}
