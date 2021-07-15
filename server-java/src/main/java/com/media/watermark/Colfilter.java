package com.media.watermark;

/**
 * Filter the columns of image using filter vector , without decimation.
 * @Time 2020-04-20 14:49:01
 */

public class Colfilter {

    public double[][] X;                                                                                                //Input image matrix
    public double[] h;                                                                                                  //convolution kernel
    public double[][] Y;                                                                                                //The filtered matrix
    public double[][] Ychange;                                                                                          //The matrix y after transpose
    public int[] sample;                                                                                                //sample matrix
    public int[] xe;                                                                                                    //The expanded matrix
    public double[][] xChange;                                                                                          //The matrix x after transpose

    /*
     * @param
     * X             2D matrix
     * h             filter vector
     */
    public Colfilter(double[][] X, double[] h){
        this.X=X;
        this.h=h;
    }

    /**
     * Filter the columns of image using filter vector , without decimation.
     *@return The filtered matrix
     * */
    public double[][] colfilter(){

        int r=this.X.length;                                                                                            //The number of rows in the matrix x
        int c=this.X[0].length;                                                                                         //The number of coles in the matrix x
        int m=h.length;                                                                                                 //The length of the convolution kernel vector
        int m2=(int) Math.floor((double)(m/2));
        sample =new int[r+m2*2];                                                                                        //Extended input matrix
        if (X!=null){
            int i=1-m2;
            int k=0;
            while(i<=r+m2){
                sample[k]=i;
                ++k;
                ++i;
            }

            //symmetrically extend with repeat of end samples.
            Reflect reflect=new Reflect(sample,0.5,r+0.5);
            xe=new int[r+m2*2];
            xe=reflect.reflect();
            Change change=new Change(xe,X);
            xChange=change.change();

            /*Perform filtering on the columns of the extended matrix X(xe,:),
            keeping only the 'valid' output samples, so Y is the same size as
            X if m is odd.
             */
           Conv2 conv2=new Conv2(xChange,h);
           this.Y=new double[X.length][X[0].length];
           this.Y=conv2.conv2();
        }
        else{
            Y=new double[r+1-m%2][c];
        }
        return this.Y;                                                                                                  //The filtered matrix
    }

    /**
     * Transpose the input matrix
     * return The matrix after transpose
     * */
    public double[][] transPosition(){
        int mm=this.Y.length;                                                                                           //The number of rows in the matrix x
        int cc=this.Y[0].length;                                                                                        //The number of rows in the matrix x
        this.Ychange=new double[cc][mm];
        for (int i=0;i<mm;i++){
            for(int j=0;j<cc;j++){
                this.Ychange[j][i]=this.Y[i][j];
            }
        }
        return this.Ychange;                                                                                            //The matrix after transpose
    }


}
