package com.media.watermark;

/*reshape the input matrix
 @Time 2020-04-20 14:49:01
 */
public class Reshape {

    /**
     * The input two-dimensional array is remade according to the size of the input
     * @param matrix 2-D matrix
     * @param row the rows of the matrix after reshape
     * @param coles the coles of the matrix after reshape
     */
    public  static double[][] reshape1(double[][] matrix,int row,int coles){
        if(matrix == null){
            System.out.println("the matrix is null at bitimgGen reshape");
            System.exit(0);
            return matrix;
        }
        if(row == 0 || coles == 0){
            System.out.println("the matrix is null at bitimgGen reshape");
            System.exit(0);
            return matrix;
        }
        int rows = matrix.length;
        int columns = matrix[0].length;

        if(columns * rows != row * coles){
            System.out.println("the matrix size is not equal to r*c at bitimgGen reshape");
            System.exit(0);
            return matrix;
        }
        double [][]result = new double[row][coles];                                                                     //reshaped matrix
        int rr = 0;
        int cc = 0;
        int index = 0;
        for(int i = 0; i < rows; ++i){
            for(int j = 0; j < columns;++j){
                index = i * columns + j;

                rr = index / row;                                                                                       //row-coordinate
                cc = index % coles;                                                                                     //cole-coordinate

                result[rr][cc] = matrix[i][j];
            }
        }
        return result;
    }

    /**
     * The input one-dimensional array is remade according to the size of the input
     * @param matrix 2-D matrix
     * @param row the rows of the matrix after reshape
     * @param cole the coles of the matrix after reshape
     */
    public static double[][] reshape1(double[] matrix,int row,int cole){
        if(matrix == null){
            System.out.println("the matrix is null at bitimgGen reshape");
            System.exit(0);
            return null;
        }

        int length = matrix.length;

        if(length != row * cole){
            System.out.println("the matrix size is not equal to r*c at bitimgGen reshape");
            System.exit(0);
            return null;
        }
        double [][]result = new double[row][cole];                                                                      //reshaped matrix;
        int rr = 0;
        int cc = 0;
        int index = 0;
        for(int i = 0; i < length; ++i){
            index = i;
            rr = index / row;                                                                                           //row-coordinate
            cc = index % cole;                                                                                          //cole-coordinate

            result[rr][cc] = matrix[i];
        }
        return result;
    }
    /**
     * Function to Transpose the matrix
     * @param matrix Input 2-D matrix
     * @return
     *matrix after transpose
     */
    public static double[][] transposition(double[][] matrix){
        int r=matrix.length;
        int c=matrix[0].length;
        double[][] result=new double[c][r];
        for (int i=0;i<r;++i){
            for (int j=0;j<c;++j){
                result[j][i]=matrix[i][j];
            }
        }
        return result;
    }

}
