package consulo.audio;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import consulo.audio.provider.AudioFileTypeProvider;

import javax.annotation.Nonnull;
import javax.inject.Inject;

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
		AudioFileTypeProvider.EP_NAME.forEachExtensionSafe(myApplication, it -> it.createFileTypes(fileTypeConsumer));
	}
}
