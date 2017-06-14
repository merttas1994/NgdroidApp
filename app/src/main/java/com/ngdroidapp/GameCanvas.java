package com.ngdroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import istanbul.gamelab.ngdroid.base.BaseCanvas;
import istanbul.gamelab.ngdroid.util.Utils;


/**
 * Created by noyan on 24.06.2016.
 * Nitra Games Ltd.
 */


public class GameCanvas extends BaseCanvas {

    /*Global Değişkenler*/
    private Bitmap tileset, spritesheet;
    private int kareno, animasyonno, animasyonyonu;
    private int spritex, spritey , hiz, hizx, hizy; // Shift+F6 - Refactor Kısatuşu
    private Rect tilesrc, tiledst, spritesrc, spritedst;

    /*İnteraktif Değişkenler*/
    int touchx, touchy; // Ekrana nereye bastığımızın koordinatlarını tutar


    public GameCanvas(NgApp ngApp) {
        super(ngApp);
    }

    public void setup() {
        tileset = Utils.loadImage(root, "images/tilea2.png");
        tilesrc = new Rect();
        tiledst = new Rect();

        spritesheet = Utils.loadImage(root, "images/cowboy.png");
        spritesrc = new Rect();
        spritedst = new Rect();

        kareno = 0;
        animasyonno = 1;
        animasyonyonu = 0;

        spritex = 0;
        spritey = 0;
        hiz = 16;
        hizx = 0;
        hizy = 0;


    }

    public void update() {


    }

    public void draw(Canvas canvas) {

        tilesrc.set(0,0,64,64);

        for (int i=0;i<getWidth();i+=128){
            for(int j=0;j<getHeight();j+=128){
                tiledst.set(i, j, i+128, j+128);
                canvas.drawBitmap(tileset,tilesrc,tiledst,null);
            }
        }
        spritex += hizx;
        spritey += hizy;

        if(spritex+256 > getWidth()){
            spritex = getWidth()-256;
            animasyonno = 0; /* Eğer ekranın sonuna ulaşırsa, animasyonno 0'a eşitleyerek aşağıdaki
            kondisyonu kareno artırmasını 0'a eşitledik
        */
        }
        if(spritey+256 > getHeight()){
            spritey=getHeight()-256;
            animasyonno=0;
        }

        if(animasyonno==1){
            kareno++; //Kareyi arttırmak için
        }
        else if(animasyonno==0){
            kareno = 0;
        }

        if(kareno>8){
            kareno=1;
        }

        if(hizx>0){
            animasyonyonu=0;
        }
        else if(hizy>0){
            animasyonyonu=9;
        }

        spritesrc.set(kareno*128, animasyonyonu*128, (kareno+1)*128, (animasyonyonu+1)*128); //Resimden aldığımız koordinatlar
        spritedst.set(spritex, spritey, spritex+256, spritey+256); //Ekrana çizeleceği koordinatlar
        canvas.drawBitmap(spritesheet, spritesrc, spritedst, null);


    }

    public void keyPressed(int key) {

    }

    public void keyReleased(int key) {

    }

    public boolean backPressed() {
        return true;
    }

    public void surfaceChanged(int width, int height) {

    }

    public void surfaceCreated() {

    }

    public void surfaceDestroyed() {

    }

    public void touchDown(int x, int y) {
        touchx=x;
        touchy=y;
    }

    public void touchMove(int x, int y) {
    }

    public void touchUp(int x, int y) {
        if(x-touchx>100){
            animasyonno=1;
            animasyonyonu=0;
            hizx=hiz;
            hizy=0;
        }
        else if(touchx-x>100){
            animasyonno=1;
            animasyonyonu=1;
            hizx=-hiz;
            hizy=0;
        }
        else if(y-touchy>100){
            animasyonno=1;
            animasyonyonu=9;
            hizy=hiz;
            hizx=0;
        }
        else if(touchy-y>100){
            animasyonno=1;
            animasyonyonu=5;
            hizy=-hiz;
            hizx=0;
        }
        else{
            animasyonno=0;
            hizx=0;
            hizy=0;
        }
    }


    public void pause() {

    }


    public void resume() {

    }


    public void reloadTextures() {

    }


    public void showNotify() {
    }

    public void hideNotify() {
    }

}
