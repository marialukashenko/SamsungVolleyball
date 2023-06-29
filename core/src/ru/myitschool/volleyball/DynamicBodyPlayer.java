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

/**
 * Динамические тела - игроки
 */
public class DynamicBodyPlayer {
    public Body body;
    public float r;
    private float lowLevel; // пол
    // состояния
    private static final int GO = 0;
    private static final int JUMP = 1;
    private static final int FALL = 2;
    private static final int STAY = 3;
    private int state;

    // время и интервал прыжка
    private long timeStartJump;
    private static final long TIME_JUMP = 200;

    // всё про фазы
    private long timeLastFaza;
    private static final long TIME_FAZA_INTERVAL = 50;
    public int faza;
    private static final int N_FAZ = 17;
    private static final int FAZA_STAY = 17;
    private static final int FAZA_JUMP_LEFT = 18;
    private static final int FAZA_JUMP_RIGHT = 19;

    public boolean isFlip;
    public static final boolean LEFT = true;
    public static final boolean RIGHT = false;
    private final boolean side;
    private float targetX;

    public DynamicBodyPlayer(World world, float x, float y, float radius, boolean side) {
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

    // касание колобка
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

    public void move() {
        if (timeStartJump + TIME_JUMP < TimeUtils.millis() && state == JUMP) {
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

    // определяем, рядом ли объект
    private boolean near(float x1, float x2, float dx){
        return x1 > x2-dx && x1 < x2+dx;
    }

    // смена фазы
    private void changeFaza() {
        if (state == GO) {
            if (Math.abs(body.getLinearVelocity().x) < 0.01) {
                isFlip = side;
                faza = FAZA_STAY;
            } else if (timeLastFaza + TIME_FAZA_INTERVAL < TimeUtils.millis()) {
                isFlip = body.getLinearVelocity().x > 0;
                if (++faza >= N_FAZ) faza = 0;
                timeLastFaza = TimeUtils.millis();
            }
        }
        if (state == JUMP && body.getLinearVelocity().x > 0) {
            if (isFlip) faza = FAZA_JUMP_RIGHT;
            else faza = FAZA_JUMP_LEFT;
        }
        if (state == JUMP && body.getLinearVelocity().x < 0) {
            if (isFlip) faza = FAZA_JUMP_LEFT;
            else faza = FAZA_JUMP_RIGHT;
        }
    }

    public float getX() {
        return body.getPosition().x;
    }

    public float getY() {
        return body.getPosition().y;
    }

    public float scrX() {
        return body.getPosition().x - r;
    }

    public float scrY() {
        return body.getPosition().y - r;
    }

    public float width() {
        return r * 2;
    }

    public float height() {
        return r * 2;
    }

    private Vector2 getCenter() {
        return body.getPosition();
    }

    public boolean overlap(DynamicBodyBall b) {
        return (getX() - b.getX()) * (getX() - b.getX()) + (getY() - b.getY()) * (getY() - b.getY()) <= (r + b.r) * (r + b.r);
    }

    // компьютерный интеллект
    public void useAi(DynamicBodyBall ball) {
        if (state == GO && (getX() < SCR_WIDTH/2 && ball.getX()<SCR_WIDTH/2 || getX()>SCR_WIDTH/2 && ball.getX()>SCR_WIDTH/2)) {
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
