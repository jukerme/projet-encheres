package fr.eni.encheres.dal.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.dal.ArticlesDao;

public class ArticlesDaoJdbcImpl implements ArticlesDao {

	private static final String SELECT_ALL = "SELECT * FROM ARTICLE_VENDU";
	private static final String SELECT = "SELECT * FROM ARTICLE_VENDU WHERE noArticle = ?";
	private static final String SAVE = "INSERT ARTICLE_VENDU (nomArticle,description,dateDebutEncheres,dateFinEncheres,miseAPrix,prixVente,etatVente) VALUES (?,?,?,?,?,?,?)";
	private static final String DELETE = "DELETE ARTICLE_VENDU WHERE noArticle = ?";
	private static final String UPDATE = "UPDATE ARTICLE_VENDU SET nomArticle=?,description=?,dateDebutEncheres=?,dateFinEncheres=?,miseAPrix=?,prixVente=?,etatVente=? WHERE noArticle = ?";
	private static final String FIND_BY_NAME = "SELECT * FROM ARTICLE_VENDU WHERE nomArticle LIKE ? ";
	private static final String FIND_BY_CATEGORY = "SELECT libelle FROM CATEGORIE GROUP BY libelle";

	@Override
	public void save(ArticleVendu articleVendu) {
		try (Connection connection = ConnectionProvider.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(SAVE);) {
			// valoriser les params de la requete
			pstmt.setString(1, articleVendu.getNomArticle());
			pstmt.setString(2, articleVendu.getDescription());
			pstmt.setDate(3, Date.valueOf(articleVendu.getDateDebutEncheres()));
			pstmt.setDate(4, Date.valueOf(articleVendu.getDateFinEncheres()));
			pstmt.setInt(5, articleVendu.getMiseAPrix());
			pstmt.setInt(6, articleVendu.getPrixVente());
			pstmt.setString(7, articleVendu.getEtatVente());

			// executer la requete
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ArticleVendu findOne(int id) {
		try (Connection connection = ConnectionProvider.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(SELECT);) {
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return new ArticleVendu(rs.getInt("noArticle"), rs.getString("nomArticle"), rs.getString("description"),
						rs.getDate("dateDebutEncheres").toLocalDate(), rs.getDate("dateFinEncheres").toLocalDate(),
						rs.getInt("miseAPrix"), rs.getInt("prixVente"), rs.getString("etatVente"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public List<ArticleVendu> findAll() {
		try (Connection connection = ConnectionProvider.getConnection();
				Statement stmt = connection.createStatement();) {
			List<ArticleVendu> articles = new ArrayList<ArticleVendu>();
			ResultSet rs = stmt.executeQuery(SELECT_ALL);
			while (rs.next()) {
				articles.add(

						new ArticleVendu(rs.getInt("noArticle"), rs.getString("nomArticle"),
								rs.getString("description"), rs.getDate("dateDebutEncheres").toLocalDate(),
								rs.getDate("dateFinEncheres").toLocalDate(), rs.getInt("miseAPrix"),
								rs.getInt("prixVente"), rs.getString("etatVente"))

				);
			}
			return articles;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public void modify(ArticleVendu articleVendu) {
		try (Connection connection = ConnectionProvider.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(UPDATE)) {

			// Update Set
			pstmt.setString(1, articleVendu.getNomArticle());
			pstmt.setString(2, articleVendu.getDescription());
			pstmt.setDate(3, Date.valueOf(articleVendu.getDateDebutEncheres()));
			pstmt.setDate(4, Date.valueOf(articleVendu.getDateFinEncheres()));
			pstmt.setInt(5, articleVendu.getMiseAPrix());
			pstmt.setInt(6, articleVendu.getPrixVente());
			pstmt.setString(7, articleVendu.getEtatVente());
			// Where id
			pstmt.setInt(8, articleVendu.getNoArticle());
			// execute
			pstmt.executeUpdate();

		} catch (SQLException e) {
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
	public List<ArticleVendu> findByName(String query) {
		try (Connection connection = ConnectionProvider.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(FIND_BY_NAME)) {
			pstmt.setString(1, "%" + query + "%");
			List<ArticleVendu> articles = new ArrayList<ArticleVendu>();
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				articles.add(

						new ArticleVendu(rs.getInt("noArticle"), rs.getString("nomArticle"),
								rs.getString("description"), rs.getDate("dateDebutEncheres").toLocalDate(),
								rs.getDate("dateFinEncheres").toLocalDate(), rs.getInt("miseAPrix"),
								rs.getInt("prixVente"), rs.getString("etatVente")));
			}
			return articles;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	@Override
	public List<String> findByCategorie() {
		try(
			Connection connection = ConnectionProvider.getConnection();
			PreparedStatement pstmt = connection.prepareStatement(FIND_BY_CATEGORY);	
			) {
			List<String> categories = new ArrayList<String>();
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				categories.add(rs.getString("libelle"));
			}
			return categories;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
