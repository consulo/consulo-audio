/**
 * @author VISTALL
 * @since 07/01/2023
 */
module consulo.audio.api {
	requires transitive consulo.ide.api;

	exports consulo.audio;
	exports consulo.audio.engine;
	exports consulo.audio.fileEditorProvider;
	exports consulo.audio.icon;
	exports consulo.audio.playlist;
	exports consulo.audio.playlist.vfs;

	opens consulo.audio.playlist to consulo.util.xml.serializer;
}