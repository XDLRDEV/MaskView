package com.xdlr.maskview.watermark;



import java.util.ArrayList;

public class Dtwaveifm2 {
    private double[][] Yl;
    private ArrayList<Complex[][][]> Yh;
    private String biort;
    private String qShift;
    private double[][] z;
    private ArrayList<double[][]> rz=new ArrayList<>();


    private double[] g0a={0.00243034994514868,-0.000227652205897772,-0.00720267787825835,0.0184986827241562,0.0222892632669227,-0.112558884257522,0.0146374059644734,0.567134484100133,0.756393765199037,0.276368643133032,-0.115911457427441,-0.0372738957998980,0.0349146123068422,-7.14419732796501e-05,-0.000446022789262285,-0.00476161193845591};
    private double[] g0b={-0.00476161193845591,-0.000446022789262285,-7.14419732796501e-05,0.0349146123068422,-0.0372738957998980,-0.115911457427441,0.276368643133032,0.756393765199037,0.567134484100133,0.0146374059644734,-0.112558884257522,0.0222892632669227,0.0184986827241562,-0.00720267787825835,-0.000227652205897772,0.00243034994514868};
    private double[] g0o={0.250000000000000,0.500000000000000,0.250000000000000};
    private double[] g1a={0.00476161193845591,-0.000446022789262285,7.14419732796501e-05,0.0349146123068422,0.0372738957998980,-0.115911457427441,-0.276368643133032,0.756393765199037,-0.567134484100133,0.0146374059644734,0.112558884257522,0.0222892632669227,-0.0184986827241562,-0.00720267787825835,0.000227652205897772,0.00243034994514868};
    private double[] g1b={0.00243034994514868,0.000227652205897772,-0.00720267787825835,-0.0184986827241562,0.0222892632669227,0.112558884257522,0.0146374059644734,-0.567134484100133,0.756393765199037,-0.276368643133032,-0.115911457427441,0.0372738957998980,0.0349146123068422,7.14419732796501e-05,-0.000446022789262285,0.00476161193845591};
    private double[] g1o={-0.125000000000000,-0.250000000000000,0.750000000000000,-0.250000000000000,-0.125000000000000};
    private double[] h0a={-0.00476161193845591,-0.000446022789262285,-7.14419732796501e-05,0.0349146123068422,-0.0372738957998980,-0.115911457427441,0.276368643133032,0.756393765199037,0.567134484100133,0.0146374059644734,-0.112558884257522,0.0222892632669227,0.0184986827241562,-0.00720267787825835,-0.000227652205897772,0.00243034994514868};
    private double[] h0b={0.00243034994514868,-0.000227652205897772,-0.00720267787825835,0.0184986827241562,0.0222892632669227,-0.112558884257522,0.0146374059644734,0.567134484100133,0.756393765199037,0.276368643133032,-0.115911457427441,-0.0372738957998980,0.0349146123068422,-7.14419732796501e-05,-0.000446022789262285,-0.00476161193845591};
    private double[] h0o={-0.125000000000000,0.250000000000000,0.750000000000000,0.250000000000000,-0.125000000000000};
    private double[] h1a={0.00243034994514868,0.000227652205897772,-0.00720267787825835,-0.0184986827241562,0.0222892632669227,0.112558884257522,0.0146374059644734,-0.567134484100133,0.756393765199037,-0.276368643133032,-0.115911457427441,0.0372738957998980,0.0349146123068422,7.14419732796501e-05,-0.000446022789262285,0.00476161193845591};
    private double[] h1b={0.00476161193845591,-0.000446022789262285,7.14419732796501e-05,0.0349146123068422,0.0372738957998980,-0.115911457427441,-0.276368643133032,0.756393765199037,-0.567134484100133,0.0146374059644734,0.112558884257522,0.0222892632669227,-0.0184986827241562,-0.00720267787825835,0.000227652205897772,0.00243034994514868};
    private double[] h1o={-0.250000000000000,0.500000000000000,-0.250000000000000};

