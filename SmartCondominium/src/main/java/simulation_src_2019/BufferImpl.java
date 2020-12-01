package simulation_src_2019;

import Casa.HouseSingleton;

public class BufferImpl implements Buffer{

    public BufferImpl(){}

    @Override
    public void addMeasurement(Measurement m) {
        HouseSingleton.getInstance().addMeasurement(m);
    }
}
