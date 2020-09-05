package consulo.audio.provider;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import consulo.extensions.StrictExtensionPointName;

/**
 * @author VISTALL
 * @since 2020-09-05
 */
public abstract class AudioFileTypeProvider extends FileTypeFactory
{
	public static final StrictExtensionPointName<Application, AudioFileTypeProvider> EP_NAME = StrictExtensionPointName.forApplication("consulo.audio.fileTypeProvider");
}
