package com.ngdroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;
import java.util.Vector;

import istanbul.gamelab.ngdroid.base.BaseCanvas;
import istanbul.gamelab.ngdroid.core.NgMediaPlayer;
import istanbul.gamelab.ngdroid.util.Log;
import istanbul.gamelab.ngdroid.util.Utils;


/**
 * Created by noyan on 24.06.2016.
 * Nitra Games Ltd.
 */


public class GameCanvas extends BaseCanvas {

    /*Global Değişkenler*/
    private Bitmap tileset, spritesheet, bullet, enemy, explode, laser, buttons;
    private Rect restartsrc, playsrc, exitsrc, restartdst, playdst, exitdst;
    private int kareno, animasyonno, animasyonyonu;
    private int spritex, spritey , hiz, hizx, hizy, bulletoffsetx_temp, bulletoffsety_temp, bulletspeed; // Shift+F6 - Refactor Kısatuşu
    private int bulletx_temp, bullety_temp, explodeframeno;
    private int laserspeed;
private long prevtime, time;
    private int lasery, laserx1, laserx2;
    private int sesefkti_patlama;
    private int enemyspeedx, enemyspeedy,enemyx, enemyy, donmenoktasi;
private Rect lasersrc, laserdst1, laserdst2;
    private boolean donmeboolean, spriteexist;

    private Random enemyrnd;

private Rect textdst;
    private NgMediaPlayer arkaplan_muzik;
    private boolean enemyexist, exploded;
private boolean guishow;
 private Paint textcolor;
    private int textsize;
    private String text;

    public Vector <Rect> bulletdst;
    public Vector <Integer> bulletx2, bullety2, bulletoffsetx2, bulletoffsety2, bulletspeedx2, bulletspeedy2;

    private Rect tilesrc, tiledst, spritesrc, spritedst, bulletsrc, enemysrc, enemydst, explodesrc, explodedst;

    /*İnteraktif Değişkenler*/
    int touchx, touchy; // Ekrana nereye bastığımızın koordinatlarını tutar


    public GameCanvas(NgApp ngApp) {
        super(ngApp);
    }

    public void setup() {
        try{
            sesefkti_patlama = root.soundManager.load("sounds/se2.wav");
        }
        catch (Exception e){
            e.printStackTrace();

        }
        //region gui
        buttons = Utils.loadImage(root,"images/buttons.png");
        restartsrc = new Rect();
        restartdst = new Rect();
        playsrc = new Rect();
        playdst = new Rect();
        exitsrc = new Rect();
        exitdst = new Rect();
        //endregion

        spriteexist = true;
        laserspeed = 45;
        lasery = -400;

guishow = false;

        prevtime = System.currentTimeMillis();
arkaplan_muzik = new NgMediaPlayer(root);
        arkaplan_muzik.load("sounds/m2.mp3");
        arkaplan_muzik.setVolume(0.5f);
        arkaplan_muzik.prepare();
        arkaplan_muzik.start();
laser = Utils.loadImage(root, "images/beams1.png");
        lasersrc = new Rect();
        laserdst1 =new Rect();
        laserdst2 =new Rect();



        tileset = Utils.loadImage(root, "images/tilea2.png");
        tilesrc = new Rect();
        tiledst = new Rect();
        explode = Utils.loadImage(root, "images/exp2_0.png");
        explodesrc = new Rect();
        explodedst = new Rect();
        explodeframeno = 0;
exploded = false;
//region enemy
enemy = Utils.loadImage(root, "images/mainship03.png");
        enemysrc = new Rect();
        enemydst = new Rect();
        enemyspeedx = 10;
                enemyspeedy =0;

        enemyx = getWidthHalf()-128;
        enemyy = getHeight()-256;

enemyexist=true;
        donmenoktasi = getWidth();
        donmeboolean = true;
        enemyrnd = new Random();


        //endregion
        spritesheet = Utils.loadImage(root, "images/cowboy.png");
        spritesrc = new Rect();
        spritedst = new Rect();

        bullet = Utils.loadImage(root,"images/bullet.png");
        bulletsrc = new Rect();

        bulletdst = new Vector<>();
        bulletx2 = new Vector<>();
        bullety2 = new Vector<>();
        bulletspeedx2 = new Vector<>();
        bulletspeedy2 = new Vector<>();
        bulletoffsetx2 = new Vector<>();
        bulletoffsety2 = new Vector<>();

        kareno = 0;
        animasyonno = 1;
        animasyonyonu = 0;

        spritex = 0;
        spritey = 0;

        bulletx_temp =0;
        bullety_temp =0;

        bulletspeed=0;

        bulletoffsetx_temp =256;
        bulletoffsety_temp =128;

        hiz = 16;
        hizx = 0;
        hizy = 0;
textcolor = new Paint();
        textcolor.setARGB(255, 255, 0, 0);
textsize = 64;
        textcolor.setTextSize(textsize);
        text = "GAME OVER!";
        // textdst.set(textcolor.getTextBounds(text, 0, text.length(), textdst));
        textcolor.setTextAlign(Paint.Align.CENTER);
    }

