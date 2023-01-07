package consulo.audio.fileEditorProvider;

import consulo.fileEditor.FileEditor;
import consulo.virtualFileSystem.VirtualFile;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 07/01/2023
 */
public interface AudioPlaylistFileEditor extends FileEditor
{
	void addFile(@Nonnull VirtualFile file);
}
