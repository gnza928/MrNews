package cl.ucn.disc.dsm.mrnews.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import cl.ucn.disc.dsm.mrnews.R;
import cl.ucn.disc.dsm.mrnews.activities.adapters.NoticiaAdapter;
import cl.ucn.disc.dsm.mrnews.activities.adapters.NoticiaViewModel;
import cl.ucn.disc.dsm.mrnews.databinding.ActivityMainBinding;
import cl.ucn.disc.dsm.mrnews.model.Noticia;
import cl.ucn.disc.dsm.mrnews.services.NewsApiNoticiaService;
import cl.ucn.disc.dsm.mrnews.services.NoticiaService;
import es.dmoral.toasty.Toasty;
import java.util.List;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Main Launcher Activity.
 *
 * @author Diego Urrutia-Astorga.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The Logger
     */
    private static final Logger log = LoggerFactory.getLogger(MainActivity.class);

    /**
     * The Adapter
     */
    private NoticiaAdapter noticiaAdapter;

    /**
     * The NoticiaService
     */
    private NoticiaService noticiaService;

    /**
     * The ViewModel of Noticia.
     */
    private NoticiaViewModel noticiaViewModel;

    /**
     * The bindings.
     */
    private ActivityMainBinding binding;

    /**
     * @param savedInstanceState to use.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout
        this.binding = ActivityMainBinding.inflate(getLayoutInflater());

        // Assign to the main view.
        setContentView(this.binding.getRoot());

        // Set the toolbar
        {
            this.setSupportActionBar(this.binding.toolbar);
        }

        // The refresh
        {
            binding.swlRefresh.setOnRefreshListener(() -> {
                log.debug("Refreshing ..");
            });
        }

        // The Adapter + RecyclerView
        {
            // The Adapter
            this.noticiaAdapter = new NoticiaAdapter();

            // The Adapter
            this.binding.rvNoticias.setAdapter(this.noticiaAdapter);

            // The layout (ListView)
            this.binding.rvNoticias.setLayoutManager(new LinearLayoutManager(this));

            // The separator (line)
            this.binding.rvNoticias.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
            );

        }

        // The NoticiaService
        this.noticiaService = (NoticiaService) new NewsApiNoticiaService();

        // The refresh
        {
            this.binding.swlRefresh.setOnRefreshListener(() -> {
                log.debug("Refreshing ..");

                // Run in background
                AsyncTask.execute(() -> {

                    // All ok
                    final int size = this.noticiaViewModel.refresh();
                    if (size != -1) {

                        // In the UI
                        runOnUiThread(() -> {

                            // Hide the loading
                            this.binding.swlRefresh.setRefreshing(false);

                            // Show a message.
                            Toasty.success(this, "Noticias fetched: " + size, Toast.LENGTH_SHORT, true).show();

                        });
                    }

                });

            });
        }

        // The ViewModel
        {
            // Build the NoticiaViewModel.
            this.noticiaViewModel = new ViewModelProvider(this).get(NoticiaViewModel.class);

            // Observe the list of noticia
            this.noticiaViewModel.getNoticias().observe(this,
                noticias -> this.noticiaAdapter.setNoticias(noticias));

            // Observe the exception
            this.noticiaViewModel.getException().observe(this, this::showException);

        }
    }

    /**
     * Show the exception.
     *
     * @param exception to use.
     */
    private void showException(final Exception exception) {

        // Hide the loading
        this.binding.swlRefresh.setRefreshing(false);

        // Build the message
        final StringBuilder sb = new StringBuilder("Error: ");
        sb.append(exception.getMessage());
        if (exception.getCause() != null) {
            sb.append(", ");
            sb.append(exception.getCause().getMessage());
        }

        Toasty.error(this, sb.toString(), Toast.LENGTH_LONG, true).show();

    }

}