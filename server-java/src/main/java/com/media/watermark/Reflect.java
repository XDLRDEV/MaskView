package com.media.watermark;

import java.util.ArrayList;
/*Reflect the values in matrix x about the scalar values minx and maxx.
 Hence a vector x containing a long linearly increasing series
 is converted into a waveform which ramps linearly up and down
 between minx and maxx.
 @Time 2020-04-20 14:49:01
 */

public class Reflect {

    public int[] X;                                                                                                     //Input matrix
    public double minX;
    public double maxX;
    public int[] Y;


    public ArrayList<Integer> t = new ArrayList<>();

    public Reflect(int[] X, double minX, double maxX){
        this.maxX=maxX;
        this.minX=minX;
        this.X=X;
    }
    /**
     * Reflect the values in matrix x about the scalar values minx and maxx.
     *
     */
    public int[] reflect(){
        this.Y=this.X;

        //Reflect y in maxx.
        for(int i=0;i<this.Y.length;i++){
            if(this.Y[i]>this.maxX){
                t.add(i);                                                                                               //coordinate sequence
            }
        }
        for(int i=0;i<t.size();i++){
            this.Y[t.get(i)]=(int)(2*maxX)-this.Y[t.get(i)];                                                            //The reflecteded sequence
        }

        t.clear();

        //Reflect y in minx.
        for (int i=0;i<this.Y.length;i++){
            if(this.Y[i]<minX){
                t.add(i);                                                                                               //coordinate sequence
            }
        }

        while (!t.isEmpty()){
            for(int i=0;i<t.size();i++){
                this.Y[t.get(i)]=(int)(2*minX) - this.Y[t.get(i)];                                                      //The reflecteded sequence
            }

            t.clear();

            //Repeat until no more values out of range.
            for(int i=0;i<this.Y.length;i++){
                if(this.Y[i]>this.maxX){
                    t.add(i);                                                                                           //coordinate sequence
                }
            }
            if (!t.isEmpty()){
                for(int i=0;i<t.size();i++){
                    this.Y[t.get(i)]=(int)(2*maxX)-this.Y[t.get(i)];                                                    //The reflecteded sequence
                }
            }
            t.clear();
            for (int i=0;i<this.Y.length;i++){
                if(this.Y[i]<minX){
                    t.add(i);                                                                                           //coordinate sequence
                }
            }
        }
        return this.Y;
    }

}
