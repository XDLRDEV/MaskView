package com.media.watermark;

/**Superimpose the input watermark image onto the host image
 * @Time 2020-04-20 14:49:01
 */
public class Watermarkembed {

    private double[][] watermarkedImg;                                                                                  //The pixel matrix after superimposing watermark image information

    public Watermarkembed(double[][] img){
        this.watermarkedImg=img;
    }

    public double[][] getWatermarkedImg(){
        return this.watermarkedImg;
    }

    /**
     * @param Rc Host image matrix
     * @param markImg Watermark image matrix
     * @param minradius The radius of the innermost ring
     * @param pattern zone bit
     * @return The fusion image matrix after embedding watermark image
     */
    public static Watermarkembed watermarkembed(Complex[][] Rc,Complex[][] markImg,double minradius,int pattern){
        int rowRc=Rc.length;                                                                                            //The number of rows in the host image
        int coleRc=Rc[0].length;                                                                                        //The number of coles in the host image
        Complex[][] watermarked_Img=new Complex[rowRc][coleRc];                                                         //The superimposed image pixel matrix
        if (pattern==1){
            //Matrix overlay
            for (int i=0;i<rowRc;++i){
                for (int j=0;j<coleRc;++j){
                    if (markImg[i][j].getRe()==0&&markImg[i][j].getIm()==0){
                        watermarked_Img[i][j]=Rc[i][j].multy(1.0);
                    }else{
                        watermarked_Img[i][j]=Rc[i][j].multy(0.8).plus(markImg[i][j].multy(0.2));
                    }
                }
            }

            FourierUtils.shift_to_center(watermarked_Img);                                                              //centralization
            double[][] imgend=FourierUtils.getInverseFft(watermarked_Img,rowRc,coleRc);
            return new Watermarkembed(imgend);
        }else {
            //Matrix overlay
            for (int i=0;i<rowRc;++i){
                for (int j=0;j<coleRc;++j){
                    if (markImg[i][j].getRe()==0&&markImg[i][j].getIm()==0){
                        watermarked_Img[i][j]=Rc[i][j].multy(1.0);
                    }else{
                        watermarked_Img[i][j]=Rc[i][j].multy(0.6).plus(markImg[i][j].multy(0.4));
                    }
                }
            }

            FourierUtils.shift_to_center(watermarked_Img);                                                              //centralization
            double[][] imgend=FourierUtils.getInverseFft(watermarked_Img,rowRc,coleRc);
            return new Watermarkembed(imgend);
        }
    }
}
