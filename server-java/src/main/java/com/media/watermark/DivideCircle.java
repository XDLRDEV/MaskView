package com.media.watermark;

import java.util.ArrayList;
/**The input matrix is divided into ring sequences according to the radius sequence of the input
 * @Time 2020-04-20 14:49:01
 */
public class DivideCircle {

    ArrayList<Complex[][]> Rc;                                                                                          //Circle the sequence

    public DivideCircle(ArrayList<Complex[][]> Rc1 ){
        this.Rc=Rc1;
    }

    public ArrayList<Complex[][]> getRc(){
        return this.Rc;
    }

    /**
     *The input matrix is divided into ring sequences according to the radius sequence of the input
     * @param radiuses Radius sequence
     * @param DFTc Input 2-D matrix
     */
    public static DivideCircle divideCircle(double[] radiuses, Complex[][] DFTc){
        ArrayList<Complex[][]> Rc=new ArrayList<>();                                                                    //Circle the sequence
        Complex zero=new Complex(0,0);                                                                     //The complex number 0

        int radiusNum=radiuses.length;                                                                                  //Ring the number
        int mr=DFTc.length;
        int mc=DFTc[0].length;
        double[][] distance=new double[mr][mc];                                                                         //The distance from each point to the center of the matrix
        double midX=(mr-1)/2,midY=(mc-1)/2;                                                                             //The coordinates of the center of the matrix
        ArrayList<Integer> x;
        ArrayList<Integer> y;
        //Calculation of distance
        for (int i=0;i<mr;i++){
            for(int j=0;j<mc;j++){
                distance[i][j]= Math.pow(Math.pow((double)(i-midX),2.0)+ Math.pow((double)(j-midY),2.0),0.5);
            }
        }

        Find find=Find.find(distance,0,radiuses[0]);                                                         //Find the coordinates of the points whose distance values are in the interval

        x=find.getX();
        y=find.getY();

        int sizeNum=x.size();
        Complex[][] circularRing=new Complex[mr][mc];

        // initialization matrix
        for (int i=0;i<mr;++i){
            for (int j=0;j<mc;j++){
                circularRing[i][j]=zero;
            }
        }

        //Take the value based on the coordinates found
        for (int i=0;i<sizeNum;++i){
            int xx= (int) x.get(i);
            int yy= (int) y.get(i);
            circularRing[xx][yy]=DFTc[xx][yy];
        }
        Rc.add(circularRing);
        for (int i=0;i<radiusNum-1;++i){
            // initialization matrix
            circularRing=new Complex[mr][mc];
            int k=1;
            for(int j=0;j<mr;++j){
                for(int l=0;l<mc;++l){
                    circularRing[j][l]=zero;
                }
            }
            //Take the value based on the coordinates found
            for(int j=0;j<mr;j++){
                for(int l=0;l<mc;l++){
                    if((distance[j][l]>radiuses[i])&&(distance[j][l])<radiuses[i+1]){
                        circularRing[j][l]=DFTc[j][l];
                    }
                }
            }
            k=k+1;
            Rc.add(circularRing);

        }

        return new DivideCircle(Rc);
    }

}
