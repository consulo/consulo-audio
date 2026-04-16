/**
 * @author VISTALL
 * @since 07/01/2023
 */
module consulo.audio {
	requires consulo.ui.ex.api;
	requires consulo.ui.ex.awt.api;
	requires consulo.configuration.editor.api;
	requires consulo.audio.api;

	// TODO [VISTALL] remove it in future
	requires java.desktop;
}