    public void update() {
        tilesrc.set(0,0,64,64);

lasersrc.set(0, 0, 64, 128);

restartsrc.set(256, 0, 512, 256);
        exitsrc.set(512, 0, 768, 256 );
        restartdst.set(getWidthHalf() -192, getHeightHalf() - 64, getWidthHalf() - 64, getHeightHalf() +64);
        exitdst.set(getWidthHalf() +64, getHeightHalf() - 64, getWidthHalf() +192, getHeightHalf() +64);

        time = System.currentTimeMillis();

        if(time>prevtime+4000 && enemyexist){
            prevtime = time;

            laserx1 = enemyx;
            laserx2 = enemyx + 192;
            laserdst1.set(laserx1, enemyy-128, enemyx+64, enemyy);
            laserdst2.set(laserx2, enemyy-128, enemyx+256, enemyy);
            lasery = enemyy-25;

        }

        lasery-=laserspeed;
        laserdst1.set(laserx1, lasery, laserx1+64, lasery+128);
        laserdst2.set(laserx2, lasery, laserx2+64, lasery+128);

if(spritedst.intersect(laserdst1) || spritedst.intersect(laserdst2)){
    spritedst.set(0, 0, 0, 0);
    spriteexist = false;
    guishow = true;

}



        if(donmeboolean){
            if(enemyspeedx>0){
                donmenoktasi = enemyrnd.nextInt(getWidth() - 256 - (enemyx+50) ) + enemyx;
            }
            else if (enemyspeedx<0){
                donmenoktasi =enemyrnd.nextInt(enemyx);
            }
           donmeboolean = false;
        }

        if(enemyspeedx>0 && enemyx> donmenoktasi){
            donmeboolean = true;
            enemyspeedx = -enemyspeedx;
        }
        else if(enemyspeedx<0 && enemyx< donmenoktasi){
            donmeboolean = true;
            enemyspeedx = -enemyspeedx;
        }

     if(enemyexist){
         enemysrc.set(0,0,64,64);
         //enemydst.set(getWidthHalf()-128, getHeight()-256, getWidthHalf()+128, getHeight());
         enemydst.set(enemyx, enemyy ,enemyx+256, enemyy+256);
     }


        for(int i = 0; i< bulletdst.size(); i++){
    if(enemydst.contains(bulletdst.elementAt(i))) {

        explodedst.set(enemyx, enemyy, enemyx + 256, enemyy + 256);

        bulletx2.removeAllElements();
        bullety2.removeAllElements();
        bulletdst.removeAllElements();
        bulletspeedx2.removeAllElements();
        bulletspeedy2.removeAllElements();


        enemyexist=false;
        enemydst.set(0,0,0,0);
        exploded = true;
        guishow = true;
        root.soundManager.play(sesefkti_patlama);



    }



}
if(exploded){
    explodesrc = getexplodeframe(explodeframeno);

    explodeframeno+=2;
}


        if(explodeframeno > 15){
            explodeframeno = 0;
            exploded = false;
        }



        spritex += hizx;
        spritey += hizy;

        enemyx += enemyspeedx;
        enemyy += enemyspeedy;

        if(enemyx + 256 > getWidth() || enemyx<0){
            enemyspeedx = -enemyspeedx;

        }
        /*bulletx += bulletspeedx;
        bullety += bulletspeedy;*/

        for (int i=0; i<bulletx2.size(); i++){
            bulletx2.set(i,bulletx2.elementAt(i) + bulletspeedx2.elementAt(i));
            bullety2.set(i,bullety2.elementAt(i) + bulletspeedy2.elementAt(i));

            if(bulletx2.elementAt(i)>getWidth() || bulletx2.elementAt(i)<0 || bullety2.elementAt(i)>getHeight() || bullety2.elementAt(i)<0){
                bulletx2.removeElementAt(i);
                bullety2.removeElementAt(i);

                bulletdst.removeElementAt(i);
                bulletspeedx2.removeElementAt(i);
                bulletspeedy2.removeElementAt(i);
            }
        }

        if(spritex+256 > getWidth() || spritex<0){
            hizx = 0;
        }
        if(spritey+256 > getHeight() || spritey<0){
            hizy=0;
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

        if(Math.abs(hizx)>0 || Math.abs(hizy)>0){
            animasyonno=1;
        }
        else{
            animasyonno=0;
        }

        spritesrc.set(kareno*128, animasyonyonu*128, (kareno+1)*128, (animasyonyonu+1)*128); //Resimden aldığımız koordinatlar
        if(spriteexist){


            spritedst.set(spritex, spritey, spritex+256, spritey+256); //Ekrana çizeleceği koordinatlar

        }
        bulletsrc.set(0,0,70,70);

        for (int i=0; i < bulletx2.size(); i++){
            bulletdst.elementAt(i).set(bulletx2.elementAt(i), bullety2.elementAt(i), bulletx2.elementAt(i)+32, bullety2.elementAt(i)+32);
        }


    }

    public void draw(Canvas canvas) {

        for (int i=0;i<getWidth();i+=128){
            for(int j=0;j<getHeight();j+=128){
                tiledst.set(i, j, i+128, j+128);
                canvas.drawBitmap(tileset,tilesrc,tiledst,null);
            }
        }

        canvas.drawBitmap(spritesheet, spritesrc, spritedst, null);

        for(int i = 0; i< bulletdst.size(); i++){
            canvas.drawBitmap(bullet, bulletsrc, bulletdst.elementAt(i), null); //(Bitmap, Kaynak, Çizdirilmek istenen yer)
        }


if(enemyexist){
    canvas.drawBitmap(enemy, enemysrc, enemydst, null);
}
if(exploded){
    canvas.drawBitmap(explode, explodesrc, explodedst, null);

}

        canvas.drawBitmap(laser, lasersrc, laserdst1, null);
        canvas.drawBitmap(laser, lasersrc, laserdst2, null);


        if(guishow) {

canvas.drawText(text, getWidthHalf(), getHeightHalf()-300, textcolor);

            canvas.drawBitmap(buttons, restartsrc, restartdst, null);
            canvas.drawBitmap(buttons, exitsrc, exitdst, null);
        }

    }
    public Rect getexplodeframe(int frameno){
       frameno=15- frameno;
        Rect temp =new Rect();
        temp.set((frameno%4)*64,((frameno/4)*64),((frameno%4)+1)*64,(((frameno/4)+1)*64));
        return temp;
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
       //region control
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
            bulletspeed=32;

            if(animasyonyonu==0){
                bulletspeedx2.add(bulletspeed);
                bulletspeedy2.add(0);
                bulletoffsetx_temp =256;
                bulletoffsety_temp =128;
            }
            else if(animasyonyonu==1){
                bulletspeedx2.add(-bulletspeed);
                bulletspeedy2.add(0);
                bulletoffsetx_temp =0;
                bulletoffsety_temp =128;
            }
            else if (animasyonyonu==9){
                bulletspeedy2.add(bulletspeed);
                bulletspeedx2.add(0);
                bulletoffsetx_temp =128;
                bulletoffsety_temp =256;
            }
            else if(animasyonyonu==5){
                bulletspeedy2.add(-bulletspeed);
                bulletspeedx2.add(0);
                bulletoffsetx_temp =128;
                bulletoffsety_temp =0;
            }
            bulletx2.add(spritex+ bulletoffsetx_temp);
            bullety2.add(spritey+ bulletoffsety_temp);
            bulletx_temp =spritex+ bulletoffsetx_temp;
            bullety_temp =spritey+ bulletoffsety_temp;
            bulletdst.add(new Rect(bulletx_temp, bullety_temp, bulletx_temp +32, bullety_temp +32));
        }
        //endregion
        //region gui control
        if(guishow) {

            if (restartdst.contains(x, y)) {
                //Log.i(TAG, "RESTART'A TIKLANDI");
                setup();
            }
            if (exitdst.contains(x, y)) {
                // Log.i(TAG, "EXIT'E TIKLANDI");
                System.exit(0);
            }
        }
                //endregion

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
