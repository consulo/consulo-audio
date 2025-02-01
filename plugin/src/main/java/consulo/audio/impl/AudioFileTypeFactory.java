package consulo.audio.impl;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.Application;
import consulo.audio.engine.AudioEngine;
import consulo.virtualFileSystem.fileType.FileTypeConsumer;
import consulo.virtualFileSystem.fileType.FileTypeFactory;
import jakarta.annotation.Nonnull;
import jakarta.inject.Inject;

/**
 * @author VISTALL
 * @since 2020-09-04
 */
@ExtensionImpl
public class AudioFileTypeFactory extends FileTypeFactory {
    private final Application myApplication;

    @Inject
    public AudioFileTypeFactory(Application application) {
        myApplication = application;
    }

    @Override
    public void createFileTypes(@Nonnull FileTypeConsumer fileTypeConsumer) {
        myApplication.getExtensionPoint(AudioEngine.class).forEachExtensionSafe(e -> e.registerFileTypes(fileTypeConsumer));
    }
}
