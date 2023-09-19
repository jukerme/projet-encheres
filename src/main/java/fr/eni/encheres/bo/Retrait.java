package fr.eni.encheres.bo;

import java.util.ArrayList;
import java.util.List;

public class Retrait {

	private String rue;
	private String codePostal;
	private String ville;
	
	private List<ArticleVendu> lieuRetrait = new ArrayList<ArticleVendu>();
	
	
	
	public Retrait(String rue, String codePostal, String ville) {
		this.rue = rue;
		this.codePostal = codePostal;
		this.ville = ville;
	}
	
	
	
	public List<ArticleVendu> getLieuRetrait() {
		return lieuRetrait;
	}
	public void setLieuRetrait(List<ArticleVendu> lieuRetrait) {
		this.lieuRetrait = lieuRetrait;
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
		return "Retrait [rue=" + rue + ", codePostal=" + codePostal + ", ville=" + ville + ", lieuRetrait="
				+ lieuRetrait + "]";
	}

}
