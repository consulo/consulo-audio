package consulo.audio.stub.impl;

import consulo.annotation.component.ExtensionImpl;
import consulo.audio.AudioFileType;
import consulo.audio.engine.AudioEngine;
import consulo.audio.engine.AudioPlayer;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.fileType.FileTypeConsumer;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 07/01/2023
 */
@ExtensionImpl(id = "stub")
public class StubAudioEngine implements AudioEngine
{
	@Override
	public boolean isAvailable(@Nonnull VirtualFile virtualFile)
	{
		return true;
	}

	@Nonnull
	@Override
	public AudioPlayer createPlayer(@Nonnull VirtualFile audioFile) throws Exception
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void registerFileTypes(FileTypeConsumer fileTypeConsumer)
	{
		fileTypeConsumer.consume(AudioFileType.INSTANCE, "wav");
		fileTypeConsumer.consume(AudioFileType.INSTANCE, "mp3");
		fileTypeConsumer.consume(AudioFileType.INSTANCE, "ogg");
	}
}
