package consulo.audio.impl.fileEditorProvider;

import consulo.annotation.component.ExtensionImpl;
import consulo.audio.icon.AudioIconGroup;
import consulo.audio.internal.PlaylistConfigurationFileEditorProvider;
import consulo.configuration.editor.ConfigurationFileEditor;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import consulo.ui.image.Image;
import consulo.virtualFileSystem.VirtualFile;
import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2025-01-25
 */
@ExtensionImpl
public class PlaylistConfigurationFileEditorProviderImpl implements PlaylistConfigurationFileEditorProvider {
    @Nonnull
    @Override
    public String getId() {
        return "playlist";
    }

    @Nonnull
    @Override
    public Image getIcon() {
        return AudioIconGroup.audiofile();
    }

    @Nonnull
    @Override
    public LocalizeValue getName() {
        return LocalizeValue.localizeTODO("Playlist");
    }

    @Nonnull
    @Override
    public ConfigurationFileEditor createEditor(@Nonnull Project project, @Nonnull VirtualFile virtualFile) {
        return new AudioPlaylistFileEditorImpl(project, virtualFile);
    }
}
