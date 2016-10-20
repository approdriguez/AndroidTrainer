package ahisahar.mytrainer;

import jkalman.JKalman;
import jama.Matrix;

public class KalmanFilter {
    private JKalman kalman;
    Matrix s = new Matrix(6, 1); // state [x, y, z, dx, dy, dz]
    Matrix c = new Matrix(6, 1); // corrected state
    Matrix m = new Matrix(3, 1); // measurement [x, y, z]

    /*
     * Inicializa el filtro kalman con 3 variables
     */
    public void initialize() throws Exception{
        double dx, dy;
        kalman = new JKalman(6, 3);
        // constant velocity
        dx = 0.2;
        dy = 0.2;

        //Values from the accelerometers
        float x = 0;
        float y = 0;
        float z = 0;


        Matrix s = new Matrix(6, 1); // state [x, y, z, dx, dy, dz]
        Matrix c = new Matrix(6, 1); // corrected state
        Matrix m = new Matrix(3, 1); // measurement [x, y, z]

        // the initial values follow (sorry for programming in stackoverflow):
        m.set(0, 0, x);
        m.set(1, 0, y);
        m.set(2, 0, z);

        // transitions for x, y, z, dx, dy, dz (velocity transitions)
        double[][] tr = { {1, 0, 0, 1, 0, 0},
                {0, 1, 0, 0, 1, 0},
                {0, 0, 1, 0, 0, 1},
                {0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 1} };
        kalman.setTransition_matrix(new Matrix(tr));

        // 1s somewhere?
        kalman.setError_cov_post(kalman.getError_cov_post().identity());

    }

    /*
     * Aplica Filtro a variables
     */
    public void push(float x,float y, float z) throws Exception{
        m.set(0, 0, x);
        m.set(1, 0, y);
        m.set(2, 0, z);

        c = kalman.Correct(m);
        s = kalman.Predict();
    }


}
