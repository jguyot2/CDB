package com.excilys.persistence;

import java.sql.SQLException;
import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.model.Company;
import com.excilys.model.Computer;

/**
 * Classe permettant d'effectuer des mises à jour sur les éléments de la table
 * computer à partir d'instances de Computer.
 *
 * @author jguyot2
 */
@Repository
public class ComputerUpdater {

	private static final Logger LOG = LoggerFactory.getLogger(ComputerUpdater.class);

	@PersistenceContext
	private EntityManager em;

	/**
	 * Ajoute une ligne à la base de données, correspondant à l'instance de Computer
	 * donnée en paramètre.
	 *
	 * @param newComputer l'instance de Computer à enregistrer dans la base de
	 *                    données
	 *
	 * @return le nouvel identifiant correspondant à la ligne ajoutée si l'ajout a
	 *         réussi, 0 si l'ajout a raté
	 */
	@Transactional(rollbackFor = DaoException.class)
	public long createComputer(@NonNull final Computer newComputer) throws DaoException {
		try {
			ComputerUpdater.LOG.trace("Création de l'instance de Computer suivante: " + newComputer);
			this.em.merge(newComputer);
			return newComputer.getId();
		} catch (PersistenceException e) {
			throw new DaoException(e);
		}
	}

	/**
	 * Suppression d'un ordinateur de la base à partir de son identifiant.
	 *
	 * @param identifiers l'identifiant
	 *
	 * @return 1 si la suppression est effective, 0 sinon
	 *
	 * @throws SQLException
	 */
	@Transactional(rollbackFor = DaoException.class)
	public int deleteById(@NonNull final Long... identifiers) throws DaoException {
		try {
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			CriteriaDelete<Computer> cd = cb.createCriteriaDelete(Computer.class);
			Root<Computer> r = cd.from(Computer.class);
			cd.where(cb.in(r.get("id").in(Arrays.asList(identifiers))));
			int ret = this.em.createQuery(cd).executeUpdate();
			return ret;
		} catch (PersistenceException e) {
			throw new DaoException(e);
		}
	}

	/**
	 * Suppression des ordinateurs dont l'id du fabricant est en paramètre.
	 *
	 * @param companyId
	 *
	 * @return le nombre d'ordinateurs supprimés
	 *
	 * @throws DaoException Si problème associé à la base
	 */
	public int deleteComputersOfCompany(@NonNull final Long companyId) throws DaoException {
		try {
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			CriteriaDelete<Computer> computerDeleteCriteria = cb.createCriteriaDelete(Computer.class);
			Root<Computer> r = computerDeleteCriteria.from(Computer.class);
			Join<Computer, Company> companyJoin = r.join("manufacturer", JoinType.LEFT);
			computerDeleteCriteria.where(cb.equal(companyJoin.get("id"), companyId));
			return this.em.createQuery(computerDeleteCriteria).executeUpdate();
		} catch (PersistenceException e) {
			throw new DaoException(e);
		}

	}

	/**
	 * Met à jour l'ordinateur en paramètre, dont l'identifiant est intialisé.
	 *
	 * @param newComputer la valeur de l'ordinateur à laquelle on veut faire
	 *                    correspondre la ligne
	 *
	 * @return 1 si la mise à jour a eu lieu, 0 sinon
	 */
	@Transactional(rollbackFor = DaoException.class)
	public int updateComputer(@NonNull final Computer newComputer) throws DaoException {
		try {
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			CriteriaUpdate<Computer> p = cb.createCriteriaUpdate(Computer.class);
			Root<Computer> r = p.from(Computer.class);
			p.set(r.get("introduced"), newComputer.getIntroduction())
					.set("discontinued", newComputer.getDiscontinuation())
					.set("manufacturer", newComputer.getManufacturer()).set("name", newComputer.getName())
					.where(cb.equal(r.get("id"), newComputer.getId()));
			int ret = this.em.createQuery(p).executeUpdate();
			return ret;
		} catch (PersistenceException e) {
			throw new DaoException(e);
		}
	}
}
