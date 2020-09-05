package consulo.audio;

import com.intellij.openapi.fileTypes.FileType;
import consulo.ui.image.Image;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2020-09-04
 */
public class AudioFileType implements FileType
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
	public String getDescription()
	{
		return "Audio files";
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
		return AudioIcons.AudioFileType;
	}
}
