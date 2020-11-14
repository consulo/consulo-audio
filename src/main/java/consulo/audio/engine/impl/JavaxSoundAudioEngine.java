package consulo.audio.engine.impl;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.util.io.StreamUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import consulo.audio.AudioFileType;
import consulo.audio.engine.AudioEngine;
import consulo.audio.engine.AudioPlayer;

import javax.annotation.Nonnull;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * @author VISTALL
 * @since 2020-11-12
 */
public class JavaxSoundAudioEngine implements AudioEngine
{
	@Override
	public boolean isAvailable(@Nonnull VirtualFile virtualFile)
	{
		return true;
	}

	@Nonnull
	@Override
	public AudioPlayer create(@Nonnull VirtualFile audioFile) throws Exception
	{
		File file = VfsUtil.virtualToIoFile(audioFile);

		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);

		Clip clip;
		try
		{
			clip = AudioSystem.getClip();

			clip.open(audioInputStream);
		}
		catch(IOException | LineUnavailableException e)
		{
			StreamUtil.closeStream(audioInputStream);
			throw e;
		}

		return new JavaxSoundAudioPlayer(clip);
	}

	@Override
	public void registerFileTypes(FileTypeConsumer fileTypeConsumer)
	{
		AudioFileFormat.Type[] audioFileTypes = AudioSystem.getAudioFileTypes();

		for(AudioFileFormat.Type type : audioFileTypes)
		{
			String extension = type.getExtension();

			fileTypeConsumer.consume(AudioFileType.INSTANCE, extension);
		}
	}
}
