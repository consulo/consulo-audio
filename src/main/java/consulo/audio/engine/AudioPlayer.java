package consulo.audio.engine;

import consulo.disposer.Disposable;

/**
 * @author VISTALL
 * @since 2020-11-12
 */
public interface AudioPlayer extends Disposable
{
	void playOrPause();

	void stop();

	boolean isPlaying();

	// in ms
	long getPosition();

	// in ms
	long getMaxPosition();
}
