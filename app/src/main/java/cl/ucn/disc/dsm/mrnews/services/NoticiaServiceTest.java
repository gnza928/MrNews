/*
 * @author Gonzalo Nieto Berríos
 */

package cl.ucn.disc.dsm.mrnews.services;

import cl.ucn.disc.dsm.mrnews.Transformer;
import cl.ucn.disc.dsm.mrnews.model.Noticia;
import cl.ucn.disc.dsm.mrnews.mrnewsapi.NewsAPIService.NewsAPIException;
import cl.ucn.disc.dsm.mrnews.services.mockup.MockupNoticiaService;
import cl.ucn.disc.dsm.mrnews.services.newsapi.NewsApiResult;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;

public class NoticiaServiceTest {
  /**
   * The Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(NoticiaServiceTest.class);

  /**
   * Test {@link NoticiaService#getNoticias(int)} with NewsAPI.org
   * @return
   */
  @Test
  public NoticiaService testGetNoticiasNewsApi() {

    final int size = 20;

    log.debug("Testing the NewsApiNoticiaService, requesting {} News.", size);

    // The noticia service
    final NoticiaService noticiaService = testGetNoticiasNewsApi();

    // The List of Noticia.
    final List<Noticia> noticias = noticiaService.getNoticias(size);

    Assertions.assertNotNull(noticias);
    Assertions.assertEquals(noticias.size(), size, "Error de tamanio");

    for (final Noticia noticia : noticias) {
      log.debug("Noticia: {}.", noticia);
    }

    log.debug("Done.");

    return noticiaService;
  }


  /**
   * Test {@link NoticiaService#getNoticias(int)}
   */
  @Test
  public void testGetNoticiasMockup() {

    log.debug("Testing the NoticiaService ..");

    // The noticia service
    final NoticiaService noticiaService = new MockupNoticiaService();

    // The List of Noticia.
    final List<Noticia> noticias = noticiaService.getNoticias(2);

    Assertions.assertNotNull(noticias);
    Assertions.assertEquals(noticias.size(), 2, "Error de tamanio");

    for (final Noticia noticia : noticias) {
      log.debug("Noticia: {}.", noticia);
    }

    log.debug("Done.");
  }



}
