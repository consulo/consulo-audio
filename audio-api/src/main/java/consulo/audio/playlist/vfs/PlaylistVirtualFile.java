package consulo.audio.playlist.vfs;

import consulo.language.file.light.LightVirtualFile;
import consulo.virtualFileSystem.VirtualFileSystem;
import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 17/12/2020
 */
public class PlaylistVirtualFile extends LightVirtualFile {
    @Nonnull
    private final VirtualFileSystem myVirtualFileSystem;

    public PlaylistVirtualFile(@Nonnull VirtualFileSystem virtualFileSystem) {
        super("Playlist");
        myVirtualFileSystem = virtualFileSystem;
    }

    @Nonnull
    @Override
    public VirtualFileSystem getFileSystem() {
        return myVirtualFileSystem;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PlaylistVirtualFile;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
