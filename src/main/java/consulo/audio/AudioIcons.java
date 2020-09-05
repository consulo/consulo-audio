package consulo.audio;

import com.intellij.openapi.util.IconLoader;
import consulo.ui.image.Image;
import consulo.ui.image.ImageEffects;

/**
 * @author VISTALL
 * @since 2020-09-04
 */
public interface AudioIcons
{
	Image TemplateFile = IconLoader.getIcon("/icons/any_type.png");
	Image Note = IconLoader.getIcon("/icons/note.svg");

	Image AudioFileType = ImageEffects.layered(TemplateFile, Note);
}
