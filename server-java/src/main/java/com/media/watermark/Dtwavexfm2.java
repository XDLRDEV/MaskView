package com.media.watermark;

import java.util.ArrayList;

/**
 * Function to perform a n-level DTCWT-2D decompostion on a 2D matrix X
 * @Time 2020-04-20 14:49:01
 */

public class Dtwavexfm2 {
    private double[][] Ug;                                        //2D real matrix/Image
    private int nLevels;                                          //No. of levels of wavelet decomposition
    private String biort;                                         //LeGall 5,3 tap filters.
    private String qShift;                                        //Q-shift 10,10 tap filters,(with 10,10 non-zero taps, unlike qshift_06).
    private ArrayList<Complex[][][] > Yh=new ArrayList<>();       //A cell array containing the 6 complex highpass subimages for each level.
    private double[][] Yl;                                        //The real lowpass image from the final level

    //convolution  kernel vector
    private double[] h0a={-0.00476161193845591,-0.000446022789262285,-7.14419732796501e-05,0.0349146123068422,-0.0372738957998980,-0.115911457427441,0.276368643133032,0.756393765199037,0.567134484100133,0.0146374059644734,-0.112558884257522,0.0222892632669227,0.0184986827241562,-0.00720267787825835,-0.000227652205897772,0.00243034994514868};
    private double[] h0b={0.00243034994514868,-0.000227652205897772,-0.00720267787825835,0.0184986827241562,0.0222892632669227,-0.112558884257522,0.0146374059644734,0.567134484100133,0.756393765199037,0.276368643133032,-0.115911457427441,-0.0372738957998980,0.0349146123068422,-7.14419732796501e-05,-0.000446022789262285,-0.00476161193845591};
    private double[] h0o={-0.125000000000000,0.250000000000000,0.750000000000000,0.250000000000000,-0.125000000000000};
    private double[] h1a={0.00243034994514868,0.000227652205897772,-0.00720267787825835,-0.0184986827241562,0.0222892632669227,0.112558884257522,0.0146374059644734,-0.567134484100133,0.756393765199037,-0.276368643133032,-0.115911457427441,0.0372738957998980,0.0349146123068422,7.14419732796501e-05,-0.000446022789262285,0.00476161193845591};
    private double[] h1b={0.00476161193845591,-0.000446022789262285,7.14419732796501e-05,0.0349146123068422,0.0372738957998980,-0.115911457427441,-0.276368643133032,0.756393765199037,-0.567134484100133,0.0146374059644734,0.112558884257522,0.0222892632669227,-0.0184986827241562,-0.00720267787825835,0.000227652205897772,0.00243034994514868};
    private double[] h1o={-0.250000000000000,0.500000000000000,-0.250000000000000};

    private double[][] Lo;                                        //The low frequency component after performing a high level filter on the columns.
    private double[][] Hi;                                        //The high frequency component after performing a high level filter on the columns.
    private double[][] LoLo;                                      //The low frequency component after performing a high level filter on the rows.

    /*
     * Ug             2D matrix
     * nLevels        levels of wavelet decomposition
     * biort          tap filters
     * qShift         tap filters
     *@return  this
     */
    public Dtwavexfm2(double[][] Ug, int nLevels, String biort, String qShift){
        this.Ug=Ug;
        this.nLevels=nLevels;
        this.biort=biort;
        this.qShift=qShift;
    }

    public ArrayList<Complex[][][] > getYh(){ return this.Yh; }

    public double[][] getYl(){ return this.Yl; }

    /**
     * Function to perform a n-level DTCWT-2D decompostion on a 2D matrix X
     *
     *  performs a n-level transform on the real image X using the 5,3-tap filters
     *  for level 1 and the Q-shift 10-tap filters for levels >= 2.
     *
     */

