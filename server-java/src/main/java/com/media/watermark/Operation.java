package com.media.watermark;

/**
 *Functions related to matrix operations
 * @Time 2020-04-20 14:49:01
 */
public class Operation {
    /**
     * Calculate the average value of the input matrix and return the result
     *
     * @param  matrix Input 2-D matrix
     * @return mean
     */
    public static double mean(double[][] matrix){
        int m=matrix.length;
        int n=matrix[0].length;
        if(m>0){
            int sum=0;
            for (int i=0;i<m;++i){
                for (int j=0;j<n;++j){
                    sum+=matrix[i][j];
                }
            }
            return sum/(m*n);
        }else{
            System.out.println("the matrix is null. at bitimgGen max");
            System.exit(0);
            return 0;
        }
    }
    /**
     * Calculate the maxinum value of the input matrix and return the result
     *
     * @param  matrix Input 2-D matrix
     * @return max
     */
    public static double max(double[][] matrix){
        int m=matrix.length;
        int n=matrix[0].length;
        if (m>0){
            double max=matrix[0][0];
            for (int i=0;i<m;++i){
                for (int j=0;j<n;++j){
                    if(max<matrix[i][j]){
                        max=matrix[i][j];
                    }
                }
            }
            return max;
        }else{
            System.out.println("the matrix is null. at bitimgGen max");
            System.exit(0);
            return 0;
        }
    }
    /**
     * Calculate the mininum value of the input matrix and return the result
     *
     * @param  matrix Input 2-D matrix
     * @return max
     */
    public static double min(double[][] matrix){
        int m=matrix.length;
        int n=matrix[0].length;
        if (m>0){
            double min=matrix[0][0];
            for (int i=0;i<m;++i){
                for (int j=0;j<n;++j){
                    if(min>matrix[i][j]){
                        min=matrix[i][j];
                    }
                }
            }
            return min;
        }else{
            System.out.println("the matrix is null. at bitimgGen max");
            System.exit(0);
            return 0;
        }
    }
}
