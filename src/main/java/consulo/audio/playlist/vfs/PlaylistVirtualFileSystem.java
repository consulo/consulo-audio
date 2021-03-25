package consulo.audio.playlist.vfs;

import com.intellij.openapi.vfs.DeprecatedVirtualFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 17/12/2020
 */
public class PlaylistVirtualFileSystem extends DeprecatedVirtualFileSystem
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
