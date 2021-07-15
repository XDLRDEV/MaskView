package com.media.watermark;

import java.util.ArrayList;
/**The input matrix is segmented according to the input template
 * @Time 2020-04-20 14:49:01
 */
public class DivideCircles {

    private ArrayList<ArrayList<Complex[][]>> Rg;                                                                       //A sequence of rings divided by random image sequences

    public DivideCircles(ArrayList<ArrayList<Complex[][]> > Rg1){
        this.Rg=Rg1;
    }

    public ArrayList<ArrayList<Complex[][]>> getRg(){
        return this.Rg;
    }

    /**
     *The input matrix is divided into ring sequences according to the radius sequence of the input
     * @param Rc The template sequence
     * @param DFTg Initial input matrix sequence
     * @param radiusNum Number of rings
     * @param num Number of random images
     */
    public static DivideCircles divideCircles(ArrayList<Complex[][]> Rc, ArrayList<Complex[][]> DFTg, int radiusNum, int num){
        Complex[][] interMatrix;                                                                                        //Intermediate matrix
        ArrayList<ArrayList<Complex[][]>> Rg=new ArrayList<>();                                                         //A sequence of rings divided by random image sequences
        Complex zero=new Complex(0,0);                                                                     //complex zero
        ArrayList<Complex[][]> circleDivideOne;                                                                         //A sequence of circles divided by a single image
        Complex[][] oneCircle=Rc.get(0);                                                                                //Extract a template from the input template sequence
        int mr=oneCircle.length;
        int mc=oneCircle[0].length;
        //Extract the corresponding matrix sequence according to the template
        for(int l=0;l<num;l++){
            circleDivideOne=new ArrayList<>();
            for (int i=0;i<radiusNum;i++){
                oneCircle=Rc.get(i);
                Complex[][]b=new Complex[mr][mc];
                interMatrix=DFTg.get(l);

                for (int j=0;j<mr;j++){
                    for (int k=0;k<mc;k++){
                        if(oneCircle[j][k].getRe()==0.0&&oneCircle[j][k].getImage()==0.0){
                            b[j][k]=zero;
                        }
                        else{
                            b[j][k]=interMatrix[j][k];
                        }
                    }
                }

                circleDivideOne.add(b);
            }
            Rg.add(circleDivideOne);
        }
        return new DivideCircles(Rg);
    }

}