    public Dtwaveifm2(double[][] Pl, ArrayList<Complex[][][]> Ph, String biort, String qShift){
        this.biort=biort;
        this.Yh=Ph;
        this.Yl=Pl;
        this.qShift=qShift;
    }

    public double[][] dtWaveifm2(){
        int a=Yh.size();
        int currentLevel=a;
        int[][] gainMask=new int[6][a];
        for (int i=0;i<gainMask.length;i++){
            for (int j=0;j<gainMask[0].length;j++){
                gainMask[i][j]=1;
            }
        }
        this.z=Yl;
        while (currentLevel>=2) {
            Complex[][][] yh16 = new Complex[2][this.Yh.get(currentLevel - 1)[0].length][this.Yh.get(currentLevel-1)[0][0].length];
            yh16[0] = Yh.get(currentLevel - 1)[0];
            yh16[1] = Yh.get(currentLevel - 1)[5];
            int[] gainMask16 = new int[2];
            gainMask16[0] = gainMask[0][currentLevel-1];
            gainMask16[1] = gainMask[5][currentLevel-1];
            double[][] lh = new double[yh16[0].length*2][yh16[0][0].length*2];
            lh=c2q(yh16, gainMask16);

            Complex[][][] yh34 = new Complex[2][this.Yh.get(currentLevel - 1)[0].length][this.Yh.get(currentLevel-1)[0][0].length];
            yh34[0] = Yh.get(currentLevel - 1)[2];
            yh34[1] = Yh.get(currentLevel - 1)[3];
            int[] gainMask34 = new int[2];
            gainMask34[0] = gainMask[2][currentLevel-1];
            gainMask34[1] = gainMask[3][currentLevel-1];
            double[][] hl = new double[yh34[0].length][yh34[0][0].length];
            hl = this.c2q(yh34, gainMask34);

            Complex[][][] yh25 = new Complex[2][this.Yh.get(currentLevel - 1)[0].length][this.Yh.get(currentLevel-1)[0][0].length];
            yh25[0] = Yh.get(currentLevel - 1)[1];
            yh25[1] = Yh.get(currentLevel - 1)[4];
            int[] gainMask25 = new int[2];
            gainMask25[0] = gainMask[1][currentLevel-1];
            gainMask25[1] = gainMask[4][currentLevel-1];
            double[][] hh = new double[yh25[0].length][yh25[0][0].length];
            hh = this.c2q(yh25, gainMask25);


            Colifilt colifilty11 = new Colifilt(g0b, g0a, this.z);
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


            y1 = transPosition1(y1);
            y2 = transPosition1(y2);
            Colifilt colifilty31 = new Colifilt(g0b, g0a, y1);
            Colifilt colifilty32 = new Colifilt(g1b, g1a, y2);
            colifilty31.coliFilt();
            colifilty32.coliFilt();
            //coldfilty31.transPosition();
            //coldfilty32.transPosition();
            double[][]z = new double[colifilty31.Y.length][colifilty31.Y[0].length];
            for (int i = 0; i < colifilty31.Y.length; i++) {
                for (int j = 0; j < colifilty31.Y[0].length; j++) {
                    z[i][j] = colifilty31.Y[i][j] + colifilty32.Y[i][j];
                }
            }
            this.z=z;
            this.z = transPosition1(this.z);
            int rowSize = this.z.length;
            int colSize = this.z[0].length;
            int s1 = 2*Yh.get(currentLevel - 2)[0].length;
            int s2 = 2*Yh.get(currentLevel - 2)[0][0].length;
            if (rowSize != s1) {
                double[][] Z = new double[this.z.length - 2][this.z[0].length];
                for (int i = 0; i < Z.length; i++) {
                    for (int j = 0; j < Z[0].length; j++) {
                        Z[i][j] = this.z[i + 1][j];
                    }
                }
                this.z = Z;
            }
            if (colSize != s2) {
                double[][] Z = new double[this.z.length][this.z[0].length - 2];
                for (int i = 0; i < Z.length; i++) {
                    for (int j = 0; j < Z[0].length; j++) {
                        Z[i][j] = z[i][j + 1];
                    }
                }
                this.z = Z;
            }
            if (this.z.length != s1 || this.z[0].length != s2) {
                System.out.println("Sizes of subbands are not valid for DTWAVEIFM2");
                System.exit(0);
            }
            currentLevel = currentLevel - 1;
        }

        if(currentLevel==1){
            Complex[][][] yh16=new Complex[2][this.Yh.get(currentLevel-1)[0].length][this.Yh.get(0)[0][0].length];
            yh16[0]=Yh.get(currentLevel-1)[0];
            yh16[1]=Yh.get(currentLevel-1)[5];
            int[] gainMask16=new int[2];
            gainMask16[0]=gainMask[0][currentLevel-1];
            gainMask16[1]=gainMask[5][currentLevel-1];
            double[][] lh = new double[yh16[0].length*2][yh16[0][0].length*2];
            lh=c2q(yh16,gainMask16);

            Complex[][][] yh34=new Complex[2][this.Yh.get(currentLevel-1)[0].length][this.Yh.get(0)[0][0].length];
            yh34[0]=Yh.get(currentLevel-1)[2];
            yh34[1]=Yh.get(currentLevel-1)[3];
            int[] gainMask34=new int[2];
            gainMask34[0]=gainMask[2][currentLevel-1];
            gainMask34[1]=gainMask[3][currentLevel-1];
            double[][] hl = new double[yh34[0].length*2][yh34[0][0].length*2];
            hl=c2q(yh34,gainMask34);

            Complex[][][] yh25=new Complex[2][this.Yh.get(currentLevel-1)[0].length][this.Yh.get(0)[0][0].length];
            yh25[0]=Yh.get(currentLevel-1)[1];
            yh25[1]=Yh.get(currentLevel-1)[4];
            int[] gainMask25=new int[2];
            gainMask25[0]=gainMask[1][currentLevel-1];
            gainMask25[1]=gainMask[4][currentLevel-1];
            double[][] hh = new double[yh25[0].length*2][yh25[0][0].length*2];
            hh=c2q(yh25,gainMask25);


            Colfilter colfiltery11=new Colfilter(this.z,g0o);
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


            y1=transPosition1(y1);
            y2=transPosition1(y2);
            Colfilter colfiltery31=new Colfilter(y1,g0o);
            Colfilter colfiltery32=new Colfilter(y2,g1o);
            colfiltery31.colfilter();
            colfiltery32.colfilter();
            //colfiltery31.transPosition();
            //colfiltery32.transPosition();
            double[][] z=new double[colfiltery31.Y.length][colfiltery31.Y[0].length];
            for (int i=0;i<colfiltery31.Y.length;i++){
                for (int j=0;j<colfiltery31.Y[0].length;j++){
                    z[i][j]=colfiltery31.Y[i][j]+colfiltery32.Y[i][j];
                }
            }
            this.z=transPosition1(z);
        }
        return this.z;
    }

    public double[][] c2q(Complex[][][] w, int[] gain){
        int sw1=w.length;
        int sw2=w[0].length;
        int sw3=w[0][0].length;
         double[][] x=new double[sw2*2][sw3*2];
        if(gain.length>0&&sw1>0){
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
            ArrayList<Integer> t1=new ArrayList<>();
            ArrayList<Integer> t2=new ArrayList<>();
            for (int i=0;2*i<x.length;++i){
                t1.add(2*i);
            }
            for (int i=0;2*i<x[0].length;++i){
                t2.add(2*i);
            }
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


    public double[][] transPosition1(double[][] a){

        int mm=a.length;
        int cc=a[0].length;
        double[][] aChange=new double[cc][mm];
        for (int i=0;i<mm;i++){
            for(int j=0;j<cc;j++){
                aChange[j][i]=a[i][j];
            }
        }
        return aChange;
    }
}
