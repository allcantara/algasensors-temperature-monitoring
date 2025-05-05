package com.algaworks.algasensors.temperature.monitoring.domain.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
public class TemperatureLogId implements Serializable {

    private UUID value;

    public TemperatureLogId(UUID value) {
        requireNonNull(value);
        this.value = value;
    }

    public TemperatureLogId(String value) {
        requireNonNull(value);
        this.value = UUID.fromString(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
