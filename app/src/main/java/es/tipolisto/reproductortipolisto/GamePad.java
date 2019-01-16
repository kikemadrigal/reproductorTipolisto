package es.tipolisto.reproductortipolisto;



public class GamePad {
    private int botonX, botonY,botonA,botonB, botonR, botonL;
    private int cursorArriba, cursorAbajo, cursorIzquierda, cursorDerecha;
    public GamePad(){
        this.botonX=0;
        this.botonY=0;
        this.botonA=0;
        this.botonB=0;
        this.botonL=0;
        this.botonR=0;
        this.cursorArriba=0;
        this.cursorAbajo=0;
        this.cursorIzquierda=0;
        this.cursorDerecha=0;
    }
    public int getBotonX() {
        return botonX;
    }

    public void setBotonX(int botonX) {
        this.botonX = botonX;
    }

    public int getBotonY() {
        return botonY;
    }

    public void setBotonY(int botonY) {
        this.botonY = botonY;
    }

    public int getBotonA() {
        return botonA;
    }

    public void setBotonA(int botonA) {
        this.botonA = botonA;
    }

    public int getBotonB() {
        return botonB;
    }

    public void setBotonB(int botonB) {
        this.botonB = botonB;
    }

    public int getBotonR() {
        return botonR;
    }

    public void setBotonR(int botonR) {
        this.botonR = botonR;
    }

    public int getBotonL() {
        return botonL;
    }

    public void setBotonL(int botonL) {
        this.botonL = botonL;
    }

    public int getCursorArriba() {
        return cursorArriba;
    }

    public void setCursorArriba(int cursorArriba) {
        this.cursorArriba = cursorArriba;
    }

    public int getCursorAbajo() {
        return cursorAbajo;
    }

    public void setCursorAbajo(int cursorAbajo) {
        this.cursorAbajo = cursorAbajo;
    }

    public int getCursorIzquierda() {
        return cursorIzquierda;
    }

    public void setCursorIzquierda(int cursorIzquierda) {
        this.cursorIzquierda = cursorIzquierda;
    }

    public int getCursorDerecha() {
        return cursorDerecha;
    }

    public void setCursorDerecha(int cursorDerecha) {
        this.cursorDerecha = cursorDerecha;
    }
    public String comprobarBotonesAsignados(){
        String mensaje="";
        if(this.botonX==0){
            mensaje+=" boton X no asignado";
        }
        if(this.botonY==0){
            mensaje+=" boton y no asignado";
        }
        if(this.botonA==0){
            mensaje+=" boton a no asignado";
        }
        if(this.botonB==0){
            mensaje+=" boton b no asignado";
        }
        if(this.botonL==0){
            mensaje+=" boton L no asignado";
        }
        if(this.botonR==0){
            mensaje+=" boton r no asignado";
        }
        if(this.cursorArriba==0){
            mensaje+=" cursor arriba no asignado";
        }
        if(this.cursorAbajo==0){
            mensaje+=" cursor abajo no asignado";
        }
        if(this.cursorIzquierda==0){
            mensaje+=" cursor izquierda no asignado";
        }
        if(this.cursorDerecha==0){
            mensaje+=" cursor derecha no asignado";
        }
        return mensaje;
    }
}
