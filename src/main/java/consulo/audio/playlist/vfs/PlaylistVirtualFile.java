package consulo.audio.playlist.vfs;

import com.intellij.ide.presentation.Presentation;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.testFramework.LightVirtualFile;
import consulo.audio.icon.AudioIconGroup;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 17/12/2020
 */
@Presentation(iconGroupId = AudioIconGroup.ID, imageId = "note")
public class PlaylistVirtualFile extends LightVirtualFile
{
	@Nonnull
	private final VirtualFileSystem myVirtualFileSystem;

	public PlaylistVirtualFile(@Nonnull VirtualFileSystem virtualFileSystem)
	{
		super("Playlist");
		myVirtualFileSystem = virtualFileSystem;
	}

	@Nonnull
	@Override
	public VirtualFileSystem getFileSystem()
	{
		return myVirtualFileSystem;
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof PlaylistVirtualFile;
	}
}
