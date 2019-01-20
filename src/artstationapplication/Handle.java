/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artstationapplication;
import processing.core.*;
/**
 *
 * @author wilder4690
 */
     class Handle{
         PApplet app;
         float modifier = 1;
         float radius = 50;
         float size = 15;
         int paint;
         PVector offset;
         Shape parent;
         

         
         Handle(PApplet drawingSpace, Shape parent, PVector which){
             app = drawingSpace;
             offset = which;
             this.parent = parent;
             paint = app.color(255,255,0);
        }
         
         Handle(Handle base, Shape parent){
             app = base.app;
             offset = base.offset;
             this.parent = parent;
             paint = base.paint;
             radius = base.radius;
             modifier = base.modifier;
         }
         
         //Load Constructor
         Handle(PApplet drawingSpace, Shape parent, String[] input){
            app = drawingSpace;
            modifier = Float.valueOf(input[0]);
            radius = Float.valueOf(input[1]);
            paint = app.color(255,255,0);
            offset = new PVector(Float.valueOf(input[2]),Float.valueOf(input[3]));
            this.parent = parent;
         }
         
         PVector getPosition(float rot){
             
             //Optional TODO: if this works set the position in a PVector that manually updates on mouse release of rotation, to avoid doing this calculation constantly
             float pointX = modifier*radius*offset.x;
             float pointY = modifier*radius*offset.y;
             return new PVector(parent.getPosition().x + pointX * app.cos(rot) - pointY*app.sin(rot), parent.getPosition().y + pointX*app.sin(rot) + pointY*app.cos(rot));
         }
                  
         void setModifier(float r){
             modifier = r/radius;
         }
         
         void setRadius(){
             radius = radius*modifier;
             modifier = 1;
         }
         
         boolean overHandle(PVector m, float rot){
             return (m.dist(getPosition(rot)) < size);
         }
         
         float getRadius(){
             return radius*modifier;
         }
         
         void drawHandle(){
             app.fill(paint);
             app.strokeWeight(1);
             app.stroke(0,0,0);
             app.ellipse(modifier*radius*offset.x, modifier*radius*offset.y, size,size);
         }
         
         void reset(){
             modifier = 1;
         }
         
         String save(){
             String output = "";
             output += modifier+"&"+radius+"&"+offset.x+"&"+offset.y;
             return output;
         }
     }
