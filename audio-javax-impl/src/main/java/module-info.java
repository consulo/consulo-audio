/**
 * @author VISTALL
 * @since 07/01/2023
 */
module consulo.audio.javax.impl {
    requires consulo.audio.api;

    requires consulo.application.api;
    requires consulo.component.api;
    requires consulo.container.api;
    requires consulo.disposer.api;
    requires consulo.proxy;
    requires consulo.ui.api;
    requires consulo.util.io;
    requires consulo.util.lang;
    requires consulo.virtual.file.system.api;

    requires java.desktop;

    uses javax.sound.sampled.spi.AudioFileWriter;
    uses javax.sound.sampled.spi.AudioFileReader;
    uses javax.sound.sampled.spi.FormatConversionProvider;
}
