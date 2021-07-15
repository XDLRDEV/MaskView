package com.media.watermark;

import java.util.ArrayList;

/**
 * Function to perform an n-level dual-tree complex wavelet (DTCWT) 2-D reconstruction.
 * @Time 2020-04-20 14:49:01
 */

public class Dtwaveifm2 {
    private double[][] Yl;                                                                                              //Low frequency wavelet coefficient matrix
    private ArrayList<Complex[][][]> Yh;                                                                                //High frequency wavelet coefficient matrix sequence
    private String biort;                                                                                               //LeGall 5,3 tap filters.
    private String qShift;                                                                                              //Q-shift 10,10 tap filters,(with 10,10 non-zero taps, unlike qshift_06).
    private double[][] reconstructedImage;                                                                              //Reconstructed image after inverse wavelet transform

    //convolution  kernel vector
    private double[] g0a={0.00243034994514868,-0.000227652205897772,-0.00720267787825835,0.0184986827241562,0.0222892632669227,-0.112558884257522,0.0146374059644734,0.567134484100133,0.756393765199037,0.276368643133032,-0.115911457427441,-0.0372738957998980,0.0349146123068422,-7.14419732796501e-05,-0.000446022789262285,-0.00476161193845591};
    private double[] g0b={-0.00476161193845591,-0.000446022789262285,-7.14419732796501e-05,0.0349146123068422,-0.0372738957998980,-0.115911457427441,0.276368643133032,0.756393765199037,0.567134484100133,0.0146374059644734,-0.112558884257522,0.0222892632669227,0.0184986827241562,-0.00720267787825835,-0.000227652205897772,0.00243034994514868};
    private double[] g0o={0.250000000000000,0.500000000000000,0.250000000000000};
    private double[] g1a={0.00476161193845591,-0.000446022789262285,7.14419732796501e-05,0.0349146123068422,0.0372738957998980,-0.115911457427441,-0.276368643133032,0.756393765199037,-0.567134484100133,0.0146374059644734,0.112558884257522,0.0222892632669227,-0.0184986827241562,-0.00720267787825835,0.000227652205897772,0.00243034994514868};
    private double[] g1b={0.00243034994514868,0.000227652205897772,-0.00720267787825835,-0.0184986827241562,0.0222892632669227,0.112558884257522,0.0146374059644734,-0.567134484100133,0.756393765199037,-0.276368643133032,-0.115911457427441,0.0372738957998980,0.0349146123068422,7.14419732796501e-05,-0.000446022789262285,0.00476161193845591};
    private double[] g1o={-0.125000000000000,-0.250000000000000,0.750000000000000,-0.250000000000000,-0.125000000000000};

    public Dtwaveifm2(double[][] Pl, ArrayList<Complex[][][]> Ph, String biort, String qShift){
        this.biort=biort;                                                                                               //LeGall 5,3 tap filters.
        this.Yh=Ph;                                                                                                     //High frequency wavelet coefficient matrix sequence
        this.Yl=Pl;                                                                                                     //Low frequency wavelet coefficient matrix
        this.qShift=qShift;                                                                                             ////Q-shift 10,10 tap filters,(with 10,10 non-zero taps, unlike qshift_06).
    }

