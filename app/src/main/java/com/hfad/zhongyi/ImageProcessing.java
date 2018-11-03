package com.hfad.zhongyi;

import android.util.Log;

public abstract class ImageProcessing {

    private static int decodeYUV420SPtoRedSum(byte[] yuv420sp, int width, int height) {
        if (yuv420sp == null) return 0;

        final int frameSize = width * height;

        // Log.d("ImageProcessing", "" + frameSize + ":" + yuv420sp.length);

        int sum = 0;
        long totalTime = 0;

        long st1 = 0;
        long st2 = 0;
        long st3 = 0;
        long st4 = 0;
        long st5 = 0;


        for (int j = 0, yp = 0; j < height; j++) {
            // long start = System.nanoTime();
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                int y = (0xff & yuv420sp[yp]) - 16;
                // st1 += (System.nanoTime() - start);
                // start = System.nanoTime();
                if (y < 0) y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }
//                st2 += (System.nanoTime() - start);
//                start = System.nanoTime();
                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);
//                st3 += (System.nanoTime() - start);
//                start = System.nanoTime();

                if (r < 0) r = 0;
                else if (r > 262143) r = 262143;
                if (g < 0) g = 0;
                else if (g > 262143) g = 262143;
                if (b < 0) b = 0;
                else if (b > 262143) b = 262143;
//                st4 += (System.nanoTime() - start);
//                start = System.nanoTime();

                int pixel = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
                int red = (pixel >> 16) & 0xff;
                sum += red;
//                st5 += (System.nanoTime() - start);
//                start = System.nanoTime();
            }
            // long end = System.nanoTime();
            // totalTime += end - start;
        }
        // Log.d("Image Processing Time", String.format("%d %d %d %d %d", st1, st2, st3, st4, st5));
        return sum;
    }

    /**
     * Given a byte array representing a yuv420sp image, determine the average
     * amount of red in the image. Note: returns 0 if the byte array is NULL.
     *
     * @param yuv420sp
     *            Byte array representing a yuv420sp image
     * @param width
     *            Width of the image.
     * @param height
     *            Height of the image.
     * @return int representing the average amount of red in the image.
     */
    public static int decodeYUV420SPtoRedAvg(byte[] yuv420sp, int width, int height) {
        if (yuv420sp == null) return 0;

        final int frameSize = width * height;

        int sum = decodeYUV420SPtoRedSum(yuv420sp, width, height);
        return (sum / frameSize);
    }
}