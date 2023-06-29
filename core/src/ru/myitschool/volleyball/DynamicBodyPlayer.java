package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.VolleyBall.SCR_WIDTH;

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

    public Body body;
    public float r;
    private float lowLevel;
    private static final int GO = 0, JUMP = 1, FALL = 2, STAY = 3;
    private int state;
    private long timeStartJump, timeJump = 200;
    private long timeLastFaza, timeFazaInterval = 50;
    public int faza, nFaz = 17, fazaStay = 17, fazaJumpLeft = 18, fazaJumpRight = 19;
    public boolean isFlip;
    public static final boolean LEFT = true, RIGHT = false;
    private boolean side;
    float targetX, targetY;

    DynamicBodyPlayer(World world, float x, float y, float radius, boolean side) {
        r = radius;
        lowLevel = y;
        this.side = side;

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
        if (state != GO && state != STAY) return;
        targetX = tx;
        if (ty > getY() + r * 2) {
            state = JUMP;
            float a = MathUtils.atan2((tx - getX()), (ty - getY()));
            float vx = 30 * MathUtils.sin(a);
            float vy = 30 * MathUtils.cos(a);
            vy = vy < 0 ? -1 * vy : vy;
            body.applyLinearImpulse(new Vector2(vx, vy), body.getPosition(), true);
            timeStartJump = TimeUtils.millis();
            return;
        }

        if (tx > getX()) {
            float vx = (tx - getX()) * 6;
            if (vx > 8) vx = 8;
            body.setLinearVelocity(vx, 0);
        } else {
            float vx = (tx - getX()) * 6;
            if (vx < -8) vx = -8;
            body.setLinearVelocity(vx, 0);
        }
    }

    void move() {
        if (timeStartJump + timeJump < TimeUtils.millis() && state == JUMP) {
            body.setLinearVelocity(body.getLinearVelocity().x > 5 ? 5 : body.getLinearVelocity().x, -4.9f);
            state = FALL;
        }
        changeFaza();

        if (state == FALL && getY() <= lowLevel + 0.1f) {
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
            state = GO;
        }

        if(near(getX(), targetX, 0.1f)) {
            body.setLinearVelocity(0, body.getLinearVelocity().y);
        }

        body.setAngularVelocity(0);
    }

    boolean near(float x1, float x2, float dx){
        return x1 > x2-dx && x1 < x2+dx;
    }

    void changeFaza() {
        if (state == GO) {
            if (Math.abs(body.getLinearVelocity().x) < 0.01) {
                isFlip = side;
                faza = fazaStay;
            } else if (timeLastFaza + timeFazaInterval < TimeUtils.millis()) {
                isFlip = body.getLinearVelocity().x > 0;
                if (++faza >= nFaz) faza = 0;
                timeLastFaza = TimeUtils.millis();
            }
        }
        if (state == JUMP && body.getLinearVelocity().x > 0) {
            if (isFlip) faza = fazaJumpRight;
            else faza = fazaJumpLeft;
        }
        if (state == JUMP && body.getLinearVelocity().x < 0) {
            if (isFlip) faza = fazaJumpLeft;
            else faza = fazaJumpRight;
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

    void useAi(DynamicBodyBall ball) {
        if (state == GO) {
            Vector2 ballPosition = ball.body.getPosition();
            if (near(getX(), ballPosition.x, r*2) && near(getY(), ballPosition.y, r*4)) {
                timeStartJump = TimeUtils.millis();
                state = JUMP;
                body.applyLinearImpulse(new Vector2((ballPosition.x-getX())*10, MathUtils.random(30f, 35f)), body.getPosition(), true);
            } else if (near(getX(), ballPosition.x, SCR_WIDTH/2) && MathUtils.random(10)==5) {
                body.setLinearVelocity((ballPosition.x-getX())*10, 0);
            }
        }
    }
}
