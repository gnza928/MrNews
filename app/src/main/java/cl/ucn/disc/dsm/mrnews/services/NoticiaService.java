/*
 * @author Gonzalo Nieto Berríos
 */

package cl.ucn.disc.dsm.mrnews.services;

import cl.ucn.disc.dsm.mrnews.model.Noticia;
import java.util.List;

public interface NoticiaService {

  /**
   * Get the Noticias from the backend.
   *
   * @param pageSize how many.
   * @return the {@link List} of {@link Noticia}.
   */
  List<Noticia> getNoticias(final int pageSize);

  /**
   *   *
   * @param pageSize - how many News
   * @return - The {@link List} of {@link Noticia}
   */
  List<Noticia> getTopHeadLines(final int pageSize);

}
