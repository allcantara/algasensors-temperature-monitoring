package com.algaworks.algasensors.temperature.monitoring.infrastructure.rabbitmq;

import com.algaworks.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import com.algaworks.algasensors.temperature.monitoring.domain.service.SensorAlertService;
import com.algaworks.algasensors.temperature.monitoring.domain.service.TemperatureMonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static com.algaworks.algasensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.ALERTING_QUEUE;
import static com.algaworks.algasensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.PROCESS_TEMPERATURE_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    private final TemperatureMonitoringService temperatureMonitoringService;
    private final SensorAlertService sensorAlertService;


    @RabbitListener(queues = PROCESS_TEMPERATURE_QUEUE, concurrency = "2-3")
    public void handleProcessTemperature(@Payload TemperatureLogData temperatureLogData) {
        log.info("Processing received: sensorId={}, temperature={}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
        temperatureMonitoringService.processTemperatureReading(temperatureLogData);
    }

    @RabbitListener(queues = ALERTING_QUEUE, concurrency = "2-3")
    public void handleAlerting(@Payload TemperatureLogData temperatureLogData) {
        log.info("Alerting received: sensorId={}, temperature={}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
        sensorAlertService.handleAlert(temperatureLogData);
    }

}
