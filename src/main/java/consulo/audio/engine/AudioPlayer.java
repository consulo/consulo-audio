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

	/**
	 * @return from 0 to 100
	 */
	float getVolume();

	void setVolume(float volume);

	void addListener(AudioPlayerListener listener, Disposable parent);
}
