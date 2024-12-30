package consulo.audio;

import consulo.audio.fileEditorProvider.AudioPlaylistFileEditor;
import consulo.audio.icon.AudioIconGroup;
import consulo.audio.playlist.vfs.PlaylistVirtualFile;
import consulo.audio.playlist.vfs.PlaylistVirtualFileSystem;
import consulo.component.ComponentManager;
import consulo.fileEditor.FileEditor;
import consulo.fileEditor.FileEditorManager;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import consulo.ui.image.Image;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.fileType.FileType;
import consulo.virtualFileSystem.fileType.INativeFileType;

import jakarta.annotation.Nonnull;

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
		return AudioIconGroup.audiofile();
	}

	@Override
	public boolean openFileInAssociatedApplication(ComponentManager project, @Nonnull VirtualFile virtualFile)
	{
		FileEditor[] fileEditors = FileEditorManager.getInstance((Project) project).openFile(new PlaylistVirtualFile(PlaylistVirtualFileSystem.getInstance()), true);
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
