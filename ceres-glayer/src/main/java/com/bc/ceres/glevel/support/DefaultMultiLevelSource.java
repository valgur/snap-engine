package com.bc.ceres.glevel.support;

import com.bc.ceres.glevel.MultiLevelModel;
import com.bc.ceres.glevel.MultiLevelSource;

import javax.media.jai.Interpolation;
import javax.media.jai.operator.ScaleDescriptor;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

public class DefaultMultiLevelSource extends AbstractMultiLevelSource {

    public static final Interpolation DEFAULT_INTERPOLATION = Interpolation.getInstance(Interpolation.INTERP_BICUBIC);
    public static final MultiLevelSource NULL = createNullImage();

    private final RenderedImage sourceImage;
    private final Interpolation interpolation;

    public DefaultMultiLevelSource(RenderedImage sourceImage) {
        this(sourceImage, 1);
    }

    public DefaultMultiLevelSource(RenderedImage sourceImage, int levelCount) {
        this(sourceImage, levelCount, DEFAULT_INTERPOLATION);
    }

    public DefaultMultiLevelSource(RenderedImage sourceImage, int levelCount, Interpolation interpolation) {
        this(sourceImage, createDefaultMultiLevelModel(sourceImage, levelCount), interpolation);
    }

    public DefaultMultiLevelSource(RenderedImage sourceImage, MultiLevelModel multiLevelModel) {
        this(sourceImage, multiLevelModel, DEFAULT_INTERPOLATION);
    }

    public DefaultMultiLevelSource(RenderedImage sourceImage, MultiLevelModel multiLevelModel, Interpolation interpolation) {
        super(multiLevelModel);
        this.sourceImage = sourceImage;
        this.interpolation = interpolation;
    }

    public RenderedImage getSourceImage() {
        return sourceImage;
    }

    public Interpolation getInterpolation() {
        return interpolation;
    }

    /**
     * Returns the level-0 image if {@code level} equals zero, otherwise calls {@code super.getLevelImage(level)}.
     *
     * @param level The level.
     * @return The image.
     */
    @Override
    public synchronized RenderedImage getImage(int level) {
        if (level == 0) {
            return sourceImage;
        }
        return super.getImage(level);
    }

    /**
     * Returns the level-0 image if {@code level} equals zero, otherwise creates a scaled version of it.
     *
     * @param level The level.
     * @return The image.
     */
    @Override
    public RenderedImage createImage(int level) {
        if (level == 0) {
            return sourceImage;
        }
        final float scale = (float) (1.0 / getModel().getScale(level));
        return ScaleDescriptor.create(sourceImage, scale, scale, 0.0f, 0.0f, interpolation, null);
    }

    public static MultiLevelModel createDefaultMultiLevelModel(RenderedImage sourceImage, int levelCount) {
        return new DefaultMultiLevelModel(levelCount,
                                          new AffineTransform(),
                                          sourceImage.getWidth(),
                                          sourceImage.getHeight());
    }


    private static MultiLevelSource createNullImage() {
        final BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_GRAY);
        final DefaultMultiLevelModel model = new DefaultMultiLevelModel(1, new AffineTransform(), null);
        return new DefaultMultiLevelSource(image, model);
    }
}