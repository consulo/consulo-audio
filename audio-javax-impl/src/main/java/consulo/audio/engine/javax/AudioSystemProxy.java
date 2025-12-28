package consulo.audio.engine.javax;

import consulo.application.Application;
import consulo.component.extension.SPIClassLoaderExtension;
import consulo.container.plugin.PluginManager;
import consulo.util.lang.function.ThrowableFunction;
import consulo.util.lang.function.ThrowableSupplier;
import jakarta.annotation.Nonnull;

import javax.sound.sampled.*;
import javax.sound.sampled.spi.AudioFileReader;
import javax.sound.sampled.spi.AudioFileWriter;
import javax.sound.sampled.spi.FormatConversionProvider;
import java.io.InputStream;
import java.util.*;

/**
 * @author VISTALL
 * @since 2025-12-28
 */
public class AudioSystemProxy {
    public static boolean isConversionSupported(Application application,
                                                AudioFormat targetFormat,
                                                AudioFormat sourceFormat) {
        if (sourceFormat.matches(targetFormat)) {
            return true;
        }

        FormatConversionProvider provider = visitPlugins(application, FormatConversionProvider.class, it -> {
            if (it.isConversionSupported(targetFormat, sourceFormat)) {
                return it;
            }

            return null;
        });

        if (provider != null) {
            return true;
        }

        return runUnderSystemClassLoader(() -> AudioSystem.isConversionSupported(targetFormat, sourceFormat));
    }

    public static AudioInputStream getAudioInputStream(Application application,
                                                       AudioFormat targetFormat,
                                                       AudioInputStream sourceStream) {
        if (sourceStream.getFormat().matches(targetFormat)) {
            return sourceStream;
        }

        AudioInputStream stream = visitPlugins(application, FormatConversionProvider.class, it -> {
            if (it.isConversionSupported(targetFormat, sourceStream.getFormat())) {
                return it.getAudioInputStream(targetFormat, sourceStream);
            }

            return null;
        });

        if (stream != null) {
            return stream;
        }

        return runUnderSystemClassLoader(() -> AudioSystem.getAudioInputStream(targetFormat, sourceStream));
    }

    public static Clip getClip(Application application) throws LineUnavailableException {
        return runUnderSystemClassLoader(AudioSystem::getClip);
    }

    public static Set<AudioFileFormat.Type> getAudioFileTypes(Application application) {
        AudioFileFormat.Type[] audioFileTypes = runUnderSystemClassLoader(AudioSystem::getAudioFileTypes);

        Set<AudioFileFormat.Type> result = new HashSet<>();

        Collections.addAll(result, audioFileTypes);

        visitPlugins(application, AudioFileWriter.class, writer -> {
            Collections.addAll(result, writer.getAudioFileTypes());
            return null;
        });

        return result;
    }

    public static AudioInputStream getAudioInputStream(Application application, InputStream stream) throws Exception {
        AudioInputStream inputStream = visitPlugins(application, AudioFileReader.class, (AudioFileReader reader) -> {
            try {
                return reader.getAudioInputStream(stream);
            }
            catch (UnsupportedAudioFileException ignored) {
            }

            return null;
        });

        if (inputStream != null) {
            return inputStream;
        }


        return runUnderSystemClassLoader(() -> AudioSystem.getAudioInputStream(stream));
    }

    private static <R, T, E extends Throwable> R visitPlugins(Application application,
                                                              Class<T> serviceClass,
                                                              ThrowableFunction<T, R, E> consumer) throws E {
        List<ClassLoader> classLoaders = new ArrayList<>(application.getExtensionPoint(SPIClassLoaderExtension.class).collectMapped(it -> {
            if (it.getTargetClass() == AudioSystem.class) {
                return PluginManager.getPlugin(it.getClass()).getPluginClassLoader();
            }
            return null;
        }));

        // current audio plugin - bundled audio providers
        classLoaders.add(PluginManager.getPlugin(AudioSystemProxy.class).getPluginClassLoader());

        for (ClassLoader classLoader : classLoaders) {
            ServiceLoader<T> loader = ServiceLoader.load(serviceClass, classLoader);

            for (T t : loader) {
                R result = consumer.apply(t);
                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }

    private static <T, E extends Exception> T runUnderSystemClassLoader(@Nonnull ThrowableSupplier<T, E> consumer) throws E {
        Thread currentThread = Thread.currentThread();

        ClassLoader old = currentThread.getContextClassLoader();

        currentThread.setContextClassLoader(ClassLoader.getSystemClassLoader());
        try {
            return consumer.get();
        }
        finally {
            currentThread.setContextClassLoader(old);
        }
    }
}
