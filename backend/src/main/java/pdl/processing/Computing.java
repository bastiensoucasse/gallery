package pdl.processing;

class Computing
{
    /**
     * Converts a RGB color into a HSV color.
     *
     * @param r The red.
     * @param g The green.
     * @param b The blue.
     * @return The HSV color.
     */
    private static float[] rgbToHsv(final int r, final int g, final int b)
    {
        float h = 0, s = 0, v = 0;

        final float r2 = (float) r / 255;
        final float g2 = (float) g / 255;
        final float b2 = (float) b / 255;

        final float max = Math.max(Math.max(r2, g2), b2);
        final float min = Math.min(Math.min(r2, g2), b2);
        final float c = max - min;

        if (c != 0)
        {
            if (max == r2)
                h = 60 * ((g2 - b2) / c);
            if (max == g2)
                h = 60 * ((b2 - r2) / c + 2);
            if (max == b2)
                h = 60 * ((r2 - g2) / c + 4);
        }

        if (max == 0)
            s = 0;
        else
            s = c / max;

        v = max;

        return new float[] { h, s, v };
    }

    /**
     * Converts a HSV color into a RGB color.
     *
     * @param h The hue.
     * @param s The saturation.
     * @param v The brightness.
     * @return The RGB color.
     */
    private static int[] hsvToRgb(final float h, final float s, final float v)
    {
        int r = 0, g = 0, b = 0;

        final float c = v * s;
        final float x = c * (1 - Math.abs((h / 60) % 2 - 1));
        final float m = v - c;

        float r2 = 0, g2 = 0, b2 = 0;
        if (0 <= h && h < 60)
        {
            r2 = c;
            g2 = x;
            b2 = 0;
        }
        else if (60 <= h && h < 120)
        {
            r2 = x;
            g2 = c;
            b2 = 0;
        }
        else if (120 <= h && h < 180)
        {
            r2 = 0;
            g2 = c;
            b2 = x;
        }
        else if (180 <= h && h < 240)
        {
            r2 = 0;
            g2 = x;
            b2 = c;
        }
        else if (240 <= h && h < 300)
        {
            r2 = x;
            g2 = 0;
            b2 = c;
        }
        else if (300 <= h && h < 360)
        {
            r2 = c;
            g2 = 0;
            b2 = x;
        }

        r = (int) ((r2 + m) * 255);
        g = (int) ((g2 + m) * 255);
        b = (int) ((b2 + m) * 255);

        return new int[] { r, g, b };
    }

    /**
     * Converts a RGB color into a HSV color.
     *
     * @param rgb The RGB color.
     * @return The HSV color.
     */
    public static float[] rgbToHsv(final int[] rgb)
    {
        return rgbToHsv(rgb[0], rgb[1], rgb[2]);
    }

    /**
     * Converts a HSV color into a RGB color.
     *
     * @param hsv The HSV color.
     * @return The RGB color.
     */
    public static int[] hsvToRgb(final float[] hsv)
    {
        return hsvToRgb(hsv[0], hsv[1], hsv[2]);
    }
}
