/*
 * @author Gonzalo Nieto Berríos
 */

package cl.ucn.disc.dsm.mrnews.mrnewsapi;

import cl.ucn.disc.dsm.mrnews.Transformer;
import cl.ucn.disc.dsm.mrnews.model.Noticia;
import cl.ucn.disc.dsm.mrnews.model.NoticiaBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import net.openhft.hashing.LongHashFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeParseException;

public final class ArticleNoticiaTransformer implements Transformer.NoticiaTrasformer<Article>{

    private static final Logger log = LoggerFactory.getLogger(ArticleNoticiaTransformer.class);


    private static ZonedDateTime parseZonedDateTime(final String fecha) {

        // Na' que hacer si la fecha no existe
        if (fecha == null) {
            throw new Transformer.NoticiaTransformerException("Can't parse null fecha");
        }

        try {
            // Tratar de convertir la fecha ..
            return ZonedDateTime.parse(fecha);
        } catch (DateTimeParseException ex) {

            // Mensaje de debug
            log.error("Can't parse date: ->{}<-. Error: ", fecha, ex);

            // Anido la DateTimeParseException en una NoticiaTransformerException.
            throw new Transformer.NoticiaTransformerException("Can't parse date: " + fecha, ex);
        }
    }

    private static String getHost(final String url) {

        try {

            final URI uri = new URI(url);
            final String hostname = uri.getHost();

            // to provide faultproof result, check if not null then return only hostname, without www.
            if (hostname != null) {
                return hostname.startsWith("www.") ? hostname.substring(4) : hostname;
            }

            return null;

        } catch (final URISyntaxException | NullPointerException ex) {
            return null;
        }
    }

    @Override
    public Noticia transform(Article article) {
        // Nullity
        if (article == null) {
            throw new Transformer.NoticiaTransformerException("Article was null");
        }

        // The host
        final String host = getHost(article.url);

        // Si el articulo es null ..
        if (article.title == null) {

            log.warn("Article without title: {}", Transformer.toString(article));

            // .. y el contenido es null, lanzar exception!
            if (article.description == null) {
                throw new Transformer.NoticiaTransformerException("Article without title and description");
            }

            // FIXME: Cambiar el titulo por alguna informacion disponible
            article.title = "No Title*";
        }

        // FIXED: En caso de no haber una fuente.
        if (article.source == null) {
            article.source = new Source();

            if (host != null) {
                article.source.name = host;
            } else {
                article.source.name = "No Source*";
                log.warn("Article without source: {}", Transformer.toString(article));
            }
        }

        // FIXED: Si el articulo no tiene author
        if (article.author == null) {

            if (host != null) {
                article.author = host;
            } else {
                article.author = "No Author*";
                log.warn("Article without author: {}", Transformer.toString(article));
            }
        }

        // The date.
        final ZonedDateTime publishedAt = ZonedDateTime.parse(article.publishedAt)
                .withZoneSameInstant(Noticia.ZONE_ID);

        // The unique id (computed from hash)
        final Long theId = LongHashFunction.xx().hashChars(article.title + article.source.name);

        // FIXED: Use a builder pattern?
        return new NoticiaBuilder()
                .setId(article.source.id)
                .setTitulo(article.title)
                .setFuente(article.source.name)
                .setAutor(article.author)
                .setUrl(article.url)
                .setUrlFoto(article.urlToImage)
                .setResumen(article.description)
                .setContenido(article.content)
                .setFecha(publishedAt)
                .createNoticia();
    }
}
