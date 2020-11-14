package consulo.audio.fileEditorProvider;

import consulo.audio.engine.AudioPlayer;
import consulo.util.dataholder.Key;

/**
 * @author VISTALL
 * @since 2020-11-12
 */
public interface AudioEditorKeys
{
	Key<AudioPlayer> AUDIO_PLAYER = Key.create("audio.player");
}
