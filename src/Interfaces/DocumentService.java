package Interfaces;

import model.Reservation;

import java.io.IOException;

/**
 * Interface pour la génération de documents.
 */
public interface DocumentService {

    void genererRecuTxt(Reservation reservation) throws IOException;
}
