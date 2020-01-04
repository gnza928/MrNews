/*
 * @author Gonzalo Nieto Berríos
 */

package cl.ucn.disc.dsm.mrnews.mrnewsapi;

import cl.ucn.disc.dsm.mrnews.Transformer;
import cl.ucn.disc.dsm.mrnews.Transformer.NoticiaTransformerException;
import cl.ucn.disc.dsm.mrnews.model.Noticia;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public final class TestNewsAPI {
    /**
     * The Logger
     */
    private static final Logger log = LoggerFactory.getLogger(TestNewsAPI.class);

    /**
     * Test of {@link ArticleNoticiaTransformer#transform}
     */
    @Test
    public void testTransform() {

        // The transformer
        final Transformer.NoticiaTrasformer<Article> transformer = new ArticleNoticiaTransformer();

        // Testing de Article con titulo y sin contenido
        {
            final Article article = new Article();
            article.publishedAt = "2019-10-15T11:04:37Z";

            // No puede haber un articulo sin titulo y description
            Assertions.assertThrows(NoticiaTransformerException.class, () -> {
                transformer.transform(article);});

            article.description = "This is the description";

            Assertions.assertDoesNotThrow(() -> {
                Assertions.assertNotNull(transformer.transform(article), "Articulo was null")
            ;});
        }

        // One article.
        final Article article = new Article();
        {
            article.source = new Source();
            article.source.id = null;
            article.source.name = "Newsbtc.com";
            article.author = "Tony Spilotro";
            article.title = "NVT: Top and Bottom Indicator Signals Time To Buy Bitcoin";
            article.description = "Bitcoin’s network to transactions ratio, or NVT for short, is an indicator developed by crypto analyst Willy Woo as a signal for deterring tops and bottoms of Bitcoin cycles. A tweaked version of the indicator, one that was designed to filter out private tran…";
            article.url = "https://www.newsbtc.com/2019/10/10/nvt-top-and-bottom-indicator-signals-time-to-buy-bitcoin/";
            article.urlToImage = "https://www.newsbtc.com/wp-content/uploads/2019/10/buy-bitcoin-signal-shutterstock_712819117-1200x780.jpg";
            article.publishedAt = "2011-12-03T10:15:30Z";
            article.content = "Bitcoins network to transactions ratio, or NVT for short, is an indicator developed by crypto analyst Willy Woo as a signal for deterring tops and bottoms of Bitcoin cycles.\\r\\nA tweaked version of the indicator, one that was designed to filter out private tran… [+2681 chars]";
        }

        log.debug("Testing with Article: {}", Transformer.toString(article));

        // Nullity
        log.debug("Testing pseudo-nullity ..");
        Assertions.assertThrows(NoticiaTransformerException.class, () -> {
            transformer.transform(null);
            transformer.transform(new Article());
        });

        // The transform
        log.debug("Testing Article to Noticia ..");
        {
            final Noticia noticia = transformer.transform(article);
            Assertions.assertNotNull(noticia, "Noticia fue null!");
            Assertions.assertEquals(noticia.getTitulo(), article.title);
            Assertions.assertEquals(noticia.getAutor(), article.author);
            Assertions.assertEquals(noticia.getContenido(), article.content);
            Assertions.assertEquals(noticia.getResumen(), article.description);
            Assertions.assertEquals(noticia.getFuente(), article.source.name);
            Assertions.assertEquals(noticia.getUrl(), article.url);
            Assertions.assertEquals(noticia.getUrlFoto(), article.urlToImage);
            Assertions.assertEquals(noticia.getFecha().toString(), article.publishedAt);
        }

        // Source: null
        log.debug("Testing Article (no source) to Noticia ..");
        {
            article.source = null;
            Assertions.assertDoesNotThrow(() -> {

                // Aca se DEBE caer (source no valida)
                final Noticia noticia = transformer.transform(article);

                Assertions.assertNotNull(noticia, "Noticia fue null!");

            }, "Article sin source NO debe lanzar exception!");
        }

        // publishedAt: null
        log.debug("Testing Article (dates) to Noticia ..");
        {
            // Date null
            article.publishedAt = null;
            Assertions.assertThrows(NoticiaTransformerException.class, () -> {
                transformer.transform(article);
            });

            // Date no valid
            article.publishedAt = "Fecha no valida";
            Assertions.assertThrows(NoticiaTransformerException.class, () -> {
                transformer.transform(article);
            });
        }

        // title: null
        log.debug("Testing Article (no title) to Noticia ..");
        {
            article.publishedAt = "2011-12-03T10:15:30Z";
            article.title = null;
            final Noticia noticia = transformer.transform(article);
            Assertions.assertNotNull(noticia.getTitulo(), "Titulo no puede ser null");
        }

    }

    /**
     * Test de la conversion de una lista de {@link Article} a una lista de {@link Noticia}.
     */
    @Test
    public void testTransformList() {

        // The A1
        final Article a1 = new Article();
        {
            a1.title = "NASA's new Artemis spacesuits make it easier for astronauts of all sizes to move on the Moon";
            a1.source = new Source();
            a1.source.id = "techcrunch";
            a1.source.name = "TechCrunch";
            a1.author = "Darrell Etherington";
            a1.content = "NASA revealed new spacesuits, specifically created for the Artemis generation of missions, which aim to get the first American woman and the next American man to the surface of the Moon by 2024. The new design’s toppling feature is greater mobility and flexib… [+4759 chars]";
            a1.description = "NASA revealed new spacesuits, specifically created for the Artemis generation of missions, which aim to get the first American woman and the next American man to the surface of the Moon by 2024. The new design’s toppling feature is greater mobility and flexib…";
            a1.url = "https://techcrunch.com/2019/10/15/nasas-new-artemis-spacesuits-make-it-easier-for-astronauts-of-all-sizes-to-move-on-the-moon/";
            a1.urlToImage = "https://techcrunch.com/wp-content/uploads/2019/10/NASA-xEMU-spacesuit.gif?w=764";
            a1.publishedAt = "2019-10-15T18:42:46Z";
        }

        // The A2
        final Article a2 = new Article();
        {
            a2.title = "Up close with Google's new Pixel 4";
            a2.source = new Source();
            a2.source.id = "techcrunch";
            a2.source.name = "TechCrunch";
            a2.author = "Brian Heater";
            a2.content = "This is the Pixel 4, the handset that literally everyone saw coming. Even by Google’s standards, the handset leaked like crazy. Some was almost certainly by design as the company looked to hype up its new flagship amid slowing smartphone sales. That said, sho… [+3098 chars]";
            a2.description = "This is the Pixel 4, the handset that literally everyone saw coming. Even by Google’s standards, the handset leaked like crazy. Some was almost certainly by design as the company looked to hype up its new flagship amid slowing smartphone sales. That said, sho…";
            a2.url = "https://techcrunch.com/2019/10/15/up-close-with-googles-new-pixel-4/";
            a2.urlToImage = "https://techcrunch.com/wp-content/uploads/2019/10/CMB_8456.jpeg?w=600";
            a2.publishedAt = "2019-10-15T16:04:45Z";
        }

        // The A3
        final Article a3 = new Article();
        {
            a3.publishedAt = "2019-10-15T16:00:50Z";
        }

        // The list of articles
        final List<Article> articles = Arrays.asList(
                a1,
                null,
                a2,
                null,
                new Article(), // Empty article
                a3,
                null
        );

        // The transformer
        final Transformer<Article> transformer = new Transformer<>(new ArticleNoticiaTransformer());

        // Transformacion
        final List<Noticia> noticias = transformer.transform(articles);
        Assertions.assertNotNull(noticias, "List of Noticia fue null");

        // El tamanio debe ser solamente los 2 articulos validos
        Assertions.assertEquals(2, noticias.size(), "Listado de tamanio incorrecto");

    }

    /**
     * Testing the News API Service. Need to end in XX seconds.
     */
    @Test
    @Timeout(30) // Waiting for ..?
    public void testNewsAPIServiceCategory() {

        // The final list
        final List<Noticia> noticiaList = new ArrayList<>();

        // The size of news
        final int pageSize = RandomUtils.nextInt(5, 11);

        // The timer
        final StopWatch stopWatch = StopWatch.createStarted();

        log.debug("Testing NewsAPIService ..");
        final NewsAPIService newsAPIService = new NewsAPIService();

        // The array of categories
        final NewsAPIService.Category[] categories = {
                NewsAPIService.Category.business,
                NewsAPIService.Category.technology,
                NewsAPIService.Category.entertainment,
                NewsAPIService.Category.health,
                NewsAPIService.Category.science,
                NewsAPIService.Category.sports,
                NewsAPIService.Category.general
        };

        // Get the news of each category
        for (int i = 0; i < categories.length; i++) {

            log.info("Getting {} news of type: {} ..", pageSize, categories[i]);

            // The list of Noticia
            final List<Noticia> noticias = newsAPIService.getNoticias(categories[i], pageSize);

            Assertions.assertNotNull(noticias);
            Assertions.assertEquals(pageSize, noticias.size());
            // log.debug("Noticia: {}", Transformer.toString(noticias));

            noticiaList.addAll(noticias);

        }

        // The time?
        log.info("Time: {}", stopWatch);

        // Ordenado por fecha (ultimas primero
        Collections.sort(noticiaList, (a, b) -> b.getFecha().compareTo(a.getFecha()));

        log.debug("Noticias: {}", noticiaList.size());

        for (final Noticia noticia : noticiaList) {
            log.debug("Noticia: {}", Transformer.toString(noticia));
            log.debug("-----------------------------------------------------------------------");
        }

    }

    /**
     * Testing the News API Service. Need to end in XX seconds.
     */
    @Test
    @Timeout(30) // Waiting for ..?
    public void testNewsAPIService() {

        log.debug("Testing NewsAPIService ..");
        final NewsAPIService newsAPIService = new NewsAPIService();

        // The timer
        final StopWatch stopWatch = StopWatch.createStarted();

        // The size of news
        final int pageSize = RandomUtils.nextInt(5, 11);

        // The final list
        final List<Noticia> noticias = newsAPIService.getNoticias(pageSize);

        for (final Noticia noticia : noticias) {
            log.debug("Noticia: {}", Transformer.toString(noticia));
        }

        // The time?
        log.info("Time: {}", stopWatch);

    }
}
