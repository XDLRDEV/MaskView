package com.xdlr.maskview.watermark;

import java.util.ArrayList;

public class Dtwavexfm2 {
    private double[][] Ug;
    private int nLevels;
    private String biort;
    private String qShift;
    private ArrayList<Complex[][][] > Yh=new ArrayList<>();
    private double[][] Yl;

    private double[] h0a={-0.00476161193845591,-0.000446022789262285,-7.14419732796501e-05,0.0349146123068422,-0.0372738957998980,-0.115911457427441,0.276368643133032,0.756393765199037,0.567134484100133,0.0146374059644734,-0.112558884257522,0.0222892632669227,0.0184986827241562,-0.00720267787825835,-0.000227652205897772,0.00243034994514868};
    private double[] h0b={0.00243034994514868,-0.000227652205897772,-0.00720267787825835,0.0184986827241562,0.0222892632669227,-0.112558884257522,0.0146374059644734,0.567134484100133,0.756393765199037,0.276368643133032,-0.115911457427441,-0.0372738957998980,0.0349146123068422,-7.14419732796501e-05,-0.000446022789262285,-0.00476161193845591};
    private double[] h0o={-0.125000000000000,0.250000000000000,0.750000000000000,0.250000000000000,-0.125000000000000};
    private double[] h1a={0.00243034994514868,0.000227652205897772,-0.00720267787825835,-0.0184986827241562,0.0222892632669227,0.112558884257522,0.0146374059644734,-0.567134484100133,0.756393765199037,-0.276368643133032,-0.115911457427441,0.0372738957998980,0.0349146123068422,7.14419732796501e-05,-0.000446022789262285,0.00476161193845591};
    private double[] h1b={0.00476161193845591,-0.000446022789262285,7.14419732796501e-05,0.0349146123068422,0.0372738957998980,-0.115911457427441,-0.276368643133032,0.756393765199037,-0.567134484100133,0.0146374059644734,0.112558884257522,0.0222892632669227,-0.0184986827241562,-0.00720267787825835,0.000227652205897772,0.00243034994514868};
    private double[] h1o={-0.250000000000000,0.500000000000000,-0.250000000000000};
    private double[][] Lo;
    private double[][] Hi;
    private double[][] LoLo;

    public Dtwavexfm2(double[][] Ug, int nLevels, String biort, String qShift){
        this.Ug=Ug;
        this.nLevels=nLevels;
        this.biort=biort;
        this.qShift=qShift;
    }



    public ArrayList<Complex[][][] > getYh(){

        return this.Yh;
    }

    public double[][] getYl(){

        return this.Yl;
    }

