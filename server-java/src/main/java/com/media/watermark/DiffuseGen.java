package com.media.watermark;

/**Function to Compute the convolution of the input matrix with the convolution kernel vector
 *@Time 2020-04-20 14:49:01
 */
public class DiffuseGen {
    private double sum;
    private double[][][] sumw;

    public DiffuseGen(double sum1, double[][][] sumw1){
        this.sum=sum1;
        this.sumw=sumw1;
    }
    /**Function to Compute the convolution of the input matrix with the convolution kernel vector
     * @param avg A sequence of pixel block averages
     * @param maxv A sequence of pixel block maxima
     * @param minv A sequence of minimum values of a pixel block
     * @param v Original pixel block sequence
     * @param prority Priority coefficient
     * @param sum diffusion coefficient;
     * @param sumw weighted sum
     */
    public static DiffuseGen diffusegen(int x, int y, int xm, int ym, int[][] prority, double[] avg, double[] maxv, double[] minv, double[][][] v, double sum, double[][][] sumw, double w, double[][][] e, double[][][] t){
        if(prority[xm][ym]>prority[x][y]){
            sum+=w;                                                                                                     //diffusion coefficient
        }
        for (int i=0;i<v[0][0].length;++i){
            if (v[xm][ym][i]>avg[i]){
                t[xm][ym][i]=maxv[i];
            }else{
                t[xm][ym][i]=minv[i];
            }
        }

        for (int i=0;i<e[0][0].length;++i){
            e[xm][ym][i]=v[xm][ym][i]-t[xm][ym][i];
            sumw[xm][ym][i]=sumw[xm][ym][i]+(e[xm][ym][i]*w);                                                           //The weighted sum in eight
        }
        return new DiffuseGen(sum,sumw);
    }

    public double getSum(){
        return this.sum;
    }

    public double[][][] getSumw() {
        return sumw;
    }
}
