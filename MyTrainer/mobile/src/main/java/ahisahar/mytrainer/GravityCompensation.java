package ahisahar.mytrainer;

/**
 * Created by a on 25/10/16.
 */
public class GravityCompensation {

    public GravityCompensation(){
        fixedAccelerometerData[0]=0;
        fixedAccelerometerData[1]=0;
        fixedAccelerometerData[2]=0;

    }

    private float[] fixedAccelerometerData = new float[3];


    public float [] fixAccelerometerData(Orientation.Quaternion quaternion, Float x,Float y, Float z){
        fixedAccelerometerData[0] =x - (float)(2 * (quaternion.q2 * quaternion.q4 - quaternion.q1 * quaternion.q3));
        fixedAccelerometerData[1] =y - (float)(2 * (quaternion.q1 * quaternion.q2 + quaternion.q3 * quaternion.q4));
        fixedAccelerometerData[2] =z - (float)(quaternion.q1 * quaternion.q1 - quaternion.q2 * quaternion.q2 - quaternion.q3 * quaternion.q3 + quaternion.q4 * quaternion.q4);
        return fixedAccelerometerData;
    }

}
