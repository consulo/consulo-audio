package consulo.audio;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.INativeFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import consulo.audio.fileEditorProvider.AudioPlaylistFileEditor;
import consulo.audio.icon.AudioIconGroup;
import consulo.audio.playlist.vfs.PlaylistVirtualFile;
import consulo.audio.playlist.vfs.PlaylistVirtualFileSystem;
import consulo.localize.LocalizeValue;
import consulo.ui.image.Image;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2020-09-04
 */
public class AudioFileType implements FileType, INativeFileType
{
	public static final AudioFileType INSTANCE = new AudioFileType();

	@Nonnull
	@Override
	public String getId()
	{
		return "AUDIO";
	}

	@Override
	public boolean isBinary()
	{
		return true;
	}

	@Nonnull
	@Override
	public LocalizeValue getDescription()
	{
		return LocalizeValue.localizeTODO("Audio files");
	}

	@Nonnull
	@Override
	public String getDefaultExtension()
	{
		return "wav";
	}

	@Nonnull
	@Override
	public Image getIcon()
	{
		return AudioIconGroup.audioFile();
	}

	@Override
	public boolean openFileInAssociatedApplication(Project project, @Nonnull VirtualFile virtualFile)
	{
		FileEditor[] fileEditors = FileEditorManager.getInstance(project).openFile(new PlaylistVirtualFile(PlaylistVirtualFileSystem.getInstance()), true);
		for(FileEditor fileEditor : fileEditors)
		{
			if(fileEditor instanceof AudioPlaylistFileEditor)
			{
				((AudioPlaylistFileEditor) fileEditor).addFile(virtualFile);
				break;
			}
		}
		return true;
	}
}