    public Dtwavexfm2 dtwavexfm2() {

        if (nLevels >= 1) {

            //Perform high level filtering on columns.
            Colfilter lO = new Colfilter(this.Ug, h0o);
            Lo = lO.colfilter();
            Lo = lO.transPosition();
            Colfilter hi = new Colfilter(this.Ug, h1o);
            Hi = hi.colfilter();
            Hi = hi.transPosition();

            //Perform high level filtering on rows.
            Colfilter lolo = new Colfilter(Lo, h0o);
            LoLo = lolo.colfilter();
            LoLo = lolo.transPosition();

            Complex[][][] levelYh = new Complex[6][(int) Math.floor(LoLo.length / 2)][(int) Math.floor(LoLo[0].length / 2)];
            Complex[][][] waveletPairs;

            //Horizontal pair
            Colfilter yh116 = new Colfilter(Hi, h0o);
            yh116.colfilter();
            waveletPairs = q2c(yh116.transPosition());

            levelYh[0] = waveletPairs[0];
            levelYh[5] = waveletPairs[1];

            //Vertical pair
            Colfilter yh134 = new Colfilter(Lo, h1o);
            yh134.colfilter();
            waveletPairs = q2c(yh134.transPosition());

            levelYh[2] = waveletPairs[0];
            levelYh[3] = waveletPairs[1];

            //Diagonal pair
            Colfilter yh125 = new Colfilter(Hi, h1o);
            yh125.colfilter();
            waveletPairs = q2c(yh125.transPosition());

            levelYh[1] = waveletPairs[0];
            levelYh[4] = waveletPairs[1];

            this.Yh.add(levelYh);
        }

        if (nLevels >= 2) {
            for (int level = 2; level <= nLevels; level++) {


                int rowSize = LoLo.length;                                                                              //The number of rows in the matrix

                int coleSize = LoLo[0].length;                                                                          //The number of coles in the matrix

                // Do  Qshift filters on rows.
                Coldfilt coldfilt1 = new Coldfilt(h0b, h0a, LoLo);
                Coldfilt coldfilt2 = new Coldfilt(h1b, h1a, LoLo);
                Lo = coldfilt1.coldFilt();
                Hi = coldfilt2.coldFilt();

                //matrix transposition
                Lo = coldfilt1.transPosition();
                Hi = coldfilt2.transPosition();

                //Do  Qshift filters on columns.
                Coldfilt coldfiltLo = new Coldfilt(h0b, h0a, Lo);
                LoLo = coldfiltLo.coldFilt();

                //matrix transposition
                LoLo = coldfiltLo.transPosition();

                Complex[][][] levelYh= new Complex[6][LoLo.length][LoLo[0].length];
                Complex[][][] waveletPairs;

                //Horizontal pair
                Coldfilt coldfiltY16 = new Coldfilt(h0b, h0a, Hi);
                coldfiltY16.coldFilt();
                waveletPairs = q2c(coldfiltY16.transPosition());
                levelYh[0] = waveletPairs[0];
                levelYh[5] = waveletPairs[1];

                //Vertical pair
                Coldfilt coldfiltY34 = new Coldfilt(h1b, h1a, Lo);
                coldfiltY34.coldFilt();
                waveletPairs = q2c(coldfiltY34.transPosition());
                levelYh[2] = waveletPairs[0];
                levelYh[3] = waveletPairs[1];

                //Diagonal pair
                Coldfilt coldfiltY25 = new Coldfilt(h1b, h1a, Hi);
                coldfiltY25.coldFilt();
                waveletPairs = q2c(coldfiltY25.transPosition());
                levelYh[1] = waveletPairs[0];
                levelYh[4] = waveletPairs[1];

                Yh.add(levelYh );                                                                                       //High frequency wavelet coefficient matrix
            }
        }
        Yl = LoLo;                                                                                                      //Low frequency wavelet coefficient matrix
        return this;
    }

    //Convert from quads in matrix to complex numbers in z.

    public Complex[][][] q2c(double[][] matrix){

        //The number of rows in the matrix
        int mr=matrix.length;

        //The number of rows in the matrix
        int mc=matrix[0].length;

        ArrayList<Integer> t1=new ArrayList<>();
        ArrayList<Integer> t2=new ArrayList<>();

        Complex[] j2=new Complex[2];
        j2[0]=new Complex(0.7071,0.0);
        j2[1]=new Complex(0.0,0.7071);
        for (int i=0;2*i<mr;i++){
            t1.add(2*i);
        }
        for (int i=0;2*i<mc;i++){
            t2.add(2*i);
        }

        /*Arrange pixels from the corners of the quads into
         2 subimages of alternate real and imag pixels.
            a----b
            |    |
            c----d
        Combine (a,b) and (d,c) to form two complex subimages.*/

        Complex[][] p=new Complex[t1.size()][t2.size()];
        Complex[][] q=new Complex[t1.size()][t2.size()];

        for (int i=0;i<t1.size();i++){
            for(int j=0;j<t2.size();j++){
            p[i][j]=(j2[0].multy((matrix[t1.get(i)][t2.get(j)]))).plus(j2[1].multy((matrix[t1.get(i)][t2.get(j)+1])));
            q[i][j]=(j2[0].multy((matrix[t1.get(i)+1][t2.get(j)+1]))).minus(j2[1].multy((matrix[t1.get(i)+1][t2.get(j)])));
            }
        }

        Complex[][] z1=new Complex[p.length][p[0].length];
        Complex[][] z2=new Complex[p.length][p[0].length];
        for (int i=0;i<p.length;i++){
            for (int j=0;j<p[0].length;j++){
                z1[i][j]=p[i][j].minus(q[i][j]);
                z2[i][j]=p[i][j].plus(q[i][j]);
            }
        }

        Complex[][][] z=new Complex[2][p.length][p[0].length];

        //Form the 2 subbands in z
        z[0]=z1;
        z[1]=z2;
        return z;
    }

}

