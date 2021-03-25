package consulo.audio.engine;

import java.util.EventListener;

/**
 * @author VISTALL
 * @since 25/03/2021
 */
public interface AudioPlayerListener extends EventListener
{
	void onPlay();

	void onStop();
}
