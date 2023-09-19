package fr.eni.encheres.dal.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.eni.encheres.bo.Retrait;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dal.UtilisateurDao;
import fr.eni.encheres.dal.jdbc.exception.JDBCException;

public class UtilisateurDaoJdbcImpl implements UtilisateurDao {

	private static final String INSERT_USER = "INSERT INTO UTILISATEUR (pseudo,nom,prenom,email,telephone,rue,codePostal,ville,motDePasse)"
			+ " VALUES (?,?,?,?,?,?,?,?,?)";

	private static final String SELECT_BY_USERNAME = "SELECT * FROM UTILISATEUR WHERE pseudo = ?";

	private static final String UNIQUE_USERNAME_CONSTRAINT = "uq_pseudo_user";
	private static final String UNIQUE_TELEPHONE_CONSTRAINT = "uq_phone_user";
	private static final String UNIQUE_EMAIL_CONSTRAINT = "uq_email_user";
	private static final String DELETE = "DELETE UTILISATEUR WHERE noUtilisateur = ?";
	private static final String SELECT_BY_EMAIL = "SELECT * FROM UTILISATEUR WHERE email = ?";

	private static final String UPDATE_USER_USER = "UPDATE UTILISATEUR SET (pseudo,nom,prenom,email,telephone,rue,codePostal,ville,motDePasse)"
			+ " VALUES (?,?,?,?,?,?,?,?,?) WHERE noUtilisateur = ?";

	private static final String UPDATE_RETRAIT_RETRAIT = "UPDATE RETRAIT SET (rue,codePostal,ville)"
			+ " VALUES (?,?,?) WHERE noUtilisateur = ?";

	@Override
	public Utilisateur findByUsername(String pseudo) {
		try (Connection connection = ConnectionProvider.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(SELECT_BY_USERNAME);) {

			pstmt.setString(1, pseudo);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return new Utilisateur(rs.getInt("noUtilisateur"), rs.getString("pseudo"), rs.getString("nom"),
						rs.getString("prenom"), rs.getString("email"), rs.getString("telephone"), rs.getString("rue"),
						rs.getString("codePostal"), rs.getString("ville"), rs.getString("motDePasse"),
						rs.getInt("credit"), rs.getBoolean("administrateur"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Utilisateur findByEmail(String email) {
		try (Connection connection = ConnectionProvider.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(SELECT_BY_EMAIL);) {

			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return new Utilisateur(rs.getInt("noUtilisateur"), rs.getString("pseudo"), rs.getString("nom"),
						rs.getString("prenom"), rs.getString("email"), rs.getString("telephone"), rs.getString("rue"),
						rs.getString("codePostal"), rs.getString("ville"), rs.getString("motDePasse"),
						rs.getInt("credit"), rs.getBoolean("administrateur"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void save(Utilisateur user) throws JDBCException {
		try (Connection connection = ConnectionProvider.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(INSERT_USER);) {
			pstmt.setString(1, user.getPseudo());
			pstmt.setString(2, user.getNom());
			pstmt.setString(3, user.getPrenom());
			pstmt.setString(4, user.getEmail());
			pstmt.setString(5, user.getTelephone());
			pstmt.setString(6, user.getRue());
			pstmt.setString(7, user.getCodePostal());
			pstmt.setString(8, user.getVille());
			pstmt.setString(9, user.getMotDePasse());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			if (e.getMessage().contains(UNIQUE_USERNAME_CONSTRAINT)) {
				throw new JDBCException("L'username est déjà utilisé !");
			}
			if (e.getMessage().contains(UNIQUE_TELEPHONE_CONSTRAINT)) {
				throw new JDBCException("Le numéro de téléphone est déjà utilisé !");
			}
			if (e.getMessage().contains(UNIQUE_EMAIL_CONSTRAINT)) {
				throw new JDBCException("L'email est déjà utilisé !");
			}
			e.printStackTrace();
		}
	}

	@Override
	public void remove(int id) {

		try (Connection connection = ConnectionProvider.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(DELETE);) {
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void modify(Utilisateur user) throws JDBCException {
		try (Connection connection = ConnectionProvider.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(UPDATE_USER_USER);) {
			pstmt.setString(1, user.getPseudo());
			pstmt.setString(2, user.getNom());
			pstmt.setString(3, user.getPrenom());
			pstmt.setString(4, user.getEmail());
			pstmt.setString(5, user.getTelephone());
			pstmt.setString(6, user.getRue());
			pstmt.setString(7, user.getCodePostal());
			pstmt.setString(8, user.getVille());
			pstmt.setString(9, user.getMotDePasse());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			if (e.getMessage().contains(UNIQUE_USERNAME_CONSTRAINT)) {
				throw new JDBCException("L'username est déjà utilisé !");
			}
			if (e.getMessage().contains(UNIQUE_TELEPHONE_CONSTRAINT)) {
				throw new JDBCException("Le numéro de téléphone est déjà utilisé !");
			}
			if (e.getMessage().contains(UNIQUE_EMAIL_CONSTRAINT)) {
				throw new JDBCException("L'email est déjà utilisé !");
			}
			e.printStackTrace();
		}
	}

	@Override
	public void modify(Retrait retrait) {
		try (Connection connection = ConnectionProvider.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(UPDATE_RETRAIT_RETRAIT);) {
			pstmt.setString(1, retrait.getRue());
			pstmt.setString(2, retrait.getCodePostal());
			pstmt.setString(3, retrait.getVille());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
