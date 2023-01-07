package consulo.audio.impl.fileEditorProvider;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.dumb.DumbAware;
import consulo.audio.playlist.vfs.PlaylistVirtualFile;
import consulo.fileEditor.FileEditor;
import consulo.fileEditor.FileEditorPolicy;
import consulo.fileEditor.FileEditorProvider;
import consulo.project.Project;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.virtualFileSystem.VirtualFile;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2020-11-12
 */
@ExtensionImpl
public class AudioPlaylistFileEditorProvider implements FileEditorProvider, DumbAware
{
	@Override
	public boolean accept(@Nonnull Project project, @Nonnull VirtualFile virtualFile)
	{
		return virtualFile instanceof PlaylistVirtualFile;
	}

	@RequiredUIAccess
	@Nonnull
	@Override
	public FileEditor createEditor(@Nonnull Project project, @Nonnull VirtualFile virtualFile)
	{
		return new AudioPlaylistFileEditorImpl(project, virtualFile);
	}

	@Nonnull
	@Override
	public String getEditorTypeId()
	{
		return "audiolist";
	}

	@Nonnull
	@Override
	public FileEditorPolicy getPolicy()
	{
		return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
	}
}
