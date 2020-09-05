package consulo.audio.provider.impl;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import consulo.audio.AudioFileType;
import consulo.audio.provider.AudioFileTypeProvider;

import javax.annotation.Nonnull;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;

/**
 * @author VISTALL
 * @since 2020-09-05
 */
public class DesktopFileTypeProvider extends AudioFileTypeProvider
{
	@Override
	public void createFileTypes(@Nonnull FileTypeConsumer consumer)
	{
		AudioFileFormat.Type[] audioFileTypes = AudioSystem.getAudioFileTypes();

		for(AudioFileFormat.Type type : audioFileTypes)
		{
			String extension = type.getExtension();

			consumer.consume(AudioFileType.INSTANCE, extension);
		}
	}
}
