package consulo.audio.fileEditorProvider;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import consulo.audio.AudioFileType;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2020-11-12
 */
public class AudioFileEditorProvider implements FileEditorProvider
{
	@Override
	public boolean accept(@Nonnull Project project, @Nonnull VirtualFile virtualFile)
	{
		return virtualFile.getFileType() == AudioFileType.INSTANCE;
	}

	@Nonnull
	@Override
	public FileEditor createEditor(@Nonnull Project project, @Nonnull VirtualFile virtualFile)
	{
		return new AudioFileEditor(project, virtualFile);
	}

	@Nonnull
	@Override
	public String getEditorTypeId()
	{
		return "audio";
	}

	@Nonnull
	@Override
	public FileEditorPolicy getPolicy()
	{
		return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
	}
}
