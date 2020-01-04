/*
 * @author Gonzalo Nieto Berríos
 */

package cl.ucn.disc.dsm.mrnews.services;

import cl.ucn.disc.dsm.mrnews.model.Noticia;
import java.util.List;

public interface NoticiaService {
  List<Noticia> getNoticias(final int pageSize);
}
