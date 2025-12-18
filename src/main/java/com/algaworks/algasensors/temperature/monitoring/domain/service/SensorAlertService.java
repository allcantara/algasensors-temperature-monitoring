package com.algaworks.algasensors.temperature.monitoring.domain.service;

import com.algaworks.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorAlertRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorAlertService {

    private final SensorAlertRepository sensorAlertRepository;

    @Transactional
    public void handleAlert(TemperatureLogData temperatureLogData) {
        sensorAlertRepository.findById(new SensorId(temperatureLogData.getSensorId()))
                .ifPresentOrElse(alert -> {

                    if (nonNull(alert.getMaxTemperature()) && temperatureLogData.getValue().compareTo(alert.getMaxTemperature()) >= 0) {
                        log.info("Alert MAX temperature: SensorId {} - Temp: {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
                    } else if (nonNull(alert.getMinTemperature()) && temperatureLogData.getValue().compareTo(alert.getMinTemperature()) <= 0) {
                        log.info("Alert MIN temperature: SensorId {} - Temp: {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
                    } else {
                        log.info("Alert NORMAL: SensorId {} - Temp: {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
                    }

                }, () -> log.info("Alert ignored: SensorId {} - Temp: {}", temperatureLogData.getSensorId(), temperatureLogData.getValue()));
    }

}
