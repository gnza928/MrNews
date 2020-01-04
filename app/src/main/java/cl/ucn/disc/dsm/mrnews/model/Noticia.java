/*
 * @author Gonzalo Nieto Berríos
 */

package cl.ucn.disc.dsm.mrnews.model;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

public final class Noticia {

    public static final ZoneId ZONE_ID = ZoneId.of("-3");

    private final Long id;
    private final String titulo;
    private final String fuente;
    private final String autor;
    private final String url;
    private final String urlFoto;
    private final String resumen;
    private final String contenido;
    private final ZonedDateTime fecha;

    public Noticia(Long id, String titulo, String fuente, String autor, String url, String urlFoto, String resumen, String contenido, ZonedDateTime fecha) {
        this.titulo = titulo;
        this.fuente = fuente;
        this.autor = autor;
        this.url = url;
        this.urlFoto = urlFoto;
        this.resumen = resumen;
        this.contenido = contenido;
        this.fecha = fecha;
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public String getFuente() {
        return this.fuente;
    }

    public String getAutor() {
        return this.autor;
    }

    public String getUrl() {
        return this.url;
    }

    public String getUrlFoto() {
        return this.urlFoto;
    }

    public String getResumen() {
        return this.resumen;
    }

    public String getContenido() {
        return this.contenido;
    }

    public ZonedDateTime getFecha() {
        return this.fecha;
    }
}
