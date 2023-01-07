package consulo.audio.playlist.vfs;

import consulo.annotation.component.ExtensionImpl;
import consulo.virtualFileSystem.BaseVirtualFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.VirtualFileManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 17/12/2020
 */
@ExtensionImpl
public class PlaylistVirtualFileSystem extends BaseVirtualFileSystem
{
	@Nonnull
	public static PlaylistVirtualFileSystem getInstance()
	{
		return (PlaylistVirtualFileSystem) VirtualFileManager.getInstance().getFileSystem(PROTOCOL);
	}

	public static final String PROTOCOL = "audiolist";

	@Nonnull
	@Override
	public String getProtocol()
	{
		return PROTOCOL;
	}

	@Nullable
	@Override
	public VirtualFile findFileByPath(@Nonnull String path)
	{
		return new PlaylistVirtualFile(this);
	}

	@Override
	public void refresh(boolean b)
	{

	}

	@Nullable
	@Override
	public VirtualFile refreshAndFindFileByPath(@Nonnull String path)
	{
		return findFileByPath(path);
	}
}
