package consulo.audio;

import consulo.audio.icon.AudioIconGroup;
import consulo.audio.internal.PlaylistConfigurationFileEditorProvider;
import consulo.component.ComponentManager;
import consulo.configuration.editor.ConfigurationFileEditorManager;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import consulo.ui.image.Image;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.fileType.FileType;
import consulo.virtualFileSystem.fileType.INativeFileType;
import jakarta.annotation.Nonnull;

import java.util.Map;

/**
 * @author VISTALL
 * @since 2020-09-04
 */
public class AudioFileType implements FileType, INativeFileType {
    public static final AudioFileType INSTANCE = new AudioFileType();

    @Nonnull
    @Override
    public String getId() {
        return "AUDIO";
    }

    @Override
    public boolean isBinary() {
        return true;
    }

    @Nonnull
    @Override
    public LocalizeValue getDescription() {
        return LocalizeValue.localizeTODO("Audio files");
    }

    @Nonnull
    @Override
    public String getDefaultExtension() {
        return "wav";
    }

    @Nonnull
    @Override
    public Image getIcon() {
        return AudioIconGroup.audiofile();
    }

    @Override
    public boolean openFileInAssociatedApplication(ComponentManager project, @Nonnull VirtualFile virtualFile) {
        Project projectCast = (Project) project;

        ConfigurationFileEditorManager editorManager = projectCast.getApplication().getInstance(ConfigurationFileEditorManager.class);

        editorManager.open(projectCast, PlaylistConfigurationFileEditorProvider.class,
            Map.of(PlaylistConfigurationFileEditorProvider.AUDIO_FILE_URL, virtualFile.getUrl()));

        return true;
    }
}
