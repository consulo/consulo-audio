/**
 * @author VISTALL
 * @since 07/01/2023
 */
module consulo.audio.api {
    requires consulo.application.api;
    requires consulo.component.api;
    requires consulo.disposer.api;
    requires transitive consulo.file.editor.api;
    requires consulo.localize.api;
    requires consulo.project.api;
    requires consulo.ui.api;
    requires consulo.util.dataholder;
    requires consulo.virtual.file.system.api;

    requires consulo.configuration.editor.api;
    requires consulo.language.api;

    exports consulo.audio;
    exports consulo.audio.engine;
    exports consulo.audio.fileEditorProvider;
    exports consulo.audio.icon;
    exports consulo.audio.playlist;
    exports consulo.audio.playlist.vfs;

    exports consulo.audio.internal to consulo.audio;

    opens consulo.audio.playlist to consulo.util.xml.serializer;
}
