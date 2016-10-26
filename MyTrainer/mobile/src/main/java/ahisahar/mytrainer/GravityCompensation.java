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

    private double[] fixedAccelerometerData = new double[3];


    public double [] fixAccelerometerData(Orientation.Quaternion quaternion, Double x,Double y, Double z){
        fixedAccelerometerData[0] = 2 * (quaternion.q2 * quaternion.q4 - quaternion.q1 * quaternion.q3);
        fixedAccelerometerData[1] = 2 * (quaternion.q1 * quaternion.q2 + quaternion.q3 * quaternion.q4);
        fixedAccelerometerData[2] = quaternion.q1 * quaternion.q1 - quaternion.q2 * quaternion.q2 - quaternion.q3 * quaternion.q3 + quaternion.q4 * quaternion.q4;
        return fixedAccelerometerData;
    }

}
