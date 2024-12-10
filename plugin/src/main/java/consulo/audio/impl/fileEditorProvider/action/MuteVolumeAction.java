package consulo.audio.impl.fileEditorProvider.action;

import consulo.audio.icon.AudioIconGroup;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.ex.action.AnActionEvent;
import consulo.ui.ex.action.DumbAwareAction;
import consulo.ui.image.Image;
import jakarta.annotation.Nonnull;

import javax.swing.*;

/**
 * @author VISTALL
 * @since 2024-12-10
 */
public class MuteVolumeAction extends DumbAwareAction {
    private final JSlider myVolumeSlider;

    private int myLastValue;

    public MuteVolumeAction(JSlider volumeSlider) {
        super("Mute");
        myVolumeSlider = volumeSlider;
    }

    @RequiredUIAccess
    @Override
    public void actionPerformed(@Nonnull AnActionEvent anActionEvent) {
        int value = myVolumeSlider.getValue();
        if (value == 0) {
            myVolumeSlider.setValue(myLastValue);
        } else {
            myLastValue = myVolumeSlider.getValue();

            myVolumeSlider.setValue(0);
        }
    }

    @RequiredUIAccess
    @Override
    public void update(@Nonnull AnActionEvent e) {
        int value = myVolumeSlider.getValue();

        double half = myVolumeSlider.getMaximum() / 2.;

        Image image;
        if (value == 0) {
            image = AudioIconGroup.volumemute();
        } else if (value < half) {
            image = AudioIconGroup.volumemin();
        } else {
            image = AudioIconGroup.volumemax();
        }

        e.getPresentation().setIcon(image);
    }
}
