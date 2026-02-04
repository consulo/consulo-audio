/**
 * @author VISTALL
 * @since 07/01/2023
 */
module consulo.audio.javax.impl {
    requires consulo.audio.api;
    requires java.desktop;

    uses javax.sound.sampled.spi.AudioFileWriter;
    uses javax.sound.sampled.spi.AudioFileReader;
    uses javax.sound.sampled.spi.FormatConversionProvider;
}