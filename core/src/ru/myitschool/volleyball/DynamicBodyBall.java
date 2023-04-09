package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.MyGdx.TYPE_BALL;
import static ru.myitschool.volleyball.MyGdx.TYPE_PERS;

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
        int type;

        DynamicBodyBall(World world, float x, float y, float radius, int type) {
                r = radius;
                this.type = type;

                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyDef.BodyType.DynamicBody;

                bodyDef.position.set(x, y);

                body = world.createBody(bodyDef);

                CircleShape circle = new CircleShape();
                circle.setRadius(radius);

                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = circle;
                if(type == TYPE_BALL) {
                        fixtureDef.density = 0.3f;
                        fixtureDef.friction = 0.4f;
                        fixtureDef.restitution = 0.8f;
                }
                if(type == TYPE_PERS) {
                        fixtureDef.density = 2.5f;
                        fixtureDef.friction = 0.4f;
                        fixtureDef.restitution = 0.2f;
                }
                Fixture fixture = body.createFixture(fixtureDef);
                circle.dispose();
        }

        void hit(float tx, float ty) {
                //if(Math.pow(tx-getX(),2) + Math.pow(ty-getY(),2) < r*r){
                if(ty > getY()+r*2) {
                        body.applyLinearImpulse(new Vector2(0, 6f), body.getPosition(), true);
                }
        }

        public void move(float tx, float ty){
                if(tx > getX()){
                        body.setLinearVelocity(4, 0);
                }
                else {
                        body.setLinearVelocity(-4, 0);
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
}
