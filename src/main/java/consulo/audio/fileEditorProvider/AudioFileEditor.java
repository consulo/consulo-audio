package consulo.audio.fileEditorProvider;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ex.CustomComponentAction;
import com.intellij.openapi.actionSystem.ex.DefaultCustomComponentAction;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorLocation;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.FileEditorStateLevel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LoadingDecorator;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.concurrency.AppExecutorUtil;
import consulo.audio.engine.AudioEngine;
import consulo.audio.engine.AudioPlayer;
import consulo.audio.fileEditorProvider.actions.PlayOrPauseAction;
import consulo.audio.fileEditorProvider.actions.StopAction;
import consulo.disposer.Disposer;
import consulo.logging.Logger;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.ui.UIAccess;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.util.concurrent.AsyncResult;
import consulo.util.dataholder.UserDataHolderBase;
import kava.beans.PropertyChangeListener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;

/**
 * @author VISTALL
 * @since 2020-11-12
 */
public class AudioFileEditor extends UserDataHolderBase implements FileEditor
{
	private static final Logger LOG = Logger.getInstance(AudioFileEditor.class);

	private final Project myProject;
	private final VirtualFile myVirtualFile;

	private LoadingDecorator myLoadingDecorator;

	private JPanel myRoot;

	@Nullable
	private AudioPlayer myAudioPlayer;

	public AudioFileEditor(Project project, VirtualFile virtualFile)
	{
		myProject = project;
		myVirtualFile = virtualFile;
	}

	@Nonnull
	@Override
	@RequiredUIAccess
	public JComponent getComponent()
	{
		if(myRoot != null)
		{
			return myRoot;
		}

		myRoot = new JPanel(new BorderLayout());

		myLoadingDecorator = new LoadingDecorator(myRoot, this, 100);
		myLoadingDecorator.startLoading(false);

		AudioEngine engine = AudioEngine.forFile(Application.get(), myVirtualFile);

		UIAccess uiAccess = UIAccess.current();
		AsyncResult<AudioPlayer> result = loadPlayer(engine, myVirtualFile);
		result.doWhenDone(audioPlayer -> {
			myAudioPlayer = audioPlayer;

			uiAccess.give(() -> initPlayerUI(audioPlayer));
		});

		result.doWhenRejectedWithThrowable(e -> {
			uiAccess.give(() -> {
				LOG.warn(e);

				JBLabel label = new JBLabel(e.getMessage(), PlatformIconGroup.generalBalloonError(), SwingConstants.CENTER);
				myRoot.add(label, BorderLayout.CENTER);

				myLoadingDecorator.stopLoading();
			});
		});
		return myLoadingDecorator.getComponent();
	}

	private void initPlayerUI(@Nonnull AudioPlayer audioPlayer)
	{
		ActionGroup.Builder builder = ActionGroup.newImmutableBuilder();

		builder.add(new PlayOrPauseAction());
		builder.add(new StopAction());
		builder.add(new DefaultCustomComponentAction(JBLabel::new)
		{
			@RequiredUIAccess
			@Override
			public void update(@Nonnull AnActionEvent e)
			{
				JBLabel component = (JBLabel) e.getPresentation().getClientProperty(CustomComponentAction.COMPONENT_KEY);

				AudioPlayer player = e.getData(AudioEditorKeys.AUDIO_PLAYER);
				if(player == null || component == null)
				{
					return;
				}

				component.setText(player.getPosition() + " of " + player.getMaxPosition() + " ms");
			}
		});

		ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("audio-editor", builder.build(), true);
		toolbar.setTargetComponent(myRoot);

		DataManager.registerDataProvider(myRoot, key -> {
			if(key == AudioEditorKeys.AUDIO_PLAYER)
			{
				return audioPlayer;
			}

			return null;
		});

		JComponent component = toolbar.getComponent();

		myRoot.add(component, BorderLayout.CENTER);

		myLoadingDecorator.stopLoading();
	}

	@Nonnull
	private AsyncResult<AudioPlayer> loadPlayer(@Nonnull AudioEngine audioEngine, @Nonnull VirtualFile file)
	{
		AsyncResult<AudioPlayer> result = AsyncResult.undefined();

		AppExecutorUtil.getAppExecutorService().execute(() -> {
			try
			{
				result.setDone(audioEngine.createPlayer(file));
			}
			catch(Throwable e)
			{
				result.rejectWithThrowable(e);
			}
		});

		return result;
	}

	@Nullable
	@Override
	public JComponent getPreferredFocusedComponent()
	{
		return myRoot;
	}

	@Nonnull
	@Override
	public String getName()
	{
		return "audio";
	}

	@Nonnull
	@Override
	public FileEditorState getState(@Nonnull FileEditorStateLevel fileEditorStateLevel)
	{
		return FileEditorState.INSTANCE;
	}

	@Override
	public void setState(@Nonnull FileEditorState fileEditorState)
	{

	}

	@Override
	public boolean isModified()
	{
		return false;
	}

	@Override
	public boolean isValid()
	{
		return true;
	}

	@Override
	public void selectNotify()
	{

	}

	@Override
	public void deselectNotify()
	{

	}

	@Override
	public void addPropertyChangeListener(@Nonnull PropertyChangeListener propertyChangeListener)
	{

	}

	@Override
	public void removePropertyChangeListener(@Nonnull PropertyChangeListener propertyChangeListener)
	{

	}

	@Nullable
	@Override
	public FileEditorLocation getCurrentLocation()
	{
		return null;
	}

	@Override
	public void dispose()
	{
		if(myAudioPlayer != null)
		{
			Disposer.dispose(myAudioPlayer);
		}
	}
}
