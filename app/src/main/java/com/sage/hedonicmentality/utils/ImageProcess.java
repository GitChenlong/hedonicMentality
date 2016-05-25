package com.sage.hedonicmentality.utils;

public abstract class ImageProcess {

    private static int decodeYUV420SPtoRedSum(byte[] yuv420sp, int width, int height) {
        if (yuv420sp == null) return 0;

        final int frameSize = width * height;

        int sum = 0;
        for (int j = 0, yp = 0; j < height; j++) {
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                int y = (0xff & ((int) yuv420sp[yp])) - 16;
                if (y < 0) y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }
                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                if (r < 0) r = 0;
                else if (r > 262143) r = 262143;
                if (g < 0) g = 0;
                else if (g > 262143) g = 262143;
                if (b < 0) b = 0;
                else if (b > 262143) b = 262143;

                int pixel = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
                int red = (pixel >> 16) & 0xff;
                sum += red;
            }
        }
        return sum;
    }

    /*
        public static int decodeYUV420SPtoRedAvg(byte[] yuv420sp, int width, int height) {
            if (yuv420sp == null) return 0;

            final int frameSize = width * height;

            int sum = decodeYUV420SPtoRedSum(yuv420sp, width, height);
            return (sum / frameSize);
        }
    */
    private static int decodeYUV420SPtoRedSumP(byte[] yuv420sp, int width, int height) {
        if (yuv420sp == null) return 0;
        final int frameSize = width * height;
        int mh = height / 2;
        int mw = width / 2;
        int smh = mh - 24, emh = mh + 24;
        int smw = mw - 24, emw = mw + 24;
        int sum = 0;
        int startyp = smh * width;
        for (int j = smh, yp = startyp; j < emh; j++) {
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            //uvp is start position of uv buffer U4 is used by Y13 Y14 Y19 Y20. Row 3(index 2) and 4(index 3) /2 is 1. 1*width is 6
            //U5 is used by Y15 Y16 Y21 Y22. It still in row 3 and 4
        /*
    	Y1 Y2 Y3 Y4 Y5 Y6
    	Y7 Y8 Y9 Y10 Y11 Y12
    	Y13 Y14 Y15 Y16 Y17 Y18
    	Y19 Y20 Y21 Y22 Y23 Y24
    	U1 V1 U2 V2 U3 V3
    	U4 V4 U5 V5 U6 V6
    	*/
            yp = yp + smw;
            for (int i = smw; i < emw; i++, yp++) { //start to process each row
                int y = (0xff & ((int) yuv420sp[yp])) - 16; //y is for each pixel. yp is index of data index for Y.
                if (y < 0) y = 0;
                uvp = uvp + smw / 2;
                if ((i & 1) == 0) { //odd number
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }
                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);
                if (r < 0) r = 0;
                else if (r > 262143) r = 262143;
                if (g < 0) g = 0;
                else if (g > 262143) g = 262143;
                if (b < 0) b = 0;
                else if (b > 262143) b = 262143;
                int pixel = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
                int red = (pixel >> 16) & 0xff;
                sum += red;
            }
        }
        //Log.d("ImageproceeRed", Integer.toString(sum));
        return sum;
    }

    /**
     * Given a byte array representing a yuv420sp image, determine the average
     * amount of red in the image. Note: returns 0 if the byte array is NULL.
     *
     * @param yuv420sp Byte array representing a yuv420sp image
     * @param width    Width of the image.
     * @param height   Height of the image.
     * @return int representing the average amount of red in the image.
     */
    public static int decodeYUV420SPtoRedAvg(byte[] yuv420sp, int width, int height, int imageprocessflag) {
        if (yuv420sp == null)
            return 0;
        int sum = decodeYUV420SPtoRedSum(yuv420sp, width, height);
        if (imageprocessflag > 1) {
             int frameSize = width * height;
            //System.err.println(sum+"/"+frameSize+"="+(sum*1.0f/frameSize)+"=="+(sum*5/frameSize));
            return (sum*10 / frameSize);
        } else {
            //System.err.println(sum+"/"+48 * 48+"="+(sum*1.0f/(48 * 48))+"=="+(sum*5/(48 * 48)));
            return (sum / (48 * 48));
        }

    }


}
