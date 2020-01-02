/*
 * @author Gonzalo Nieto Berríos
 */

package cl.ucn.disc.dsm.mrnews.mrnewsapi;

import android.os.Build;

import androidx.annotation.RequiresApi;

import cl.ucn.disc.dsm.mrnews.Transformer;
import cl.ucn.disc.dsm.mrnews.model.Noticia;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public final class NewsAPIService {
    /**
     * The Logger
     */
    private static final Logger log = LoggerFactory.getLogger(NewsAPIService.class);

    /**
     * The Api Key
     */
    private static final String API_KEY = "THIS_IS_MY_API_KEY";

    /**
     * El conversor de List de Article a List de Noticia.
     */
    private static final Transformer<Article> TRANSFORMER = new Transformer<>(new ArticleNoticiaTransformer());

    /**
     * The NewsAPI
     */
    private final NewsAPI newsAPI;

    /**
     * Constructor que inicializa las NewsAPI via retrofit.
     */
    public NewsAPIService() {

        // https://futurestud.io/tutorials/retrofit-getting-started-and-android-client
        this.newsAPI = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NewsAPI.class);
    }

    /**
     * @param theCall to use to get the Noticia.
     * @return the {@link List} of {@link Noticia}.
     */
    private static List<Noticia> getNoticiasFromCall(final Call<NewsResult> theCall) {

        try {

            // Get the News from Category
            final Response<NewsResult> response = theCall.execute();

            // Code in the 2xx range
            if (response.isSuccessful()) {

                // .. may be the data can be null?
                if (response.body() == null) {
                    throw new NewsAPIException("Body was null");
                }

                // Obtengo el newsResult del body
                final NewsResult newsResult = response.body();

                // .. may the articles can be null?
                if (newsResult.articles == null) {
                    throw new NewsAPIException("Articles was null");
                }
                return TRANSFORMER.transform(newsResult.articles);

            }

            final HttpException httpException = new HttpException(response);
            log.error("Error: {}.", httpException.response().errorBody().string());

            // No fue exitoso, algun error ocurrio
            throw new NewsAPIException("Can't get the NewsResult, code: " + response.code() + ". ", httpException);

        } catch (final IOException ex) {
            throw new NewsAPIException("Can't get the NewsResult", ex);
        }

    }

    /**
     * Get the List of Noticia filtered by category.
     *
     * @param category to filter.
     * @param pageSize numbers of Noticia to get.
     * @return la {@link List} de {@link Noticia}.
     */
    public List<Noticia> getNoticias(final Category category, final int pageSize) {

        return getNoticiasFromCall(newsAPI.getTopHeadlines(category.toString(), pageSize));

    }

    /**
     * Get the top everything of Noticia.
     *
     * @param pageSize numbers of Noticia to get.
     * @return the {@link List} of {@link Noticia}.
     */
    public List<Noticia> getNoticias(final int pageSize) {

        return getNoticiasFromCall(newsAPI.getEverything(pageSize));

    }

    /**
     * Categorias del servicio.
     */
    public enum Category {
        business,
        entertainment,
        general,
        health,
        science,
        sports,
        technology
    }

    /**
     * API que provee NewsAPI.org
     */
    public interface NewsAPI {

        /**
         * https://newsapi.org/docs/endpoints/top-headlines
         *
         * @param category to use as filter.
         * @param pageSize the number of results to get.
         * @return the call of {@link NewsResult}.
         */
        @Headers({"X-Api-Key: " + API_KEY})
        @GET("top-headlines")
        Call<NewsResult> getTopHeadlines(@Query("category") final String category, @Query("pageSize") final int pageSize);

        /**
         * https://newsapi.org/docs/endpoints/everything
         *
         * @return the call of {@link NewsResult}.
         */
        @Headers({"X-Api-Key: " + API_KEY})
        @GET("everything?sources=ars-technica,wired,hacker-news")
        Call<NewsResult> getEverything(@Query("pageSize") final int pageSize);

    }

    /**
     * The Exception.
     */
    public static final class NewsAPIException extends RuntimeException {

        public NewsAPIException(final String message) {
            super(message);
        }

        public NewsAPIException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
}
