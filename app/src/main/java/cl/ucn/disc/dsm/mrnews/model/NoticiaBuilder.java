/*
 * @author Gonzalo Nieto Berríos
 */

package cl.ucn.disc.dsm.mrnews.model;

import org.threeten.bp.ZonedDateTime;

public class NoticiaBuilder {
    private String id;
    private String titulo;
    private String fuente;
    private String autor;
    private String url;
    private String urlFoto;
    private String resumen;
    private String contenido;
    private ZonedDateTime fecha;

    public NoticiaBuilder setId(final String id) {
        this.id = id;
        return this;
    }

    public NoticiaBuilder setTitulo(final String titulo) {
        this.titulo = titulo;
        return this;
    }

    public NoticiaBuilder setFuente(final String fuente) {
        this.fuente = fuente;
        return this;
    }

    public NoticiaBuilder setAutor(final String autor) {
        this.autor = autor;
        return this;
    }

    public NoticiaBuilder setUrl(final String url) {
        this.url = url;
        return this;
    }

    public NoticiaBuilder setUrlFoto(final String urlFoto) {
        this.urlFoto = urlFoto;
        return this;
    }

    public NoticiaBuilder setResumen(final String resumen) {
        this.resumen = resumen;
        return this;
    }

    public NoticiaBuilder setContenido(final String contenido) {
        this.contenido = contenido;
        return this;
    }

    public NoticiaBuilder setFecha(final ZonedDateTime fecha) {
        this.fecha = fecha;
        return this;
    }

    public Noticia createNoticia() {
        return new Noticia(this.id, this.titulo, this.fuente, this.autor, this.url, this.urlFoto, this.resumen, this.contenido, this.fecha);
    }
}