    public Dtwavexfm2 dtwavexfm2() {

        //S = [];


        if (nLevels >= 1) {
            Colfilter lO = new Colfilter(this.Ug, h0o);
            Lo = lO.colfilter();
            Lo = lO.transPosition();

            Colfilter hi = new Colfilter(this.Ug, h1o);
            Hi = hi.colfilter();
            Hi = hi.transPosition();

            Colfilter lolo = new Colfilter(Lo, h0o);
            LoLo = lolo.colfilter();
            LoLo = lolo.transPosition();

            Complex[][][] aaa = new Complex[6][(int) Math.floor(LoLo.length / 2)][(int) Math.floor(LoLo[0].length / 2)];
            Complex[][][] bbb;

            Colfilter yh116 = new Colfilter(Hi, h0o);
            yh116.colfilter();
            bbb = q2c(yh116.transPosition());

            aaa[0] = bbb[0];
            aaa[5] = bbb[1];

            Colfilter yh134 = new Colfilter(Lo, h1o);
            yh134.colfilter();
            bbb = q2c(yh134.transPosition());

            aaa[2] = bbb[0];
            aaa[3] = bbb[1];

            Colfilter yh125 = new Colfilter(Hi, h1o);
            yh125.colfilter();
            bbb = q2c(yh125.transPosition());

            aaa[1] = bbb[0];
            aaa[4] = bbb[1];

            this.Yh.add(aaa);
        }

        if (nLevels >= 2) {
            for (int level = 2; level <= nLevels; level++) {

                int rowSize = LoLo.length;
                int coleSize = LoLo[0].length;

                Coldfilt coldfilt1 = new Coldfilt(h0b, h0a, LoLo);
                Coldfilt coldfilt2 = new Coldfilt(h1b, h1a, LoLo);
                Lo = coldfilt1.coldFilt();
                Hi = coldfilt2.coldFilt();
                Lo = coldfilt1.transPosition();
                Hi = coldfilt2.transPosition();

                Coldfilt coldfiltLo = new Coldfilt(h0b, h0a, Lo);
                LoLo = coldfiltLo.coldFilt();
                LoLo = coldfiltLo.transPosition();

                Complex[][][] aaa = new Complex[6][LoLo.length][LoLo[0].length];
                Complex[][][] bbb;

                Coldfilt coldfiltY16 = new Coldfilt(h0b, h0a, Hi);
                coldfiltY16.coldFilt();
                bbb = q2c(coldfiltY16.transPosition());
                aaa[0] = bbb[0];
                aaa[5] = bbb[1];

                Coldfilt coldfiltY34 = new Coldfilt(h1b, h1a, Lo);
                coldfiltY34.coldFilt();
                bbb = q2c(coldfiltY34.transPosition());
                aaa[2] = bbb[0];
                aaa[3] = bbb[1];

                Coldfilt coldfiltY25 = new Coldfilt(h1b, h1a, Hi);
                coldfiltY25.coldFilt();
                bbb = q2c(coldfiltY25.transPosition());
                aaa[1] = bbb[0];
                aaa[4] = bbb[1];

                Yh.add(aaa);
            }
        }
        Yl = LoLo;
        return this;
    }

    public Complex[][][] q2c(double[][] a){
        int mr=a.length;
        int mc=a[0].length;
        ArrayList<Integer> t1=new ArrayList<>();
        ArrayList<Integer> t2=new ArrayList<>();
        //ArrayList<ArrayList<Complex>> p=new ArrayList<>();
        //ArrayList<ArrayList<Complex>> q=new ArrayList<>();


        Complex[] j2=new Complex[2];
        j2[0]=new Complex(0.7071,0.0);
        j2[1]=new Complex(0.0,0.7071);
        for (int i=0;2*i<mr;i++){
            t1.add(2*i);
        }
        for (int i=0;2*i<mc;i++){
            t2.add(2*i);
        }

        /*Complex[][] z1=new Complex[t1.size()][t2.size()];
        Complex[][] z2=new Complex[t1.size()][t2.size()];
        for (int i=0;i<t1.size();i++){
            for(int j=0;j<t2.size();j++){
                z1[i][j]=(j2[0].multy((a[t1.get(i)][t2.get(j)]))).plus(j2[1].multy((a[t1.get(i)][t2.get(j)+1]))).minus((j2[0].multy((a[t1.get(i)+1][t2.get(j)+1]))).minus(j2[1].multy((a[t1.get(i)+1][t2.get(j)]))));
                z2[i][j]=(j2[0].multy((a[t1.get(i)+1][t2.get(j)+1]))).minus(j2[1].multy((a[t1.get(i)+1][t2.get(j)]))).plus((j2[0].multy((a[t1.get(i)][t2.get(j)]))).plus(j2[1].multy((a[t1.get(i)][t2.get(j)+1]))));
            }
        }
        Complex[][][] z=new Complex[2][t1.size()][t2.size()];*/
        Complex[][] p=new Complex[t1.size()][t2.size()];
        Complex[][] q=new Complex[t1.size()][t2.size()];

        for (int i=0;i<t1.size();i++){
            for(int j=0;j<t2.size();j++){
            p[i][j]=(j2[0].multy((a[t1.get(i)][t2.get(j)]))).plus(j2[1].multy((a[t1.get(i)][t2.get(j)+1])));
            q[i][j]=(j2[0].multy((a[t1.get(i)+1][t2.get(j)+1]))).minus(j2[1].multy((a[t1.get(i)+1][t2.get(j)])));
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

        z[0]=z1;
        z[1]=z2;
        return z;
    }

}

