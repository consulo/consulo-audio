package consulo.audio.impl.fileEditorProvider;

import consulo.audio.fileEditorProvider.AudioPlaylistFileEditor;
import consulo.audio.internal.PlaylistConfigurationFileEditorProvider;
import consulo.audio.playlist.AudioPlaylistStore;
import consulo.configuration.editor.ConfigurationFileEditor;
import consulo.disposer.Disposer;
import consulo.logging.Logger;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.project.Project;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.ex.JBColor;
import consulo.ui.ex.SimpleTextAttributes;
import consulo.ui.ex.awt.*;
import consulo.ui.image.Image;
import consulo.util.io.FileUtil;
import consulo.util.io.PathUtil;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.VirtualFileManager;
import consulo.virtualFileSystem.util.VirtualFileUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author VISTALL
 * @since 2020-11-12
 */
public class AudioPlaylistFileEditorImpl extends ConfigurationFileEditor implements AudioPlaylistFileEditor {
    private static final Logger LOG = Logger.getInstance(AudioPlaylistFileEditorImpl.class);

    private JPanel myRoot;

    private Map<String, AudioPlayerWrapper> myLoadedFiles = new ConcurrentHashMap<>();

    private CollectionListModel<AudioPlayerWrapper> myModel;

    private AudioPlayerUI myAudioPlayerUI;
    private JBList<AudioPlayerWrapper> myList;

    public AudioPlaylistFileEditorImpl(Project project, VirtualFile virtualFile) {
        super(project, virtualFile);
    }

    @Override
    public void onUpdateRequestParams(@Nonnull Map<String, String> params) {
        String fileUrl = params.get(PlaylistConfigurationFileEditorProvider.AUDIO_FILE_URL);
        if (fileUrl != null) {
            Optional<AudioPlayerWrapper> opt = myModel
                .getItems()
                .stream()
                .filter(it -> Objects.equals(it.getFileUrl(), fileUrl))
                .findFirst();

            opt.ifPresentOrElse(it -> {
                myProject.getUIAccess().give(() -> myList.setSelectedValue(it, true));
            }, () -> {
                VirtualFile fileByUrl = VirtualFileManager.getInstance().findFileByUrl(fileUrl);
                if (fileByUrl != null) {
                    myProject.getUIAccess().give(() -> addFile(fileByUrl));
                }
            });
        }
    }

    @Override
    @Nonnull
    @RequiredUIAccess
    public JComponent getComponent() {
        if (myRoot != null) {
            return myRoot;
        }

        myRoot = new JPanel(new BorderLayout());

        OnePixelSplitter splitter = new OnePixelSplitter("AudioPlaylistFileEditor", 0.7f);
        myRoot.add(splitter, BorderLayout.CENTER);

        myModel = new CollectionListModel<>();
        myList = new JBList<>(myModel);

        AudioPlaylistStore playlistStore = AudioPlaylistStore.getInstance(myProject);

        Collection<String> files = playlistStore.getFiles();
        for (String fileUrl : files) {
            AudioPlayerWrapper wrapper = new AudioPlayerWrapper(fileUrl);
            myLoadedFiles.put(fileUrl, wrapper);
            myModel.add(wrapper);
        }

        myList.addListSelectionListener(listSelectionEvent -> {
            AudioPlayerWrapper value = myList.getSelectedValue();
            myAudioPlayerUI.update(value);
        });

        myList.setCellRenderer((list, playerWrapper, index, isSelected, cellHasFocus) -> {
            JPanel panel = new JPanel(new BorderLayout()) {
                @Override
                public Dimension getPreferredSize() {
                    Dimension preferredSize = super.getPreferredSize();
                    return new Dimension(preferredSize.width, JBUI.scale(50));
                }
            };

            panel.setBorder(JBUI.Borders.empty(0, 5));
            panel.setBackground(UIUtil.getListBackground(isSelected));

            Image icon = PlatformIconGroup.filetypesUnknown();
            String fileRelativePath = "";
            String filePath = FileUtil.toSystemDependentName(playerWrapper.getFilePath());

            VirtualFile file = playerWrapper.getFile();
            if (file != null) {
                String relativeLocation = VirtualFileUtil.getRelativeLocation(file, myProject.getBaseDir());
                if (relativeLocation != null) {
                    fileRelativePath = relativeLocation;
                }
                else {
                    fileRelativePath = file.getPath();
                }

                icon = file.getFileType().getIcon();
            }
            else {
                fileRelativePath = PathUtil.getFileName(filePath);
            }

            JBLabel label = new JBLabel(icon);
            label.setOpaque(false);

            panel.add(label, BorderLayout.WEST);

            SimpleColoredComponent relativePathComp = new SimpleColoredComponent();
            relativePathComp.setForeground(UIUtil.getListForeground(isSelected));
            relativePathComp.setTextAlign(SwingConstants.LEFT);
            relativePathComp.setOpaque(false);
            relativePathComp.append(fileRelativePath);

            SimpleColoredComponent pathComp = new SimpleColoredComponent();
            pathComp.setTextAlign(SwingConstants.LEFT);
            pathComp.setOpaque(false);
            if (isSelected) {
                pathComp.append(filePath);
                pathComp.setForeground(UIUtil.getListForeground(true));
            }
            else if (file == null) {
                pathComp.append(filePath, new SimpleTextAttributes(SimpleTextAttributes.STYLE_SMALLER, JBColor.RED));
            }
            else {
                pathComp.append(filePath, SimpleTextAttributes.GRAYED_SMALL_ATTRIBUTES);
            }

            JPanel vertial = new JPanel(new VerticalFlowLayout(true, true));
            vertial.setOpaque(false);
            vertial.add(relativePathComp);
            vertial.add(pathComp);

            panel.add(vertial, BorderLayout.CENTER);

            return panel;
        });

        myAudioPlayerUI = new AudioPlayerUI(playlistStore);
        Disposer.register(this, myAudioPlayerUI);

        splitter.setFirstComponent(ScrollPaneFactory.createScrollPane(myList, true));
        splitter.setSecondComponent(myAudioPlayerUI.getComponent());

        return myRoot;
    }

    @Override
    public void addFile(@Nonnull VirtualFile file) {
        AudioPlayerWrapper wrapper = myLoadedFiles.computeIfAbsent(file.getUrl(), s ->
        {
            AudioPlaylistStore playlistStore = AudioPlaylistStore.getInstance(myProject);

            playlistStore.addFile(file);

            AudioPlayerWrapper w = new AudioPlayerWrapper(file.getUrl());

            myModel.add(w);

            return w;
        });

        myList.setSelectedValue(wrapper, true);
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return myRoot;
    }

    @Override
    public void dispose() {
        for (AudioPlayerWrapper wrapper : myLoadedFiles.values()) {
            Disposer.dispose(wrapper);
        }
    }
}
