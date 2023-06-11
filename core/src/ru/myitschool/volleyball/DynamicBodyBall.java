package ru.myitschool.volleyball;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class DynamicBodyBall {

        public Body body;
        public float r;

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

                fixtureDef.density = 0.3f;
                fixtureDef.friction = 0.4f;
                fixtureDef.restitution = 0.5f;

                Fixture fixture = body.createFixture(fixtureDef);
                circle.dispose();
        }

        float getX(){
                return body.getPosition().x;
        }

        float getY(){
                return body.getPosition().y;
        }

        float scrX(){
                return body.getPosition().x-r;
        }

        float scrY(){
                return body.getPosition().y-r;
        }

        float width(){
                return r*2;
        }

        float height(){
                return r*2;
        }

        float getRotation() {
                return body.getAngle()*MathUtils.radiansToDegrees;

        }

        boolean isGoal(){
                return getY()<=0.3f+r;
        }

        Vector2 getCenter(){
                return body.getPosition();
        }
}
