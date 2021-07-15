package com.media.watermark;

import java.util.ArrayList;
/**
 * LBG algorithm, training vector set and a certain iterative algorithm are
 * used to approximate the optimal regenerative codebook.
 * @Time 2020-04-20 14:49:01
 */
public class Linde_Buzo_Gray {
    private double[][]img;
    public double[][]code_book;
    private int box_r,box_c,no_blocks;
    public double min_err;
    public double[][] scode_t;

    /**Based on the initial input pixel matrix, the maximum/minimum quantifier sequence generates the CHF sequence
     * @param img Input 2-D matrix
     * @param code_book The template sequence
     * @param no_blocks Block number
     * @param box_c The number of coles after matrix reconstruction
     * @param box_r The number of rows after matrix reconstruction
     *@return  this
     */
    Linde_Buzo_Gray(double[][]img,int box_r,int box_c,int no_blocks,double[][]code_book) {
        this.img=img;
        this.box_c=box_c;
        this.box_r=box_r;
        this.code_book=code_book;
        this.no_blocks=no_blocks;
        min_err=0;
    }

    /**LBG algorithm, training vector set and a certain iterative algorithm are
     * used to approximate the optimal regenerative codebook.
     */
    public double[][] LBG(){
        int max_iter= 500;                                                                                              //The largest threshold
        int min_error=0;                                                                                                //The boundary value
        int imgRow=img.length;
        int imgCole=img[0].length;
        if(box_r==1){
            double[][]blc=img;                                                                                          //Matrix initialization
            int size=blc.length*blc[0].length;
            double[][]code_t=reshape(blc,size,1);                                                                 //Matrix reconstruction
            int siz_code = box_r;
            //Reconstruct and transpose
            scode_t=trans(reshape(code_t,siz_code,size));
            double[][]scode_r=code_book;
            double err=min_error+1;
            int iter=0;
            //Train to generate a new array
            while (iter<max_iter&&err>min_error){
                iter=iter+1;
                double[][]d1=dist(scode_t,trans(scode_r));                                                              //Calculate the distance array between the two matrices
                double[][]d2=trans(d1);                                                                                 //matrix transpose
                int[] m1=min(d2);                                                                                       //The coordinate sequence of the minimum values of each column
                double[][]Nscode_r=new double[no_blocks][siz_code];
                //Generate a new training matrix
                for(int i=0;i<no_blocks;i++){
                    double[] y=mean(equal(scode_t,m1,i));
                    for(int j=0;j<siz_code;j++){
                        Nscode_r[i][j]=y[j];
                    }

                }
                err=mean(diag(dist(scode_r,trans(Nscode_r))));
                scode_r =Nscode_r;
                min_err=err;
                code_book=scode_r;
            }
        }else{
            scode_t=block(img,box_r,box_c);
            int siz_code = box_r;
            double[][]scode_r=code_book;
            double err=min_error+1;
            int iter=0;
            while (iter<max_iter&&err>min_error){
                iter=iter+1;
                double[][]d1=dist(scode_t,trans(scode_r));                                                              //Calculate the distance array between the two matrices
                double[][]d2=trans(d1);                                                                                 //matrix transpose
                int[] m1=min(d2);                                                                                       //The coordinate sequence of the minimum values of each column
                double[][]Nscode_r=new double[no_blocks][siz_code];
                //Generate a new training matrix
                for(int i=0;i<no_blocks;i++){
                    double[] y=mean(equal(scode_t,m1,i));
                    int c=y.length;
                    for(int j=0;j<siz_code;j++){
                        if(y.length==1){
                            Nscode_r[i][j]=Double.NaN;
                        }else{
                            Nscode_r[i][j]=y[j];
                        }
                    }
                }
                err=mean(diag(dist(scode_r,trans(Nscode_r))));
                scode_r =Nscode_r;
                min_err=err;
                code_book=scode_r;
            }
        }
        return code_book;
    }
    /**According to the input sequence and input constant,
     * the required values are extracted from the input matrix
     * @param matrix Input 2-D matrix
     * @param d Input 1-D matrix
     * @param i The input constant
     *@return  this
     */
    private ArrayList<double[]> equal(double[][] matrix, int[] d, int i){
        ArrayList<double[]>t=new ArrayList<>();
        for(int j=0;j<d.length;j++){
            if(d[j]==i){
                t.add(matrix[j]);
            }
        }
        return t;
    }
    /**Extract the diagonal elements of the input matrix
     * @param matrix Input 2-D matrix
     *@return  this
     */
    private double[][]diag(double[][] matrix){
        double[][]temp=new double[matrix.length][1];
        for(int i=0;i<matrix.length;i++){
            temp[i][0]=matrix[i][i];
        }
        return temp;
    }
    /**Calculate the average value of the matrix
     * @param matrix Input 2-D matrix
     *@return  this
     */
    private double mean(double[][] matrix){
        double max=0;
        for(int i=0;i<matrix.length;i++){
            max+=matrix[i][0];
        }
        return max/matrix.length;
    }
    /**Calculate the mean of the input sequence
     * @param t Input 1-D matrix sequence
     *@return  this
     */
    private double[]mean(ArrayList<double[]> t){
        int size=t.size();
        if(size==0){
            double[]temps=new double[1];
            temps[0]=Double.NaN;
            return temps;
        }
        double[]temp=new double[t.get(0).length];
        for(int k=0;k<t.get(0).length;k++){
            double sum=0;
            for(int i=0;i<size;i++){
                sum+=t.get(i)[k];
            }
            sum/=size;
            temp[k]=sum;
        }
        return temp;
    }
    /**Find the minimum value of each column in the input matrix and the corresponding number of rows
     * @param matrix Input 2-D matrix
     *@return  this
     */
    private int[] min(double[][] matrix){
        int[]temp=new int[matrix[0].length];
        for(int i=0;i<matrix[0].length;i++){
            double min=matrix[0][i];
            temp[i]=0;
            for(int j=0;j<matrix.length;j++){
                if(matrix[j][i]<min){
                    min=matrix[j][i];
                    temp[i]=j;
                }
            }
        }
        return temp;
    }
    /**Calculate the distance between two matrices
     * @param matrixa Input 2-D matrix
     * @param matrixb Input 2-D matrix
     *@return  this
     */
    private double[][] dist(double[][] matrixa,double[][] matrixb){
        double[][]temp=new double[matrixa.length][matrixb[0].length];
        for(int i=0;i<matrixa.length;i++){
            for(int j=0;j<matrixb[0].length;j++){
                double max=0;
                for(int k=0;k<matrixb.length;k++){
                    max+=(matrixa[i][k]-matrixb[k][j])*(matrixa[i][k]-matrixb[k][j]);
                }
                temp[i][j]=Math.sqrt(max);
            }
        }
        return temp;
    }
    /**Transpose the input matrix
     * @param matrix Input 2-D matrix
     *@return  this
     */
    private double[][] trans(double[][] matrix) {
        int m=matrix.length;
        int n=matrix[0].length;
        double[][] temp=new double[n][m];
        for(int i=0;i<temp.length;i++){
            for(int j=0;j<temp[0].length;j++){
                temp[i][j]=matrix[j][i];
            }
        }
        return temp;
    }
    /**Converts a one-dimensional array to a two-dimensional array
     * @param matrix Input 2-D matrix
     *@return  this
     */
    private double[][] trans(double[] matrix) {
        double[][]temp=new double[matrix.length][1];
        for(int i=0;i<temp.length;i++){
            temp[i][0]=matrix[i];
        }
        return temp;
    }
    /**The initial input matrix is reconstructed according to the size of the input
     * @param matrix Input 2-D matrix
     * @param coles The number of rows in the reconstructed matrix
     * @param rows The number of coles in the reconstructed matrix
     *@return  this
     */
    private double[][] block(double[][] matrix, int coles, int rows) {
        int x=matrix.length*matrix[0].length/coles;
        double temp[][]=new double[matrix.length*matrix[0].length/coles][coles];
        int flag=0;
        for(int i=0;i<x;i++){
            for(int j=0;j<coles;j++){
                temp[i][j]=matrix[flag%matrix.length][flag/matrix.length];
                flag++;
            }
        }
        return temp;
    }
    /**The initial input matrix is reconstructed according to the size of the input
     * @param matrix Input 2-D matrix
     * @param row The number of rows in the reconstructed matrix
     * @param cole The number of coles in the reconstructed matrix
     *@return  this
     */
    public double[][] reshape(double[][] matrix,int row,int cole){
        double[][] temp=new double[row][cole];
        int flag=0;
        for(int i=0;i<matrix[0].length;i++){
            for(int j=0;j<matrix.length;j++){
                temp[flag%row][flag/row]=matrix[j][i];
                flag++;
            }
        }
        return temp;
    }
}
