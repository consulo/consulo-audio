package consulo.audio.engine.javax;

import consulo.annotation.component.ComponentProfiles;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.Application;
import consulo.audio.AudioFileType;
import consulo.audio.engine.AudioEngine;
import consulo.audio.engine.AudioPlayer;
import consulo.ui.UIAccess;
import consulo.util.io.StreamUtil;
import consulo.util.lang.function.ThrowableSupplier;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.fileType.FileTypeConsumer;
import jakarta.annotation.Nonnull;
import jakarta.inject.Inject;

import javax.sound.sampled.*;
import java.io.IOException;

/**
 * @author VISTALL
 * @since 2020-11-12
 */
@ExtensionImpl(id = "javax", profiles = ComponentProfiles.AWT)
public class JavaxSoundAudioEngine implements AudioEngine {
    private final Application myApplication;

    @Inject
    public JavaxSoundAudioEngine(Application application) {
        myApplication = application;
    }

    @Override
    public boolean isAvailable(@Nonnull VirtualFile virtualFile) {
        try {
            AudioInputStream.class.getName();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Nonnull
    @Override
    public AudioPlayer createPlayer(@Nonnull VirtualFile audioFile) throws Exception {
        UIAccess.assetIsNotUIThread();

        AudioInputStream encodedStream = AudioSystemProxy.getAudioInputStream(myApplication, audioFile.getInputStream());

        Clip clip;
        try {
            AudioFormat encodedFormat = encodedStream.getFormat();

            int bitDepth = 16;

            AudioFormat decodedFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                encodedFormat.getSampleRate(),
                bitDepth,
                encodedFormat.getChannels(),
                encodedFormat.getChannels() * (bitDepth / 8), // 2*8 = 16-bits per sample per channel
                44100,
                encodedFormat.isBigEndian());

            clip = AudioSystemProxy.getClip(myApplication);

            if (AudioSystemProxy.isConversionSupported(myApplication, decodedFormat, encodedFormat)) {
                AudioInputStream decodedStream = AudioSystemProxy.getAudioInputStream(myApplication, decodedFormat, encodedStream);

                clip.open(decodedStream);
            }
            else {
                clip.open(encodedStream);
            }
        }
        catch (IOException | LineUnavailableException e) {
            StreamUtil.closeStream(encodedStream);
            throw e;
        }

        return new JavaxSoundAudioPlayer(clip);
    }

    @Override
    public void registerFileTypes(FileTypeConsumer fileTypeConsumer) {
        for (AudioFileFormat.Type type : AudioSystemProxy.getAudioFileTypes(myApplication)) {
            String extension = type.getExtension();

            fileTypeConsumer.consume(AudioFileType.INSTANCE, extension);
        }
    }

    @Deprecated
    private <T, E extends Exception> T runUnderSystemClassLoader(@Nonnull ThrowableSupplier<T, E> consumer) throws E {
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
