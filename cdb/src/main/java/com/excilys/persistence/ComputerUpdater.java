package com.excilys.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.model.Computer;

/**
 * Classe permettant d'effectuer des mises à jour sur les éléments de la table computer à partir
 * d'instances de Computer.
 *
 * @author jguyot2
 */
@Transactional
@Repository
public class ComputerUpdater {
    private static final Logger LOG = LoggerFactory.getLogger(ComputerUpdater.class);

    private static final String REQUEST_DELETE_COMPUTER_FROM_COMPANY_ID = "DELETE FROM computer WHERE company_id = ?";

    @Autowired
    private EntityManager em;

    /**
     * Ajoute une ligne à la base de données, correspondant à l'instance de Computer donnée en
     * paramètre.
     *
     * @param newComputer l'instance de Computer à enregistrer dans la base de données
     *
     * @return le nouvel identifiant correspondant à la ligne ajoutée si l'ajout a réussi, 0 si
     *         l'ajout a raté
     */
    public long createComputer(@NonNull final Computer newComputer) {
        LOG.trace("Création de l'instance de Computer suivante: " + newComputer);
        this.em.getTransaction().begin();

        try {

            this.em.merge(newComputer);
            this.em.getTransaction().commit();
            return newComputer.getId();

        } catch (Exception e) {
            LOG.error("", e);
            throw e;
        }
    }

    /**
     * Suppression d'un ordinateur de la base à partir de son identifiant.
     *
     * @param id l'identifiant
     *
     * @return 1 si la suppression est effective, 0 sinon
     *
     * @throws SQLException
     */
    public int deleteById(final Long id) {
        this.em.getTransaction().begin();

        try {
            CriteriaBuilder cb = this.em.getCriteriaBuilder();
            CriteriaDelete<Computer> cd = cb.createCriteriaDelete(Computer.class);
            Root<Computer> r = cd.from(Computer.class);
            cd.where(cb.equal(r.get("id"), id));
            int ret = this.em.createQuery(cd).executeUpdate();
            this.em.getTransaction().commit();
            return ret;
        } catch (Exception e) {
            LOG.error("", e);
            throw e;
        }
    }

    /**
     * Suppression des ordinateurs d'un fabricant dont l'identifiant est en paramètre
     *
     * @param manufacturerId l'identifiant du fabricant dont il faut supprimer les ordinateurs.
     * @param conn           la connexion à utiliser
     * @return le nombre de PC supprimés
     * @throws SQLException
     */
    public int deleteComputersFromManufacturerIdWithConnection(final long manufacturerId, final Connection conn)
            throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(REQUEST_DELETE_COMPUTER_FROM_COMPANY_ID)) {
            stmt.setLong(1, manufacturerId);
            return stmt.executeUpdate();
        }
    }

    /**
     * Met à jour l'ordinateur en paramètre, dont l'identifiant est intialisé.
     *
     * @param newComputer la valeur de l'ordinateur à laquelle on veut faire correspondre la ligne
     *
     * @return 1 si la mise à jour a eu lieu, 0 sinon
     */
    public int updateComputer(@NonNull final Computer newComputer) {

        this.em.getTransaction().begin();

        try {
            CriteriaBuilder cb = this.em.getCriteriaBuilder();

            CriteriaUpdate<Computer> p = cb.createCriteriaUpdate(Computer.class);
            Root<Computer> r = p.from(Computer.class);
            p.set(r.get("introduced"), newComputer.getIntroduction())
                    .set("discontinued", newComputer.getDiscontinuation())
                    .set("manufacturer", newComputer.getManufacturer()).set("name", newComputer.getName())
                    .where(cb.equal(r.get("id"), new Long(newComputer.getId())));
            int ret = this.em.createQuery(p).executeUpdate();
            this.em.getTransaction().commit();
            return ret;
        } catch (Exception e) {
            LOG.error("", e);
            throw e;
        }
    }
}
