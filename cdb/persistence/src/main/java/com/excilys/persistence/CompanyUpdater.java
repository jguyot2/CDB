





package com.excilys.persistence;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.excilys.model.NotImplementedException;


/**
 * Gestion de la mise à jour des entreprises dans la base
 *
 * @author jguyot2
 */
@Repository
public class CompanyUpdater {

     private static final Logger LOG = LoggerFactory.getLogger(CompanyUpdater.class);
     private static final String REQUEST_DELETE_COMPANY = "DELETE FROM company WHERE id = ?";

     @Autowired
     private JdbcTemplate template;
     @Autowired
     private ComputerUpdater computerUpdater;


     /**
      * Suppression d'une entreprise à partir de son identifiant, ainsi que tous les ordinateurs liés à cette
      * entreprise
      *
      * @param companyId l'identifiant de l'entreprise à supprimer.
      *
      * @return 1 si l'entreprise a été supprimée, 0 si l'id n'existe pas.
      *
      * @throws SQLException         si erreur dans la base
      * @throws PersistanceException
      */
     public int deleteCompany(final long companyId) throws SQLException, PersistanceException {
          throw new NotImplementedException();
     }
}
