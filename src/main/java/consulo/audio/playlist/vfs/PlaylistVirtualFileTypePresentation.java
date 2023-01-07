package consulo.audio.playlist.vfs;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.presentation.TypePresentationProvider;
import consulo.audio.icon.AudioIconGroup;
import consulo.ui.image.Image;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 12-Jul-22
 */
@ExtensionImpl
public class PlaylistVirtualFileTypePresentation extends TypePresentationProvider<PlaylistVirtualFile>
{
	@Nonnull
	@Override
	public Class<PlaylistVirtualFile> getItemClass()
	{
		return PlaylistVirtualFile.class;
	}

	@Nullable
	@Override
	public Image getIcon(PlaylistVirtualFile playlistVirtualFile)
	{
		return AudioIconGroup.audiofile();
	}
}
