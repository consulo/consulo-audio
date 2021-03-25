package consulo.audio.fileEditorProvider;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.ui.LoadingDecorator;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.panels.HorizontalLayout;
import com.intellij.ui.table.TableView;
import com.intellij.util.concurrency.AppExecutorUtil;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.ListTableModel;
import consulo.audio.engine.AudioPlayer;
import consulo.audio.engine.AudioPlayerListener;
import consulo.audio.fileEditorProvider.actions.PlayOrPauseAction;
import consulo.audio.fileEditorProvider.actions.StopAction;
import consulo.audio.playlist.AudioPlaylistStore;
import consulo.disposer.Disposable;
import consulo.ui.annotation.RequiredUIAccess;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author VISTALL
 * @since 17/12/2020
 */
public class AudioPlayerUI implements Disposable
{
	public static final float STEPPING = 10f;

	@Nonnull
	private final AudioPlaylistStore myPlaylistStore;

	private LoadingDecorator myLoadingDecorator;

	private AudioPlayerWrapper myAudioPlayerWrapper;

	private JPanel myRootPanel;

	private JComponent myPlayerUI;

	private JSlider myVolumeSlider;

	private Future<?> myUIRepainter = CompletableFuture.completedFuture(null);
	private final JBLabel myPositionInfo;
	private final JSlider myPositionSlider;

	public AudioPlayerUI(AudioPlaylistStore playlistStore)
	{
		myPlaylistStore = playlistStore;
		ActionGroup.Builder builder = ActionGroup.newImmutableBuilder();

		builder.add(new PlayOrPauseAction());
		builder.add(new StopAction());

		myVolumeSlider = new JSlider(0, (int) (100 * STEPPING), (int) (playlistStore.getVolume() * 10f));
		myVolumeSlider.addChangeListener(changeEvent ->
		{
			float value = myVolumeSlider.getValue() / STEPPING;

			myPlaylistStore.setVolume(value);

			if(myAudioPlayerWrapper != null)
			{
				AudioPlayer player = myAudioPlayerWrapper.getPlayer();
				if(player != null)
				{
					player.setVolume(value);
				}
			}
		});

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBorder(JBUI.Borders.empty(5));

		JPanel topRight = new JPanel(new HorizontalLayout(5));
		topRight.add(new JBLabel("Volume:"));
		topRight.add(myVolumeSlider);

		topPanel.add(topRight, BorderLayout.EAST);

		myRootPanel = new JPanel(new BorderLayout());
		myRootPanel.add(topPanel, BorderLayout.NORTH);

		ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("audio-editor", builder.build(), true);
		toolbar.setTargetComponent(myRootPanel);

		DataManager.registerDataProvider(myRootPanel, key -> {
			if(key == AudioEditorKeys.AUDIO_PLAYER)
			{
				return myAudioPlayerWrapper == null ? null : myAudioPlayerWrapper.getPlayer();
			}

			return null;
		});

		myPlayerUI = toolbar.getComponent();

		myLoadingDecorator = new LoadingDecorator(myRootPanel, this, 100);

		myPositionSlider = new JSlider(0, 100, 0);
		myPositionInfo = new JBLabel("", SwingConstants.CENTER);

		JPanel centerPanel = new JPanel(new VerticalFlowLayout(VerticalFlowLayout.MIDDLE, true, false));
		centerPanel.add(myPositionSlider);
		centerPanel.add(myPositionInfo);
		centerPanel.add(myPlayerUI);

		myRootPanel.add(centerPanel, BorderLayout.CENTER);

		ColumnInfo.StringColumn name = new ColumnInfo.StringColumn("Name");
		ColumnInfo.StringColumn value = new ColumnInfo.StringColumn("Value");

		Map<String, String> properties = new TreeMap<>();

		TableView<Map.Entry<String, String>> propertiesTable = new TableView<>(new ListTableModel<>(new ColumnInfo[]{
				name,
				value
		}, new ArrayList<>(properties.entrySet())));

		myRootPanel.add(ScrollPaneFactory.createScrollPane(propertiesTable, true), BorderLayout.SOUTH);
	}

	private void update()
	{
		long currentPosition = 0;
		long maxPosition = 0;
		AudioPlayerWrapper audioPlayerWrapper = myAudioPlayerWrapper;
		if(audioPlayerWrapper != null)
		{
			AudioPlayer player = audioPlayerWrapper.getPlayer();
			if(player != null)
			{
				currentPosition = player.getPosition();
				maxPosition = player.getMaxPosition();
			}
		}

		int currentPerc = (int) ((currentPosition / (float) maxPosition) * 100f);

		myPositionSlider.setValue(currentPerc);
		
		myPositionInfo.setText(StringUtil.formatDuration(currentPosition) + " of " + StringUtil.formatDuration(maxPosition));
	}

	@RequiredUIAccess
	public void update(@Nonnull AudioPlayerWrapper wrapper)
	{
		AudioPlayerWrapper oldWrapper = myAudioPlayerWrapper;
		if(oldWrapper != null)
		{
			AudioPlayer player = oldWrapper.getPlayer();
			if(player != null)
			{
				player.stop();

				myUIRepainter.cancel(false);
			}
		}

		myAudioPlayerWrapper = wrapper;

		myLoadingDecorator.startLoading(false);

		wrapper.getOrLoad().doWhenProcessed(myLoadingDecorator::stopLoading).doWhenDone((player) -> {
			player.setVolume(myVolumeSlider.getValue() / STEPPING);

			update();

			player.addListener(new AudioPlayerListener()
			{
				@Override
				public void onPlay()
				{
					onStop();

					myUIRepainter = AppExecutorUtil.getAppScheduledExecutorService().scheduleWithFixedDelay(() -> update(), 100, 100, TimeUnit.MILLISECONDS);
				}

				@Override
				public void onStop()
				{
					myUIRepainter.cancel(false);

					update();
				}
			}, AudioPlayerUI.this);
		});
	}

	public JComponent getComponent()
	{
		return myLoadingDecorator.getComponent();
	}

	@Override
	public void dispose()
	{
		myUIRepainter.cancel(false);
	}
}
