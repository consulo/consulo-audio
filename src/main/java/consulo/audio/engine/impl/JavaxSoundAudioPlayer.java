package consulo.audio.engine.impl;

import consulo.audio.engine.AudioPlayer;

import javax.annotation.Nonnull;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

/**
 * @author VISTALL
 * @since 2020-11-12
 */
public class JavaxSoundAudioPlayer implements AudioPlayer
{
	private static enum State
	{
		EMPTY,
		PLAYING,
		PAUSE
	}

	private final Clip myClip;

	private State myState = State.EMPTY;

	private long myMicroSecondLenght;

	private long myPosition;

	public JavaxSoundAudioPlayer(@Nonnull Clip clip)
	{
		myClip = clip;

		myMicroSecondLenght = myClip.getMicrosecondLength();

		myClip.addLineListener(new LineListener()
		{
			@Override
			public void update(LineEvent lineEvent)
			{
				if(lineEvent.getType() == LineEvent.Type.START)
				{
					myState = State.PLAYING;
				}

				if(lineEvent.getType() == LineEvent.Type.STOP)
				{
					myState = State.PAUSE;
				}
			}
		});
	}

	@Override
	public void playOrPause()
	{
		switch(myState)
		{
			case EMPTY:
				myState = State.PLAYING;
				myClip.start();
				break;
			case PAUSE:
				myState = State.PLAYING;
				myClip.setMicrosecondPosition(myPosition);
				myClip.start();
				break;
			case PLAYING:
				myState = State.PAUSE;
				myClip.stop();
				myPosition = myClip.getFramePosition();
				break;
		}
	}

	@Override
	public void stop()
	{
		myPosition = 0;
		myClip.stop();
		myClip.setMicrosecondPosition(myPosition);
	}

	@Override
	public boolean isPlaying()
	{
		return myState == State.PLAYING;
	}

	@Override
	public long getLengthInSeconds()
	{
		return myClip.getMicrosecondLength() / 1000_000L;
	}

	@Override
	public void dispose()
	{
		myClip.close();
	}
}
