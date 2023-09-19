package fr.eni.encheres.bll;

import java.util.Random;

import fr.eni.encheres.bo.ForgetPassword;
import fr.eni.encheres.bo.Retrait;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dal.DaoFactory;
import fr.eni.encheres.dal.UtilisateurDao;
import fr.eni.encheres.dal.jdbc.exception.JDBCException;
import fr.eni.encheres.helper.PasswordEncoder;

public class UtilisateursManager {

	// DÉBUT SINGLETON

	private static UtilisateursManager instance;

	private UtilisateursManager() {
	}

	public static UtilisateursManager getInstance() {
		if (instance == null)
			instance = new UtilisateursManager();
		return instance;
	}

	// FIN SINGLETON

	// DÉBUT LA LOGIQUE MÉTIER

	// BLL UTILISATEUR

	private UtilisateurDao utilisateurDao = DaoFactory.getUtilisateurDao();
	private Random rd = new Random();

//	public List<Utilisateur> searchAllUtilisateur() {
//		return utilisateurDao.findAll();
//	}

	public void addUtilisateur(Utilisateur user) throws JDBCException {
		utilisateurDao.save(user);
	}

	public void modifyUtilisateur(Utilisateur user) throws JDBCException {
		utilisateurDao.modify(user);
	}

	public void removeUtilisateur(int id) {
		utilisateurDao.remove(id);
		;
	}

	public Utilisateur searchUtilisateur(String query) {
		return utilisateurDao.findByUsername(query);
	}

	// BLL AUTRES

	public void inscription(Utilisateur utilisateur) throws JDBCException, JDBCException {
		checkFields(utilisateur);
		utilisateur.setMotDePasse(PasswordEncoder.hashPassword(utilisateur.getMotDePasse() // PASSWORD NON HACHER
		));
		utilisateurDao.save(utilisateur);
	}

	private void checkFields(Utilisateur utilisateur) throws JDBCException {
		if (utilisateur == null)
			throw new JDBCException("User est null");

		if (utilisateur.getPseudo().isBlank())
			throw new JDBCException("L'username est obligatoire !");
		if (utilisateur.getEmail().isBlank())
			throw new JDBCException("L'e-mail est obligatoire !");

		// VÉRIFIE LA SYNTAXE DE L'EMAIL
		if (utilisateur.getMotDePasse().isBlank())
			throw new JDBCException("Le mot de passe est obligatoire !");
		if (utilisateur.getMotDePasse().length() < 8 || utilisateur.getMotDePasse().length() > 35)
			throw new JDBCException("La taille du mot de passe doit etre entre 8 et 35 carachtères !");
		// if(!user.getPassword().equals(user.getConfirmpassword))
	}

	public Utilisateur login(String email, String password) {
		Utilisateur utilisateur = utilisateurDao.findByEmail(email);
		if (utilisateur != null && utilisateur.getPseudo().equals(email)
				&& PasswordEncoder.verifyPassword(password, utilisateur.getMotDePasse())) {
			return utilisateur;
		}
		return null;
	}

	public ForgetPassword checkEmail(String email) throws JDBCException {

		Utilisateur utilisateur = utilisateurDao.findByEmail(email);
		if (utilisateur == null)
			throw new JDBCException("Erreur: l'email n'existe pas");

		// GÉNÉRE LE CODE
		String code = rd.nextLong(1, 9999999999L) + "";
		ForgetPassword fp = new ForgetPassword(code, utilisateur);

		// SAVE
		DaoFactory.getForgetPasswordDao().save(fp);

		// ENVOIE LE MAIL
		// PAR SMS VIA API
		System.out.println(code);

		return fp;
	}

	public void resetPassword(String email, String code, String newPassword) throws JDBCException {

		ForgetPassword fp = DaoFactory.getForgetPasswordDao().resetPassword(email);

		if (!fp.getCode().equals(code))
			throw new JDBCException("Le code est érroné!");

		Utilisateur utilisateur = fp.getUser();

		utilisateur.setMotDePasse(PasswordEncoder.hashPassword(newPassword));

		utilisateurDao.modify(utilisateur);

	}

	// BLL ADRESSE

	public void modifyAdresse(Retrait adresse) {
		utilisateurDao.modify(adresse);
	}

}
