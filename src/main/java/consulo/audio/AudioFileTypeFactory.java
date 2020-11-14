package consulo.audio;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import consulo.audio.engine.AudioEngine;
import jakarta.inject.Inject;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2020-09-04
 */
public class AudioFileTypeFactory extends FileTypeFactory
{
	private final Application myApplication;

	@Inject
	public AudioFileTypeFactory(Application application)
	{
		myApplication = application;
	}

	@Override
	public void createFileTypes(@Nonnull FileTypeConsumer fileTypeConsumer)
	{
		AudioEngine.EP_NAME.forEachExtensionSafe(myApplication, audioEngine -> audioEngine.registerFileTypes(fileTypeConsumer));
	}
}
