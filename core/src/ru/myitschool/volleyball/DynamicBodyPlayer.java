package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.MyGdx.SCR_HEIGHT;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class DynamicBodyPlayer {

    Body body;
    float r;
    float lowLevel;
    boolean isJumping;

    DynamicBodyPlayer(World world, float x, float y, float radius) {
        r = radius;
        lowLevel = y;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;

        fixtureDef.density = 5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.1f;

        Fixture fixture = body.createFixture(fixtureDef);
        circle.dispose();
    }

    public void touch(float tx, float ty){
        if(isJumping) return;
        if(ty > getY()+r && tx > getX()-r*3 && tx < getX()+r*3) {
            //body.applyLinearImpulse(new Vector2(0, (float) (10*Math.sqrt(2*ty/10)*1.3)), body.getPosition(), true);
            body.applyLinearImpulse(new Vector2(0, 11f), body.getPosition(), true);
            isJumping = true;
            return;
        }
        if(tx > getX()){
            float vx = (tx - getX()) * 8;
            if(vx > 9) vx = 9;
            body.setLinearVelocity(vx, 0);
        }
        else {
            float vx = (tx - getX()) * 8;
            if(vx < -9) vx = -9;
            body.setLinearVelocity(vx, 0);
        }

    }

    void move() {
        if(getY()>SCR_HEIGHT/2+r) {
            body.setLinearVelocity(body.getLinearVelocity().x, -(0.9f*body.getLinearVelocity().y));
            //body.applyLinearImpulse(new Vector2(0, -15f), body.getPosition(), true);
        }

        if(getY() <= lowLevel + 0.1f) {
            isJumping = false;
        }
    }

    float getX(){
        return body.getPosition().x;
    }

    float getY(){
        return body.getPosition().y;
    }

    Vector2 getCenter(){
        return body.getPosition();
    }

    boolean overlap(DynamicBodyBall b) {
        return (getX()-b.getX())*(getX()-b.getX())+(getY()-b.getY())*(getY()-b.getY())<=(r+b.r)*(r+b.r);
    }
}