    /*
     * @param
     * Yl        Low frequency wavelet coefficient matrix
     * Yh        levels of wavelet decomposition
     * biort          tap filters
     * qShift         tap filters
     *@return  this
     */
    public double[][] dtWaveifm2(){
        int size=Yh.size();                                                                                             //The length of the Yh
        int currentLevel=size;                                                                                          //The number of layers of the inverse wavelet transform
        int[][] gainMask=new int[6][size];                                                                              //Gain matrix to be applied to each subband.
        for (int i=0;i<gainMask.length;i++){
            for (int j=0;j<gainMask[0].length;j++){
                gainMask[i][j]=1;
            }
        }
        this.reconstructedImage =Yl;
        while (currentLevel>=2) {                                                                                       //this ensures that for level 1 we never do the following
            //Horizontal pair
            Complex[][][] yh16 = new Complex[2][this.Yh.get(currentLevel - 1)[0].length][this.Yh.get(currentLevel-1)[0][0].length];
            yh16[0] = Yh.get(currentLevel - 1)[0];
            yh16[1] = Yh.get(currentLevel - 1)[5];
            int[] gainMask16 = new int[2];
            gainMask16[0] = gainMask[0][currentLevel-1];
            gainMask16[1] = gainMask[5][currentLevel-1];
            double[][] lh=c2q(yh16, gainMask16);

            //Vertical pair
            Complex[][][] yh34 = new Complex[2][this.Yh.get(currentLevel - 1)[0].length][this.Yh.get(currentLevel-1)[0][0].length];
            yh34[0] = Yh.get(currentLevel - 1)[2];
            yh34[1] = Yh.get(currentLevel - 1)[3];
            int[] gainMask34 = new int[2];
            gainMask34[0] = gainMask[2][currentLevel-1];
            gainMask34[1] = gainMask[3][currentLevel-1];
            double[][] hl = this.c2q(yh34, gainMask34);

            //Diagonal pair
            Complex[][][] yh25 = new Complex[2][this.Yh.get(currentLevel - 1)[0].length][this.Yh.get(currentLevel-1)[0][0].length];
            yh25[0] = Yh.get(currentLevel - 1)[1];
            yh25[1] = Yh.get(currentLevel - 1)[4];
            int[] gainMask25 = new int[2];
            gainMask25[0] = gainMask[1][currentLevel-1];
            gainMask25[1] = gainMask[4][currentLevel-1];
            double[][] hh = this.c2q(yh25, gainMask25);


            //Do even Qshift filters on columns.
            Colifilt colifilty11 = new Colifilt(g0b, g0a, this.reconstructedImage);
            Colifilt colifilty12 = new Colifilt(g1b, g1a, lh);
            colifilty11.coliFilt();
            colifilty12.coliFilt();
            double[][] y1 = new double[colifilty11.Y.length][colifilty11.Y[0].length];
            for (int i = 0; i < colifilty11.Y.length; i++) {
                for (int j = 0; j < colifilty11.Y[0].length; j++) {
                    y1[i][j] = colifilty11.Y[i][j] + colifilty12.Y[i][j];
                }
            }

            Colifilt colifilty21 = new Colifilt(g0b, g0a, hl);
            Colifilt colifilty22 = new Colifilt(g1b, g1a, hh);
            colifilty21.coliFilt();
            colifilty22.coliFilt();
            double[][] y2 = new double[colifilty21.Y.length][colifilty21.Y[0].length];
            for (int i = 0; i < colifilty21.Y.length; i++) {
                for (int j = 0; j < colifilty21.Y[0].length; j++) {
                    y2[i][j] = colifilty21.Y[i][j] + colifilty22.Y[i][j];
                }
            }

            //Do even Qshift filters on rows.
            y1 = transPosition1(y1);                                                                                    //Transpose the matrix
            y2 = transPosition1(y2);
            Colifilt colifilty31 = new Colifilt(g0b, g0a, y1);
            Colifilt colifilty32 = new Colifilt(g1b, g1a, y2);
            colifilty31.coliFilt();
            colifilty32.coliFilt();
            double[][]z = new double[colifilty31.Y.length][colifilty31.Y[0].length];
            for (int i = 0; i < colifilty31.Y.length; i++) {
                for (int j = 0; j < colifilty31.Y[0].length; j++) {
                    z[i][j] = colifilty31.Y[i][j] + colifilty32.Y[i][j];
                }
            }
            this.reconstructedImage =z;
            this.reconstructedImage = transPosition1(this.reconstructedImage);

            //Check size of rconstructedImage and crop as required
            int rowSize = this.reconstructedImage.length;
            int colSize = this.reconstructedImage[0].length;
            int s1 = 2*Yh.get(currentLevel - 2)[0].length;
            int s2 = 2*Yh.get(currentLevel - 2)[0][0].length;
            if (rowSize != s1) {                                                                                        //check to see if this result needs to be cropped for the rows
                double[][] Z = new double[this.reconstructedImage.length - 2][this.reconstructedImage[0].length];
                for (int i = 0; i < Z.length; i++) {
                    for (int j = 0; j < Z[0].length; j++) {
                        Z[i][j] = this.reconstructedImage[i + 1][j];
                    }
                }
                this.reconstructedImage = Z;
            }
            if (colSize != s2) {                                                                                        //check to see if this result needs to be cropped for the cols
                double[][] Z = new double[this.reconstructedImage.length][this.reconstructedImage[0].length - 2];
                for (int i = 0; i < Z.length; i++) {
                    for (int j = 0; j < Z[0].length; j++) {
                        Z[i][j] = z[i][j + 1];
                    }
                }
                this.reconstructedImage = Z;
            }
            if (this.reconstructedImage.length != s1 || this.reconstructedImage[0].length != s2) {
                System.out.println("Sizes of subbands are not valid for DTWAVEIFM2");
                System.exit(0);
            }
            currentLevel = currentLevel - 1;
        }

        if(currentLevel==1){

            //Horizontal pair
            Complex[][][] yh16=new Complex[2][this.Yh.get(currentLevel-1)[0].length][this.Yh.get(0)[0][0].length];
            yh16[0]=Yh.get(currentLevel-1)[0];
            yh16[1]=Yh.get(currentLevel-1)[5];
            int[] gainMask16=new int[2];
            gainMask16[0]=gainMask[0][currentLevel-1];
            gainMask16[1]=gainMask[5][currentLevel-1];
            double[][] lh=c2q(yh16,gainMask16);

            //Vertical pair
            Complex[][][] yh34=new Complex[2][this.Yh.get(currentLevel-1)[0].length][this.Yh.get(0)[0][0].length];
            yh34[0]=Yh.get(currentLevel-1)[2];
            yh34[1]=Yh.get(currentLevel-1)[3];
            int[] gainMask34=new int[2];
            gainMask34[0]=gainMask[2][currentLevel-1];
            gainMask34[1]=gainMask[3][currentLevel-1];
            double[][] hl=c2q(yh34,gainMask34);

            //Diagonal pair
            Complex[][][] yh25=new Complex[2][this.Yh.get(currentLevel-1)[0].length][this.Yh.get(0)[0][0].length];
            yh25[0]=Yh.get(currentLevel-1)[1];
            yh25[1]=Yh.get(currentLevel-1)[4];
            int[] gainMask25=new int[2];
            gainMask25[0]=gainMask[1][currentLevel-1];
            gainMask25[1]=gainMask[4][currentLevel-1];
            double[][] hh=c2q(yh25,gainMask25);

            //Do odd top-level filters on columns.
            Colfilter colfiltery11=new Colfilter(this.reconstructedImage,g0o);
            Colfilter colfiltery12=new Colfilter(lh,g1o);
            colfiltery11.colfilter();
            colfiltery12.colfilter();
            double[][] y1=new double[colfiltery11.Y.length][colfiltery11.Y[0].length];
            for (int i=0;i<colfiltery11.Y.length;i++){
                for (int j=0;j<colfiltery11.Y[0].length;j++){
                    y1[i][j]=colfiltery11.Y[i][j]+colfiltery12.Y[i][j];
                }
            }
            Colfilter colfiltery21=new Colfilter(hl,g0o);
            Colfilter colfiltery22=new Colfilter(hh,g1o);
            colfiltery21.colfilter();
            colfiltery22.colfilter();
            double[][] y2=new double[colfiltery21.Y.length][colfiltery21.Y[0].length];
            for (int i=0;i<colfiltery21.Y.length;i++){
                for (int j=0;j<colfiltery21.Y[0].length;j++){
                    y2[i][j]=colfiltery21.Y[i][j]+colfiltery22.Y[i][j];
                }
            }

            //Do odd top-level filters on rows.
            y1=transPosition1(y1);
            y2=transPosition1(y2);
            Colfilter colfiltery31=new Colfilter(y1,g0o);
            Colfilter colfiltery32=new Colfilter(y2,g1o);
            colfiltery31.colfilter();
            colfiltery32.colfilter();
            double[][] z=new double[colfiltery31.Y.length][colfiltery31.Y[0].length];
            for (int i=0;i<colfiltery31.Y.length;i++){
                for (int j=0;j<colfiltery31.Y[0].length;j++){
                    z[i][j]=colfiltery31.Y[i][j]+colfiltery32.Y[i][j];
                }
            }
            this.reconstructedImage =transPosition1(z);
        }
        return this.reconstructedImage;                                                                                 //Reconstructed image after inverse wavelet transform
    }


