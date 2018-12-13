/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artstationapplication;
import java.util.ArrayList;
import processing.core.*;

/**
 *
 * @author wilder4690
 */
public class Polygon extends Shape{
    ArrayList<VertexHandle> vertices = new ArrayList<>();
    VertexHandle activeHandle;
    VertexHandle origin;
    final int SMALLEST_X = 0;
    final int SMALLEST_Y = 1;
    final int GREATEST_X = 2;
    final int GREATEST_Y = 3;
    float[] boundingBox = new float[4]; // Index 0 is smallest x, 1 is smallest y, 2 is greatest x, 3 is greatest y  

    
    Polygon(PApplet drawingSpace, float x, float y){
        super(drawingSpace, x,y);
        origin = new VertexHandle(app, 0,0);
    }
    
    void adjustOrigin(PVector point){
        //fix (check handles is commented out for now)
        PVector delta = PVector.sub(point, pos);
        setPosition(point.x, point.y);
        for(int i = 1; i < vertices.size(); i++){
            vertices.get(i).shift(delta);
        }
    }
    
    void addVertex(float x, float y){
        
        if(vertices.isEmpty()){
            setBoundingBox(0,0,0,0);
            setPosition(x,y);
            vertices.add(new VertexHandle(app, 0,0));
        }
        else{
            PVector vPos = new PVector(x,y);
            vPos.sub(pos);
            vertices.add(new VertexHandle(app, vPos));
            calculateBoundingBox(vPos.x,vPos.y);
        }
    }
    
    void addVertex(PVector mouse){
        addVertex(mouse.x, mouse.y);
    }
    
    void setBoundingBox(float a, float b, float c, float d){
        boundingBox[SMALLEST_X] = a;
        boundingBox[SMALLEST_Y] = b;
        boundingBox[GREATEST_X] = c;
        boundingBox[GREATEST_Y] = d;        
    }
    
    void calculateBoundingBox(){
        for(int i = 0; i < vertices.size(); i++){

            float x = vertices.get(i).getPosition().x;
            float y = vertices.get(i).getPosition().y;
            if(i == 0){
                setBoundingBox(x,y,x,y);
            }
            if(x > boundingBox[GREATEST_X]) boundingBox[GREATEST_X] = x;
            if(x < boundingBox[SMALLEST_X]) boundingBox[SMALLEST_X] = x;
            if(y > boundingBox[GREATEST_Y]) boundingBox[GREATEST_Y] = y;
            if(y < boundingBox[SMALLEST_Y]) boundingBox[SMALLEST_Y] = y;
        }
    }
    
    //Compares new point to existing bounding box points to see if new extreme
    void calculateBoundingBox(float x, float y){
        if(x > boundingBox[GREATEST_X]) boundingBox[GREATEST_X] = x;
        if(x < boundingBox[SMALLEST_X]) boundingBox[SMALLEST_X] = x;
        if(y > boundingBox[GREATEST_Y]) boundingBox[GREATEST_Y] = y;
        if(y < boundingBox[SMALLEST_Y]) boundingBox[SMALLEST_Y] = y;
    }
    
    @Override
    boolean checkHandles(PVector mouse){
        float deltaX = mouse.x - pos.x;
        float deltaY = mouse.y - pos.y;
        float rotX = deltaX*app.cos(-rotation) - deltaY*app.sin(-rotation);
        float rotY = deltaY*app.cos(-rotation) + deltaX*app.sin(-rotation);
        PVector rot = new PVector(rotX, rotY);
//        if(origin.overHandle(rot)){
//            activeHandle = origin;
//            System.out.println("here");
//            return true;
//        }
        for(int i = 0; i < vertices.size(); i++){
                if(vertices.get(i).overHandle(rot)){
                    activeHandle = vertices.get(i);
                    return true;
                }
            }
        return false;
    }

    @Override
    void adjustActiveHandle(PVector mouse){
        float deltaX = mouse.x - pos.x;
        float deltaY = mouse.y - pos.y;
        float rotX = deltaX*app.cos(-rotation) - deltaY*app.sin(-rotation);
        float rotY = deltaY*app.cos(-rotation) + deltaX*app.sin(-rotation);
        PVector rot = new PVector(rotX, rotY);
        activeHandle.setPosition(rot);
        if(activeHandle == origin){
            adjustOrigin(rot);
        }
        calculateBoundingBox();
    }
       
    @Override
    boolean mouseOver(PVector mouse){
        float deltaX = mouse.x - pos.x;
        float deltaY = mouse.y - pos.y;
        float rotX = deltaX*app.cos(-rotation) - deltaY*app.sin(-rotation);
        float rotY = deltaY*app.cos(-rotation) + deltaX*app.sin(-rotation);

        if(rotX < boundingBox[SMALLEST_X] || rotX > boundingBox[GREATEST_X]) return false;
        if(rotY < boundingBox[SMALLEST_Y] || rotY > boundingBox[GREATEST_Y]) return false;
        return true;
    }
    

    @Override
    void drawShape(){
        app.pushMatrix();
        app.translate(pos.x, pos.y);
        app.rotate(rotation);
        if(completed){
            app.fill(paint);
            app.beginShape();
            for(int i = 0; i < vertices.size(); i++){
                app.vertex(vertices.get(i).getPositionFloats());
            }
            app.endShape(app.CLOSE);
            app.noFill();
            app.rectMode(app.CORNERS);
            app.rect(boundingBox[SMALLEST_X], boundingBox[SMALLEST_Y], boundingBox[GREATEST_X], boundingBox[GREATEST_Y]);
        }
        if(!completed){
            app.noFill();
            app.beginShape();
            for(int i = 0; i < vertices.size(); i++){
                app.vertex(vertices.get(i).getPositionFloats());
            }
            app.endShape();
            for(int i = 0; i < vertices.size(); i++){
                vertices.get(i).drawHandle();
            }
        }
        if(selected){
            app.noFill();
            app.strokeWeight(3);
            app.stroke(255,255, 0);
            app.beginShape();
            for(int i = 0; i < vertices.size(); i++){
                app.vertex(vertices.get(i).getPositionFloats());
            }
            app.endShape(); 
            drawHandles();
        }
        app.fill(0,255,0);
        app.ellipse(0,0, 15,15);
        app.popMatrix();
        
    }

    void drawHandles(){
        for(int i = 0; i < vertices.size(); i++){
            vertices.get(i).drawHandle();
        }
    }
    
    @Override
    void modify(PVector mouse){
        addVertex(mouse);
    }

   
    
}