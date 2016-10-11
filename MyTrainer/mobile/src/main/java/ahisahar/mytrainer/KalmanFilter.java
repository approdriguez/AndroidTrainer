package ahisahar.mytrainer;

/**
 * Created by a on 10/10/16.
 */
import jkalman.JKalman;
import jama.Matrix;

public class KalmanFilter {
    private JKalman kalman;
    Matrix s = new Matrix(6, 1); // state [x, y, z, dx, dy, dz]
    Matrix c = new Matrix(6, 1); // corrected state
    Matrix m = new Matrix(3, 1); // measurement [x, y, z]

    /*
     * Inicializa el filtro kalman con 2 variables
     */
    public void initialize() throws Exception{
        double dx, dy;
        kalman = new JKalman(6, 3);
        // constant velocity
        dx = 0.2;
        dy = 0.2;

        s = new Matrix(4, 1); // state [x, y, dx, dy, dxy]
        c = new Matrix(4, 1); // corrected state [x, y, dx, dy, dxy]

        m = new Matrix(2, 1); // measurement [x]
        m.set(0, 0, 0);
        m.set(1, 0, 0);

        // transitions for x, y, dx, dy
        double[][] tr = { {1, 0, dx, 0},
                {0, 1, 0, dy},
                {0, 0, 1, 0},
                {0, 0, 0, 1} };
        kalman.setTransition_matrix(new Matrix(tr));

        // 1s somewhere?
        kalman.setError_cov_post(kalman.getError_cov_post().identity());

    }

    /*
     * Aplica Filtro a variables
     */
    public void push(double x,double y) throws Exception{
        m.set(0, 0, x);
        m.set(1, 0, y);

        c = kalman.Correct(m);
        s = kalman.Predict();
    }


}
