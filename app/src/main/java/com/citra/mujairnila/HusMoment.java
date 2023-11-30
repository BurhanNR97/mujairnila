package com.citra.mujairnila;

public class HusMoment {
    public double[] getMoments() {
        return moments;
    }

    public void setMoments(double[] moments) {
        this.moments = moments;
    }

    private double[] moments = null;

    public HusMoment(){};

    public double[] Compute(double[][] image){

        moments = new double[7];

        double
                n20 = getNormalizedCentralMoment(2, 0, image),
                n02 = getNormalizedCentralMoment(0, 2, image),
                n30 = getNormalizedCentralMoment(3, 0, image),
                n12 = getNormalizedCentralMoment(1, 2, image),
                n21 = getNormalizedCentralMoment(2, 1, image),
                n03 = getNormalizedCentralMoment(0, 3, image),
                n11 = getNormalizedCentralMoment(1, 1, image);

        //First moment
        moments[0] = n20 + n02;

        //Second moment
        moments[1] = Math.pow((n20 - 02), 2) + Math.pow(2 * n11, 2);

        //Third moment
        moments[2] = Math.pow(n30 - (3 * (n12)), 2)
                + Math.pow((3 * n21 - n03), 2);

        //Fourty moment
        moments[3] = Math.pow((n30 + n12), 2) + Math.pow((n12 + n03), 2);

        //Fifty moment
        moments[4] = (n30 - 3 * n12) * (n30 + n12)
                * (Math.pow((n30 + n12), 2) - 3 * Math.pow((n21 + n03), 2))
                + (3 * n21 - n03) * (n21 + n03)
                * (3 * Math.pow((n30 + n12), 2) - Math.pow((n21 + n03), 2));

        //Sixty moment
        moments[5] = (n20 - n02)
                * (Math.pow((n30 + n12), 2) - Math.pow((n21 + n03), 2))
                + 4 * n11 * (n30 + n12) * (n21 + n03);

        //Seventy moment
        moments[6] = (3 * n21 - n03) * (n30 + n12)
                * (Math.pow((n30 + n12), 2) - 3 * Math.pow((n21 + n03), 2))
                + (n30 - 3 * n12) * (n21 + n03)
                * (3 * Math.pow((n30 + n12), 2) - Math.pow((n21 + n03), 2));

        return moments;
    }

    public double getHuMoment (double[][] image,int n) {
        //Ensures the values are in the range [1..7].
        n = Math.min(7, Math.max(1, n));

        double result = 0.0;

        double
                n20 = getNormalizedCentralMoment(2, 0, image),
                n02 = getNormalizedCentralMoment(0, 2, image),
                n30 = getNormalizedCentralMoment(3, 0, image),
                n12 = getNormalizedCentralMoment(1, 2, image),
                n21 = getNormalizedCentralMoment(2, 1, image),
                n03 = getNormalizedCentralMoment(0, 3, image),
                n11 = getNormalizedCentralMoment(1, 1, image);

        switch (n) {
            case 1:
                result = n20 + n02;
                break;
            case 2:
                result = Math.pow((n20 - 02), 2) + Math.pow(2 * n11, 2);
                break;
            case 3:
                result = Math.pow(n30 - (3 * (n12)), 2)
                        + Math.pow((3 * n21 - n03), 2);
                break;
            case 4:
                result = Math.pow((n30 + n12), 2) + Math.pow((n12 + n03), 2);
                break;
            case 5:
                result = (n30 - 3 * n12) * (n30 + n12)
                        * (Math.pow((n30 + n12), 2) - 3 * Math.pow((n21 + n03), 2))
                        + (3 * n21 - n03) * (n21 + n03)
                        * (3 * Math.pow((n30 + n12), 2) - Math.pow((n21 + n03), 2));
                break;
            case 6:
                result = (n20 - n02)
                        * (Math.pow((n30 + n12), 2) - Math.pow((n21 + n03), 2))
                        + 4 * n11 * (n30 + n12) * (n21 + n03);
                break;
            case 7:
                result = (3 * n21 - n03) * (n30 + n12)
                        * (Math.pow((n30 + n12), 2) - 3 * Math.pow((n21 + n03), 2))
                        + (n30 - 3 * n12) * (n21 + n03)
                        * (3 * Math.pow((n30 + n12), 2) - Math.pow((n21 + n03), 2));
                break;

            default:
                throw new IllegalArgumentException("Invalid number for Hu moment.");
        }
        return result;
    }

    public static double getRawMoment(int p, int q, double[][] image) {
        double m = 0;
        for (int i = 0, k = image.length; i < k; i++) {
            for (int j = 0, l = image[i].length; j < l; j++) {
                m += Math.pow(i, p) * Math.pow(j, q) * image[i][j];
            }
        }
        return m;
    }

    public static double getCentralMoment(int p, int q, double[][] img) {
        double mc = 0;
        double m00 = getRawMoment(0, 0, img);
        double m10 = getRawMoment(1, 0, img);
        double m01 = getRawMoment(0, 1, img);
        double x0 = m10 / m00;
        double y0 = m01 / m00;
        for (int i = 0, k = img.length; i < k; i++) {
            for (int j = 0, l = img[i].length; j < l; j++) {
                mc += Math.pow((i - x0), p) * Math.pow((j - y0), q) * img[i][j];
            }
        }
        return mc;
    }

    public static double getCovarianceXY(double[][] image) {
        double mc00 = getCentralMoment(0, 0, image);
        double mc11 = getCentralMoment(1, 1, image);
        return mc11 / mc00;
    }

    public static double getVarianceX(double[][] image) {
        double mc00 = getCentralMoment(0, 0, image);
        double mc20 = getCentralMoment(2, 0, image);
        return mc20 / mc00;
    }

    public static double getVarianceY(double[][] image) {
        double mc00 = getCentralMoment(0, 0, image);
        double mc02 = getCentralMoment(0, 2, image);
        return mc02 / mc00;
    }

    public static double getNormalizedCentralMoment(int p, int q, double[][] image) {
        double gama = ((p + q) / 2) + 1;
        double mpq = getCentralMoment(p, q, image);
        double m00gama = Math.pow(getCentralMoment(0, 0, image), gama);
        return mpq / m00gama;
    }
}
