package fr.eni.encheres.bo;

public class Retrait {

	private String rue;
	private String codePostal;
	private String ville;
	
	private ArticleVendu articleVendu;
	
	
	
	public Retrait(String rue, String codePostal, String ville) {
		this.rue = rue;
		this.codePostal = codePostal;
		this.ville = ville;
	}
	
	
	
	public ArticleVendu getLieuRetrait() {
		return articleVendu;
	}
	public void setLieuRetrait(ArticleVendu articleVendu) {
		this.articleVendu = articleVendu;
	}

	public String getRue() {
		return rue;
	}
	public void setRue(String rue) {
		this.rue = rue;
	}
	
	public String getCodePostal() {
		return codePostal;
	}
	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}
	
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}



	@Override
	public String toString() {
		return "Retrait [rue=" + rue + ", codePostal=" + codePostal + ", ville=" + ville + ", articleVendu="
				+ articleVendu + "]";
	}

}
