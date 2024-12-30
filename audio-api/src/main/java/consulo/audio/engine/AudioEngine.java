package consulo.audio.engine;

import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ExtensionAPI;
import consulo.application.Application;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.fileType.FileTypeConsumer;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2020-11-12
 */
@ExtensionAPI(ComponentScope.APPLICATION)
public interface AudioEngine
{
	static AudioEngine forFile(@Nonnull Application application, @Nonnull VirtualFile file)
	{
		for(AudioEngine engine : application.getExtensionList(AudioEngine.class))
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
	AudioPlayer createPlayer(@Nonnull VirtualFile audioFile) throws Exception;

	void registerFileTypes(FileTypeConsumer fileTypeConsumer);
}
