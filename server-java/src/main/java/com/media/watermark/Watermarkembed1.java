package com.media.watermark;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
/**
 * Embed the watermark sequence into the image
 * @Time 2020-04-20 14:49:01
 */
public class Watermarkembed1 {

    private BufferedImage coverImg;
    private ArrayList<double[]> mark;

    public Watermarkembed1(BufferedImage coverImg1,ArrayList<double[]> b){
        this.coverImg=coverImg1;
        this.mark=b;
    }

    public BufferedImage getCoverImg() {
        return coverImg;
    }

    public ArrayList<double[]> getMark() {
        return mark;
    }

    /**Add watermark information and location information in the two channels of the image
     * @param biort filter
     * @param qShift filter
     * @param markImg Watermark image matrix
     * @param nLevels The number of layers of the wavelet transform
     * @param pattern zone bit
     * @param Ug Frequency domain image sequence of random image after ring division
     * @param a blocks
     * @param b blocks
     * @param rr The number of rows in a pixel block
     * @param cc The number of coles in a pixel block
     * @param minradies Inner ring radius
     * @return Generated image matrix sequence
     */
    public static Watermarkembed1 waterMarkEmbed1(int nLevels, String biort, String qShift, BufferedImage BI, int rr, int cc, int a, int b, Complex[][] markImg, double[][] Ug,double minradies,int pattern,int k) throws IOException {
        //File file1 = new File(file);
        //BufferedImage BI  = ImageIO.read(file1);                                                                        //Reads the image matrix for the specified path
        int mc = BI.getWidth();
        int mr = BI.getHeight();
        ArrayList<double[]> mark=new ArrayList<>();

        int[][][] corner1=new int[a][b][2];                                                                             //Coordinate arrays on corner 1
        int[][][] corner2=new int[a][b][2];                                                                             //Coordinate arrays on corner 2
        int[][][] corner3=new int[a][b][2];                                                                             //Coordinate arrays on corner 3
        int[][][] corner4=new int[a][b][2];                                                                             //Coordinate arrays on corner 4

        //Gets the coordinates of the watermark block at each corner
        for (int i=0;i<a;++i){
            for (int j=0;j<b;++j){
                corner1[i][j][0]=rr-k+(i)*rr*2;
                corner1[i][j][1]=cc-k+(j)*cc*2;
                corner2[i][j][0]=mr-rr*2*(i+1)+k;
                corner2[i][j][1]=rr-k+(j)*cc*2;
                corner3[i][j][0]=mr-rr*2*(i+1)+k;
                corner3[i][j][1]=mc-cc*2*(j+1)+k;
                corner4[i][j][0]=rr-k+(i)*rr*2;
                corner4[i][j][1]=mc-cc*2*(j+1)+k;
            }
        }

        //Add a watermark to the watermark block
        for (int i=0;i<a;++i){
            for (int j=0;j<b;++j){
                int[] x=new int[4];                                                                                     //Abscissa sequence
                int[] y=new int[4];                                                                                     //Ordinate sequence
                x[0]=corner1[i][j][0];y[0]=corner1[i][j][1];
                x[1]=corner2[i][j][0];y[1]=corner2[i][j][1];
                x[2]=corner3[i][j][0];y[2]=corner3[i][j][1];
                x[3]=corner4[i][j][0];y[3]=corner4[i][j][1];

                //Gets the corresponding matrix block
                for (int l=0;l<4;++l){
                    int[][][] coverImg=new int[rr][cc][3];
                    for(int m=0;m<rr;++m){
                        for(int n=0;n<cc;++n){
                            int pixel = BI.getRGB(y[l]+n, x[l]+m);
                            coverImg[m][n][0]=(pixel & 0xff0000) >> 16;
                            coverImg[m][n][1]=(pixel & 0xff00) >> 8;
                            coverImg[m][n][2]=(pixel & 0xff);
                        }
                    }

                    //rotation matrix
                    coverImg=rotateImage2(coverImg,360-l*90);
                    //Embed the watermark
                    Treatfirst treatfirst=Treatfirst.treatfirst(coverImg,nLevels,biort,qShift,markImg,Ug,minradies,pattern);
                    mark.add(treatfirst.getZeromark());                                                                 //Zero watermark sequence
                    coverImg=treatfirst.getCoverImg();                                                                  //Matrix after embedding watermark image
                    //rotation matrix
                    coverImg=rotateImage2(coverImg,l*90);

                    //Overwrite the original data of the image
                    for(int m=0;m<rr;++m){
                        for(int n=0;n<cc;++n){
                            if(coverImg[m][n][0]>255){
                                coverImg[m][n][0]=255;
                            }else if(coverImg[m][n][0]<0){
                                coverImg[m][n][0]=0;
                            }
                            if(coverImg[m][n][1]>255){
                                coverImg[m][n][1]=255;
                            }else if(coverImg[m][n][1]<0){
                                coverImg[m][n][1]=0;
                            }
                            if(coverImg[m][n][2]>255){
                                coverImg[m][n][2]=255;
                            }else if(coverImg[m][n][2]<0){
                                coverImg[m][n][2]=0;
                            }
                            Color color = new Color(coverImg[m][n][0],coverImg[m][n][1],coverImg[m][n][2]);
                            BI.setRGB(y[l]+n,x[l]+m, color.getRGB());
                        }
                    }
                }
            }
        }
        return new Watermarkembed1(BI,mark);
    }

    /**
     * Function to Transpose the matrix
     * @param matrix Input 2-D matrix
     * @param degree Rotation Angle
     */
    public static int[][][] rotateImage2(int[][][] matrix,int degree) {
        int mr=matrix.length;
        int mc=matrix[0].length;
        if(degree==0||degree==360){
            return matrix;
        }else if(degree==90){
            int[][][] c=new int[mc][mr][3];
            for (int i=0;i<mr;++i){
                for (int j=0;j<mc;++j){
                    c[mc-1-j][i][0]=matrix[i][j][0];
                    c[mc-1-j][i][1]=matrix[i][j][1];
                    c[mc-1-j][i][2]=matrix[i][j][2];
                }
            }
            return c;
        }else if(degree==180){
            int[][][] c=new int[mr][mc][3];
            for (int i=0;i<mr;++i){
                for (int j=0;j<mc;++j){
                    c[mr-1-i][mc-j-1][0]=matrix[i][j][0];
                    c[mr-1-i][mc-j-1][1]=matrix[i][j][1];
                    c[mr-1-i][mc-j-1][2]=matrix[i][j][2];
                }
            }
            return c;
        }else if (degree==270){
            int[][][] c=new int[mc][mr][3];
            int[][][] d=new int[mc][mr][3];
            for (int i=0;i<mr;++i){
                for (int j=0;j<mc;++j){
                    c[mc-1-j][i][0]=matrix[i][j][0];
                    c[mc-1-j][i][1]=matrix[i][j][1];
                    c[mc-1-j][i][2]=matrix[i][j][2];
                }
            }
            for (int i=0;i<mc;++i){
                for (int j=0;j<mr;++j){
                    d[mr-1-i][mc-j-1][0]=c[i][j][0];
                    d[mr-1-i][mc-j-1][1]=c[i][j][1];
                    d[mr-1-i][mc-j-1][2]=c[i][j][2];
                }
            }
            return d;
        }else{
            return matrix;
        }
    }
};




