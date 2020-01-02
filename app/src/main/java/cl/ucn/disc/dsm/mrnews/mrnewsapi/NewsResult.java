/*
 * @author Gonzalo Nieto Berríos
 */

package cl.ucn.disc.dsm.mrnews.mrnewsapi;

import java.util.ArrayList;
import java.util.List;

public class NewsResult {

    public String status;
    public long totalResults;
    public List<Article> articles = new ArrayList<Article>();

}
