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

		AudioInputStream encodedStream = AudioSystem.getAudioInputStream(file);

		Clip clip;
		try
		{
			AudioFormat encodedFormat = encodedStream.getFormat();

			int bitDepth = 16;

			AudioFormat decodedFormat = new javax.sound.sampled.AudioFormat(
					javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED,
					encodedFormat.getSampleRate(),
					bitDepth,
					encodedFormat.getChannels(),
					encodedFormat.getChannels() * (bitDepth / 8), // 2*8 = 16-bits per sample per channel
					44100,
					encodedFormat.isBigEndian());


			clip = AudioSystem.getClip();

			if(AudioSystem.isConversionSupported(decodedFormat, encodedFormat))
			{
				AudioInputStream decodedStream = AudioSystem.getAudioInputStream(decodedFormat, encodedStream);

				clip.open(decodedStream);
			}
			else
			{
				clip.open(encodedStream);
			}
		}
		catch(IOException | LineUnavailableException e)
		{
			StreamUtil.closeStream(encodedStream);
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
