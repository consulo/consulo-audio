package consulo.audio.impl.fileEditorProvider.action;

import consulo.audio.engine.AudioPlayer;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.ex.action.AnActionEvent;
import consulo.ui.ex.action.DumbAwareAction;
import consulo.ui.ex.action.Presentation;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2020-11-12
 */
public class StopAction extends DumbAwareAction
{
	public StopAction()
	{
		super("Stop", null, PlatformIconGroup.actionsSuspend());
	}

	@RequiredUIAccess
	@Override
	public void actionPerformed(@Nonnull AnActionEvent e)
	{
		AudioPlayer player = e.getData(AudioPlayer.KEY);

		if(player != null)
		{
			player.stop();
		}
	}

	@RequiredUIAccess
	@Override
	public void update(@Nonnull AnActionEvent e)
	{
		AudioPlayer player = e.getData(AudioPlayer.KEY);
		Presentation presentation = e.getPresentation();

		presentation.setEnabled(player != null);
	}
}
