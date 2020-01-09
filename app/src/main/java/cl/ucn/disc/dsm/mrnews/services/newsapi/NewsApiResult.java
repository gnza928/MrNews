/*
 * @author Gonzalo Nieto Berríos
 */

/*
 * @author Gonzalo Nieto Berríos
 */

package cl.ucn.disc.dsm.mrnews.services.newsapi;

import java.util.ArrayList;
import java.util.List;

public class NewsApiResult {

  public String status;
  public long totalResults;
  public List<Article> articles = new ArrayList<Article>();

}
