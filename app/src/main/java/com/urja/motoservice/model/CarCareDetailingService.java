package com.urja.motoservice.model;

import java.util.List;

/**
 * Created by BAPI1 on 9/28/2016.
 */
public class CarCareDetailingService {
    private List<CarCareDetailing> carCareDetailing;

    public CarCareDetailingService() {
    }

    public CarCareDetailingService(List<CarCareDetailing> carCareDetailing) {
        this.carCareDetailing = carCareDetailing;
    }

    public List<CarCareDetailing> getCarCareDetailing() {
        return carCareDetailing;
    }

    public void setCarCareDetailing(List<CarCareDetailing> carCareDetailing) {
        this.carCareDetailing = carCareDetailing;
    }
}
