package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.MyGdx.SCR_WIDTH;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class KinematicBodyPerson {
    Body body;
    float r;
    int person;

    KinematicBodyPerson(World world, float x, float y, float radius, int number) {
        r = radius;
        person = number;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x,y);

        body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f;
        Fixture fixture = body.createFixture(fixtureDef);
        circle.dispose();
    }

    public void move(float tx, float ty){
        if(tx > getX()){
            body.setLinearVelocity(2, 0);
        }
        else {
            body.setLinearVelocity(-2, 0);
        }
    }

    void move(){
        if(person == 0) {
            if (getX() - r < 0.15f) {
                body.setLinearVelocity(0, 0);
                //body.setTransform(0.15f+r, getY(), 0);
            }
            else if(getX()+r>SCR_WIDTH/2-0.1f){
                body.setLinearVelocity(0, 0);
                //body.setTransform(0.15f+r, getY(), 0);
            }
        } else {
            if (getX() - r < SCR_WIDTH/2+0.1f) {
                body.setLinearVelocity(0, 0);
            }
            else if(getX()+r>SCR_WIDTH-0.15f){
                body.setLinearVelocity(0, 0);
            }
        }
    }

    float getX(){
        return body.getPosition().x;
    }

    float getY(){
        return body.getPosition().y;
    }
}
