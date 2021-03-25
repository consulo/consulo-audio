package consulo.audio.playlist;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import jakarta.inject.Singleton;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author VISTALL
 * @since 17/12/2020
 */
@Singleton
@State(name = "AudioPlaylistStore", storages = @Storage(StoragePathMacros.WORKSPACE_FILE))
public class AudioPlaylistStore implements PersistentStateComponent<AudioPlaylistStore.State>
{
	public static class State
	{
		public Deque<String> myHistory = new ArrayDeque<>();

		public float myVolume = 50f;
	}

	@Nonnull
	public static AudioPlaylistStore getInstance(@Nonnull Project project)
	{
		return project.getInstance(AudioPlaylistStore.class);
	}

	private final State myState = new State();

	@Nullable
	@Override
	public State getState()
	{
		return myState;
	}

	@Override
	public void loadState(State state)
	{
		myState.myHistory.clear();
		myState.myHistory.addAll(state.myHistory);
		myState.myVolume = state.myVolume;
	}

	public void addFile(VirtualFile file)
	{
		myState.myHistory.remove(file.getUrl());
		myState.myHistory.addFirst(file.getUrl());
	}

	@Nonnull
	public Collection<String> getFiles()
	{
		return myState.myHistory;
	}

	public void setVolume(float volume)
	{
		myState.myVolume = volume;
	}

	public float getVolume()
	{
		return myState.myVolume;
	}
}
