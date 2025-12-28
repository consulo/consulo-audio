package consulo.audio.engine.javax;

import consulo.audio.engine.AudioPlayer;
import consulo.audio.engine.AudioPlayerListener;
import consulo.disposer.Disposable;
import consulo.proxy.EventDispatcher;
import jakarta.annotation.Nonnull;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;

/**
 * @author VISTALL
 * @since 2020-11-12
 */
public class JavaxSoundAudioPlayer implements AudioPlayer {
    private static enum State {
        EMPTY,
        PLAYING,
        PAUSE
    }

    private final Clip myClip;

    private State myState = State.EMPTY;

    private long myPosition;

    private float myLastVolume = 0f;

    private final EventDispatcher<AudioPlayerListener> myListeners = EventDispatcher.create(AudioPlayerListener.class);

    public JavaxSoundAudioPlayer(@Nonnull Clip clip) {
        myClip = clip;

        myClip.addLineListener(lineEvent -> {
            if (lineEvent.getType() == LineEvent.Type.START) {
                myListeners.getMulticaster().onPlay();
            }
            else if (lineEvent.getType() == LineEvent.Type.STOP) {
                myListeners.getMulticaster().onStop();
            }
        });

        setVolume(50f);
    }

    @Override
    public void addListener(AudioPlayerListener listener, Disposable parent) {
        myListeners.addListener(listener, parent);
    }

    @Override
    public float getVolume() {
        return myLastVolume;
    }

    @Override
    public void setVolume(float volume) {
        FloatControl gainControl = (FloatControl) myClip.getControl(FloatControl.Type.MASTER_GAIN);

        gainControl.setValue(-30);

        float min = gainControl.getMinimum();
        float max = gainControl.getMaximum();

        int positiveValue = (int) (((max - min) / 100f) * volume);

        positiveValue += min;

        myLastVolume = positiveValue;

        gainControl.setValue(positiveValue);
    }

    @Override
    public void playOrPause() {
        switch (myState) {
            case EMPTY:
                if (myClip.isActive()) {
                    myClip.stop();
                }
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
    public void stop() {
        myPosition = 0;
        myClip.stop();
        myClip.setMicrosecondPosition(myPosition);
    }

    @Override
    public boolean isPlaying() {
        return myState == State.PLAYING;
    }

    @Override
    public long getPosition() {
        return myClip.getMicrosecondPosition() / 1000L;
    }

    @Override
    public long getMaxPosition() {
        return myClip.getMicrosecondLength() / 1000L;
    }

    @Override
    public void dispose() {
        myClip.close();
    }
}
