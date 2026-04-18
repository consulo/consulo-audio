/**
 * @author VISTALL
 * @since 07/01/2023
 */
module consulo.audio {
	requires consulo.audio.api;

	requires consulo.application.api;
	requires consulo.base.icon.library;
	requires consulo.configuration.editor.api;
	requires consulo.datacontext.api;
	requires consulo.disposer.api;
	requires consulo.localize.api;
	requires consulo.logging.api;
	requires consulo.project.api;
	requires consulo.ui.api;
	requires consulo.ui.ex.api;
	requires consulo.ui.ex.awt.api;
	requires consulo.util.concurrent;
	requires consulo.util.io;
	requires consulo.util.lang;
	requires consulo.virtual.file.system.api;

	// TODO [VISTALL] remove it in future
	requires java.desktop;
}
