package ru.myitschool.volleyball;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class DynamicBodyBall {

    public Body body;
    public float r = 0.4f;
    float width = 0.4f, height = 0.2f;

    DynamicBodyBall(World world, float x, float y, int type) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);
        if(type == VolleyBall.STYLE_STEAM) { // кирпич
            PolygonShape box = new PolygonShape();
            box.setAsBox(width, height);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = box;
            fixtureDef.density = 0.6f;
            fixtureDef.friction = 0.4f;
            fixtureDef.restitution = 0.4f;
            Fixture fixture = body.createFixture(fixtureDef);
            box.dispose();
        } else if (type == VolleyBall.STYLE_CASTLE) { // топор
             /*float[] verts = {0, 0, 0, -2, 0, -3, -2, -2.2f, -3.5f, -1, -4.3f, 0, -4.7f, 1, -5, 3,
                     -2, 2.5f, -2, 3, -1, 4, -0.5f, 3.5f, 0, 4, 2, 4, 0.2f, 2.5f, 5, -4, 4, -5, 0, 0};*/
            float[] verts = {-2, -2.2f, -3.5f, -1, -5, 3, -1, 4, 2, 4, 5, -4, 4, -5, -2, -2.2f};

            for (int i = 0; i < verts.length; i++) verts[i] /= 12;
            PolygonShape axe = new PolygonShape();
            axe.set(verts);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = axe;
            fixtureDef.density = 0.7f;
            fixtureDef.friction = 0.4f;
            fixtureDef.restitution = 0.3f;
            Fixture fixture = body.createFixture(fixtureDef);
            axe.dispose();
        } else if (type == VolleyBall.STYLE_GRAVE) { // пентаграмма
            CircleShape circle = new CircleShape();
            r = 0.6f;
            width = 0.6f;
            height = 0.3f;
            circle.setRadius(r);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = circle;
            fixtureDef.density = 0.01f;
            fixtureDef.friction = 0.1f;
            fixtureDef.restitution = 0.8f;
            Fixture fixture = body.createFixture(fixtureDef);
            circle.dispose();
        } else { // мяч
            CircleShape circle = new CircleShape();
            circle.setRadius(r);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = circle;
            fixtureDef.density = 0.4f;
            fixtureDef.friction = 0.4f;
            fixtureDef.restitution = 0.7f;
            Fixture fixture = body.createFixture(fixtureDef);
            circle.dispose();
        }
        System.out.println(type);
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

    float getRotation() {
        return body.getAngle() * MathUtils.radiansToDegrees;

    }

    boolean isGoal() {
        return getY() <= 0.3f + r;
    }

    Vector2 getCenter() {
        return body.getPosition();
    }
}
