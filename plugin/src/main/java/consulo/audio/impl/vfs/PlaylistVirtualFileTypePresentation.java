package consulo.audio.impl.vfs;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.presentation.TypePresentationProvider;
import consulo.audio.icon.AudioIconGroup;
import consulo.audio.playlist.vfs.PlaylistVirtualFile;
import consulo.ui.image.Image;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

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
