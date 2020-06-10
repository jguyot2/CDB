package com.excilys.service;

/**
 * Exception lancée pour laisser le code compiler quand toutes les features sont pas encore faites.
 * Protip : Remplacer "RuntimeException" par "Exception" pour voir dans quels fichiers l'exception
 * est appellée
 * @author jguyot2
 *
 */
public class NotImplementedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

}
