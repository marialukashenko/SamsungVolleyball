package ru.myitschool.volleyball.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


public class StaticBody {
    public Body body;

    public StaticBody (World world, float x, float y, float width, float height, float[] verts) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(new Vector2(x, y));

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        if(verts == null) {
            shape.setAsBox(width / 2, height / 2);
        } else {
            shape.set(verts);
        }
        body.createFixture(shape, 0.0f);
        shape.dispose();
    }
}
