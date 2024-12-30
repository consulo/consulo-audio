package consulo.audio.playlist;

import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceAPI;
import consulo.annotation.component.ServiceImpl;
import consulo.component.persist.PersistentStateComponent;
import consulo.component.persist.State;
import consulo.component.persist.Storage;
import consulo.component.persist.StoragePathMacros;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import jakarta.inject.Singleton;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

/**
 * @author VISTALL
 * @since 17/12/2020
 */
@Singleton
@State(name = "AudioPlaylistStore", storages = @Storage(StoragePathMacros.WORKSPACE_FILE))
@ServiceAPI(ComponentScope.PROJECT)
@ServiceImpl
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
