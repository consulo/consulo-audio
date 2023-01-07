package consulo.audio.impl.fileEditorProvider;

import consulo.application.Application;
import consulo.application.util.concurrent.AppExecutorUtil;
import consulo.audio.engine.AudioEngine;
import consulo.audio.engine.AudioPlayer;
import consulo.disposer.Disposable;
import consulo.util.concurrent.AsyncResult;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.VirtualFileManager;
import consulo.virtualFileSystem.util.VirtualFileUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * @author VISTALL
 * @since 17/12/2020
 */
public class AudioPlayerWrapper implements Disposable
{
	private final String myFileUrl;
	private final VirtualFile myFileByUrl;

	private AudioPlayer myPlayer;
	private Throwable myError;

	private AsyncResult<AudioPlayer> myProcessingResult;

	public AudioPlayerWrapper(String fileUrl)
	{
		myFileUrl = fileUrl;

		myFileByUrl = VirtualFileManager.getInstance().findFileByUrl(fileUrl);
	}

	@Nonnull
	public AsyncResult<AudioPlayer> getOrLoad()
	{
		if(myFileByUrl == null)
		{
			return AsyncResult.rejected();
		}

		if(myPlayer != null)
		{
			return AsyncResult.resolved(myPlayer);
		}

		if(myError != null)
		{
			return AsyncResult.<AudioPlayer>undefined().rejectWithThrowable(myError);
		}

		if(myProcessingResult != null)
		{
			return myProcessingResult;
		}

		myProcessingResult = AsyncResult.undefined();

		AudioEngine engine = AudioEngine.forFile(Application.get(), myFileByUrl);

		AppExecutorUtil.getAppExecutorService().execute(() -> {
			try
			{
				myProcessingResult.setDone(myPlayer = engine.createPlayer(myFileByUrl));
			}
			catch(Throwable e)
			{
				myProcessingResult.rejectWithThrowable(myError = e);
			}
		});

		return myProcessingResult;
	}

	@Nonnull
	public String getFilePath()
	{
		return VirtualFileUtil.urlToPath(myFileUrl);
	}

	@Nullable
	public VirtualFile getFile()
	{
		return myFileByUrl;
	}

	public boolean isLoaded()
	{
		return myPlayer != null;
	}

	@Nullable
	public AudioPlayer getPlayer()
	{
		return myPlayer;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o)
		{
			return true;
		}
		if(o == null || getClass() != o.getClass())
		{
			return false;
		}
		AudioPlayerWrapper that = (AudioPlayerWrapper) o;
		return Objects.equals(myFileUrl, that.myFileUrl);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(myFileUrl);
	}

	@Override
	public void dispose()
	{
		if(myPlayer != null)
		{
			myPlayer.dispose();
		}
	}
}
