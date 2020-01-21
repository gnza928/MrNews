/*
 * @author Gonzalo Nieto Berríos
 */

/*
 * @author Gonzalo Nieto Berríos
 */

package cl.ucn.disc.dsm.mrnews;

import android.app.Application;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.VmPolicy;
import androidx.appcompat.app.AppCompatDelegate;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.jakewharton.threetenabp.AndroidThreeTen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MainApplication extends Application {

  /**
   * The Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(MainApplication.class);

  /**
   * Called when the application is starting, before any activity, service, or receiver objects (excluding content
   * providers) have been created.
   */
  @Override
  public void onCreate() {
    super.onCreate();

    log.debug("Initializing ..");

    // Day and Night support
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    AndroidThreeTen.init(this);

    // Fresco configuration for large images
    ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
        .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
        .setResizeAndRotateEnabledForNetwork(true)
        .setDownsampleEnabled(true)
        .build();

    // Fresco initialization
    Fresco.initialize(this, config);


    if (BuildConfig.DEBUG) {

      StrictMode.setThreadPolicy(new ThreadPolicy.Builder()
          .detectDiskReads()
          .detectDiskWrites()
          .detectNetwork()
          .penaltyLog()
          .penaltyFlashScreen()
          .build());

      StrictMode.setVmPolicy(new VmPolicy.Builder()
          .detectLeakedSqlLiteObjects()
          .detectLeakedClosableObjects()
          .detectLeakedRegistrationObjects()
          .detectActivityLeaks()
          .detectCleartextNetwork()
          .penaltyLog()
          .build());

    }


    log.debug("Initializing: Done.");
  }

}