    /**
     * Scale by gain and convert from complex w(:,:,1:2) to real quad-numbers in z.
     * Arrange pixels from the real and imag parts of the 2 subbands into 4 separate subimages.
     * A----B     Re   Im of w(:,:,1)
     * |    |
     * C----D     Re   Im of w(:,:,2)
     * @param gain Gain matrix to be applied to each subband.
     *             w Input matrix
     */
    public double[][] c2q(Complex[][][] w,int[] gain){
        int sizew1=w.length;
        int sizew2=w[0].length;
        int sizew3=w[0][0].length;
         double[][] x=new double[sizew2*2][sizew3*2];
        if(gain.length>0&&sizew1>0){
            double[] sc=new double[gain.length];
            for (int i=0;i<gain.length;i++){
                sc[i]= Math.sqrt(0.5)*gain[i];
            }
            Complex[][] p=new Complex[w[0].length][w[0][0].length];
            Complex[][] q=new Complex[w[0].length][w[0][0].length];
            for (int i=0;i<w[0].length;i++){
                for (int j=0;j<w[0][0].length;j++){
                    p[i][j]=(w[0][i][j].multy(sc[0])).plus(w[1][i][j].multy(sc[1]));
                    q[i][j]=(w[0][i][j].multy(sc[0])).minus(w[1][i][j].multy(sc[1]));
                }
            }
            ArrayList<Integer> t1=new ArrayList<>();                                                                    //Coordinate vector
            ArrayList<Integer> t2=new ArrayList<>();                                                                    //Coordinate vector
            for (int i=0;2*i<x.length;++i){
                t1.add(2*i);
            }
            for (int i=0;2*i<x[0].length;++i){
                t2.add(2*i);
            }

            //Form the 4 subbands in x
            for (int i=0;i<t1.size();i++){
                for (int j=0;j<t2.size();j++){
                    x[t1.get(i)][t2.get(j)]=p[i][j].getRe();
                    x[t1.get(i)][t2.get(j)+1]=p[i][j].getIm();
                    x[t1.get(i)+1][t2.get(j)]=q[i][j].getIm();
                    x[t1.get(i)+1][t2.get(j)+1]=-q[i][j].getRe();
                }
            }
        }
        return x;
    }

    /**
     * Function to Transpose the matrix
     * @return
     *matrix after transpose
     */
    public double[][] transPosition1(double[][] matrix){

        int matRows=matrix.length;
        int matColes=matrix[0].length;
        double[][] aChange=new double[matColes][matRows];
        for (int i=0;i<matRows;i++){
            for(int j=0;j<matColes;j++){
                aChange[j][i]=matrix[i][j];
            }
        }
        return aChange;
    }
}
