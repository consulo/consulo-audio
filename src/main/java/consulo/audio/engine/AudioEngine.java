package consulo.audio.engine;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.vfs.VirtualFile;
import consulo.extensions.StrictExtensionPointName;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2020-11-12
 */
public interface AudioEngine
{
	StrictExtensionPointName<Application, AudioEngine> EP_NAME = StrictExtensionPointName.forApplication("consulo.audio.engine");

	static AudioEngine forFile(@Nonnull Application application, @Nonnull VirtualFile file)
	{
		for(AudioEngine engine : EP_NAME.getExtensionList(application))
		{
			if(engine.isAvailable(file))
			{
				return engine;
			}
		}

		throw new UnsupportedOperationException();
	}

	boolean isAvailable(@Nonnull VirtualFile virtualFile);

	@Nonnull
	AudioPlayer create(@Nonnull VirtualFile audioFile) throws Exception;

	void registerFileTypes(FileTypeConsumer fileTypeConsumer);
}
