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
import com.badlogic.gdx.utils.TimeUtils;

public class DynamicBodyPlayer {

    Body body;
    float r;
    float lowLevel;
    private static final int GO = 0, JUMP = 1, FALL = 2;
    int state;
    long timeStartJump, timeJump = 100;

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

    public void touch(float tx, float ty) {
        if (state != GO) return;
        if (ty > getY() + r) {
            state = JUMP;
            float a = MathUtils.atan2((tx - getX()),(ty - getY()));
            float vx = 60*MathUtils.sin(a);
            float vy = 60*MathUtils.cos(a);
            vy=vy<0?-1*vy:vy;
            System.out.println("a: "+a+"vx: "+vx+"vy: "+vy);
            body.applyLinearImpulse(new Vector2(vx, vy), body.getPosition(), true);
            timeStartJump = TimeUtils.millis();
            return;
        }

        if (tx > getX()) {
            float vx = (tx - getX()) * 8;
            if (vx > 10) vx = 10;
            body.setLinearVelocity(vx, 0);
        } else {
            float vx = (tx - getX()) * 8;
            if (vx < -10) vx = -10;
            body.setLinearVelocity(vx, 0);
        }
    }

    void move() {
        if (timeStartJump + timeJump < TimeUtils.millis() && state == JUMP) {
            body.setLinearVelocity(body.getLinearVelocity().x>5?5:body.getLinearVelocity().x, -4.9f);
            state = FALL;
        }

        if (getY() <= lowLevel + 0.1f) {
            body.setLinearVelocity(0, 0);
            state = GO;
        }
    }

    float getX() {
        return body.getPosition().x;
    }

    float getY() {
        return body.getPosition().y;
    }

    float scrX() {
        return body.getPosition().x - r;
    }

    float scrY() {
        return body.getPosition().y - r;
    }

    float width() {
        return r * 2;
    }

    float height() {
        return r * 2;
    }

    Vector2 getCenter() {
        return body.getPosition();
    }

    boolean overlap(DynamicBodyBall b) {
        return (getX() - b.getX()) * (getX() - b.getX()) + (getY() - b.getY()) * (getY() - b.getY()) <= (r + b.r) * (r + b.r);
    }
}
