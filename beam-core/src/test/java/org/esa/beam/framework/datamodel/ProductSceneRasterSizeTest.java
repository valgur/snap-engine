/*
 * Copyright (C) 2010 Brockmann Consult GmbH (info@brockmann-consult.de)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, see http://www.gnu.org/licenses/
 */

package org.esa.beam.framework.datamodel;

import org.esa.beam.framework.dataio.ProductIO;
import org.junit.Test;

import java.awt.Dimension;
import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ProductSceneRasterSizeTest {
    @Test
    public void testSizeChangeWithInitialSize() throws Exception {
        Product product = new Product("N", "T", 30, 20);
        assertEquals(new Dimension(30, 20), product.getSceneRasterSize());

        product.addBand(new Band("B1", ProductData.TYPE_FLOAT32, 100, 200));
        assertEquals(new Dimension(100, 200), product.getSceneRasterSize());

        product.addBand(new Band("B2", ProductData.TYPE_FLOAT32, 110, 190));
        assertEquals(new Dimension(110, 200), product.getSceneRasterSize());

        product.addTiePointGrid(new TiePointGrid("TPG1", 1000, 1, 0f, 0f, 1f, 1f, new float[1000]));
        assertEquals(new Dimension(110, 200), product.getSceneRasterSize());
    }

    @Test
    public void testSizeChangeWithoutInitialSize() throws Exception {
        Product product = new Product("N", "T");
        assertNull(product.getSceneRasterSize());

        product.addBand(new Band("B1", ProductData.TYPE_FLOAT32, 100, 200));
        assertEquals(new Dimension(100, 200), product.getSceneRasterSize());

        product.addBand(new Band("B2", ProductData.TYPE_FLOAT32, 110, 190));
        assertEquals(new Dimension(110, 200), product.getSceneRasterSize());

        product.addTiePointGrid(new TiePointGrid("TPG1", 1000, 1, 0f, 0f, 1f, 1f, new float[1000]));
        assertEquals(new Dimension(110, 200), product.getSceneRasterSize());
    }


    @Test
    public void testDimap() throws Exception {
        Product product = new Product("N", "T");
        assertNull(product.getSceneRasterSize());

        product.addBand(new Band("B1", ProductData.TYPE_FLOAT32, 100, 200));
        assertEquals(new Dimension(100, 200), product.getSceneRasterSize());

        product.addBand(new Band("B2", ProductData.TYPE_FLOAT32, 110, 190));
        assertEquals(new Dimension(110, 200), product.getSceneRasterSize());

        File file = new File("multisize_product.dim");
        ProductIO.writeProduct(product, file, "BEAM-DIMAP", false);
        Product product2 = ProductIO.readProduct(file);
        assertEquals(new Dimension(110, 200), product2.getSceneRasterSize());
        assertEquals(new Dimension(100, 200), product2.getBand("B1").getRasterSize());
        assertEquals(new Dimension(110, 190),product2.getBand("B2").getRasterSize());
    }
}

