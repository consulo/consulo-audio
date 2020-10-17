package consulo.audio;

import consulo.audio.icon.AudioIconGroup;
import consulo.ui.image.Image;
import consulo.ui.image.ImageEffects;

/**
 * @author VISTALL
 * @since 2020-09-04
 */
public interface AudioIcons
{
	Image AudioFileType = ImageEffects.layered(AudioIconGroup.templateType(), AudioIconGroup.note());
}
