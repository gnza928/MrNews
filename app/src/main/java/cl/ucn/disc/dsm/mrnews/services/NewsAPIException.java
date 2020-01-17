/*
 * @author Gonzalo Nieto Berríos
 */

package cl.ucn.disc.dsm.mrnews.services;

/**
 * The Exception.
 */
public final class NewsAPIException extends RuntimeException {

  public NewsAPIException(final String message) {
    super(message);
  }

  public NewsAPIException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
