package ru.myitschool.volleyball;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class DynamicBodyBall {
        Body body;
        float r;

        DynamicBodyBall(World world, float x, float y, float radius) {
                r = radius;

                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyDef.BodyType.DynamicBody;

                bodyDef.position.set(x, y);

                body = world.createBody(bodyDef);

                CircleShape circle = new CircleShape();
                circle.setRadius(radius);

                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = circle;
                fixtureDef.density = 0.5f;
                fixtureDef.friction = 0.4f;
                fixtureDef.restitution = 0.6f; // Make it bounce a little bit
                Fixture fixture = body.createFixture(fixtureDef);
                circle.dispose();
                body.setFixedRotation(true);
                body.setAngularVelocity(-1);
        }

        boolean hit(float tx, float ty) {
                return Math.pow(tx-getX(),2) + Math.pow(ty-getY(),2) < r*r;
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
}
