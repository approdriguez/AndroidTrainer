package ahisahar.mytrainer;

public class Ex{

        private float max_velocity=0, max_power=0, series=0,peso=0;

        public Ex(){

        }

        public Ex(float max_velocity, float max_power,float series, float peso){
                this.max_power = max_power;
                this.max_velocity = max_velocity;
                this.series = series;
                this.peso = peso;
        }

        public float getMax_power() {
                return max_power;
        }

        public void setMax_power(float max_power) {
                this.max_power = max_power;
        }

        public float getMax_velocity() {
                return max_velocity;
        }

        public void setMax_velocity(float max_velocity) {
                this.max_velocity = max_velocity;
        }

        public float getPeso() {
                return peso;
        }

        public float getSeries() {
                return series;
        }
}
